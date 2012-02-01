package kukaWii.simulation;

import java.awt.Font;
import java.awt.Frame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import javax.xml.ws.Holder;

import kukaWii.movement.MoveAction;
import kukaWii.movement.MovementService;

import com.sun.opengl.util.Animator;
import com.sun.opengl.util.FPSAnimator;
import com.sun.opengl.util.j2d.TextRenderer;

/**
 * Dient zur Simulation von 3-dimensionalen Bewegungen im Raum. JOGL:
 * http://download.java.net/media/jogl/builds/archive/jsr-231-1.1.1a/
 * 
 * @author Kai Hufenbach
 * 
 */
public class Simulator {
	
	private final float[] x = new float[1];
	private final float[] y = new float[1];
	private final float[] z = new float[1];
	
	
	/**
	 * Erzeugt eine neue Simulator Klasse. Dabei wird ein JOGL Fenster mit einer
	 * Roten Kugel geöffnet, die mit einer bestimmten Geschwindigkeit relativ
	 * bewegt werden kann.
	 */
	public Simulator() {
		GLCapabilities caps = new GLCapabilities();
		final GLCanvas canvas = new GLCanvas(caps);
		final Frame frame = new Frame("Movement Simulator");
		frame.add(canvas);
		frame.setSize(800, 800);
		
		x[0] = 0;
		y[0] = 0;
		z[0] = 0;

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});
		
		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyTyped(KeyEvent evt) {
				if(evt.getKeyChar() == ' '){
					reset();
				}
			}
			
		});

		canvas.addGLEventListener(new GLEventListener() {
			
			
			
			Animator animator;
			GLU glu;

			TextRenderer textRenderer;

			

			int framecount = 0;
			
			long fps = 0;

			MoveAction remainingMove = null;

			long timeDifference;
			long tsBefore = System.currentTimeMillis();
			long timeToSecond = 0;
			
			float remainingTimeDifference = 0;

			MovementService movementService = MovementService.getService();

			@Override
			public void reshape(GLAutoDrawable drawable, int x, int y,
					int width, int height) {
				GL gl = drawable.getGL();
				gl.glViewport(0, 0, width, height);

			}

			@Override
			public void init(GLAutoDrawable drawable) {
				movementService.setTakeAction(MovementService.NULL);

				GL gl = drawable.getGL();
				glu = new GLU();

				drawable.setGL(new DebugGL(gl));

				gl.glEnable(GL.GL_DEPTH_TEST);
				gl.glDepthFunc(GL.GL_LEQUAL);
				gl.glShadeModel(GL.GL_SMOOTH);
				gl.glHint(GL.GL_PERSPECTIVE_CORRECTION_HINT, GL.GL_NICEST);
				gl.glClearColor(0f, 0f, 0f, 1f);

				// Beleuchtung
				float SHINE_ALL_DIRECTIONS = 1;
				float[] lightPos = { -30, -10, 100, SHINE_ALL_DIRECTIONS };
				float[] lightColorAmbient = { 0.4f, 0.4f, 0.4f, 1f };
				float[] lightColorSpecular = { 0.6f, 0.6f, 0.6f, 1f };

				gl.glLightfv(GL.GL_LIGHT1, GL.GL_POSITION, lightPos, 0);
				gl.glLightfv(GL.GL_LIGHT1, GL.GL_AMBIENT, lightColorAmbient, 0);
				gl.glLightfv(GL.GL_LIGHT1, GL.GL_SPECULAR, lightColorSpecular,
						0);

				gl.glEnable(GL.GL_LIGHT1);
				gl.glEnable(GL.GL_LIGHTING);

				float[] rgba = { 0.9f, 0.2f, 0.2f };
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_AMBIENT, rgba, 0);
				gl.glMaterialfv(GL.GL_FRONT, GL.GL_SPECULAR, rgba, 0);
				gl.glMaterialf(GL.GL_FRONT, GL.GL_SHININESS, 2.5f);

				textRenderer = new TextRenderer(new Font("SansSerif",
						Font.PLAIN, 14));

				animator = new FPSAnimator(canvas, 50);
				animator.start();
			}

			@Override
			public void displayChanged(GLAutoDrawable drawable,
					boolean modeChanged, boolean deviceChanged) {
				// TODO Auto-generated method stub

			}

			@Override
			public void display(GLAutoDrawable drawable) {
				if (!animator.isAnimating()) {
					return;
				}

				GL gl = drawable.getGL();

				gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

				setCamera(gl, glu, 1000);

				gl.glPushMatrix();
				gl.glLoadIdentity();
				
				// Zeitdifferenz zwischen dem letzten Mal neuzeichnen wird hier berechnet
				calculateFPS();

			
				//Wenn keine Bewegung mehr abzuarbeiten ist, dann eine neue holen
				if (remainingMove == null) {
					remainingMove = movementService.getNextMoveAction();
				}

				//Es ist noch eine Bewegung abzuarbeiten
				if (remainingMove != null) {
					
					//Um wieviel kann ich mich bewegen? (Zeit zwischen Neuzeichnen und verbleibender vorherigen Bewegung)
					double moveDistance = (timeDifference + remainingTimeDifference)
							* remainingMove.getSpeed();
					remainingTimeDifference = 0;

					//Wieweit muss ich mich eigentlich für das Paket insgesamt bewegen?
					float remainingDistance = (float) Math.sqrt((remainingMove
							.getX() * remainingMove.getX())
							+ (remainingMove.getY() * remainingMove.getY())
							+ (remainingMove.getZ() * remainingMove.getZ()));

					
					double proportion = moveDistance / remainingDistance;

					//Die Bewegung kann nicht vollständig abgearbeitet werden
					if (proportion < 1) {
						//Partielle Anteile berechnen
						double partX = proportion * remainingMove.getX();
						double partY = proportion * remainingMove.getY();
						double partZ = proportion * remainingMove.getZ();

						//Position verändern
						x[0] += partX;
						y[0] += partY;
						z[0] += partZ;

						//Verbleibende Bewegung berechnen
						remainingMove.setX(remainingMove.getX() - partX);
						remainingMove.setY(remainingMove.getY() - partY);
						remainingMove.setZ(remainingMove.getZ() - partZ);
						
					}
					//Die Bewegung kann vollständig abgearbeitet werden
					else {

						//Position verändern
						x[0] += remainingMove.getX();
						y[0] += remainingMove.getY();
						z[0] += remainingMove.getZ();

						//Es stand für die Bewegung ja mehr Zeit zur Verfügung, den Rest für das nächste mal merken
						if(proportion != 1){
							remainingTimeDifference = timeDifference - (float)((1/proportion)*timeDifference);
						}
						

						remainingMove = null;

					}

					gl.glTranslatef(x[0], y[0], z[0]);
				}

				GLUquadric ball = glu.gluNewQuadric();
				glu.gluQuadricDrawStyle(ball, GLU.GLU_FILL);
				glu.gluQuadricNormals(ball, GLU.GLU_FLAT);
				glu.gluQuadricOrientation(ball, GLU.GLU_OUTSIDE);
				final float radius = 10f;
				final int slices = 64;
				final int stacks = 64;
				glu.gluSphere(ball, radius, slices, stacks);
				glu.gluDeleteQuadric(ball);

				gl.glPopMatrix();

				// Text
				textRenderer.beginRendering(drawable.getWidth(),
						drawable.getHeight());
				textRenderer.setColor(1.0f, 1.0f, 1.0f, 0.7f);
				textRenderer.draw("X: " + x[0], drawable.getWidth() - 80,
						drawable.getHeight() - 20);
				textRenderer.draw("Y: " + y[0], drawable.getWidth() - 80,
						drawable.getHeight() - 45);
				textRenderer.draw("Z: " + z[0], drawable.getWidth() - 80,
						drawable.getHeight() - 70);
				textRenderer.draw("FPS: " + fps, drawable.getWidth() - 80,
						drawable.getHeight() - 95);
				textRenderer.endRendering();
			}

			private void setCamera(GL gl, GLU glu, float distance) {
				gl.glMatrixMode(GL.GL_PROJECTION);
				gl.glLoadIdentity();

				float widthHeightRatio = (float) canvas.getWidth()
						/ (float) canvas.getHeight();
				glu.gluPerspective(45, widthHeightRatio, 1, 1000);
				glu.gluLookAt(0, 0, distance, 0, 0, 0, 0, 1, 0);

				gl.glMatrixMode(GL.GL_MODELVIEW);
				gl.glLoadIdentity();
			}

			private void calculateFPS() {
				framecount++;

				long ts = System.currentTimeMillis();
				timeDifference = ts - tsBefore;
				tsBefore = ts;
				
				timeToSecond += timeDifference;

				if (timeToSecond > 1000) {
					fps = framecount / (timeToSecond / 1000);

					framecount = 0;
					timeToSecond = 0;
				}
			}
		});

		frame.setVisible(true);
	}
	
	private void reset(){
		x[0] = 0;
		y[0] = 0;
		z[0] = 0;
		
	}

	public static void main(String[] args) {
		new Simulator();
	}
}

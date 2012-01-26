package kukaWii.simulation;

import java.awt.Font;
import java.awt.Frame;
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

	/**
	 * Erzeugt eine neue Simulator Klasse. Dabei wird ein JOGL Fenster mit einer
	 * Roten Kugel geöffnet, die mit einer bestimmten Geschwindigkeit relativ
	 * bewegt werden kann.
	 */
	public Simulator() {
		GLCapabilities caps = new GLCapabilities();
		final GLCanvas canvas = new GLCanvas(caps);
		Frame frame = new Frame("Movement Simulator");
		frame.add(canvas);
		frame.setSize(800, 800);

		frame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}

		});

		canvas.addGLEventListener(new GLEventListener() {

			Animator animator;
			GLU glu;

			TextRenderer textRenderer;

			float x = 0;
			float y = 0;
			float z = 0;

			int framecount = 0;
			long time = System.currentTimeMillis();
			long fps = 0;

			MoveAction remainingMove = null;

			long timeDifference;
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

				// Das eigentliche Objekt und die dazugehörige Bewegung
				gl.glPushMatrix();

				// Zeitdifferenz wird hier berechnet
				calculateFPS();

				// Die Geschwindigkeit gibt an, wieviele Einheiten sich pro
				// Sekunde bewegt werden soll

				if (remainingMove == null) {
					remainingMove = movementService.getNextMoveAction();
				}

				if (remainingMove != null) {
					float moveDistance = (timeDifference + remainingTimeDifference)
							* remainingMove.getSpeed() / 1000;

					float remainingDistance = (float) Math.sqrt((remainingMove
							.getX() * remainingMove.getX())
							+ (remainingMove.getY() * remainingMove.getY())
							+ (remainingMove.getZ() * remainingMove.getZ()));

					float proportion = moveDistance / remainingDistance;

					if (proportion < 1) {
						float partX = proportion * remainingMove.getX();
						float partY = proportion * remainingMove.getY();
						float partZ = proportion * remainingMove.getZ();

						x += partX;
						y += partY;
						z += partZ;

						remainingMove.setX(remainingMove.getX() - partX);
						remainingMove.setY(remainingMove.getY() - partY);
						remainingMove.setZ(remainingMove.getZ() - partZ);

					} else {

						x += remainingMove.getX();
						y += remainingMove.getY();
						z += remainingMove.getZ();

						remainingTimeDifference = timeDifference
								- (timeDifference / proportion);

						remainingMove = null;

					}

					gl.glTranslatef(x, y, z);
				}

				remainingTimeDifference = 0;

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
				textRenderer.draw("X: " + x, drawable.getWidth() - 80,
						drawable.getHeight() - 20);
				textRenderer.draw("Y: " + y, drawable.getWidth() - 80,
						drawable.getHeight() - 45);
				textRenderer.draw("Z: " + z, drawable.getWidth() - 80,
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
				timeDifference = ts - time;

				if (timeDifference > 1000) {
					time = ts;
					fps = framecount / (timeDifference / 1000);

					framecount = 0;
				}
			}
		});

		frame.setVisible(true);
	}

	public static void main(String[] args) {
		new Simulator();
	}
}

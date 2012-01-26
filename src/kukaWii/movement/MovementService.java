package kukaWii.movement;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kukaWii.wiiHandle.Security.SecurityService;

/**
 * Hilfsklasse, in der Bewegungsbefehle ({@link MoveAction}) gesammelt werden können.
 * @author Kai Hufenbach
 *
 */
public class MovementService {
	//Maximale Größe der Queue, wird diese überschrieben, passiert ein eingestelltes Verhalten.
	private  int bufferSize = 10;
	
	private int overflowAction = 2;
	private int takeAction = 0;
	
	public static final int DROP = 0;
	public static final int SOFT_INTERRUPT = 1;
	public static final int HARD_INTERRUPT = 2;
	
	public static final int BLOCK = 0;
	public static final int NULL = 1;
	
	
	private static MovementService instance = new MovementService();
	private BlockingQueue<MoveAction> moveQueue = new ArrayBlockingQueue<MoveAction>(bufferSize);
	
	public MovementService(){
		
	}
	
	/**
	 * Fügt eine MoveAction hinzu.
	 * Ist die Queue voll, wird je nach Einstellung, ein Packet weggeworfen, ein harter- oder weicher Abbruch eingeleitet.
	 * @param action
	 */
	public synchronized void addMoveAction(MoveAction action){
		if(!moveQueue.offer(action)){
			if(overflowAction==DROP){
				try {
					moveQueue.take();
					moveQueue.add(action);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}else if(overflowAction==SOFT_INTERRUPT){
				//TODO muss noch entschieden
			}else if(overflowAction==HARD_INTERRUPT){
				SecurityService.panicInterrupt("Bewegungs-Queue übergelaufen, Größe: "+bufferSize);
			}
		}
	}
	
	/**
	 * Gibt die nächste Bewegungsanweisung zurück.
	 * Das Verhalten, was bei leerer Queue passiert, kann eingesetellt werden.
	 * @return
	 */
	public synchronized MoveAction getNextMoveAction(){
		if(takeAction==BLOCK){
			try {
				return moveQueue.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}else if(takeAction==NULL){
			return moveQueue.poll();
		}
		
		
		//Sollte nicht passieren.
		return null;
	}
	
	/**
	 * Setzt die Größe des Bewegungs-Buffers auf die angegebene Größe. (Standard ist 10)
	 * Der Bewegungsbuffer wird bei dieser Aktion geleert!
	 * @param size
	 */
	public void setBufferSize(int size){
		bufferSize = size;
		moveQueue = new ArrayBlockingQueue<MoveAction>(bufferSize);
	}
	
	
	/**
	 * Setzt das Verhalten, was passieren soll, wenn beim Herausnehmen eines Elements eine leere Queue vorliegt.
	 * @param action 0: Der Aufruf blockt solange, bis ein Element vorhanden ist (Standard), 1: es wird null zurückgegeben
	 */
	public void setTakeAction(int action){
		if(action >= 0 && action < 2){
			this.takeAction = action;
		}else{
			throw new IllegalArgumentException("Es sind nur 0, 1 als Aktion erlaubt. Siehe dazu die Konstanten der Klasse.");
		}
	}
	
	/**
	 * Bestimmt, was passieren soll, wenn der Bewegungs-Buffer überläuft.
	 * @param action zur Auswahl stehen: 0: ältestes verwerfen, 1: weicher Abbruch, 2: harter Abbruch (Standard)
	 */
	public void setOverflowAction(int action){
		if(action >= 0 && action < 4){
			this.overflowAction = action;
		}else{
			throw new IllegalArgumentException("Es sind nur 0, 1 o.2 als Aktion erlaubt. Siehe dazu die Konstanten der Klasse.");
		}
		
	}
	
	public static  MovementService getService(){
		return instance;
	}
}

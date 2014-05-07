package edu.chalmers.Blockster.core.util;
import javax.vecmath.Vector2f;


/**
 * A class to handle the player animation states.
 * 
 * @author Oskar Jönefors
 *
 */

public class AnimationState {
	
	public final static AnimationState NONE = new AnimationState(Movement.NONE, "NONE");
	public final static AnimationState GRAB_LEFT = new AnimationState(Movement.NONE, "GRAB_LEFT");
	public final static AnimationState GRAB_RIGHT = new AnimationState(Movement.NONE, "GRAB_RIGHT");
	public final static AnimationState LIFT_LEFT = new AnimationState(Movement.WAIT, "LIFT_LEFT");
	public final static AnimationState LIFT_RIGHT = new AnimationState(Movement.WAIT, "LIFT_RIGHT");
	public final static AnimationState PLACE_LEFT = new AnimationState(Movement.WAIT, "PLACE_LEFT");
	public final static AnimationState PLACE_RIGHT = new AnimationState(Movement.WAIT, "PLACE_RIGHT");
	
	private float elapsedTime;
	private Movement move;
	private String name;

	/**
	 * Create an animation with the given movement.
	 * @param move	Movement to animate.
	 */
	public AnimationState (Movement move) {
		this(move, null);
	}
	
	public AnimationState (Movement move, String name) {
		elapsedTime = 0f;
		this.move = move;
		this.name = name;
	}
	
	public Movement getMovement() {
		return move;
	}
	
	public Float getElapsedTime(){
		return elapsedTime;
	}
	
	/**
	 * Returns the current relative position.
	 * @return a Vector2f with the current relative position
	 */
	public Vector2f getRelativePosition() {
		return move.getSpline().getPosition((elapsedTime / move.getDuration() * 100f));
	}
	
	/**
	 * Check if the AnimationState is done.
	 * @return
	 */
	public boolean isDone() {
		return move == Movement.NONE || elapsedTime >= move.getDuration();
	}
	
	public String toString() {
		if(name == null) {
			return move.name();
		} else {
			return name;
		}
	}
	
	/**
	 * Update the position according to the given delta time.
	 * @param deltaTime	a float time
	 */
	public void updatePosition(float deltaTime) {
		elapsedTime = Math.min(elapsedTime+deltaTime, move.getDuration());
	}
}

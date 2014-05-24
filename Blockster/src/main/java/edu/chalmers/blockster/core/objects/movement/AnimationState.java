package edu.chalmers.blockster.core.objects.movement;
import javax.vecmath.Vector2f;


/**
 * A class to handle the player animation states.
 * 
 * @author Oskar Jönefors
 *
 */

public class AnimationState {
	
	public static final AnimationState NONE = new AnimationState(Movement.NONE, "NONE");
		
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
		if(move.getDuration() == 0) {
			return new Vector2f(0, 0);
		}
		return move.getSpline().getPosition(elapsedTime / move.getDuration() * 100f);
	}
	
	/**
	 * Check if the AnimationState is done.
	 * @return
	 */
	public boolean isDone() {
		return move.getDirection() == Direction.NONE || elapsedTime >= move.getDuration();
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

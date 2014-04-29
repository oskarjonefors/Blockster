package edu.chalmers.Blockster.core;
import javax.vecmath.Vector2f;

import edu.chalmers.Blockster.core.util.Direction;


/**
 * A class to handle the player animation states.
 * 
 * @author Oskar Jönefors
 *
 */
public class AnimationState {
	
	public final static AnimationState NONE = new AnimationState(Movement.NONE);
	
	private float elapsedTime;
	private Movement move;
	
	/**
	 * Create an animation with the given movement.
	 * @param move	Movement to animate.
	 */
	public AnimationState (Movement move) {
		elapsedTime = 0f;
		this.move = move;
	}
	
	public Movement getMovement() {
		return move;
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
		return(elapsedTime >= move.getDuration());
	}
	
	/**
	 * Update the position according to the given delta time.
	 * @param deltaTime	a float time
	 */
	public void updatePosition(float deltaTime) {
		elapsedTime = Math.min(elapsedTime+deltaTime, move.getDuration());
	}
}

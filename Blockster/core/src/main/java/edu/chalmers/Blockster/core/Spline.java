package edu.chalmers.Blockster.core;



import javax.vecmath.Vector2f;

/**
 * An interface class to represent the trajectory of a movement in the game world.
 * The coordinates returned is the offset from the original point of the movement.
 * @author Oskar Jönefors
 *
 */
public interface Spline {
	
	/**
	 * Returns the relative position at the given percent of the movement.
	 * @param percent
	 * @return A float vector with the current coordinate offset.
	 * 			If percentage is negative or higher than 100, the vector will
	 * 			be (0, 0).
	 */
	public Vector2f getPosition(float percent);
}

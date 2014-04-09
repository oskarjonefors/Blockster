package edu.chalmers.Blockster.core;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.util.Direction;

/**
 * An interface representing a player in the game Blockster
 * @author sweed
 *
 */
public interface Player extends Position {

	/**
	 * Gets the player's current gravity velocity.
	 * @return a vecmath implementation of the player's gravity velocity.
	 */
	public Vector2f getGravity();
	
	/**
	 * Gets the player's height.
	 * @return a float representing the player's height
	 */
	public float getHeight();
	
	/**
	 * Gets the player's maximum movement speed.
	 * @return a float representing the player's movement speed.
	 */
	public float getMaximumMovementSpeed();
	
	/**
	 * Gets the player's velocity.
	 * @return a vecmath implementation of the player's velocity.
	 */
	public Vector2f getVelocity();
	
	/**
	 * Gets the player's width.
	 * @return a float representing the players width.
	 */
	public float getWidth();
	
	/**
	 * Gets the player's x coordinate.
	 * @return a float representing the players x coordinate.
	 */
	public float getX();
	
	/**
	 * Gets the player's x coordinate
	 * @return a float representing the players x coordinate.
	 */
	public float getY();
	
	/**
	 * Increases the gravity velocity linearly by G*deltaTime.
	 * @param deltaTime the amount of time since last increase.
	 */
	public void increaseGravity(float deltaTime);
	
	/**
	 * Moves a player towards a fixed direction, with a set distance.
	 * @param dir the direction of the movement
	 * @param distance the distance of the movement
	 */
	public void move(Direction dir, float distance);
	
	/**
	 * Resets the gravity velocity.
	 */
	public void resetGravity();

	/**
	 * Sets the velocity of the player.
	 * @param velX the velocity along the x axis.
	 */
	public void setVelocityX(float velX);
	
	/**
	 * Sets the velocity of the player.
	 * @param velY the velocity along the y axis.
	 */
	public void setVelocityY(float velY);
	
	/**
	 * Sets the player's x coordinate
	 * @param the x coordinate as a float
	 */
	public void setX(float x);
	
	/**
	 * Sets the player's y coordinate
	 * @param y the y coordinate as a float
	 */
	public void setY(float y);
	
	/**
	 * Sets an active animation
	 * @param animation the animation to set
	 */
	public void setAnimation(Animation animation);
	
	/**
	 * Gets the active animation.
	 * @return the active animation.
	 */
	public Animation getAnimation();
}

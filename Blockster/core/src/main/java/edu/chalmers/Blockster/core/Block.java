package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;

import com.badlogic.gdx.Gdx;

import edu.chalmers.Blockster.core.util.Direction;

/**
 * An interface to represent a block.
 * @author Oskar Jönefors
 *
 */
public interface Block extends Position {

	/**
	 * Check if the block is liftable.
	 * @return True if block can be lifted, false otherwise.
	 */
	public abstract boolean isLiftable();

	/**
	 * Check if the block is movable.
	 * @return True if it's movable, false otherwise.
	 */
	public abstract boolean isMovable();

	/**
	 * Check if the block is solid.
	 * @return True if the block is solid. False otherwise.
	 */
	public abstract boolean isSolid();

	/**
	 * Get the animation that currently is playing.
	 * @return Animation currently playing.
	 */
	public abstract Animation getAnimation();

	/**
	 * Get the elapsed animation time.
	 * @return Elapsed animation time in float seconds.
	 */
	public abstract float getElapsedAnimationTime();

	/**
	 * Get the length of the entire animation in float seconds..
	 * @return
	 */
	public abstract float getAnimationDuration();
	
	/**
	 * Get the X coordinate of the block in the BlockMap.
	 * @return A float x coordinate.
	 */
	public abstract float getX();
	
	/**
	 * Get the Y coordinate of the block in the BlockMap. 
	 * @return A float y coordinate.
	 */
	public abstract float getY();
	
	/**
	 * Animate the block with the given animation..
	 * @param anim The animation to play.
	 */
	public abstract void setAnimation(Animation anim);

	/**
	 * Set the X coordinate of the block in the BlockMap.
	 * @param x X coordinate.
	 */
	public abstract void setX(float x);
	
	/**
	 * Set the Y coordinate of the block in the BlockMap.
	 * @param y	Y coordinate.
	 */
	public abstract void setY(float y);
}
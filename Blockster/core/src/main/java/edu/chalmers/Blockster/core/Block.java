package edu.chalmers.Blockster.core;

/**
 * An interface to represent a block.
 * @author Oskar Jönefors
 *
 */
public interface Block {
	
	/**
	 * An enum to represent different animation states of the block.
	 */
	public enum Animation {
		NONE, PUSH_LEFT, PUSH_RIGHT, PULL_LEFT, PULL_RIGHT, DESTROY, LIFT
	}

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
	 * Animate the block with the given animation..
	 * @param anim The animation to play.
	 */
	public abstract void setAnimation(Animation anim);

}
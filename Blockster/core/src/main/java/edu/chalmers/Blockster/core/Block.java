package edu.chalmers.Blockster.core;

import edu.chalmers.Blockster.core.util.Direction;

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
		NONE(0), PUSH_LEFT(0.5f), PUSH_RIGHT(0.5f), PULL_LEFT(0.5f),
		PULL_RIGHT(0.5f), DESTROY(0.5f), LIFT(0);
		
		public final float duration;
		
		private Animation(float f) {
			duration = f;
		}
		
		private static Animation getPushAnimation(Direction dir) {
			switch (dir) {
				case LEFT:
					return PUSH_LEFT;
				case RIGHT:
					return PUSH_LEFT;
				default:
					return NONE;
			}
		}
		
		private static Animation getPullAnimation(Direction dir) {
			switch (dir) {
				case LEFT:
					return PULL_LEFT;
				case RIGHT:
					return PULL_RIGHT;
				default:
					return NONE;
			}
		}
		
		public static Animation getMoveAnimation(Direction dir, float relativePositionSignum) {
			if (relativePositionSignum * dir.deltaX > 0) {
				return getPullAnimation(dir);
			} else {
				return getPushAnimation(dir);
			}
		}
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
	 * Get the X coordinate of the block in the BlockMap.
	 * @return An int x coordinate.
	 */
	public abstract int getX();
	
	/**
	 * Get the Y coordinate of the block in the BlockMap. 
	 * @return An int y coordinate.
	 */
	public abstract int getY();
	
	/**
	 * Animate the block with the given animation..
	 * @param anim The animation to play.
	 */
	public abstract void setAnimation(Animation anim);

	/**
	 * Set the X coordinate of the block in the BlockMap.
	 * @param x X coordinate.
	 */
	public abstract void setX(int x);
	
	/**
	 * Set the Y coordinate of the block in the BlockMap.
	 * @param y	Y coordinate.
	 */
	public abstract void setY(int y);
}
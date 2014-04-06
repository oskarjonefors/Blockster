package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * An enum to represent different animation states of the block.
 */
public enum Animation {
	NONE(0), PUSH_LEFT(LEFT, 0.5f), PUSH_RIGHT(RIGHT, 0.5f), PULL_LEFT(LEFT, 0.5f),
	PULL_RIGHT(RIGHT, 0.5f), DESTROY(0.5f), UP_LEFT(Direction.UP_LEFT, 0.5f),
	UP_RIGHT(Direction.UP_RIGHT, 0.5f), DOWN_LEFT(0.5f), DOWN_RIGHT(0.5f);
	
	public final Direction direction;
	public final float duration;
	
	private Animation(float duration) {
		this(Direction.NONE, duration);
	}
	
	private Animation(Direction dir, float duration) {
		direction = dir;
		this.duration = duration;
	}
	
	public static Animation getPushAnimation(Direction dir) {
		switch (dir) {
			case LEFT:
				return PUSH_LEFT;
			case RIGHT:
				return PUSH_LEFT;
			default:
				return NONE;
		}
	}
	
	public static Animation getPullAnimation(Direction dir) {
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
			return getPushAnimation(dir);
		} else {
			return getPullAnimation(dir);
		}
	}
	
	public static Animation getLiftAnimation(float relativePositionSignum) {
		if (relativePositionSignum > 0) {
			//Player stands on the right side of the block
			return UP_LEFT;
		} else {
			//Player stands on the left side of the block
			return UP_RIGHT;
		}
	}
	
}
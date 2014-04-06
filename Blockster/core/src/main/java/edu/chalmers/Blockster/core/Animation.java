package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.LEFT;
import static edu.chalmers.Blockster.core.util.Direction.RIGHT;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * An enum to represent different animation states of the block.
 */
public enum Animation {
	NONE(0), PUSH_LEFT(LEFT, 0.5f), PUSH_RIGHT(RIGHT, 0.5f), PULL_LEFT(LEFT, 0.5f),
	PULL_RIGHT(RIGHT, 0.5f), DESTROY(0.5f), LIFT(0);
	
	public final Direction direction;
	public final float duration;
	
	private Animation(float f) {
		this(Direction.NONE, f);
	}
	
	private Animation(Direction dir, float f) {
		direction = dir;
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
			return getPushAnimation(dir);
		} else {
			return getPullAnimation(dir);
		}
	}
	
}
package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;
import edu.chalmers.Blockster.core.util.Direction;

import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * An enum to represent different animation states of the block.
 */
public enum Animation {
	
	NONE(0),
	PUSH_LEFT(LEFT, STANDARD_MOVE_DURATION), 
	PUSH_RIGHT(RIGHT, STANDARD_MOVE_DURATION), 
	PULL_LEFT(LEFT, STANDARD_MOVE_DURATION),
	PULL_RIGHT(RIGHT, STANDARD_MOVE_DURATION), 
	DESTROY(0.5f), 
	LIFT_LEFT(UP_LEFT, STANDARD_MOVE_DURATION),
	LIFT_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION), 
	DOWN_LEFT(Direction.DOWN_LEFT, STANDARD_MOVE_DURATION), 
	DOWN_RIGHT(Direction.DOWN_RIGHT, STANDARD_MOVE_DURATION),
	CLIMB_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION),
	CLIMB_LEFT(UP_LEFT, STANDARD_MOVE_DURATION);
	
	
	
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
				return PUSH_RIGHT;
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
		System.out.println("relative position signum "+relativePositionSignum);
		if (relativePositionSignum > 0) {
			//Player stands on the right side of the block
			return LIFT_LEFT;
		} else {
			//Player stands on the left side of the block
			return LIFT_RIGHT;
		}
	}
	
	public static Animation getPutDownAnimation(Direction lastDirection) {
		if (lastDirection == Direction.LEFT) {
			return DOWN_LEFT;
		} else if (lastDirection == Direction.RIGHT) {
			return DOWN_RIGHT;
		} else {
			return NONE;
		}
	}
	public static Animation getClimbAnimation(Direction dir){
		if (dir == LEFT) {
			return CLIMB_LEFT;
		} else if (dir == RIGHT) {
			return CLIMB_RIGHT;
		} else
			return NONE;
			
	}
	
}
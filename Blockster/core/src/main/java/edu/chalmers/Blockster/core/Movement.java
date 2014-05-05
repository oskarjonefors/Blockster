package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;
import edu.chalmers.Blockster.core.util.Direction;

import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * An enum to represent different animation states of the block.
 */
public enum Movement {
	
	NONE,
	PULL_LEFT(LEFT, STANDARD_MOVE_DURATION, new LinearSpline(LEFT), true),
	PULL_RIGHT(RIGHT, STANDARD_MOVE_DURATION, new LinearSpline(RIGHT), true),
	PUSH_LEFT(LEFT, STANDARD_MOVE_DURATION, new LinearSpline(LEFT)),
	PUSH_RIGHT(RIGHT, STANDARD_MOVE_DURATION, new LinearSpline(RIGHT)),
	LIFT_LEFT(UP_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(UP_LEFT)),
	LIFT_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(UP_RIGHT)), 
	PLACE_LEFT(DOWN_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_LEFT)), 
	PLACE_RIGHT(DOWN_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_RIGHT)),
	MOVE_DOWN(DOWN, STANDARD_MOVE_DURATION, new LinearSpline(DOWN));
	
	private final Direction direction;
	private final float duration;
	private final Spline spline;
	private boolean pullMovement;
	
	private Movement() {
		this(Direction.NONE, 0, new LinearSpline(Direction.NONE), false);
	}
	
	private Movement(Direction dir, float duration, Spline spline) {
		this(dir, duration, spline, false);
	}
	
	private Movement(Direction dir, float duration, Spline spline, 
			boolean pullMovement) {
		this.spline = spline;
		direction = dir;
		this.duration = duration;
		this.pullMovement = pullMovement;
	}
	
	public boolean isPullMovement() {
		return pullMovement;
	}

	public Direction getDirection() {
		return direction;
	}

	/**
	 * Return the duration of the movement in float seconds.
	 * @return	Float time in seconds.
	 */
	public float getDuration() {
		return duration;
	}
	
	public static Movement getLiftMovement(Direction dir) {
		if(dir != Direction.LEFT && dir != Direction.RIGHT) {
			return NONE;
		} else {
			return dir == Direction.LEFT ? LIFT_RIGHT : LIFT_LEFT;
		}
	}
	
	/**
	 * Return a movement that is either a push or a pull, depending on the relative
	 * position of the player and the direction of the movement.
	 * @param dir
	 * @param relativePositionSignum
	 * @return
	 */
	public static Movement getPushPullMovement(Direction dir, float relativePositionSignum) {
		return dir.deltaX * relativePositionSignum > 0 ?
				getPushMovement(dir) : getPullMovement(dir);
	}
	
	public static Movement getPullMovement(Direction dir) {
		if (dir != Direction.LEFT && dir != Direction.RIGHT) {
			return NONE;
		} else {
			return dir == Direction.LEFT ? PULL_LEFT : PULL_RIGHT;
		}
	}
	
	public static Movement getPushMovement(Direction dir) {
		if (dir != Direction.LEFT && dir != Direction.RIGHT) {
			return NONE;
		} else {
			return dir == Direction.LEFT ? PUSH_LEFT : PUSH_RIGHT;
		}
	}
	
	public static Movement getPlaceMovement(Direction dir) {
		if (dir != Direction.LEFT && dir != Direction.RIGHT) {
			return NONE;
		} else {
			return dir == Direction.LEFT ? PLACE_LEFT : PLACE_RIGHT;
		}
	}

	/**
	 * Return the spline representing the movement.
	 * @return	A Spline
	 */
	public Spline getSpline() {
		return spline;
	}
}
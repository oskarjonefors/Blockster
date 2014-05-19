package edu.chalmers.blockster.core.objects.movement;

import static edu.chalmers.blockster.core.objects.movement.Direction.DOWN;
import static edu.chalmers.blockster.core.objects.movement.Direction.DOWN_LEFT;
import static edu.chalmers.blockster.core.objects.movement.Direction.DOWN_RIGHT;
import static edu.chalmers.blockster.core.objects.movement.Direction.LEFT;
import static edu.chalmers.blockster.core.objects.movement.Direction.RIGHT;
import static edu.chalmers.blockster.core.objects.movement.Direction.UP;
import static edu.chalmers.blockster.core.objects.movement.Direction.UP_LEFT;
import static edu.chalmers.blockster.core.objects.movement.Direction.UP_RIGHT;
import static edu.chalmers.blockster.core.util.Calculations.BLOCK_FALL_DURATION;
import static edu.chalmers.blockster.core.util.Calculations.GRAB_BLOCK_DURATION;
import static edu.chalmers.blockster.core.util.Calculations.MOVE_LIFTED_BLOCK_DURATION;
import static edu.chalmers.blockster.core.util.Calculations.STANDARD_MOVE_DURATION;

/**
 * An enum to represent different animation states of the block.
 */
public enum Movement {
	
	NONE(0),
	PULL_LEFT(LEFT, STANDARD_MOVE_DURATION, new LinearSpline(LEFT), true),
	PULL_RIGHT(RIGHT, STANDARD_MOVE_DURATION, new LinearSpline(RIGHT), true),
	PUSH_LEFT(LEFT, STANDARD_MOVE_DURATION, new LinearSpline(LEFT)),
	PUSH_RIGHT(RIGHT, STANDARD_MOVE_DURATION, new LinearSpline(RIGHT)),
	MOVE_LEFT(LEFT, MOVE_LIFTED_BLOCK_DURATION, new LinearSpline(LEFT)),
	MOVE_RIGHT(RIGHT, MOVE_LIFTED_BLOCK_DURATION, new LinearSpline(RIGHT)),
	LIFT_LEFT(UP_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(UP_LEFT)),
	LIFT_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(UP_RIGHT)), 
	PLACE_LEFT(DOWN_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_LEFT)), 
	PLACE_RIGHT(DOWN_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_RIGHT)),
	CLIMB_LEFT(UP_LEFT, STANDARD_MOVE_DURATION, new CompositeSpline(UP, LEFT)),
	CLIMB_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION, new CompositeSpline(UP, RIGHT)),
	FALL_DOWN(DOWN, BLOCK_FALL_DURATION, new LinearSpline(DOWN)),
	WAIT(STANDARD_MOVE_DURATION),
	GRAB_RIGHT(GRAB_BLOCK_DURATION),
	GRAB_LEFT(GRAB_BLOCK_DURATION);
	
	private final Direction direction;
	private final float duration;
	private final Spline spline;
	private boolean pullMovement;
	
	private Movement(float duration) {
		this(Direction.NONE, duration, new LinearSpline(Direction.NONE), false);
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
	
	public static boolean isPullMovement(float relativePosition, Direction dir) {
		return dir.getDeltaX() * relativePosition < 0;
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
	
	public static Movement getClimbMovement(Direction dir) {
		if(dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? CLIMB_LEFT : CLIMB_RIGHT;
		} else {
			return NONE;
		}
	}
	
	public static Movement getLiftMovement(Direction dir) {
		if(dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? LIFT_RIGHT : LIFT_LEFT;
		} else {
			return NONE;
		}
	}
	
	public static Movement getPullMovement(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? PULL_LEFT : PULL_RIGHT;
		} else {
			return NONE;
		}
	}
	
	public static Movement getPushMovement(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? PUSH_LEFT : PUSH_RIGHT;
		} else {
			return NONE;
		}
	}
	
	public static Movement getPlaceMovement(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? PLACE_LEFT : PLACE_RIGHT;
		} else {
			return NONE;
		}
	}
	
	public static Movement getMoveMovement(Direction dir) {
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return dir == Direction.LEFT ? MOVE_LEFT : MOVE_RIGHT;
		} else {
			return NONE;
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
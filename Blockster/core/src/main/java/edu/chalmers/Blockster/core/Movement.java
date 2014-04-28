package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;
import edu.chalmers.Blockster.core.util.Direction;

import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * An enum to represent different animation states of the block.
 */
public enum Movement {
	
	NONE(0),
	MOVE_LEFT(LEFT, STANDARD_MOVE_DURATION, new LinearSpline(LEFT)), 
	MOVE_RIGHT(RIGHT, STANDARD_MOVE_DURATION, new LinearSpline(RIGHT)), 
	LIFT_LEFT(UP_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(UP_LEFT)),
	LIFT_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(UP_RIGHT)), 
	FALL_LEFT(DOWN_LEFT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_LEFT)), 
	FALL_RIGHT(DOWN_RIGHT, STANDARD_MOVE_DURATION, new BezierSpline(DOWN_RIGHT)),
	MOVE_DOWN(DOWN, STANDARD_MOVE_DURATION, new LinearSpline(DOWN));
	
	private final Direction direction;
	private final float duration;
	private final Spline spline;
	
	private Movement(float duration) {
		this(Direction.NONE, duration, null);
	}
	
	private Movement(Direction dir, float duration, Spline spline) {
		this.spline = spline;
		direction = dir;
		this.duration = duration;
	}

	/**
	 * Get the direction of the movement.
	 * @return	Direction of movement.
	 */
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

	/**
	 * Return the spline representing the movement.
	 * @return	A Spline
	 */
	public Spline getSpline() {
		return spline;
	}
}
package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Direction.*;
import edu.chalmers.Blockster.core.util.Direction;

import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * An enum to represent different animation states of the block.
 */
public enum Movement {
	
	NONE(0),
	MOVE_LEFT(LEFT, STANDARD_MOVE_DURATION), 
	MOVE_RIGHT(RIGHT, STANDARD_MOVE_DURATION), 
	LIFT_LEFT(UP_LEFT, STANDARD_MOVE_DURATION),
	LIFT_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION), 
	FALL_LEFT(DOWN_LEFT, STANDARD_MOVE_DURATION), 
	FALL_RIGHT(DOWN_RIGHT, STANDARD_MOVE_DURATION),
	FALL_DOWN(DOWN, STANDARD_MOVE_DURATION),
	CLIMB_LEFT(UP_LEFT, STANDARD_MOVE_DURATION ),
	CLIMB_RIGHT(UP_RIGHT, STANDARD_MOVE_DURATION),
	MOVE_DOWN(DOWN, STANDARD_MOVE_DURATION);
	
	public final Direction direction;
	public final float duration;
	
	private Movement(float duration) {
		this(Direction.NONE, duration);
	}
	
	private Movement(Direction dir, float duration) {
		direction = dir;
		this.duration = duration;
	}
	
}
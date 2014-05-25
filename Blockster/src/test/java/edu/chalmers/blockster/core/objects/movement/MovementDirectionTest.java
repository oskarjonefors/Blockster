package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.*;

import org.junit.Test;

public class MovementDirectionTest {
	
	

	private final static Direction LEFT = Direction.LEFT;
	private final static Direction RIGHT = Direction.RIGHT;
	private final static Direction UP = Direction.UP;
	
	
	
	@Test
	public void testDirectionGetDirection() {
		Direction left = Direction.getDirection(2, 1);
		Direction right = Direction.getDirection(1, 2);
		
		assertTrue(left == Direction.LEFT 
				&& right == Direction.RIGHT);
	}

	@Test
	public void testGetClimbMovement() {
		Movement leftMovement = Movement.getClimbMovement(LEFT);
		Movement rightMovement = Movement.getClimbMovement(RIGHT);
		Movement neither = Movement.getClimbMovement(UP);
		
		assertTrue(leftMovement == Movement.CLIMB_LEFT 
				&& rightMovement == Movement.CLIMB_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetDeltaX() {
		assertTrue(LEFT.getDeltaX() < 0 && RIGHT.getDeltaX() > 0);
	}
	
	@Test
	public void testGetDeltaY() {
		Direction up = Direction.UP;
		Direction down = Direction.DOWN;
		
		assertTrue(up.getDeltaY() > 0 && down.getDeltaY() < 0);
	}

	@Test
	public void testGetLiftMovement() {
		Movement liftRight = Movement.getLiftMovement(LEFT);
		Movement liftLeft = Movement.getLiftMovement(RIGHT);
		Movement neither = Movement.getLiftMovement(UP);
		
		assertTrue(liftLeft == Movement.LIFT_LEFT 
				&& liftRight == Movement.LIFT_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetMoveMovement() {
		Movement moveLeft = Movement.getMoveMovement(LEFT);
		Movement moveRight = Movement.getMoveMovement(RIGHT);
		Movement neither = Movement.getMoveMovement(UP);
		
		assertTrue(moveLeft == Movement.MOVE_LEFT 
				&& moveRight == Movement.MOVE_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPlaceMovement() {
		Movement placeLeft = Movement.getPlaceMovement(LEFT);
		Movement placeRight = Movement.getPlaceMovement(RIGHT);
		Movement neither = Movement.getPlaceMovement(UP);
		
		assertTrue(placeLeft == Movement.PLACE_LEFT 
				&& placeRight == Movement.PLACE_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPullMovement() {
		Movement pullLeft = Movement.getPullMovement(LEFT);
		Movement pullRight = Movement.getPullMovement(RIGHT);
		Movement neither = Movement.getPullMovement(UP);
		
		assertTrue(pullLeft == Movement.PULL_LEFT 
				&& pullRight == Movement.PULL_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPushMovement() {
		Movement pushLeft = Movement.getPushMovement(LEFT);
		Movement pushRight = Movement.getPushMovement(RIGHT);
		Movement neither = Movement.getPushMovement(UP);
		
		assertTrue(pushLeft == Movement.PUSH_LEFT 
				&& pushRight == Movement.PUSH_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetSpline() {
		final Movement moveLeft = Movement.getMoveMovement(LEFT);
		final Movement placeRight = Movement.getPlaceMovement(RIGHT);
		final Movement climbLeft = Movement.getClimbMovement(LEFT);
		final Spline moveLeftSpline = moveLeft.getSpline();
		final Spline placeRightSpline = placeRight.getSpline();
		final Spline climbLeftSpline = climbLeft.getSpline();
		
		boolean linearMoveLeft = moveLeftSpline instanceof LinearSpline;
		boolean bezierPlaceRight = placeRightSpline instanceof BezierSpline;
		boolean compositeClimbLeft = climbLeftSpline instanceof CompositeSpline;
		boolean leftSplineLeftwardsDirection = moveLeftSpline.getDirection().getDeltaX() < 0;
		boolean rightSplineRightwardsDirection = placeRightSpline.getDirection().getDeltaX() > 0;
		boolean rightSplineDownwardsDirection = placeRightSpline.getDirection().getDeltaY() < 0;
		boolean climbSplineUpwardsDirection = climbLeftSpline.getDirection().getDeltaY() > 0;
		boolean climbSplineLeftwardsDirection = climbLeftSpline.getDirection().getDeltaX() < 0;
		
		assertTrue(linearMoveLeft && bezierPlaceRight && compositeClimbLeft && leftSplineLeftwardsDirection 
				&& rightSplineRightwardsDirection && rightSplineDownwardsDirection && climbSplineUpwardsDirection
				&& climbSplineLeftwardsDirection);
	}

	@Test
	public void testIsPullMovement() {
		Movement pullLeft = Movement.getPullMovement(LEFT);
		Movement pullRight = Movement.getPullMovement(RIGHT);
		Movement notPullLeft = Movement.getPushMovement(LEFT);
		Movement notPullRight = Movement.getPushMovement(RIGHT);
		
		boolean pullMovements = pullLeft.isPullMovement() 
							&& pullRight.isPullMovement();
		boolean notPullMovements = !notPullLeft.isPullMovement() 
								&& !notPullRight.isPullMovement();
		
		assertTrue(pullMovements && notPullMovements);
	}

	@Test
	public void testIsPullMovementFloatDirection() {
		float positive = 1.5f;
		float negative = -1.5f;
		boolean success = true;
		success &= Movement.checkIfPullMovement(positive, LEFT);
		success &= !Movement.checkIfPullMovement(negative, LEFT);
		success &= !Movement.checkIfPullMovement(positive, RIGHT);
		success &= Movement.checkIfPullMovement(negative, RIGHT);
		
		assertTrue(success);
	}
	
	@Test
	public void testGetOpposite() {
		if (Direction.getOpposite(Direction.LEFT) != Direction.RIGHT) {
			fail("Logical error");
		}
		if (Direction.getOpposite(Direction.UP_LEFT) != Direction.DOWN_RIGHT) {
			fail("Logical error");
		}
		if (Direction.getOpposite(Direction.UP) != Direction.DOWN) {
			fail("Logical error");
		}
	}
	
	@Test
	public void testIsPull() {
		if (Movement.isPull(null)) {
			fail("Null can never be a pull movement");
		}
		if (Movement.isPull(Movement.CLIMB_DOWN_LEFT)) {
			fail("Should not be a pull movement");
		}
		if (!Movement.isPull(Movement.PULL_LEFT)) {
			fail("Should be a pull movement");
		}
	}

}

package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.*;

import org.junit.Test;

public class MovementDirectionTest {
	
	

	private final static Direction left = Direction.LEFT;
	private final static Direction right = Direction.RIGHT;
	private final static Direction up = Direction.UP;
	
	
	
	@Test
	public void testDirectionGetDirection() {
		Direction left = Direction.getDirection(2, 1);
		Direction right = Direction.getDirection(1, 2);
		
		assertTrue(left == Direction.LEFT 
				&& right == Direction.RIGHT);
	}

	@Test
	public void testGetClimbMovement() {
		Movement leftMovement = Movement.getClimbMovement(left);
		Movement rightMovement = Movement.getClimbMovement(right);
		Movement neither = Movement.getClimbMovement(up);
		
		assertTrue(leftMovement == Movement.CLIMB_LEFT 
				&& rightMovement == Movement.CLIMB_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetDeltaX() {
		assertTrue(left.getDeltaX() < 0 && right.getDeltaX() > 0);
	}
	
	@Test
	public void testGetDeltaY() {
		Direction up = Direction.UP;
		Direction down = Direction.DOWN;
		
		assertTrue(up.getDeltaY() > 0 && down.getDeltaY() < 0);
	}

	@Test
	public void testGetLiftMovement() {
		Movement liftRight = Movement.getLiftMovement(left);
		Movement liftLeft = Movement.getLiftMovement(right);
		Movement neither = Movement.getLiftMovement(up);
		
		assertTrue(liftLeft == Movement.LIFT_LEFT 
				&& liftRight == Movement.LIFT_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetMoveMovement() {
		Movement moveLeft = Movement.getMoveMovement(left);
		Movement moveRight = Movement.getMoveMovement(right);
		Movement neither = Movement.getMoveMovement(up);
		
		assertTrue(moveLeft == Movement.MOVE_LEFT 
				&& moveRight == Movement.MOVE_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPlaceMovement() {
		Movement placeLeft = Movement.getPlaceMovement(left);
		Movement placeRight = Movement.getPlaceMovement(right);
		Movement neither = Movement.getPlaceMovement(up);
		
		assertTrue(placeLeft == Movement.PLACE_LEFT 
				&& placeRight == Movement.PLACE_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPullMovement() {
		Movement pullLeft = Movement.getPullMovement(left);
		Movement pullRight = Movement.getPullMovement(right);
		Movement neither = Movement.getPullMovement(up);
		
		assertTrue(pullLeft == Movement.PULL_LEFT 
				&& pullRight == Movement.PULL_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetPushMovement() {
		Movement pushLeft = Movement.getPushMovement(left);
		Movement pushRight = Movement.getPushMovement(right);
		Movement neither = Movement.getPushMovement(up);
		
		assertTrue(pushLeft == Movement.PUSH_LEFT 
				&& pushRight == Movement.PUSH_RIGHT
				&& neither == Movement.NONE);
	}

	@Test
	public void testGetSpline() {
		final Movement moveLeft = Movement.getMoveMovement(left);
		final Movement placeRight = Movement.getPlaceMovement(right);
		final Movement climbLeft = Movement.getClimbMovement(left);
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
		Movement pullLeft = Movement.getPullMovement(left);
		Movement pullRight = Movement.getPullMovement(right);
		Movement notPullLeft = Movement.getPushMovement(left);
		Movement notPullRight = Movement.getPushMovement(right);
		
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
		success &= Movement.checkIfPullMovement(positive, left);
		success &= !Movement.checkIfPullMovement(negative, left);
		success &= !Movement.checkIfPullMovement(positive, right);
		success &= Movement.checkIfPullMovement(negative, right);
		
		assertTrue(success);
	}

}

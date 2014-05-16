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
		Movement moveLeft = Movement.getMoveMovement(left);
		Movement placeRight = Movement.getPlaceMovement(right);
		Movement climbLeft = Movement.getClimbMovement(left);
		
		boolean linearMoveLeft = moveLeft.getSpline() instanceof LinearSpline;
		boolean bezierPlaceRight = placeRight.getSpline() instanceof BezierSpline;
		boolean compositeClimbLeft = climbLeft.getSpline() instanceof CompositeSpline;
		boolean leftSplineLeftwardsDirection = moveLeft.getSpline().getDirection().getDeltaX() < 0;
		boolean rightSplineRightwardsDirection = placeRight.getSpline().getDirection().getDeltaX() > 0;
		boolean rightSplineDownwardsDirection = placeRight.getSpline().getDirection().getDeltaY() < 0;
		boolean climbSplineUpwardsDirection = climbLeft.getSpline().getDirection().getDeltaY() > 0;
		boolean climbSplineLeftwardsDirection = climbLeft.getSpline().getDirection().getDeltaX() < 0;
		
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
		success &= Movement.isPullMovement(positive, left);
		success &= !Movement.isPullMovement(negative, left);
		success &= !Movement.isPullMovement(positive, right);
		success &= Movement.isPullMovement(negative, right);
		
		assertTrue(success);
	}

}

package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.*;

import javax.vecmath.Vector2f;

import org.junit.Test;

public class CompositeSplineTest {

	private float diff = (float) Math.pow(10, -5);
	
	@Test
	public void testGetPosition() {
		float p1 = 0, p3 = 100;

		Direction[] dir = new Direction[] { Direction.UP, 
				Direction.LEFT, Direction.UP };
		CompositeSpline spline = new CompositeSpline(dir);
		
		Vector2f end = new Vector2f();
		Vector2f pos1 = spline.getPosition(p1);
		Vector2f pos3 = spline.getPosition(p3);
		
		float distStart = Math.abs(pos1.length());
		float distEnd = Math.abs(pos3.length());
		boolean successLoop = true;
		
		for (int i = 0; i < dir.length; i++) {
			Vector2f splinePos;
			Direction d = dir[i];
			end.add(new Vector2f(d.getDeltaX(), d.getDeltaY()));
			splinePos = spline.getPosition((i+1)*100f/dir.length);
			
			splinePos.sub(end);
			successLoop &= Math.abs(splinePos.length()) < diff;
		}
		
		final float tolerance = 0.00001f;
		final boolean testStart = distStart < diff;
		final boolean testEnd = spline.getDirection() == Direction.NONE
				|| Math.abs(distEnd - Math.abs(end.length())) < tolerance;
		
		assertTrue(testStart && testEnd  && successLoop);
	}

	@Test
	public void testGetDirection() {
		Direction[] down = { Direction.LEFT, Direction.DOWN_RIGHT };
		Direction[] downLeft = { Direction.DOWN_LEFT, Direction.NONE};
		Direction[] left = { Direction.UP_LEFT, Direction.DOWN_LEFT };
		Direction[] upLeft = { Direction.UP, Direction.LEFT };
		Direction[] up = { Direction.UP_LEFT, Direction.UP_RIGHT };
		Direction[] upRight = { Direction.RIGHT, Direction.UP };
		Direction[] right = { Direction.UP_RIGHT, Direction.DOWN_RIGHT };
		Direction[] downRight = { Direction.RIGHT, Direction.DOWN };
		Direction[] none = { Direction.NONE };
		
		boolean test1 = testDirections(Direction.DOWN, down);
		boolean test2 = testDirections(Direction.DOWN_LEFT, downLeft);
		boolean test3 = testDirections(Direction.LEFT, left);
		boolean test4 = testDirections(Direction.UP_LEFT, upLeft);
		boolean test5 = testDirections(Direction.UP, up);
		boolean test6 = testDirections(Direction.UP_RIGHT, upRight);
		boolean test7 = testDirections(Direction.RIGHT, right);
		boolean test8 = testDirections(Direction.DOWN_RIGHT, downRight);
		boolean test9 = testDirections(Direction.NONE, none);
		assertTrue(test1 && test2 && test3 && test4 && test5 && test6 && test7 
				&& test8 && test9);
	}

	private boolean testDirections(Direction dir, Direction...directions) {
		CompositeSpline spline = new CompositeSpline(directions);
		return spline.getDirection() == dir;
	}
	
	@Test
	public void testGetDirections() {
		Direction[] directions = Direction.values();
		CompositeSpline spline = new CompositeSpline(directions);
		boolean success = true;
		
		for (Direction dir : spline.getDirections()) {
			if (!success) {
				break;
			}
			
			boolean found = false;
			for (Direction dir2 : directions) {
				if (dir == dir2) {
					found = true;
					break;
				}
			}
			
			success &= found;
		}
		
		assertTrue(success);
	}
		
}

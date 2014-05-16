package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.*;

import javax.vecmath.Vector2f;

import org.junit.BeforeClass;
import org.junit.Test;

public class BezierSplineTest {

	private float diff = (float) Math.pow(10, -5);

	@Test
	public void testGetPositionOne() {
		BezierSpline spline = new BezierSpline(Direction.UP_LEFT);
		float p1 = 0, p2 = 50f, p3 = 100;
		Vector2f pos1 = spline.getPosition(p1);
		Vector2f pos2 = spline.getPosition(p2);
		Vector2f pos3 = spline.getPosition(p3);
		
		float distStart = Math.abs(pos1.length());
		float distMiddle = Math.abs(pos2.length());
		float distEnd = Math.abs(pos3.length());
		
		boolean test1 = distStart < diff;
		boolean test2 = distStart < distMiddle && distMiddle < distEnd;
		boolean test3 = Math.abs(distEnd - Math.sqrt(2)) < diff;
		boolean lifting = Math.abs(pos2.y) > Math.abs(pos2.x);
		
		assertTrue(test1 && test2 && test3 && lifting);
	}

	@Test
	public void testGetPositionTwo() {
		BezierSpline spline = new BezierSpline(Direction.DOWN_RIGHT);
		float p1 = 0, p2 = 50f, p3 = 100;
		Vector2f pos1 = spline.getPosition(p1);
		Vector2f pos2 = spline.getPosition(p2);
		Vector2f pos3 = spline.getPosition(p3);
		
		float distStart = Math.abs(pos1.length());
		float distMiddle = Math.abs(pos2.length());
		float distEnd = Math.abs(pos3.length());
		
		boolean test1 = distStart < diff;
		boolean test2 = distStart < distMiddle && distMiddle < distEnd;
		boolean test3 = Math.abs(distEnd - Math.sqrt(2)) < diff;
		boolean falling = Math.abs(pos2.x) > Math.abs(pos2.y);
		
		assertTrue(test1 && test2 && test3 && falling);
	}
	
	@Test
	public void testGetDirection() {
		Direction dir = Direction.UP_LEFT;
		BezierSpline spline = new BezierSpline(dir);
		assertTrue(dir == spline.getDirection());
	}

}

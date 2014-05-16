package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.*;

import javax.vecmath.Vector2f;

import org.junit.BeforeClass;
import org.junit.Test;

public class LinearSplineTest {

	private float diff = (float) Math.pow(10, -5);
	private static LinearSpline spline;
	private static Direction dir;
	
	@BeforeClass
	public static void setUp() {
		dir = Direction.LEFT;
		spline = new LinearSpline(dir);
	}

	@Test
	public void testGetPosition() {
		float p1 = 0, p2 = 50f, p3 = 100;
		Vector2f pos1 = spline.getPosition(p1);
		Vector2f pos2 = spline.getPosition(p2);
		Vector2f pos3 = spline.getPosition(p3);
		
		float distStart = pos1.length();
		float distMiddle = pos2.length();
		float distEnd = pos3.length();
		
		boolean test1 = Math.abs(distStart) < diff;
		boolean test2 = distStart < distMiddle && distMiddle < distEnd;
		boolean test3 = Math.abs(Math.abs(distEnd) - Math.sqrt(2)) < diff;
		
		assertTrue(test1 && test2 && test3);
	}

	@Test
	public void testGetDirection() {
		assertTrue(dir == spline.getDirection());
	}

}

package edu.chalmers.blockster.core.objects.movement;

import javax.vecmath.Vector2f;

/**
 * A Bezier curve spline
 * @author Oskar Jönefors
 *
 */
public class BezierSpline implements Spline {
	
	private final Direction dir;
	private boolean isLifting;
	
	/**
	 * Create a spline in the given direction.
	 * @param dir	A direction
	 */
	public BezierSpline (Direction dir) {
		
		this.dir = dir;
		
		if (dir.deltaY > 0) {
			isLifting = true;
		} else {
			isLifting = false;
		}
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		if (percent < 0 || percent > 100) {
			return new Vector2f();
		}
		float offsetX;
		float offsetY;
		final double t = (double)(1 - percent / 100);
		
		// If movement is climbing, set x to 0, else 1
		final int x1 = isLifting ? 0 : dir.deltaX;
		final int x2 = dir.deltaX;
		offsetX = (float) (2*t*(1-t)*x1 + Math.pow((double)(1-t), 2)*x2);
		
		final int y1 = isLifting ? dir.deltaY : 0;
		final int y2 = dir.deltaY;
		offsetY = (float) (2 * t * (1 - t) * y1 + Math.pow((double)(1 - t), 2) * y2);
		
		return new Vector2f(offsetX, offsetY);
	}

	@Override
	public Direction getDirection() {
		return dir;
	}

}

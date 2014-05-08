package edu.chalmers.Blockster.core.objects.movement;

import javax.vecmath.Vector2f;

/**
 * A Bezier curve spline
 * @author Oskar Jönefors
 *
 */
public class BezierSpline implements Spline {
	
	private Direction dir;
	private boolean isClimbing;
	
	/**
	 * Create a spline in the given direction.
	 * @param dir	A direction
	 */
	public BezierSpline (Direction dir) {
		
		this.dir = dir;
		
		if (dir.deltaY > 0) {
			isClimbing = true;
		} else {
			isClimbing = false;
		}
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		if (percent < 0 || percent > 100) {
			return new Vector2f();
		}
		float offsetX;
		float offsetY;
		double t = (double)(percent / 100);
		
		// If movement is climbing, set x to 0, else 1
		int x1 = (isClimbing) ? 0 : 1;
		int x2 = (isClimbing) ? 1 : 0;
		offsetX = (float) (2*t*(1-t)*x1 + Math.pow((double)(1-t), 2)*x2);
		
		int y1 = (isClimbing) ? 1 : 0;
		int y2 = (isClimbing) ? 1 : -1;
		offsetY = (float) (2 * t * (1 - t) * y1 + Math.pow((double)(1 - t), 2) * y2);
		
		return new Vector2f(offsetX*dir.deltaX, offsetY);
	}

}

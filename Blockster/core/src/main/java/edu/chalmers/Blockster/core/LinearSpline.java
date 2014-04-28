package edu.chalmers.Blockster.core;

import javax.vecmath.Vector2f;

import edu.chalmers.Blockster.core.util.Direction;

public class LinearSpline implements Spline {
	private Direction dir;
	
	public LinearSpline(Direction dir) {
		this.dir = dir;
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		
		if (percent <= 0 || percent > 100) {
			return new Vector2f();
		}
		float offsetX;
		float offsetY;
		
		offsetX = percent * dir.deltaX / 100;
		offsetY = percent * dir.deltaY / 100;
		
		return new Vector2f(offsetX, offsetY);
	}

}

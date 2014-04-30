package edu.chalmers.Blockster.core;

import javax.vecmath.Vector2f;

import edu.chalmers.Blockster.core.util.Direction;

public class CompositeSpline implements Spline {

	private LinearSpline[] partialSplines;
	
	public CompositeSpline(Direction[] dir) {
		partialSplines = new LinearSpline[dir.length];
		for (int i = 0; i < dir.length; i++) {
			partialSplines[i] = new LinearSpline(dir[i]);
		}
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		return partialSplines[(int) percent * partialSplines.length]
				.getPosition((percent * partialSplines.length) % 1);
	}

}

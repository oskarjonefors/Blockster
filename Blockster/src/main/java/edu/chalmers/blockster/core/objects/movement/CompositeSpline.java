package edu.chalmers.blockster.core.objects.movement;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

public class CompositeSpline implements Spline {

	private final List<LinearSpline> partialSplines;
	
	public CompositeSpline(Direction... dirs) {
		partialSplines = new ArrayList<LinearSpline>();
		
		for(final Direction dir : dirs) {
			partialSplines.add(new LinearSpline(dir));
		}
	}
	
	/**
	 * Returns the sum of all the spline directions up to and including the one
	 * with the index in the parameter.
	 * @param highestIndex
	 * @return
	 */
	private Vector2f sumOfSplines(int highestIndex) {
		Vector2f splineOffset = new Vector2f();
		
		if(highestIndex >= partialSplines.size()) {
			return splineOffset;
		}
		
		for (int i = 0; i <= highestIndex; i++) {
			final Direction dir = partialSplines.get(i).getDirection();
			splineOffset.add(new Vector2f(dir.getDeltaX(), dir.getDeltaY()));
		}
		
		return splineOffset;
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		final int nbrOfSplines = partialSplines.size();
		final int currentSplineIndex = (int)((percent * nbrOfSplines ) / 100);
		
		if (currentSplineIndex == nbrOfSplines) {
			return sumOfSplines(currentSplineIndex - 1);
		}
		
		final Vector2f currentOffset = partialSplines.get(currentSplineIndex)
				.getPosition((percent * nbrOfSplines) % 100);
		
		if (currentSplineIndex > 0) {
			currentOffset.add(sumOfSplines(currentSplineIndex - 1));
		}
		
		return currentOffset;
	}

	@Override
	public Direction getDirection() {
		return Direction.NONE;
	}

}

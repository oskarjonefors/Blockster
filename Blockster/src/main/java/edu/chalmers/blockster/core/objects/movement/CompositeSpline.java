package edu.chalmers.blockster.core.objects.movement;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

public class CompositeSpline implements Spline {

	private final List<LinearSpline> partialSplines;
	private final Direction[] directions;
	
	public CompositeSpline(Direction... directions) {
		partialSplines = new ArrayList<LinearSpline>();
		this.directions = directions;
		
		for(final Direction dir : directions) {
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
		
		for (int i = 0; i <= Math.min(directions.length, highestIndex); i++) {
			final Direction dir = directions[i];
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

	public Direction[] getDirections() {
		return directions.clone();
	}
	
	@Override
	public Direction getDirection() {
		int deltaX = 0;
		int deltaY = 0;
		for (LinearSpline spline : partialSplines) {
			deltaX += spline.getDirection().getDeltaX();
			deltaY += spline.getDirection().getDeltaY();
		}
		return evaluateDirection(deltaX, deltaY);
	}
	
	private Direction evaluateDirection(int deltaX, int deltaY) {
		if (deltaX == 0 && deltaY == 0) {
			return Direction.NONE;
		}
		if (deltaX > 0) {
			if (deltaY == 0) {
				return Direction.RIGHT;
			} else {
				return deltaY > 0 ? Direction.UP_RIGHT : Direction.DOWN_RIGHT;
			}
		} else if (deltaX < 0) {
			if (deltaY == 0) {
				return Direction.LEFT;
			} else {
				return deltaY > 0 ? Direction.UP_LEFT : Direction.DOWN_LEFT;
			}
		} else {
			return deltaY > 0 ? Direction.UP : Direction.DOWN;
		}
	}

}

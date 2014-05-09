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
	
	@Override
	public Vector2f getPosition(float percent) {
		final int nbrOfSplines = partialSplines.size();
		return partialSplines.get((int) percent * nbrOfSplines)
				.getPosition((percent * nbrOfSplines) % 1);
	}

}

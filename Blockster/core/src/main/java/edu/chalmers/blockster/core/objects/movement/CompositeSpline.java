package edu.chalmers.blockster.core.objects.movement;

import java.util.ArrayList;
import javax.vecmath.Vector2f;

public class CompositeSpline implements Spline {

	private ArrayList<LinearSpline> partialSplines;
	
	public CompositeSpline(Direction... dirs) {
		partialSplines = new ArrayList<LinearSpline>();
		
		for(Direction dir : dirs) {
			partialSplines.add(new LinearSpline(dir));
		}
	}
	
	@Override
	public Vector2f getPosition(float percent) {
		int nbrOfSplines = partialSplines.size();
		return partialSplines.get((int) percent * nbrOfSplines)
				.getPosition((percent * nbrOfSplines) % 1);
	}

}

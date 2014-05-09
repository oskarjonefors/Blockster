package edu.chalmers.blockster.core.objects.movement;

/**
 * 
 * @author Eric Bjuhr
 *
 */
public enum Direction {
	LEFT(-1,0), RIGHT(1,0), UP_LEFT(-1,1), UP_RIGHT(1,1), DOWN_LEFT(-1,-1), DOWN_RIGHT(1,-1), DOWN(0,-1), NONE(0,0);
	
	public final int deltaX;
	public final int deltaY;
	
	private Direction(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
	
	/**
	 * Returns in which horizontal direction x2 is compared to x1
	 * @return Direction
	 */
	public static Direction getDirection(float x1, float x2) {
		return ((x1	- x2) > 0 ? Direction.LEFT : Direction.RIGHT);
	}
}

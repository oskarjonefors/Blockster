package edu.chalmers.Blockster.core;

/**
 * 
 * @author Eric Bjuhr
 *
 */
public enum Direction {
	LEFT(-1,0), RIGHT(1,0), CLIMB_LEFT(-1,1), CLIMB_RIGHT(1,1), FALL(0,-1), NONE(0,0);
	
	public final int deltaX;
	public final int deltaY;
	
	private Direction(int deltaX, int deltaY) {
		this.deltaX = deltaX;
		this.deltaY = deltaY;
	}
}

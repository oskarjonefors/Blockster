package edu.chalmers.Blockster.core.util;

public interface GridMap {

	public float getBlockWidth();

	public float getBlockHeight();

	public int getHeight();

	public int getWidth();

	public GridObject getBlock(int x, int y);

	public boolean hasBlock(int x, int y);

}

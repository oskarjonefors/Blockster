package edu.chalmers.blockster.core.util;

public interface GridMap {

	float getBlockWidth();

	float getBlockHeight();

	int getHeight();

	int getWidth();

	GridObject getBlock(int x, int y);

	boolean hasBlock(int x, int y);

}

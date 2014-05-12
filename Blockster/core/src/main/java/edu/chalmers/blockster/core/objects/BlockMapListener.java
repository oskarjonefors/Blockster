package edu.chalmers.blockster.core.objects;

public interface BlockMapListener {
	void blockInserted(int x, int y, Block block);
	void blockRemoved(int x, int y);
}
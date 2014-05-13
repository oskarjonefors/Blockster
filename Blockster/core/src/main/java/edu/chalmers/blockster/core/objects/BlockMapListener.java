package edu.chalmers.blockster.core.objects;

public interface BlockMapListener {
	void blockInserted(Block block);
	void blockRemoved(Block block);
}
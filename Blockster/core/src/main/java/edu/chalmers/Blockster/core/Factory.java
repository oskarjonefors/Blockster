package edu.chalmers.Blockster.core;

public interface Factory {
	
	public BlockMap createMap();

	public Player createPlayer(int startX, int startY, BlockLayer blockLayer);

}

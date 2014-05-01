package edu.chalmers.Blockster.core;

public interface Factory {
	
	public BlockMap createMap();

	public Player createPlayer(float startX, float startY, BlockLayer blockLayer);

}

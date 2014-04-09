package edu.chalmers.Blockster.core;

public interface Factory {
	
	public Player createPlayer(String spritePath, Model model);
	
	public BlockMap createMap();

}

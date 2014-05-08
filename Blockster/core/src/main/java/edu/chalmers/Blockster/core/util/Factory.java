package edu.chalmers.Blockster.core.util;

import edu.chalmers.Blockster.core.objects.Player;

public interface Factory {
	
	public GridMap createMap();

	public Player createPlayer(float startX, float startY, GridMap blockLayer);

}

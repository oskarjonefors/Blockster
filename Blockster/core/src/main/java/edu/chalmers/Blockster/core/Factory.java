package edu.chalmers.Blockster.core;

import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.Player;

public interface Factory {
	
	public BlockMap createMap();

	public Player createPlayer(float startX, float startY, BlockMap blockLayer);

}

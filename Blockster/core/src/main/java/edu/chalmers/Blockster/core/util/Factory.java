package edu.chalmers.Blockster.core.util;

import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Player;

public interface Factory {
	
	public BlockMap createMap();

	public Player createPlayer(float startX, float startY, BlockMap blockLayer);

}

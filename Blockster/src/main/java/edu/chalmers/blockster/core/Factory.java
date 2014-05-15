package edu.chalmers.blockster.core;

import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public interface Factory {
	
	void createMap();
	
	BlockMap getMap();

	Player createPlayer(float startX, float startY, BlockMap blockLayer);

}

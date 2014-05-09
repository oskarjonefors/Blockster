package edu.chalmers.blockster.core;

import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public interface Factory {
	
	BlockMap createMap();

	Player createPlayer(float startX, float startY, BlockMap blockLayer);

}

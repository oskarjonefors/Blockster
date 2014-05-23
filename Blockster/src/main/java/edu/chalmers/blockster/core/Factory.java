package edu.chalmers.blockster.core;

import edu.chalmers.blockster.core.objects.BlocksterMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;

public interface Factory {
	
	void createMap();
	
	BlocksterMap getMap();

	Player createPlayer(float startX, float startY, BlocksterMap blockLayer, World world);

}

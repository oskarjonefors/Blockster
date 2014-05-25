package edu.chalmers.blockster.core;

import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;

/**
 * A Factory interface. Blockster requires this functionality by an external framework.
 * @author Oskar Jönefors
 *
 */

public interface Factory {
	
	void createMap();
	
	BlockMap getMap();

	Player createPlayer(float startX, float startY, BlockMap blockLayer, World world);

}

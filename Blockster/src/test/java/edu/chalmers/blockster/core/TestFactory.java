package edu.chalmers.blockster.core;

import edu.chalmers.blockster.core.Factory;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

/**
 * Helper class to provide Factory functionality for the tests.
 * @author Oskar Jönefors
 *
 */

public class TestFactory implements Factory {

	private BlockMap blockMap;
	
	public TestFactory(int mapWidth, int mapHeight, float blockWidth,
			float blockHeight, int[][] playerStartingPositions) {
		blockMap = new BlockMap(mapWidth, mapHeight, blockWidth, blockHeight,
				playerStartingPositions);
	}
	
	@Override
	public void createMap() {

	}

	@Override
	public BlockMap getMap() {
		return blockMap;
	}

	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockMap);
	}

}

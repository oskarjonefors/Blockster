package edu.chalmers.blockster.core;

import java.awt.Point;
import java.util.List;

import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.BlocksterMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;

/**
 * Helper class to provide Factory functionality for the tests.
 * @author Oskar Jönefors
 *
 */

public class TestFactory implements Factory {

	private BlocksterMap blockMap;
	
	public TestFactory(int mapWidth, int mapHeight, float blockWidth,
			float blockHeight, List<Point> playerStartingPositions) {
		blockMap = new BlocksterMap(mapWidth, mapHeight, blockWidth, blockHeight,
				playerStartingPositions);
	}
	
	@Override
	public void createMap() {

	}

	@Override
	public BlocksterMap getMap() {
		return blockMap;
	}

	@Override
	public Player createPlayer(float startX, float startY, BlockMap map, World world) {
		return new Player(startX, startY, map, World.DAY);
	}

}

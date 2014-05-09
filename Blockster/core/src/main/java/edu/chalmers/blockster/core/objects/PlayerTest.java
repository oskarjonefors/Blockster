package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import edu.chalmers.blockster.core.gdx.view.GdxMap;
import edu.chalmers.blockster.core.objects.movement.Direction;

public class PlayerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
	}

	@Test
	public void testGetAdjecentBlock() {
		TmxMapLoader loader = new TmxMapLoader();
		TiledMap map = loader.load("test/testMap.tmx");
		BlockMap blockMap = new GdxMap(map);
		Player player = new Player(256, 256, blockMap);
		player.setX(384 -player.getWidth());
		player.setDirection(Direction.RIGHT);
		
		Block block = blockMap.getBlock(3, 2);
		
		
		assertTrue(player.getAdjacentBlock() == block);
	}

}

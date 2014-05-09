package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

import edu.chalmers.blockster.core.gdx.view.GdxMap;
import edu.chalmers.blockster.core.objects.movement.Direction;

public class PlayerTest {

	private static BlockMap blockMap;
	private static Player player;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		TiledMap map = new TiledMap();
		TiledMapTileLayer layer = new TiledMapTileLayer(10, 10, 128, 128);
		map.getLayers().add(layer);
		Cell cell = new Cell();
		TextureRegion textureRegion = new TextureRegion();
		TiledMapTile tile = new StaticTiledMapTile(textureRegion);
		cell.setTile(tile);
		layer.setCell(3, 2, cell);
		map.getProperties().put("nbrOfPlayers", "1");
		map.getProperties().put("playerStart1", "256:256");
		blockMap = new GdxMap(map);
		player = new Player(256, 256, blockMap);
		player.setWidth(100);
	}

	@Test
	public void testGetAdjecentBlock() {		

		player.setX(383 - player.getWidth() );
		player.setDirection(Direction.RIGHT);
		assertTrue(player.getAdjacentBlock() != null);
		
	}

}

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
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Movement;
	
public class BlockTest {
	
	private static BlockMap blockMap;
	private static Block block;
	private static AnimationState anim;

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
		block = blockMap.getBlock(3, 2);
		anim = new AnimationState(Movement.PULL_RIGHT);
	}

	@Test
	public void testMoveToNextPosition() {
		block.setAnimationState(anim);
		block.moveToNextPosition();
		assertTrue(Math.round(block.getX()) == 4);
	}

	@Test
	public void testSetAnimationState() {
		block.setAnimationState(anim);
		assertTrue(anim.getMovement() == Movement.PULL_RIGHT);
	}

	@Test
	public void testSetProperty() {
		block.setProperty("liftable");
		assertTrue(block.isLiftable());
	}

	@Test
	public void testIsSolid() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsLiftable() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsMovable() {
		fail("Not yet implemented");
	}

	@Test
	public void testHasWeight() {
		fail("Not yet implemented");
	}

}

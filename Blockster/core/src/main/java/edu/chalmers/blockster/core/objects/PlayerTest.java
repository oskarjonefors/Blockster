package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import javax.vecmath.Vector2f;

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
	private static Block block; 
	
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
		block = blockMap.getBlock(3, 2); 
	}

	@Test
	public void testGetAdjecentBlock() {		
		Boolean correct = false;
		
		player.setDirection(Direction.LEFT);
		correct = !(player.getAdjacentBlock() == block);
		
		player.setDirection(Direction.RIGHT);
		correct &= player.getAdjacentBlock() == block;
		
		assertTrue(correct);
		
	}
	
	@Test
	public void testGrabBlock(){
		boolean correct = false;
		
		//#1
		player.setDirection(Direction.RIGHT);
		player.grabBlock();
		correct = !player.isGrabbingBlock();
		player.endInteraction();
		
		//#2
		player.setDirection(Direction.LEFT);
		player.grabBlock();
		correct &= !player.isGrabbingBlock();
		player.endInteraction();
		
		//#3
		player.setDirection(Direction.RIGHT);
		block.setProperty("movable");
		player.grabBlock();
		correct &= player.isGrabbingBlock();
		player.endInteraction();
		
		//#4
		player.setDirection(Direction.LEFT);
		player.grabBlock();
		correct &= !player.isGrabbingBlock();
		player.endInteraction();
		
		assertTrue(correct);
	}
	
	@Test
	public void testIsNextToBlock(){
		
		boolean correct = false;
		
		//#1
		correct = player.isNextToBlock(block);
		
		//#2
		player.setX(512);
		correct &= player.isNextToBlock(block);
		
		//#3
		player.setY(0);
		correct &= !player.isNextToBlock(block);
		
		assertTrue(correct);
	}
	 @Test
	 public void testMove(){
		 
		 boolean correct = false;
		 Vector2f vector = new Vector2f(10,10);
		 
		 //#1
		 float prevPos = player.getX();
		 player.move(vector);
		 correct = (prevPos != player.getX());
		 
		 assertTrue(correct);
	 }

}

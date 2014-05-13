package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.assertTrue;

import javax.vecmath.Vector2f;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.movement.Direction;

public class PlayerTest {

	private static BlockMap blockMap;
	private static Player player;
	private static Block block;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		int[][] playerPositions = {{0,0}};
		blockMap = new BlockMap(10, 10, 128, 128, playerPositions);
		block = new Block(3,2, blockMap);
		
		blockMap.insertBlock(block); 
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
	 
	 @Test
	 public void testLiftBlock(){
		 
		 block.setProperty("liftable");
		 player.grabBlock();
		 
		 assertTrue(player.isLiftingBlock());
	 }

}

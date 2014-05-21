package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import javax.vecmath.Vector2f;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Player.World;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerTest {

	private static BlockMap blockMap;
	private static Player player;
	private static Block block; 
	

	@Before
	public void setUp()  {
		int[][] playerPositions = {{2,2}};
		blockMap = new BlockMap(10, 10, 128, 128, playerPositions);
		block = new Block(3,2, blockMap);
		player = new Player(2, 2, blockMap, World.DAY);
		player.setWidth(100);
		
		blockMap.insertBlock(block);
	}

	@Test
	public void testCanClimbBlock() {
		player.setDirection(Direction.LEFT);
		player.climbBlock();
		if (player.getAnimationState() != AnimationState.NONE) {
			fail("Could climb empty blocks");
		}
		
		setUp();
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (player.getAnimationState() == AnimationState.NONE) {
			fail("Couldn't climb a climbable block");
		}
		
		setUp();
		player.setDirection(Direction.LEFT);
		player.setX(4.5f * player.getScaleX());
		player.climbBlock();
		if (player.getAnimationState() != AnimationState.NONE) {
			fail("Climbable block should be out of reach");
		}
	}
	
	@Test
	public void testClimbingCollision() {
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (player.getAnimationState() == AnimationState.NONE) {
			fail("Couldn't climb a climbable block");
		}
		
		setUp();
		blockMap.insertBlock(new Block(3, 3, blockMap));
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (player.getAnimationState() != AnimationState.NONE) {
			fail("Could climb despite climbing collision");
		}
		
		setUp();
		blockMap.insertBlock(new Block(3, 3, blockMap));
		player.setDirection(Direction.LEFT);
		player.setX(4.5f * player.getScaleX());
		player.climbBlock();
		if (player.getAnimationState() != AnimationState.NONE) {
			fail("Climbable block should be out of reach");
		}
	}
	
	@Test
	public void testCanGrabBlock() {
		
	}
	
	@Test
	public void testCanLiftBlock() {
		
	}
	
	@Test
	public void testStartInteraction() {
		
	}
	
	@Test
	public void hasMovedBlock() {
		
	}
	
	@Test
	public void testIsSwitchingToMe() {
		
	}
	
	@Test
	public void testGetAdjacentBlock() {		
		Boolean correct = false;
		
		player.setDirection(Direction.LEFT);
		correct = !(player.getAdjacentBlock() == block);
		
		player.setDirection(Direction.RIGHT);
		correct &= (player.getAdjacentBlock() == block);
		
		assertTrue(correct);
	}
	
	@Test
	public void testGrabBlock() {
		boolean correct = false;
		
		//#1
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		correct = !player.isGrabbingBlock();
		player.endInteraction();
		
		//#2
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		correct &= !player.isGrabbingBlock();
		player.endInteraction();
		
		//#3
		player.setDirection(Direction.RIGHT);
		block.setProperty("movable");
		player.startInteraction();
		correct &= player.isGrabbingBlock();
		player.endInteraction();
		
		//#4
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		correct &= !player.isGrabbingBlock();
		player.endInteraction();
		
		assertTrue(correct);
	}
	
	@Test
	public void testIsNextToBlock() {
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
	 public void testMove() {
		 boolean correct = false;
		 Vector2f vector = new Vector2f(10,10);
		 
		 //#1
		 float prevPos = player.getX();
		 player.move(vector);
		 correct = (prevPos != player.getX());
		 
		 assertTrue(correct);
	 }
	 
	 @Test
	 public void testLiftBlock() {
		 
		 player.setDirection(Direction.RIGHT);
		 block.setProperty("liftable");
		 block.setProperty("movable");
		 player.startInteraction(); 
		 player.liftBlock();
		 
		 float lastposX = player.getX();
		 player.setAnimationState(new AnimationState(Movement.MOVE_LEFT));
		 player.updatePosition(0.5f);
		 
		 assertTrue(player.isLiftingBlock());
	 }
	 
	 @Test
	 public void testCollidedHorizontally() {
		 boolean correct = false;
		 
		 //#1
		 correct = player.collidedHorisontally();
		 
		 //#2
		 player.setX(384);
		 correct = !player.collidedHorisontally();
		 
		 assertTrue(correct);
	 }
	 
	 @Test
	 public void testCollidedVerticaly() {
		 boolean correct = false;
		 
		 //#1
		 correct = player.collidedVertically();
		 
		 //#2
		 player.setX(384);
		 player.setY(300);
		 correct = !player.collidedHorisontally();
		 
		 assertTrue(correct); 
	 }
	 
	 @Test
	 public void testEndInteraction() {
		 boolean correct = false;
		 player.setDirection(Direction.RIGHT);
		 
		 //#1
		 player.startInteraction();
		 player.endInteraction();
		 correct = !player.isGrabbingBlock();

		 //#2
		 player.liftBlock();
		 player.endInteraction();
		 correct &= !player.isLiftingBlock();
		 
		 assertTrue(correct);
	 }
	 
	 @Test
	 public void testGetProcessedBlock() {
		 player.setDirection(Direction.RIGHT);
		 block.setProperty("movable");
		 player.startInteraction();

		 assertTrue(player.getProcessedBlock() == block);
	 }
	 
	 @Test
	 public void testUpdatePosition() {
		 boolean correct = false;
		 player.setVelocityX(-100);
		 player.setVelocityY(100);
		 
		 //#1
		 float lastXPos = player.getOriginX();
		 player.updatePosition(0.1f);
		 correct = (lastXPos != player.getX());
		 
		 //#2
		 lastXPos = player.getX();
		 AnimationState animState = new AnimationState(Movement.MOVE_LEFT);
		 player.setAnimationState(animState);
		 player.updatePosition(1f);
		 correct &= (lastXPos != player.getX());
		 
		 assertTrue(correct);
	 }
	 
	 @Test
	 public void testClimbBlock() {
		 boolean correct = true;
		 player.setDirection(Direction.RIGHT);
		 
		 //#1 Block is there
		 player.climbBlock();
		 correct &= (player.getAnimationState().getMovement()
				 					== Movement.CLIMB_RIGHT);
		 player.setAnimationState(new AnimationState(Movement.NONE));
		 
		 //#2 Block is null
		 block = null;
		 player.climbBlock();
		 correct &= (player.getAnimationState().getMovement() == Movement.NONE);
		 
		 //#3 Block is empty
		 block = EmptyBlock.getInstance();
		 player.climbBlock();
		 correct &= (player.getAnimationState().getMovement() == Movement.NONE);
		 

//		 block.setProperty("solid");
		 
		 assertTrue(player.getAnimationState().getMovement() == Movement.CLIMB_RIGHT);
	 }
	 
	 @Test
	 public void testEnterTeleporter() {
		 boolean correct = false;
		 block.setProperty("teleporter");
		 
		 //#1
		 player.setDirection(Direction.RIGHT);
		 player.startInteraction();
		 correct = (player.getAnimationState().getMovement() == Movement.MOVE_RIGHT);
				 
		 //#2
		 Player player2 = new Player(4, 2, blockMap, World.DAY);
		 player2.setDirection(Direction.LEFT);
		 player2.startInteraction();
		 correct &= (player2.getAnimationState().getMovement() == Movement.MOVE_LEFT);
		 
		 assertTrue(correct);		 
	 }
}
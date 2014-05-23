package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerTest {

	private BlocksterMap blockMap;
	private Player player;
	private Block block; 
	

	@Before
	public void setUp()  {
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(2, 2));
		blockMap = new BlocksterMap(10, 10, 128, 128, playerPositions);
		block = new Block(3,2, blockMap);
		block.setProperty("solid");
		player = new Player(0, 0, blockMap, World.DAY);
		player.move(new Vector2f(256, 256));
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
		blockMap.insertBlock(new Block(2, 3, blockMap));
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (player.getAnimationState() != AnimationState.NONE) {
			fail("Could climb despite climbing collision");
		}
		
		setUp();
		Block liftable = new Block(1, 2, blockMap);
		liftable.setProperty("liftable");
		blockMap.insertBlock(liftable);
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		player.liftBlock();
		liftable.moveToNextPosition();
		liftable.getAnimationState().updatePosition(5);
		liftable.setAnimationState(AnimationState.NONE);
		blockMap.insertBlock(liftable);
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (liftable.isLiftable() && player.getAnimationState() == AnimationState.NONE) {
			fail("Couldn't climb a climbable block");
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
		boolean correct = false;
		
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
		
		//#1 Left side of block
		if (!player.isNextToBlock(block)) {
			fail("Player should be next to block");
		}
		
		//#2 Right side of block
		player.setDirection(Direction.LEFT);
		player.setX(512);
		if (!player.isNextToBlock(block)) {
			fail("Player should be next to block");
		}
		
		//#3
		player.setY(0);
		if (player.isNextToBlock(block)) {
			fail("Player shouldn't be next to block");
		}
	}
	 @Test
	 public void testMove() {
		 final Vector2f vector = new Vector2f(10,10);
		 
		 //#1
		 final float prevPos = player.getX();
		 player.move(vector);
		 
		 final float tolerance = 0.0001f;
		 
		 assertTrue(Math.abs(prevPos - player.getX()) > tolerance);
	 }
	 
	 @Test
	 public void testInteract() {
		 player.setDirection(Direction.LEFT);
		 player.interact();
		 
		 if (player.getVelocity().x == 0) {
			 fail("Should be moving freely");
		 }
	 }
	 
	 @Test
	 public void testLiftBlock() {
		 
		 player.setDirection(Direction.RIGHT);
		 block.setProperty("liftable");
		 block.setProperty("movable");
		 player.startInteraction(); 
		 player.liftBlock();
		 player.setAnimationState(new AnimationState(Movement.MOVE_LEFT));
		 player.updatePosition(0.5f);
		 
		 assertTrue(player.isLiftingBlock());
	 }
	 
	 @Test
	 public void testCollidedHorizontally() {
		 
		 //#1
		 boolean correct = !player.collidedHorisontally();
		 
		 //#2
		 player.move(new Vector2f(128, 0));
		 correct &= player.collidedHorisontally();
		 
		 assertTrue(correct);
	 }
	 
	 @Test
	 public void testCollidedVerticaly() {
		 //#1
		 final Block tempBlock = new Block(2, 1, blockMap);
		 tempBlock.setProperty("solid");
		 blockMap.insertBlock(tempBlock);
		 player.move(new Vector2f(0, -128));
		 boolean correct = player.collidedVertically();

		 //#2
		 player.move(new Vector2f(128, 44));
		 correct &= !player.collidedHorisontally();
		 
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
		 final float playerX = player.getX();
		 correct = (lastXPos != playerX);
		 
		 //#2
		 lastXPos = playerX;
		 AnimationState animState = new AnimationState(Movement.MOVE_LEFT);
		 player.setAnimationState(animState);
		 player.updatePosition(1f);
		 
		 final float tolerance = 0.0001f;
		 
		 correct &= (Math.abs(lastXPos - player.getX()) > tolerance);
		 
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
		 setUp();
		 block = null;
		 player.climbBlock();
		 correct &= (player.getAnimationState().getMovement() == Movement.NONE);
		 //#3 Block is empty
		 setUp();
		 block = EmptyBlock.getInstance();
		 player.climbBlock();
		 correct &= (player.getAnimationState().getMovement() == Movement.NONE);
		 assertTrue(correct);
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
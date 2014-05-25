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
	public void testCanGrabBlockPlayerBusy() {
		block.setProperty("movable");
		player.setAnimationState(new AnimationState(Movement.WAIT));
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		if (player.isGrabbingBlock()) {
			fail("Should not be grabbing block");
		}
	}

	@Test
	public void testCanGrabBlockOutOfRange() {
		block.setProperty("movable");
		player.setDirection(Direction.RIGHT);
		player.setX(0);
		player.startInteraction();
		if (player.isGrabbingBlock()) {
			fail("Should not be grabbing block");
		}
	}

	@Test
	public void testCanGrabBlockPlayerLiftingBlock() {
		//Make blocks on both sides of player movable
		Block anotherBlock = new Block(1, 2, blockMap);
		anotherBlock.setProperty("movable");
		block.setProperty("movable");

		//Make the block that will be lifted liftable
		block.setProperty("liftable");

		//Begin lifting the block to the right
		player.setDirection(Direction.RIGHT);
		player.startInteraction();

		//Finish grabbing animation
		block.getAnimationState().updatePosition(5);
		player.updatePosition(5);

		//Lift up the block
		player.liftBlock();

		//Finish the animation
		block.getAnimationState().updatePosition(5);
		player.updatePosition(5);

		//Move the actors to the next positions
		block.moveToNextPosition();
		player.moveToNextPosition();

		//Remove animations
		block.setAnimationState(AnimationState.NONE);
		player.setAnimationState(AnimationState.NONE);

		//Insert blocks to map
		blockMap.insertBlock(block);
		blockMap.insertBlock(anotherBlock);

		//Check if preconditions were met
		if (!player.isLiftingBlock()) {
			fail("At this point the player should be lifting "+block);
		}

		//Try to grab block on the left side and hopefully fail
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		if (player.isGrabbingBlock()) {
			fail("Should not be grabbing block");
		}
	}

	@Test
	public void testCanLiftBlockOutOfRange() {
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();

		//Finish grabbing animation
		block.getAnimationState().updatePosition(5);
		player.updatePosition(5);

		//Test if out of range
		player.setX(0);
		player.liftBlock();
		if (player.getAnimationState().getMovement() != Movement.GRAB_RIGHT) {
			fail(player + " should not be lifting " + block 
					+ " due to being out of range");
		}
	}

	@Test
	public void testCanLiftNoBlockInParticular() {
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.liftBlock();
		if (player.getAnimationState().getMovement() == Movement.LIFT_RIGHT ||
				player.getAnimationState().getMovement() == Movement.LIFT_LEFT) {
			fail(player + " should not be lifting " + block 
					+ " due to not actually being a grabbed block");
		}
	}

	@Test
	public void testCanMove() {
		if (!player.canMove(Direction.LEFT)) {
			fail("Player should be able to move left");
		}

		player.setX(0);
		if (player.canMove(Direction.LEFT)) {
			fail("Player shouldn't be able to move left");
		}

		player.setX(player.getScaleX() + 1);
		Block solidBlock = new Block(0, 2, blockMap);
		solidBlock.setProperty("solid");
		blockMap.insertBlock(solidBlock);
		if (player.canMove(Direction.LEFT)) {
			fail("Player shouldn't be able to move left");
		}
	}

	@Test
	public void hasMovedBlock() {
		if (player.hasMovedBlock()) {
			fail("Player has not actually moved a block");
		}

		Block floor = new Block(3, 1, blockMap);
		floor.setProperty("solid");

		block.setProperty("movable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.updatePosition(5);
		player.interact();

		if (player.hasMovedBlock() && 
				player.getAnimationState().getMovement().getDuration() == 0) {
			fail("Player has not actually moved a block");
		}

		if (!player.hasMovedBlock()) {
			fail("Player has actually moved a block");
		}
	}

	@Test
	public void testIsSwitchingToMe() {
		if (player.isSwitchingToMe()) {
			fail("Shouldn't be switching to player");
		}
		player.switchingToMe(true);
		if (!player.isSwitchingToMe()) {
			fail("Should be switching to player");
		}
		player.switchingToMe(false);
		if (player.isSwitchingToMe()) {
			fail("Shouldn't be switching to player");
		}
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
		final float tolerance = 0.0001f;

		final float prevPos = player.getX();
		player.move(vector);


		if (Math.abs(prevPos - player.getX()) <= tolerance) {
			fail("Didn't move");
		}
	}
	
	@Test
	public void testMoveAndCollideDown() {
		final Vector2f vector = new Vector2f(0,-32);
		Block solidBlock = new Block(2, 1, blockMap);
				
		solidBlock.setProperty("solid");
		blockMap.insertBlock(solidBlock);
		player.move(vector);

		if (!player.collidedVertically()) {
			fail("Moved");
		}
	}
	
	@Test
	public void testMoveAndCollideUp() {
		final Vector2f vector = new Vector2f(0, 130 - player.getHeight());
		Block solidBlock = new Block(2, 3, blockMap);
				
		solidBlock.setProperty("solid");
		blockMap.insertBlock(solidBlock);
		player.move(vector);

		if (!player.collidedVertically()) {
			fail("Moved");
		}
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
	public void testLiftBlockSuccess() {

		player.setDirection(Direction.RIGHT);
		block.setProperty("liftable");
		block.setProperty("movable");
		player.startInteraction(); 
		player.liftBlock();

		if (!player.isLiftingBlock()) {
			fail("Should be lifting block");
		}
	}

	@Test
	public void testLiftBlockCantLift() {

		player.setDirection(Direction.RIGHT);
		block.setProperty("liftable");
		block.setProperty("movable");
		player.liftBlock();

		if (player.isLiftingBlock()) {
			fail("Should not be lifting block");
		}
	}

	@Test
	public void testLiftBlockCantBeLifted() {

		player.setDirection(Direction.RIGHT);
		block.setProperty("movable");
		player.startInteraction(); 
		player.liftBlock();

		if (player.isLiftingBlock()) {
			fail("Should not be lifting block");
		}
	}

	@Test
	public void testLiftBlockNeitherPreconditions() {
		player.setDirection(Direction.RIGHT);
		player.liftBlock();

		if (player.isLiftingBlock()) {
			fail("Should not be lifting block");
		}
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
	public void testCollidedVertically() {
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
	public void testEndGrabInteraction() {
		block.setProperty("movable");
		player.setDirection(Direction.RIGHT);

		player.startInteraction();
		if (!player.isGrabbingBlock()) {
			fail("Invalid precondition");
		}
		player.endInteraction();
		if (player.isGrabbingBlock()) {
			fail("Should no longer be grabbing a block");
		}

	}

	@Test
	public void testEndLiftInteractionRight() {
		block.setProperty("movable");
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);

		player.startInteraction();
		if (!player.isGrabbingBlock()) {
			fail("Invalid precondition");
		}
		player.updatePosition(5);
		player.liftBlock();
		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		if (!player.isLiftingBlock()) {
			fail("Invalid precondition");
		}

		player.endInteraction();
		if (player.isLiftingBlock()) {
			fail("Should no longer be lifting a block");
		}
		if (block.getAnimationState().getMovement() != Movement.PLACE_RIGHT) {
			fail("Should be placing block");
		}
	}

	@Test
	public void testEndLiftInteractionLeft() {
		Block anotherBlock = new Block(1, 2, blockMap);
		anotherBlock.setProperty("movable");
		anotherBlock.setProperty("liftable");
		blockMap.insertBlock(anotherBlock);
		player.setDirection(Direction.LEFT);

		player.startInteraction();
		if (!player.isGrabbingBlock()) {
			fail("Invalid precondition");
		}
		player.updatePosition(5);
		player.liftBlock();
		player.updatePosition(5);
		anotherBlock.getAnimationState().updatePosition(5);
		if (!player.isLiftingBlock()) {
			fail("Invalid precondition");
		}

		player.endInteraction();
		if (player.isLiftingBlock()) {
			fail("Should no longer be lifting a block");
		}
		if (anotherBlock.getAnimationState().getMovement() != Movement.PLACE_LEFT) {
			fail("Should be placing block");
		}
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
		player.setDirection(Direction.RIGHT);
		player.climbBlock();
		if (player.getAnimationState().getMovement()
				!= Movement.CLIMB_RIGHT) {
			fail("Should be climbing block");
		} 
	}

	@Test
	public void testClimbEmptyBlock() {
		player.setDirection(Direction.RIGHT);
		blockMap.removeBlock(block);
		player.climbBlock();
		if (player.getAnimationState().getMovement() != Movement.NONE) {
			fail("Shouldn't be climbing block");
		}
	}

	@Test
	public void testClimbLeft() {
		Block solidBlock = new Block(1, 2, blockMap);
		solidBlock.setProperty("solid");
		blockMap.insertBlock(solidBlock);
		player.setDirection(Direction.LEFT);
		player.climbBlock();
		if (player.getAnimationState().getMovement()
				!= Movement.CLIMB_LEFT) {
			fail("Should be climbing block");
		} 
	}

	@Test
	public void testClimbLiftingBlockLeft() {
		Block anotherBlock = new Block(1, 2, blockMap);
		anotherBlock.setProperty("solid");
		block.setProperty("liftable");
		blockMap.insertBlock(anotherBlock);

		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.liftBlock();

		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		block.moveToNextPosition();
		blockMap.insertBlock(block);

		player.setDirection(Direction.LEFT);
		player.climbBlock();

		if (player.getAnimationState().getMovement() != Movement.LIFTING_CLIMB_LEFT) {
			fail("Should be climbing with block");
		}
	}

	@Test
	public void testClimbLiftingBlockRight() {
		Block anotherBlock = new Block(1, 2, blockMap);
		anotherBlock.setProperty("liftable");
		blockMap.insertBlock(anotherBlock);

		player.setDirection(Direction.LEFT);
		player.startInteraction();
		player.liftBlock();

		player.updatePosition(5);
		anotherBlock.getAnimationState().updatePosition(5);
		anotherBlock.moveToNextPosition();
		blockMap.insertBlock(anotherBlock);

		player.setDirection(Direction.RIGHT);
		player.climbBlock();

		if (player.getAnimationState().getMovement() != Movement.LIFTING_CLIMB_RIGHT) {
			fail("Should be climbing with block");
		}
	}

	@Test
	public void testClimbWhileGrabbing() {
		block.setProperty("movable");

		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.climbBlock();
		if (player.getAnimationState().getMovement() != Movement.GRAB_RIGHT) {
			fail("Should still be grabbing");
		}
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
	
	@Test
	public void testIsMoving() {
		if (player.isMoving()) {
			fail("Should not be moving yet");
		}
		
		player.setDefaultVelocity(Direction.LEFT);
		player.updatePosition(5);
		
		if (!player.isMoving()) {
			fail("Should be moving");
		}
	}
	
	@Test
	public void testIsLiftingOrPlacingPlaceLeft() {
		player.setAnimationState(new AnimationState(Movement.PLAYER_PLACE_LEFT));
		if (!player.isLiftingOrPlacing()) {
			fail("Player is actually lifting or placing");
		}
	}
	
	@Test
	public void testIsLiftingOrPlacingPlaceRight() {
		player.setAnimationState(new AnimationState(Movement.PLAYER_PLACE_RIGHT));
		if (!player.isLiftingOrPlacing()) {
			fail("Player is actually lifting or placing");
		}
	}
	
	@Test
	public void testIsLiftingOrPlacingLiftLeft() {
		player.setAnimationState(new AnimationState(Movement.PLAYER_LIFT_LEFT));
		if (!player.isLiftingOrPlacing()) {
			fail("Player is actually lifting or placing");
		}
	}
	
	@Test
	public void testIsLiftingOrPlacingLiftRight() {
		player.setAnimationState(new AnimationState(Movement.PLAYER_LIFT_RIGHT));
		if (!player.isLiftingOrPlacing()) {
			fail("Player is actually lifting or placing");
		}
	}
	
	@Test
	public void testNotLiftingOrPlacing() {
		player.setAnimationState(AnimationState.NONE);
		if (player.isLiftingOrPlacing()) {
			fail("Player is actually  neither lifting nor placing");
		}
	}
	
	@Test
	public void testInteractWhenLifting() {
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.liftBlock();
		if (!player.isInteracting()) {
			fail("Incorrect preconditions");
		}
		
		player.interact();
		if (player.hasMovedBlock()) {
			fail("Should not have moved a block yet");
		}
		
		player.updatePosition(5);
		player.getProcessedBlock().getAnimationState().updatePosition(5);
		
		//Direction changed, reset the wait counter.
		player.interact();
		player.interact();
		player.interact();
		if (player.hasMovedBlock()) {
			fail("Should still not have moved yet");
		}
		
		player.interact();
		if (!player.hasMovedBlock()) {
			fail("Should not have moved a block yet");
		}
	}
	
	@Test
	public void testInteractWhenLiftedBlockNotDone() {
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.liftBlock();
		
		player.updatePosition(5);
		player.interact();
		if (player.hasMovedBlock()) {
			fail("Should not have moved a block yet");
		}
	}
	
	@Test
	public void testInteractWhenLiftingPlayerNotDone() {
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.liftBlock();
		
		player.getProcessedBlock().getAnimationState().updatePosition(5);
		player.interact();
		if (player.hasMovedBlock()) {
			fail("Should not have moved a block yet");
		}
	}
	
	@Test
	public void testIsNextToNull() {
		if (player.isNextToBlock(null)) {
			fail("Player cannot be close to null");
		}
	}
	
	@Test
	public void testIsNotNextToBlockHorisontally() {
		block.setProperty("movable");
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		
		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		
		player.setX(0);
		if (player.isNextToBlock(block)) {
			fail("Player is pretty far away from block");
		}
	}
	
	@Test
	public void testIsNotNextToBlockVertically() {
		block.setProperty("movable");
		player.setDirection(Direction.LEFT);
		player.startInteraction();
		
		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		
		player.setY(6 * player.getScaleY());
		if (player.isNextToBlock(block)) {
			fail("Player is pretty far away from block");
		}
	}
	
	@Test
	public void testIsNotNextToLiftedBlockVertically() {
		block.setProperty("movable");
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		player.liftBlock();
		if (!player.isLiftingBlock()) {
			fail("Incorrect precondition");
		}
		
		player.setY(6 * player.getScaleY());
		if (player.isNextToBlock(block)) {
			fail("Player is pretty far away from block");
		}
	}
	
	@Test
	public void testIsNotNextToLiftedBlockHorisontally() {
		block.setProperty("movable");
		block.setProperty("liftable");
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		player.updatePosition(5);
		block.getAnimationState().updatePosition(5);
		player.liftBlock();
		if (!player.isLiftingBlock()) {
			fail("Incorrect precondition");
		}
		
		player.setX(0);
		if (player.isNextToBlock(block)) {
			fail("Player is pretty far away from block");
		}
	}
	
}
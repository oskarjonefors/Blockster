package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.interactions.AbstractPlayerInteraction;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlockTest {
	
	private BlocksterMap blockMap;
	private Block block;
	private Movement movementLeft;
	private int startX;
	private int startY;
	

	@Before
	public void setUp() {
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(0, 0));
		blockMap = new BlocksterMap(10, 10, 128, 128, playerPositions);
		startX = 3;
		startY = 2;
		block = new Block(startX, startY, blockMap);
		block.setProperty("weight");
		movementLeft = Movement.PULL_LEFT;
		
		blockMap.insertBlock(block);
	}

	@Test
	public void testGetInteraction() {
		Player player = new Player(2, 2, blockMap, World.DAY);
		AbstractPlayerInteraction interaction;
		
		block.setProperty("movable");
		
		player.setWidth(100);
		player.setHeight(100);
		player.setDirection(Direction.RIGHT);
		
		player.startInteraction();
		interaction = block.getInteraction();
		if (interaction == null || interaction == AbstractPlayerInteraction.NONE) {
			fail("Should have an interaction");
		}
		
	}
	
	@Test
	public void testCanMove() {
		final Movement movementRight = Movement.PULL_RIGHT;
		Direction dir = movementLeft.getDirection();
		block.setAnimationState(new AnimationState(movementLeft));
		
		//#1 Can move
		if (!block.canMove(dir)) {
			fail("Block should be able to move");
		}
		
		//#2 Can not move
		Block newBlock = new Block(startX - 1, startY, blockMap);
		blockMap.insertBlock(newBlock);
		if (block.canMove(dir)) {
			fail("Block shouldn't be able to move");
		}
		
		//#3 Move outside the map width
		block.removeFromGrid();
		block.setX(0);
		block.setY(startY);
		blockMap.insertBlock(block);
		if (block.canMove(dir)) {
			fail("Block shouldn't be able to move");
		}
		
		//#4 Move outside the map width
		block.removeFromGrid();
		block.setX(blockMap.getWidth() - 1);
		block.setY(startY);
		blockMap.insertBlock(block);
		block.setAnimationState(new AnimationState(movementRight));
		dir = movementRight.getDirection();
		if (block.canMove(dir)) {
			fail("Block shouldn't be able to move");
		}
	}

	@Test
	public void testMoveToNextPosition() {
		block.setAnimationState(new AnimationState(movementLeft));
		block.moveToNextPosition();
		if (Math.round(block.getX()) != movementLeft.getDirection()
												.getDeltaX() + startX) {
			fail("Did not move to the next position");
		}
	}

	@Test
	public void testSetAnimationState() {
		block.setAnimationState(new AnimationState(movementLeft));
		if (block.getAnimationState().getMovement() != movementLeft) {
			fail("did not set animation state");
		}
	}

	@Test
	public void testBlock() {
		Block block = new Block(startX, startY, blockMap);
		if (block.isLifted()) {
			fail("Should not be lifted");
		}
	}

	@Test
	public void testSetProperty() {
		
		//#1 Liftable
		String property = "liftable";
		block.setProperty(property);
		if (!block.isLiftable()) {
			fail("Should be liftable");
		}
		
		//#2 Movable
		property = "movable";
		block.setProperty(property);
		if (!block.isMovable()) {
			fail("Should be movable");
		}
		
		//#3 Weight
		property = "weight";
		block.setProperty(property);
		if (!block.hasWeight()) {
			fail("Should have weight");
		}
		
		//#4 Solid
		property = "solid";
		block.setProperty(property);
		if (!block.isSolid()) {
			fail("Should be solid");
		}
	}

	@Test
	public void testIsLifted() {
		block.setLifted(true);
		if (!block.isLifted()) {
			fail("Should be lifted");
		}
	}

	@Test
	public void testIsTeleporter() {
		//#1 Non-teleporter block
		if (block.isTeleporter()) {
			fail("Incorrect precondition");
		}
		
		//#2 Teleporter block
		block.setProperty("teleporter");
		if (!block.isTeleporter()) {
			fail("Incorrect boolean value");
		}
	}

	@Test
	public void testIsSolid() {
		//#1 Non-solid block
		if (block.isSolid()) {
			fail("Incorrect preconditions");
		}
		
		//#2 Solid block
		block.setProperty("solid");
		if (!block.isSolid()) {
			fail("Incorrect boolean value");
		}
	}

	@Test
	public void testIsLiftable() {
		
		//#1 Non-liftable block
		if (block.isLiftable()) {
			fail("Incorrect preconditions");
		}
		
		//#2 Liftable block
		block.setProperty("liftable");
		if (!block.isLiftable()) {
			fail("Incorrect boolean value");
		}
	}

	@Test
	public void testIsMovable() {
		//#1 Non-movable block
		if (block.isMovable()) {
			fail("Incorrect preconditions");
		}
		
		//#2 Movable block
		block.setProperty("movable");
		if (!block.isMovable()) {
			fail("Incorrect boolean value");
		}
	}

	@Test
	public void testHasWeight() {
		//#1 Has no weight
		if (block.isSolid()) {
			fail("Incorrect preconditions");
		}
		
		//#2 Has weight
		block.setProperty("weight");
		if (!block.hasWeight()) {
			fail("Incorrect boolean value");
		}
	}
	
	@Test
	public void testFallNormally() {
		block.fallDown();

		//Is animation correct?
		final AnimationState anim = block.getAnimationState();
		final Movement movement = anim.getMovement();
		if (movement != Movement.FALL_DOWN) {
			fail("Should be falling down");
		}
		
		//Move block so the falling down happens
		block.moveToNextPosition();
		if (block.getY() != 1) {
			fail("Did not move to the correct place");
		}
	}
	
	@Test
	public void testFallCollisionBelow() {
		Block newBlock = new Block(startX, 1, blockMap);
		newBlock.setProperty("solid");
		blockMap.insertBlock(newBlock);
		block.fallDown();
		block.moveToNextPosition();
		if (block.getY() == 1) {
			fail("Moved when it shouldn't");
		}
	}
	
	@Test
	public void testFallCollisionBelowNotSolid() {
		Block newBlock = new Block(startX, 1, blockMap);
		blockMap.insertBlock(newBlock);
		block.fallDown();
		block.moveToNextPosition();
		if (block.getY() != 1) {
			fail("Did not move to the correct place");
		}
	}
	
	@Test
	public void testFallWeightless() {
		block.removeProperty("Weight");
		block.fallDown();
		block.moveToNextPosition();
		if (block.getY() == 1) {
			fail("Moved when it shouldn't");
		}	
	}
	
	@Test
	public void testFallWeightlessAndCollision() {
		Block newBlock = new Block(startX, 1, blockMap);
		newBlock.setProperty("solid");
		block.removeProperty("weight");
		blockMap.insertBlock(newBlock);
		block.fallDown();
		block.moveToNextPosition();
		if (block.getY() == 1) {
			fail("Moved when it shouldn't");
		}
	}
	
	@Test
	public void testFallLiftedCollisionBeneath() {
		Player player = new Player(2, 2, blockMap, World.DAY);
		Block anotherBlock = new Block(2, 1, blockMap);
		
		block.setProperty("movable");
		block.setProperty("liftable");
		anotherBlock.setProperty("solid");
		blockMap.insertBlock(anotherBlock);
		
		player.setWidth(100);
		player.setHeight(100);
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		
		player.liftBlock();
		player.updatePosition(5);
		block.moveToNextPosition();
		blockMap.updateActiveBlocks(5);
		
		if (block.getAnimationState() != AnimationState.NONE) {
			fail("Should have stopped moving by now");
		}
	}
	
	@Test
	public void testFallLiftedNoCollisionBeneath() {
		Player player = new Player(2, 2, blockMap, World.DAY);
		AnimationState anim;
		block.setProperty("movable");
		block.setProperty("liftable");
		
		player.setWidth(100);
		player.setHeight(100);
		player.setDirection(Direction.RIGHT);
		player.startInteraction();
		
		player.liftBlock();
		player.updatePosition(5);
		block.moveToNextPosition();
		blockMap.updateActiveBlocks(5);
		
		anim = block.getAnimationState();
		
		if (anim == AnimationState.NONE) {
			fail("Shouldn't have stopped moving yet: "+anim);
		}
	}

	@Test
	public void testSetLifted() {
		block.setLifted(true);
		if (!block.isLifted()) {
			fail("Wasn't lifted");
		}
		
		block.setLifted(false);
		if (block.isLifted()) {
			fail("Wasn't put down");
		}
	}

	@Test
	public void testRemoveFromGrid() {
		block.removeFromGrid();
		if (blockMap.hasBlock(startX, startY)) {
			fail("Was not removed from grid");
		}
	}

	@Test
	public void testCanBeClimbed() {
		
		if (!block.canBeClimbed()) {
			fail("Should be climbable");
		}
		
		Block newBlock = new Block(startX, startY + 1, blockMap);
		blockMap.insertBlock(newBlock);
		if (block.canBeClimbed()) {
			fail("Should not be climbable");
		}
	}

	@Test
	public void testCanBeGrabbed() {
		
		if (block.canBeGrabbed()) {
			fail("Should not be grabable");
		}
		
		block.setProperty("movable");
		if (!block.canBeGrabbed()) {
			fail("Should be grabable");
		}
		
		block.removeProperty("movable");
		block.setProperty("liftable");
		if (!block.canBeGrabbed()) {
			fail("Should be grabable");
		}
		
		block.setProperty("movable");
		if (!block.canBeGrabbed()) {
			fail("Should be grabable");
		}
	}
	
	@Test
	public void testCanBeLiftedBlockAbove() {
		blockMap.insertBlock(new Block(3, 3, blockMap));
		block.setProperty("liftable");
		if (block.canBeLifted()) {
			fail("There is a block blocking the way");
		}
	}
	
	@Test
	public void testCanBeLiftedNotLiftable() {
		if (block.canBeLifted()) {
			fail("The block isn't liftable");
		}
	}
	
	@Test
	public void testCanBeLiftedDoubleFail() {
		blockMap.insertBlock(new Block(3, 3, blockMap));
		if (block.canBeLifted()) {
			fail("There is a block blocking the way, and the block isn't liftable");
		}
	}
	
	public void testCanBeLiftedSuccess() {
		block.setProperty("liftable");
		if (!block.canBeLifted()) {
			fail("The block should be liftable");
		}
	}
}
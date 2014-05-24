package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

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
	public void testFallDown() {
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
		//Make the animation done and set it to none
		float duration = movement.getDuration();
		anim.updatePosition(duration);
		block.setAnimationState(AnimationState.NONE);
		
		//Place a block in the way
		Block newBlock = new Block(startX, 0, blockMap);
		newBlock.setProperty("solid");
		blockMap.insertBlock(newBlock);
		block.fallDown();
		block.moveToNextPosition();
		if (block.getY() == 0) {
			fail("Did not move to the correct place");
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
}
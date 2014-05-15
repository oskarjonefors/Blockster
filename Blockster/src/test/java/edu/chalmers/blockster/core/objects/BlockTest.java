package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlockTest {
	
	private BlockMap blockMap;
	private Block block;
	private AnimationState anim;
	private Movement movement;
	private int startX;
	private int startY;
	private String property;

	@Before
	public void setUp() {
		int[][] playerPositions = {{0,0}};
		startX = 3;
		startY = 2;
		blockMap = new BlockMap(10, 10, 128, 128, playerPositions);
		block = new Block(startX, startY, blockMap);
		movement = Movement.PULL_LEFT;
		anim = new AnimationState(movement);
		
		blockMap.insertBlock(block);
	}

	@Ignore
	@Test
	public void testCanMove() {
		boolean correct = true;
		Direction dir = movement.getDirection();
		block.setAnimationState(anim);
		
		//#1 Can move
		correct &= block.canMove(movement.getDirection());
		
		//#2 Can not move
		Block newBlock = new Block(startX - 1, startY, blockMap);
		blockMap.insertBlock(newBlock);
		correct &= !block.canMove(dir);
		
		fail("Not yet implemented");
	}

	@Test
	public void testMoveToNextPosition() {
		block.setAnimationState(anim);
		block.moveToNextPosition();
		assertTrue(Math.round(block.getX()) == movement.getDirection()
												.getDeltaX() + startX);
	}

	@Test
	public void testSetAnimationState() {
		block.setAnimationState(anim);
		assertTrue(anim.getMovement() == movement);
	}

	@Test
	public void testBlock() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetProperty() {
		boolean correct = true;
		
		//#1 Liftable
		property = "liftable";
		block.setProperty(property);
		correct &= block.isLiftable();
		
		//#2 Movable
		property = "movable";
		block.setProperty(property);
		correct &= block.isMovable();
		
		//#3 Weight
		property = "weight";
		block.setProperty(property);
		correct &= block.hasWeight();
		
		//#4 Solid
		property = "solid";
		block.setProperty(property);
		correct &= block.isSolid();
		
		assertTrue(correct);
	}

	@Ignore
	@Test
	public void testIsLifted() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testIsTeleporter() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsSolid() {
		boolean correct = true;
		
		//#1 Non-solid block
		correct &= !block.isSolid();
		
		//#2 Solid block
		block.setProperty("solid");
		correct &= block.isSolid();
		
		assertTrue(correct);
	}

	@Test
	public void testIsLiftable() {
		boolean correct = true;
		
		//#1 Non-liftable block
		correct &= !block.isLiftable();
		
		//#2 Liftable block
		block.setProperty("liftable");
		correct &= block.isLiftable();
		
		assertTrue(correct);
	}

	@Test
	public void testIsMovable() {
		boolean correct = true;
		
		//#1 Non-movable block
		correct &= !block.isMovable();
		
		//#2 Movable block
		block.setProperty("movable");
		correct &= block.isMovable();
		
		assertTrue(correct);
	}

	@Test
	public void testHasWeight() {
		boolean correct = true;
		
		//#1 Has no weight
		correct &= !block.isSolid();
		
		//#2 Has weight
		block.setProperty("weight");
		correct &= block.hasWeight();
		
		assertTrue(correct);
	}

	@Ignore
	@Test
	public void testFallDown() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testSetLifted() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveFromGrid() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testCanBeClimbed() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testCanBeGrabbed() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testRemoveProperty() {
		fail("Not yet implemented");
	}

}

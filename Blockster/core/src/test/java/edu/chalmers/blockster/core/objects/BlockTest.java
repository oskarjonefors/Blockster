package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Movement;
	
public class BlockTest {
	
	private BlockMap blockMap;
	private Block block;
	private AnimationState anim;

	@Before
	public void setUp() {
		int[][] playerPositions = {{0,0}};
		blockMap = new BlockMap(10, 10, 128, 128, playerPositions);
		block = new Block(3,2, blockMap);
		anim = new AnimationState(Movement.PULL_LEFT);
		
		blockMap.insertBlock(block);
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

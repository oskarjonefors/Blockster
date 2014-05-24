package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlocksterMap;
import edu.chalmers.blockster.core.objects.BlocksterObject;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlockLiftedInteractionTest {
	
	private BlocksterMap blockMap;
	private Block block;
	private Player player;
	private BlockLiftedInteraction interaction;
	
	@Before
	public void setUp() {
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(0, 0));
		blockMap = new BlocksterMap(3, 4, 128, 128, playerPositions);
		player = new Player(0, 0, blockMap, World.DAY);
		player.setDirection(Direction.RIGHT);
		block = new Block(1, 0, blockMap);
		interaction = new BlockLiftedInteraction(player, block, blockMap);
	}
	
	@Test
	public void testClimbDown() {
		blockMap.removeBlock(block);
		player.setX(1 * player.getScaleX());
		player.setY(1 * player.getScaleY());
		block.setX(2);
		block.setY(1);
		blockMap.insertBlock(block);
		interaction.startInteraction();
		player.getAnimationState().updatePosition(5);
		block.getAnimationState().updatePosition(5);
		block.moveToNextPosition();
		blockMap.insertBlock(block);
		
		interaction.interact(Direction.LEFT);
		
		if (player.getAnimationState().getMovement() != Movement.CLIMB_DOWN_LEFT) {
			fail("Did not set the correct animation state on "+player);
		}
		
		
		if (block.getAnimationState().getMovement() != Movement.CLIMB_DOWN_LEFT) {
			fail("Did not set the correct animation state on "+block);
		}
	}
	
	@Test
	public void testInteractFailure() {
		startInteraction();
		blockMap.insertBlock(new Block(1,1,blockMap));
		
		interaction.interact(Direction.RIGHT);
		block.removeFromGrid();
		finishAnimation(block);
		finishAnimation(player);
		
		System.out.println(block);
		System.out.println(player);
		if(block.getX() != 0) {
			fail("Block is positioned incorrectly");
		}
		

		if(player.getX() != 0) {
			fail("Block is positioned incorrectly");
		}
	}

	@Test
	public void testInteractSuccess() {
		startInteraction();
		
		interaction.interact(Direction.RIGHT);
		block.removeFromGrid();
		finishAnimation(block);
		finishAnimation(player);
		if (block.getX() != 1) {
			fail("Block is positioned incorrectly");
		}
		
		final float tolerance = 0.00001f;
		
		if (Math.abs(player.getX() - player.getScaleX()) > tolerance) {
			fail("Player is positioned incorrectly");
		}
	}
	
	private void finishAnimation(BlocksterObject object) {
		AnimationState anim = object.getAnimationState();
		anim.updatePosition(anim.getMovement().getDuration());
		System.out.println(object+" "+anim);
		object.moveToNextPosition();
		object.setAnimationState(AnimationState.NONE);
	}
	
	private void startInteraction() {
		block.setY(1);
		player.setY(128);
		for (int i = 0; i < blockMap.getWidth(); i++) {
			Block floor = new Block(i, 0, blockMap);
			floor.setProperty("solid");
			blockMap.insertBlock(floor);
		}
		interaction.startInteraction();
		block.removeFromGrid();
		finishAnimation(block);
		finishAnimation(player);
	}

	@Test
	public void testEndInteractionSuccess() {
		interaction.startInteraction();
		interaction.endInteraction();
		assertTrue(!player.isLiftingBlock());
	}
	
	@Test
	public void testEndInteractionFail() {
		interaction.startInteraction();
		block.removeFromGrid();
		finishAnimation(block);
		
		blockMap.insertBlock(new Block(1, 0, blockMap));
		blockMap.insertBlock(new Block(1, 1, blockMap));
		interaction.endInteraction();
		
		assertTrue(player.isLiftingBlock());
	}
	
	@Test
	public void testEndInteractionPlaceAbove() {
		AnimationState anim;
		
		interaction.startInteraction();
		block.removeFromGrid();
		finishAnimation(block);
		
		blockMap.insertBlock(new Block(1, 0, blockMap));
		interaction.endInteraction();
		anim = block.getAnimationState();
		
		assertTrue(anim.getMovement() == Movement.MOVE_RIGHT);
	}


	@Test
	public void testStartInteractionSuccess() {
		interaction.startInteraction();
		assertTrue(player.isLiftingBlock());
	}
	
	@Test
	public void testStartInteractionFailure1() {
		blockMap.insertBlock(new Block(0, 1, blockMap));
		interaction.startInteraction();
		assertTrue(!player.isLiftingBlock());
	}
	
	@Test
	public void testStartInteractionFailure2() {
		blockMap.insertBlock(new Block(1, 1, blockMap));
		interaction.startInteraction();
		assertTrue(!player.isLiftingBlock());
	}

	@Test
	public void testCanPerformMoveCollisionPlayer() {
		startInteraction();
		blockMap.insertBlock(new Block(1, 1, blockMap));
		assertTrue(interaction.getMovePerformType(Direction.RIGHT) == 0);
	}
	
	@Test
	public void testCanPerformMoveCollisionBlock() {
		startInteraction();
		blockMap.insertBlock(new Block(1, 2, blockMap));
		blockMap.insertBlock(new Block(1, 0, blockMap));
		assertTrue(interaction.getMovePerformType(Direction.RIGHT) == 0);
	}
	
	@Test
	public void testCanPerformMoveCollisionBoth() {
		startInteraction();
		blockMap.insertBlock(new Block(1, 2, blockMap));
		blockMap.insertBlock(new Block(1, 1, blockMap));
		assertTrue(interaction.getMovePerformType(Direction.RIGHT) == 0);
	}
	
	@Test
	public void testCanPerformMoveSuccess() {
		startInteraction();
		if(interaction.getMovePerformType(Direction.RIGHT) != 1) {
			fail("Should be able to perform move.");
		};
	}
	
}

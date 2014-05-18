package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.BlocksterObject;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlockLiftedInteractionTest {
	
	private BlockMap blockMap;
	private Block block;
	private Player player;
	private BlockLiftedInteraction interaction;
	
	@Before
	public void setUp() {
		blockMap = new BlockMap(3, 4, 128, 128, new int[][] {{0,0}});
		player = new Player(0, 0, blockMap);
		player.setDirection(Direction.RIGHT);
		block = new Block(1, 0, blockMap);
		interaction = new BlockLiftedInteraction(player, block, blockMap);
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
		assertTrue(block.getX() == 0 && player.getX() == 0);
	}

	@Test
	public void testInteractSuccess() {
		startInteraction();
		
		interaction.interact(Direction.RIGHT);
		block.removeFromGrid();
		finishAnimation(block);
		finishAnimation(player);
		
		System.out.println(block);
		System.out.println(player);
		assertTrue(block.getX() == 1 && player.getX() == 1*player.getScaleX());
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
		assertTrue(!interaction.canPerformMove(Direction.RIGHT));
	}
	
	@Test
	public void testCanPerformMoveCollisionBlock() {
		startInteraction();
		blockMap.insertBlock(new Block(1, 2, blockMap));
		blockMap.insertBlock(new Block(1, 0, blockMap));
		assertTrue(!interaction.canPerformMove(Direction.RIGHT));
	}
	
	@Test
	public void testCanPerformMoveCollisionBoth() {
		startInteraction();
		blockMap.insertBlock(new Block(1, 2, blockMap));
		blockMap.insertBlock(new Block(1, 1, blockMap));
		assertTrue(!interaction.canPerformMove(Direction.RIGHT));
	}
	
	@Test
	public void testCanPerformMoveSuccess() {
		startInteraction();
		assertTrue(interaction.canPerformMove(Direction.RIGHT));
	}
	
}

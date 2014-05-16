package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlockGrabbedInteractionTest {
	
	private Player player;
	private Block block;
	private BlockMap blockMap;
	private BlockGrabbedInteraction interaction;
	
	@Before
	public void setUp() {
		blockMap = new BlockMap(10, 10, 128, 128, new int[][] {{1, 3}});
		player = new Player(1, 3, blockMap);
		block = new Block(2, 3, blockMap);
		interaction = new BlockGrabbedInteraction(player, block, blockMap);
		block.setProperty("movable");
		blockMap.insertBlock(new Block(1, 2, blockMap));
		blockMap.insertBlock(new Block(3, 3, blockMap));
		blockMap.insertBlock(block);
		player.setWidth(100);
		startInteraction();
	
	}

	@Test
	public void testInteractLeft() {
		boolean success = true;
		
		player.setDirection(Direction.LEFT);
		interaction.interact(Direction.LEFT);
		
		success &= player.getAnimationState()
				.getMovement() == Movement.NONE;
		
		blockMap.insertBlock(new Block(0, 2, blockMap));
		interaction.interact(Direction.LEFT);
		
		success &= player.getAnimationState()
				.getMovement().isPullMovement();
		
		assertTrue(success);
	}

	@Test
	public void testInteractRight() {
		boolean success = true;
		player.setDirection(Direction.RIGHT);
		interaction.interact(Direction.RIGHT);
		success &= (player.getAnimationState()
				.getMovement() == Movement.NONE);
		
		blockMap.getBlock(3,3).setProperty("movable");
		
		interaction.interact(Direction.RIGHT);
		success &= (player.getAnimationState()
				.getMovement() != Movement.NONE);

		assertTrue(success);
	}
	
	@Test
	public void testEndInteraction() {
		boolean success = (player.getAnimationState() 
						!= AnimationState.NONE);
		
		interaction.endInteraction();
		
		success &= player.getAnimationState()
				== AnimationState.NONE;
		
		assertTrue(success);
	}
	
	@Test
	public void testStartInteraction() {
		assertTrue(player.getAnimationState() 
				== AnimationState.GRAB_RIGHT);

	}
	
	private void startInteraction() {
		player.setDirection(Direction.RIGHT);
		interaction.startInteraction();
	}
	

}

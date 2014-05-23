package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.Player.World;
import edu.chalmers.blockster.core.objects.movement.Direction;

/**
 * Only tests that PlayerInteraction.NONE doesn't give any exceptions.
 * @author Eric Bjuhr
 *
 */
public class PlayerInteractionTest {
	
	@Test
	public void testGetters() {
		BlockMap blockMap = new BlockMap(10, 10, 100, 100, new int[][] {{2, 4}});
		Block block = new Block(1, 1, blockMap);
		Player player = new Player(2, 4, blockMap, World.DAY);
		
		PlayerInteraction interaction = new BlockGrabbedInteraction(player, block, blockMap);
		if (interaction.getInteracted() != block) {
			fail("Incorrect return value from getInteracted()");
		}
		
		if(interaction.getInteractor() != player) {
			fail("Incorrect return value from getInteractor()");	
		}
	}
	
	@Test
	public void testInteract() {
		PlayerInteraction.NONE.interact(Direction.NONE);
		assertTrue(true);
	}

	@Test
	public void testEndInteraction() {
		PlayerInteraction.NONE.endInteraction();
		assertTrue(true);
	}

	@Test
	public void testStartInteraction() {
		PlayerInteraction.NONE.startInteraction();
		assertTrue(true);
	}

}

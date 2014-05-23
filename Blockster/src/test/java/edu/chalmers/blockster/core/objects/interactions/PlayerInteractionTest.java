package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlocksterMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.Direction;

/**
 * Only tests that PlayerInteraction.NONE doesn't give any exceptions.
 * @author Eric Bjuhr
 *
 */
public class PlayerInteractionTest {
	
	@Test
	public void testGetters() {
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(2, 4));
		BlocksterMap blockMap = new BlocksterMap(10, 10, 100, 100, playerPositions);
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

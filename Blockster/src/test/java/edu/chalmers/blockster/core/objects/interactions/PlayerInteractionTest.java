package edu.chalmers.blockster.core.objects.interactions;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.chalmers.blockster.core.objects.movement.Direction;

/**
 * Only tests that PlayerInteraction.NONE doesn't give any exceptions.
 * @author Eric Bjuhr
 *
 */
public class PlayerInteractionTest {
	
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

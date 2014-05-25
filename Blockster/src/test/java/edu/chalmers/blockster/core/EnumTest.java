package edu.chalmers.blockster.core;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class EnumTest {

	@Test
	public void testGameStateValues() {
		if (GameState.values().length != 3) {
			fail("Incorrect size of array");
		}
	}
	
	@Test
	public void testGameStateValueOf() {
		if (GameState.valueOf("GAME_OVER") != GameState.GAME_OVER) {
			fail("Wrong value of string");
		}
	}
	
	@Test
	public void testWorldValues() {
		if (World.values().length != 2) {
			fail("Incorrect size of array");
		}
	}
	
	@Test
	public void testWorldValueOf() {
		if (World.valueOf("DAY") != World.DAY) {
			fail("Wrong value of string");
		}
	}
	
	@Test
	public void testDirectionValues() {
		if (Direction.values().length != 9) {
			fail("Incorrect size of array");
		}
	}
	
	@Test
	public void testDirectionValueOf() {
		if (Direction.valueOf("LEFT") != Direction.LEFT) {
			fail("Wrong value of string");
		}
	}

	@Test
	public void testMovementValues() {
		if (Movement.values().length != 25) {
			fail("Incorrect size of array");
		}
	}
	
	@Test
	public void testMovementValueOf() {
		if (Movement.valueOf("MOVE_LEFT") != Movement.MOVE_LEFT) {
			fail("Wrong value of string");
		}
	}
	
}

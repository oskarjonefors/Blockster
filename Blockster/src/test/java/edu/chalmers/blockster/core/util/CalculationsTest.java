package edu.chalmers.blockster.core.util;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.Player.World;

public class CalculationsTest {

	private BlockMap blockMap;
	private Block block;
	private Player player;
	
	@Test(expected=InvocationTargetException.class)
	public void testConstructor() throws Exception {
		Constructor<Calculations> c = Calculations.class.getDeclaredConstructor();
		c.setAccessible(true);
		c.newInstance();
	}
	
	@Before
	public void setUp() {
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(0, 0));
		blockMap = new BlockMap(3, 3, 128, 128, playerPositions);
		block = new Block(1, 1, blockMap);
		player = new Player(1, 1, blockMap, World.DAY);
		player.setWidth(100);
		player.setHeight(100);
		block.setProperty("solid");
		blockMap.setBlock(1, 1, block);
	}
	
	
	@Test
	public void testCollisionEitherCorner() {
		testLowerLeftCorner();
		testUpperLeftCorner();
		testUpperRightCorner();
		testLowerRightCorner();
		testNeitherCorner();
		testNonSolidEitherCorner();
	}

	private void testLowerLeftCorner() {
		player.setX(0.5f * player.getScaleX());
		player.setY(0.5f * player.getScaleY());
		
		if (!Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Should be collision");
		}
	}
	
	private void testUpperLeftCorner() {
		player.setX(0.5f * player.getScaleX());
		player.setY(1.5f * player.getScaleY());
		
		if (!Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Should be collision");
		}
	}
	
	private void testUpperRightCorner() {
		player.setX(1.5f * player.getScaleX());
		player.setY(1.5f * player.getScaleY());
		
		if (!Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Should be collision");
		}
	}
	
	private void testLowerRightCorner() {
		player.setX(1.5f * player.getScaleX());
		player.setY(0.5f * player.getScaleY());
		
		if (!Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Should be collision");
		}
	}
	
	private void testNeitherCorner() {
		player.setX(2*player.getScaleX());
		player.setY(2*player.getScaleY());
		
		if (Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Shouldn't be collision");
		}
	}
	
	private void testNonSolidEitherCorner() {
		player.setX(1*player.getScaleX());
		player.setY(1*player.getScaleY());
		block.removeProperty("solid");
		
		if (Calculations.collisionEitherCorner(player, blockMap)) {
			fail("Shouldn't be collision");
		}
	}
	
	@Test
	public void testGetClosestNumber() {
		int dif = 45, targetVal = Math.round(360 % 360);
		int[] ortagonalDegrees = { 0, 90, 180, 270 };
		int val =  Calculations.getClosestNumber(targetVal, dif, 0, ortagonalDegrees);
		assertTrue(val == 0);
	}	
}
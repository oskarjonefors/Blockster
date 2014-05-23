package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector2f;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class BlocksterObjectTest {
	
	private BlockMap blockMap;
	private Player player;

	@Before
	public void setUp(){
		final List<Point> playerPositions = new ArrayList<Point>(); 
		playerPositions.add(new Point(0, 0));
		blockMap = new BlockMap(10, 10, 128, 128, playerPositions);
		player = new Player(0,0,blockMap, World.DAY);
	}

	@Test
	public void testSetDefaultVelocity() {
		final AnimationState anim = new AnimationState(Movement.MOVE_RIGHT);
		final Direction dir = anim.getMovement().getDirection();
		player.setVelocityX(0f);
		player.setVelocityY(0f);
		Vector2f vector = new Vector2f(4f*blockMap.getBlockWidth(),0f);
		player.setDefaultVelocity(dir);
		
		assertTrue(player.getVelocity().equals(vector));
	}

	@Test
	public void testSetVelocityX() {
		Vector2f vector = new Vector2f(4f*blockMap.getBlockWidth(),0f);
		player.setVelocityX(700f);
		
		assertTrue(player.getVelocity().equals(vector));
	}
	
	@Test
	public void testGetAdjecentBlock() {
		player.setDirection(Direction.NONE);
		Block block = player.getAdjacentBlock();
		assertTrue(block instanceof EmptyBlock);
	}

}

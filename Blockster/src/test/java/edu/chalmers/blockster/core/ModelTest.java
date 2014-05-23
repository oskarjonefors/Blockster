package edu.chalmers.blockster.core;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.vecmath.Vector2f;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class ModelTest {
	
	private Model model;
	private Model model2;
	private Model model3;
	private Model model4;
	private Factory factory;
	
	@Before
	public void setUp() {
		List<Point> playerStartingPositions = new ArrayList<Point>();
		playerStartingPositions.add(new Point(100, 100));
		playerStartingPositions.add(new Point(120, 100));
		factory = new TestFactory(320, 240, 1f, 1f, playerStartingPositions);
		
		model = new Model(factory, "blockModel");
		model2 = new Model(factory, "BlockModel");
		model3 = new Model(factory, "BlockModel");
		model4 = new Model(factory, "BlockModel ");
	}

	@Test
	public void testHashCode() {
		
		final int modelHash = model.hashCode();
		final int model2Hash = model2.hashCode();
		final int model3Hash = model3.hashCode();
		final int model4Hash = model4.hashCode();
		final Model nullModel = new Model(factory, null);
		
		if (modelHash == model2Hash) {
			fail("Hashcode shouldn't be equal");
		}
		
		if (model2Hash != model3Hash) {
			fail("Hashcode should be equal");
		}
		if (model3Hash == model4Hash) {
			fail("Hashcode should be equal");
		}
		
		if (nullModel.hashCode() != 31) {
			fail("Hashcode should be 31");
		}
		
	}

	@Test
	public void testModel() {
		final String name = "testModel";
		Model testModel = new Model(factory, name);
		
		assertTrue(name.equals(testModel.getName()));
		
	}

	@Test
	public void testInit() {
		model.init();
		final BlockMap map = model.getMap();
		
		//Check correct dimensions
		if (map.getWidth() != 320) {
			fail("Map has the incorrect width");
		}
		
		if (map.getHeight() != 240) {
			fail("Map has the incorrect width");
		}
		
		//Check player starts
		final Player p1 = model.getActivePlayer();
		if (p1.getX() != 100) {
			fail(p1 + " is incorrectly positioned");
		}
		
		if (p1.getY() != 100) {
			fail(p1 + " is incorrectly positioned");
		}
		
		model.nextPlayer();
		final Player p2 = model.getActivePlayer();
		
		if (p2.getX() != 120) {
			fail(p2 + " is incorrectly positioned");
		}
		
		if (p2.getY() != 100) {
			fail(p2 + " is incorrectly positioned");
		}
		
	}

	@Test
	public void testCompareTo() {		
		if (model.compareTo(model2) == 0) {
			fail();
		}
		if (model2.compareTo(model3) != 0) {
			fail();
		}
		if (model3.compareTo(model4) == 0) {
			fail();
		}
	}

	@Test
	public void testEqualsObject() {
		if (model.equals(model2)) {
			fail("The two models should not be equal");
		}
		if (model3.equals(model4)) {
			fail("The two models should not be equal");
		}
		if (!model2.equals(model3)) {
			fail("The two models should be equal");
		}
		if (model.equals(null)) {
			fail("The model should not be null");
		}
	}

	@Test
	public void testGetActiveBlocks() {
		Set<Block> activeBlocks = model.getActiveBlocks();
		activeBlocks.add(new Block(0, 0, null));
		
		assertTrue(activeBlocks.size() == 1);
	}

	@Test
	public void testGetActivePlayer() {
		assertFalse(model.getActivePlayer() == null);
	}

	@Test
	public void testGetMap() {

	}

	@Test
	public void testGetName() {
		final String name = "blockModel";
		if (!name.equals(model.getName())) {
			fail("The two strings should be equal");
		}
		if (name.equals(model2.getName())) {
			fail("The two strings shouldn't be equal");
		}
	}

	@Test
	public void testGetPlayers() {
		List<Player> players = model.getPlayers();
		
		if (players.isEmpty()) {
			fail("There are no players");
		}
		if (players.size() != 2) {
			fail("There should be exactly two players");
		}
		
	}

	@Test
	public void testGetCurrentWorld() {
		assertTrue(model.getCurrentWorld() != null);
	}
	
	@Test
	public void testNextPlayer() {
		
		final Player p1 = model.getActivePlayer();
		
		model.nextPlayer();
		
		final Player p2 = model.getActivePlayer();
		
		model.nextPlayer();
		
		final Player p3 = model.getActivePlayer();
		
		p3.setAnimationState(new AnimationState(Movement.PULL_LEFT));
		
		model.nextPlayer();
		
		final Player p4 = model.getActivePlayer();
		
		if (p1.equals(p2)) {
			fail(p1 + " should not equal " + p2);
		}
		
		if (!p1.equals(p3)) {
			fail(p1 + " should be equal to " + p3);
		}
		
		if (!p3.equals(p4)) {
			fail(p3 + " should be equal to " + p4);
		}
	}

	@Test
	public void testResetStartPositions() {
		
		final Player p1 = model.getActivePlayer();
		final float player1StartX = p1.getX();
		final float player1StartY = p1.getY();
		model.nextPlayer();
		
		final Player p2 = model.getActivePlayer();
		final float player2StartX = p2.getX();
		final float player2StartY = p2.getY();
		
		p1.move(new Vector2f(10, 10));
		p2.move(new Vector2f(-10, -10));
		
		final float movedPlayer1X = p1.getX();
		final float movedPlayer1Y = p1.getY();
		
		final float movedPlayer2X = p2.getX();
		final float movedPlayer2Y = p2.getY();
		
		model.resetStartPositions();
		
		final float resetPlayer1X = p1.getX();
		final float resetPlayer1Y = p1.getY();
		
		final float resetPlayer2X = p2.getX();
		final float resetPlayer2Y = p2.getY();
		
		final float tolerance = 0.0001f;
		
		if (Math.abs(player1StartX - movedPlayer1X) <= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player1StartY - movedPlayer1Y) <= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player2StartX - movedPlayer2X) <= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player2StartY - movedPlayer2Y) <= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player1StartX - resetPlayer1X) >= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player1StartY - resetPlayer1Y) >= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player2StartX - resetPlayer2X) >= tolerance) {
			fail("Player should have moved");
		}
		if (Math.abs(player2StartY - resetPlayer2Y) >= tolerance) {
			fail("Player should have moved");
		}
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testUpdateException() {
		model.update(-4);
	}
	
	@Test
	public void testUpdateWin() {
		final int preGoalPlayers = model.getPlayers().size();
		final int postGoalPlayers;
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		model.playerReachedGoal();
		model.getActivePlayer().setAnimationState(new AnimationState(Movement.MOVE_LEFT));
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		model.update(Movement.MOVE_LEFT.getDuration() / 2);
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		model.update(Movement.MOVE_LEFT.getDuration() / 2 + 1);
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		
		postGoalPlayers = model.getPlayers().size();
		model.playerReachedGoal();
		model.getActivePlayer().setAnimationState(new AnimationState(Movement.MOVE_LEFT));
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		model.update(Movement.MOVE_LEFT.getDuration() / 2);
		if (model.getGameState() != GameState.GAME_RUNNING) {
			fail("Game should be running");
		}
		model.update(Movement.MOVE_LEFT.getDuration() / 2 + 1);
		if (postGoalPlayers >= preGoalPlayers) {
			fail("There should be at least one player in goal already");
		}
		if (model.getGameState() != GameState.GAME_WON) {
			fail("Game should be running");
		}
	}
	
	@Test
	public void testUpdatePlayerAnimation() {
		final Player activePlayer = model.getActivePlayer();
		activePlayer.setAnimationState(new AnimationState(Movement.PUSH_RIGHT));
		model.update(0.01f);
		if (activePlayer.getAnimationState().getMovement() != Movement.PUSH_RIGHT) {
			fail("Player should still be moving");
		}
		model.update(100);
		if (activePlayer.getAnimationState() != AnimationState.NONE) {
			fail("Player should not have an animation");
		}
	}
	
	@Test
	public void testCollision() {
		final Player activePlayer = model.getActivePlayer();
		final BlockMap blockMap = model.getMap();
		
		model.update(5);
		final Vector2f velocity = activePlayer.getVelocity();
		if (velocity.y == 0) {
			fail("Should be falling");
		}
		
		final Block solidBlock = new Block(50, 50, blockMap);
		solidBlock.setProperty("Solid");
		blockMap.insertBlock(solidBlock);
		activePlayer.setX(50f);
		activePlayer.setY(51f);
		model.update(0.01f);
		
		if (velocity.y != 0) {
			fail("Should not be moving vertically");
		}
		
	}
	
	
}

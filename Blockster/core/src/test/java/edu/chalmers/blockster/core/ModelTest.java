package edu.chalmers.blockster.core;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Set;

import javax.vecmath.Vector2f;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import edu.chalmers.blockster.core.gdx.view.GdxFactory;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public class ModelTest {
	
	private static Model model;
	private static Model model2;
	private static Model model3;
	private static Model model4;
	private static Factory factory;
	private static TiledMap tiledMap;
	
	private class TestHelper {
		private String name;
		
		public TestHelper(String name) {
			this.name = name;
		}
		
		private String getName() {
			return name;
		}
		
		private void setName(String name) {
			this.name = name;
		}
	}
	
	@Before
	public void setUp() {
		tiledMap = new TiledMap();
		final MapProperties props = tiledMap.getProperties();
		props.put("nbrOfPlayers", "2");
		props.put("playerStart1", "100:100");
		props.put("playerStart2", "120:100");
		
		final TiledMapTileLayer tileLayer = new TiledMapTileLayer(320, 240, 1, 1);
		tiledMap.getLayers().add(tileLayer);
		
		factory = new GdxFactory(tiledMap);
		
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
		
		boolean correct = true;
		
		correct &= modelHash != model2Hash;
		correct &= model2Hash == model3Hash;
		correct &= model3Hash != model4Hash;
		
		assertTrue(correct);
	}

	@Test
	public void testModel() {
		Model testModel = new Model(factory, "testModel");
		
		assertTrue(testModel.getName().equals("testModel"));
		
	}

	@Test
	public void testInit() {
		boolean correct = true;
		model.init();
		final BlockMap map = model.getMap();
		
		//Check correct dimensions
		correct &= map.getWidth() == 320;
		correct &= map.getHeight() == 240;
		
		//Check player starts
		final Player p1 = model.getActivePlayer();
		correct &= p1.getX() == 100;
		correct &= p1.getY() == 100;
		
		model.nextPlayer();
		
		final Player p2 = model.getActivePlayer();
		
		correct &= p2.getX() == 120;
		correct &= p2.getY() == 100;
		
		assertTrue(correct);
		
	}

	@Test
	public void testCompareTo() {
		
		boolean correct = true;
		
		correct &= model.compareTo(model2) != 0;
		correct &= model2.compareTo(model3) == 0;
		correct &= model3.compareTo(model4) != 0;
		
		assertTrue(correct);
	}

	@Test
	public void testEqualsObject() {
		
		final TestHelper helper = new TestHelper("blockModel");
		
		boolean correct = true;
		correct &= !model.equals(helper);
		correct &= !model.equals(model2);
		correct &= !model.equals("blockModel");
		correct &= !model3.equals(model4);
		correct &= model2.equals(model3);
		
		assertTrue(correct);
	}

	@Test
	public void testGetActiveBlocks() {
		Set<Block> activeBlocks = model.getActiveBlocks();
		activeBlocks.add(new Block(0, 0, null));
		
		assertTrue(model.getActiveBlocks().size() == 1);
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
		final String blockModelString = "blockModel";
		boolean correct = true;
		correct &= blockModelString.equals(model.getName());
		correct &= !blockModelString.equals(model2.getName());
		assertTrue(correct);
	}

	@Test
	public void testGetPlayers() {
		boolean correct = true;
		List<Player> players = model.getPlayers();
		
		correct &= !players.isEmpty();
		correct &= players.size() == 2;
		
		assertTrue(correct);
	}

	@Test
	public void testNextPlayer() {
		boolean correct = true;
		
		final Player p1 = model.getActivePlayer();
		
		model.nextPlayer();
		
		final Player p2 = model.getActivePlayer();
		
		model.nextPlayer();
		
		final Player p3 = model.getActivePlayer();
		
		correct &= !p1.equals(p2);
		correct &= p1.equals(p3);
		
		assertTrue(correct);
	}

	@Test
	public void testResetStartPositions() {
		boolean correct = true;
		
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
		
		correct &= player1StartX != movedPlayer1X;
		correct &= player1StartY != movedPlayer1Y;
		correct &= player2StartX != movedPlayer2X;
		correct &= player2StartY != movedPlayer2Y;
		
		correct &= player1StartX == resetPlayer1X;
		correct &= player1StartY == resetPlayer1Y;
		correct &= player2StartX == resetPlayer2X;
		correct &= player2StartY == resetPlayer2Y;
		
		assertTrue(correct);
	}
}

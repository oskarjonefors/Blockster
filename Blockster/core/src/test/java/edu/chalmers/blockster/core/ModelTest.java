package edu.chalmers.blockster.core;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import edu.chalmers.blockster.core.gdx.util.GdxFactory;
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
		
		private TestHelper(String name) {
			this.name = name;
		}
		
		private String getName() {
			return name;
		}
		
		private void setName(String name) {
			this.name = name;
		}
	}
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tiledMap = new TiledMap();
		final MapProperties props = tiledMap.getProperties();
		props.put("nbrOfPlayers", "2");
		props.put("playerStart1", "100:100");
		props.put("playerStart2", "120:100");
		
		final TiledMapTileLayer tileLayer = new TiledMapTileLayer(100, 100, 128, 128);
		tiledMap.getLayers().add(tileLayer);
		
		factory = new GdxFactory(tiledMap);
		
		model = new Model(factory, "blockModel");
		model2 = new Model(factory, "BlockModel");
		model3 = new Model(factory, "BlockModel");
		model4 = new Model(factory, "BlockModel ");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Test
	public void testHashCode() {
		
		final int modelHash = model.hashCode();
		final int model2Hash = model2.hashCode();
		final int model3Hash = model3.hashCode();
		final int model4Hash = model4.hashCode();
		
		assertFalse(modelHash != model2Hash);
		assertTrue(model2Hash == model3Hash);
		assertFalse(model3Hash != model4Hash);
	}

	@Test
	public void testModel() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testInit() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testCompareTo() {
		assertFalse(model.compareTo(model2) == 0);
		assertTrue(model2.compareTo(model3) == 0);
		assertFalse(model3.compareTo(model4) == 0);
	}

	@Test
	public void testEqualsObject() {
		
		final TestHelper helper = new TestHelper("blockModel");
		assertFalse(model.equals(helper));
		assertFalse(model.equals(model2));
		assertFalse(model.equals("blockModel"));
		assertFalse(model3.equals(model4));
		assertTrue(model2.equals(model3));
		
	}

	@Test
	public void testGetActiveBlocks() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetActivePlayer() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetMap() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testGetName() {
		final String blockModelString = "blockModel";
		assertTrue(blockModelString.equals(model.getName()));
		assertFalse(blockModelString.equals(model2.getName()));
	}

	@Test
	public void testGetPlayers() {
		assertFalse(model.getPlayers().isEmpty());
		assertTrue(model.getPlayers().size() == 2);
	}

	@Test
	public void testGetProcessedBlock() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsGrabbingBlock() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testIsLiftingBlock() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testInteractPlayer() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testNextPlayer() {
		Player p1 = model.getActivePlayer();
		
		model.nextPlayer();
		
		Player p2 = model.getActivePlayer();
		
		model.nextPlayer();
		
		Player p3 = model.getActivePlayer();
		
		assertFalse(p1.equals(p2));
		assertTrue(p1.equals(p3));
	}

	@Test
	public void testResetStartPositions() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testStopProcessingBlock() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public void testUpdate() {
		fail("Not yet implemented"); // TODO
	}

}

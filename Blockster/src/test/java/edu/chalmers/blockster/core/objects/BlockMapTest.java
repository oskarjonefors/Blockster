package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.gdx.view.MiniMap;

public class BlockMapTest {

	private BlockMap blockMap;
	private MiniMap map;
	private List<Point> startPos;
	
	@Before
	public void setUp() {
		final List<Point> startPos = new ArrayList<Point>();
		startPos.add(new Point(1, 1));
		startPos.add(new Point(2, 2));
		blockMap = new BlockMap(8, 12, 48, 48, startPos);
		map = new MiniMap(2, 2, new Player(0f, 0f, blockMap, World.DAY));
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestWidth() {
		blockMap = new BlockMap(-1, 1, 48, 48, startPos);
		// if no AssertionError, set test to fail
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestHeight(){
		blockMap = new BlockMap(1, -1, 48, 48, startPos);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestBlockWidth() {
		blockMap = new BlockMap(1, 1, -48, 48, startPos);
		assertTrue(false);
	}
	
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestBlockHeigth() {
		blockMap = new BlockMap(1, 1, 48, 48, startPos);
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestNbrOfPlayers() {
		try {
			final List<Point> emptyList = new ArrayList<Point>();
			blockMap = new BlockMap(1, 1, 48, 48, emptyList);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}
	@Test(expected=AssertionError.class)
	public void constructorFailureTestStartPosX() {
		try {
			final List<Point> starts = new ArrayList<Point>();
			starts.add(new Point(-1, 1));
			starts.add(new Point(2, 2));
			blockMap = new BlockMap(1, 1, 48, 48, starts);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestStartPosY() {
		try {
			final List<Point> starts = new ArrayList<Point>();
			starts.add(new Point(1, -1));
			starts.add(new Point(2, 2));
			blockMap = new BlockMap(1, 1, 48, 48, starts);
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}

	@Test
	public void addActiveBlockListenerTest() {
		
		if (blockMap.getListeners().size() == 0) {
			fail("incorrect number of listeners");
		}
		
		blockMap.addActiveBlockListener(map);
		
		if (blockMap.getActiveBlockListener().size() != 1) {
			fail("incorrect number of listeners");
		}

		if (!blockMap.getActiveBlockListener().get(0).equals(map)) {
			fail("incorrect listener was added");
		}
	}
	
	@Test
	public void removeActiveBlockListenerTest(){
		
		blockMap.addActiveBlockListener(map);
		
		if (blockMap.getActiveBlockListener().size() != 1) {
			fail("incorrect number of listeners");
		}
		
		blockMap.removeActiveBlockListener(map);
		
		if (!blockMap.getActiveBlockListener().isEmpty()) {
			fail("block map should not have any listeners");
		}
	}
	
	@Test
	public void addListenerTest() {
		
		if (blockMap.getListeners().size() != 0) {
			fail("incorrect number of listeners");
		}
		blockMap.addListener(map);
		if (blockMap.getListeners().size() != 1) {
			fail("incorrect number of listeners");
		}
	}
	
	@Test
	public void removeListenerTest() {
		blockMap.addListener(map);
		if (blockMap.getListeners().size() != 1) {
			fail("incorrect number of listeners");
		}
		blockMap.removeListener(map);
		if (blockMap.getListeners().size() != 0) {
			fail("incorrect number of listeners");
		}
	}
	
	@Test
	public void insertBlockTest() {
		Block block = new Block(2, 1, blockMap);
		
		blockMap.insertBlock(block);
		if (blockMap.getBlock(2, 1) != block) {
			fail("block was not inserted in blockmap");
		}
	}
	
	@Test
	public void removeBlockTest() {
		Block block = new Block(3, 2, blockMap);
		
		blockMap.insertBlock(block);	
		if (blockMap.getBlock(3, 2) != block) {
			fail("Incorrect preconditions, block wasn't inserted");
		}
		blockMap.removeBlock(block);
		if (blockMap.getBlock(3, 2) != block) {
			fail("block wasn't removed");
		}
	}
	
	@Test
	public void getHeightTest() {
		if (blockMap.getHeight() != 12) {
			fail("incorrect height");
		}
	}
	
	@Test
	public void getWidthTest() {
		if (blockMap.getWidth() != 8) {
			fail("incorrect width");
		}
	}
}

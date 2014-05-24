package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.gdx.view.MiniMap;

public class BlocksterMapTest {

	private BlocksterMap blockMap;
	private MiniMap map;
	private List<Point> startPos;
	
	@Before
	public void setUp() {
		startPos = new ArrayList<Point>();
		startPos.add(new Point(1, 1));
		startPos.add(new Point(2, 2));
		blockMap = new BlocksterMap(8, 12, 48, 48, startPos);
		map = new MiniMap(2, 2, new Player(0f, 0f, blockMap, World.DAY));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestWidth() {
		blockMap = new BlocksterMap(-1, 1, 48, 48, startPos);
		// if no AssertionError, set test to fail
		assertTrue(false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestHeight(){
		blockMap = new BlocksterMap(1, -1, 48, 48, startPos);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestBlockWidth() {
		blockMap = new BlocksterMap(1, 1, -48, 48, startPos);
		assertTrue(false);
	}
	
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestBlockHeigth() {
		blockMap = new BlocksterMap(1, 1, 48, 48, startPos);
		assertTrue(false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestNbrOfPlayers() {
			final List<Point> emptyList = new ArrayList<Point>();
			blockMap = new BlocksterMap(1, 1, 48, 48, emptyList);
		assertTrue(false);
	}
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestStartPosX() {
			final List<Point> starts = new ArrayList<Point>();
			starts.add(new Point(-1, 1));
			starts.add(new Point(2, 2));
			blockMap = new BlocksterMap(1, 1, 48, 48, starts);
		

		assertTrue(false);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void constructorFailureTestStartPosY() {
			final List<Point> starts = new ArrayList<Point>();
			starts.add(new Point(1, -1));
			starts.add(new Point(2, 2));
			blockMap = new BlocksterMap(1, 1, 48, 48, starts);

			assertTrue(false);
	}

	@Test
	public void addActiveBlockListenerTest() {
		

		if (!blockMap.getListeners().isEmpty()) {
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
		
		if (!blockMap.getListeners().isEmpty()) {
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
		if (!blockMap.getListeners().isEmpty()) {
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
		if (blockMap.getBlock(3, 2) == block) {
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
	
	@Test
	public void blockMapListenersTest() {
		blockMap.addListener(map);
		Block block = new Block(1, 1,  blockMap);
		blockMap.insertBlock(block);
		blockMap.removeBlock(block);
		
		if (blockMap.hasBlock(1, 1)) {
			fail("did not remove block properly");
		}
	}
		@Test
		public void getBlocksTest() {
			Block block = new Block(1, 1, blockMap);
			blockMap.insertBlock(block);
			HashSet set = (HashSet)blockMap.getBlocks();
			
			if (!set.contains(block)) {
				fail("getBlock fail");
		}
	}
		@Test
		public void getActiveBlocksTest() {
			Block block = new Block(1, 1, blockMap);
			blockMap.addActiveBlock(block);
			
			HashSet set = (HashSet)blockMap.getActiveBlocks();
			
			if (!set.contains(block)) {
				fail("Fail to get active blocks");
			}
		}
		@Test
		public void insertFinishedBlockTest() {
			Block blockTop = new Block(1, 2, blockMap);
			blockTop.setProperty("solid");
			blockTop.setProperty("weight");
			Block blockBottom = new Block(1, 1, blockMap);
			blockBottom.setProperty("solid");
			
			blockMap.insertBlock(blockBottom);
			blockMap.addActiveBlock(blockTop);
			
			blockMap.updateActiveBlocks(0.1f);
			
			if (blockMap.getActiveBlocks().contains(blockTop)) {
				fail("Did not remove active block after insertFinishedBLock");
			}
		}
}

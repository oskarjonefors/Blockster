package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.*;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.chalmers.blockster.gdx.view.MiniMap;

public class BlockMapTest {

	private BlockMap blockMap;
	private boolean correct = false;
	private MiniMap map;
	
	@Before
	public void setUp() {
		blockMap = new BlockMap(8, 12, 48, 48, new int[][] {{1,1},{2,2}});
		map = new MiniMap(2, 2);
		correct = false;
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestWidth() {
		blockMap = new BlockMap(-1, 1, 48, 48, new int[][] {{1,1},{2,2}});
		// if no AssertionError, set test to fail
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestHeigth(){
		blockMap = new BlockMap(1, -1, 48, 48, new int[][] {{1,1},{2,2}});
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestBlockWidth() {
		blockMap = new BlockMap(1, 1, -48, 48, new int[][] {{1,1},{2,2}});
		assertTrue(false);
	}
	
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestBlockHeigth() {
		blockMap = new BlockMap(1, 1, 48, 48, new int[][] {{1,1},{2,2}});
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestNbrOfPlayers() {
		try {
			blockMap = new BlockMap(1, 1, 48, 48, new int[][] {{}, {}});
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}
	@Test(expected=AssertionError.class)
	public void constructorFailureTestStartPosX() {
		try {
			blockMap = new BlockMap(1, 1, 48, 48, new int[][] {{-1,1}, {2,2}});
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}
	
	@Test(expected=AssertionError.class)
	public void constructorFailureTestStartPosY() {
		try {
			blockMap = new BlockMap(1, 1, 48, 48, new int[][] {{1,-1}, {2,2}});
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
		assertTrue(false);
	}

	@Test
	public void addActiveBlockListenerTest() {
		MiniMap map = new MiniMap(2, 2);
		
		correct = blockMap.getListeners().size() == 0;
		blockMap.addActiveBlockListener(map);
		correct &= blockMap.getActiveBlockListener().size() == 1;
		correct &= blockMap.getActiveBlockListener().get(0).equals(map);
		
		assertTrue(correct);
	}
	
	@Test
	public void removeActiveBlockListenerTest(){
		
		blockMap.addActiveBlockListener(map);
		correct = blockMap.getActiveBlockListener().size() == 1;
		blockMap.removeActiveBlockListener(map);
		correct &= blockMap.getActiveBlockListener().isEmpty();
		
		assertTrue(correct);
	}
	
	@Test
	public void addListenerTest() {
		
		correct = blockMap.getListeners().size() == 0;
		blockMap.addListener(map);
		correct &= blockMap.getListeners().size() == 1;
		
		assertTrue(correct);
	}
	
	@Test
	public void removeListenerTest() {
		blockMap.addListener(map);
		correct = blockMap.getListeners().size() == 1;
		blockMap.removeListener(map);
		correct &= blockMap.getListeners().size() == 0;
		
		assertTrue(correct);
	}
	
	@Test
	public void insertBlockTest() {
		Block block = new Block(2, 1, blockMap);
		
		blockMap.insertBlock(block);
		correct = blockMap.getBlock(2, 1) == block;
		
		assertTrue(correct);
	}
	
	@Test
	public void removeBlockTest() {
		Block block = new Block(3, 2, blockMap);
		
		blockMap.insertBlock(block);	
		correct = blockMap.getBlock(3, 2) == block;
		blockMap.removeBlock(block);
		correct &= blockMap.getBlock(3, 2) != block;
		
		assertTrue(correct);
	}
	
	@Test
	public void getHeightTest() {
		assertTrue(blockMap.getHeight() == 12);
	}
	
	@Test
	public void getWidthTest() {
		assertTrue(blockMap.getWidth() == 8);
	}
	
	@Test
	public void hasBlockTest() {
		
	}
}

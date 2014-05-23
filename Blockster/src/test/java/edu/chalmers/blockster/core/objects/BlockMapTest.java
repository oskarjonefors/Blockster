package edu.chalmers.blockster.core.objects;

import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.chalmers.blockster.core.objects.Player.World;
import edu.chalmers.blockster.gdx.view.MiniMap;

public class BlockMapTest {

	private BlockMap blockMap;
	private boolean correct = false;
	private MiniMap map;
	private List<Point> startPos;
	
	@Before
	public void setUp() {
		final List<Point> startPos = new ArrayList<Point>();
		startPos.add(new Point(1, 1));
		startPos.add(new Point(2, 2));
		blockMap = new BlockMap(8, 12, 48, 48, startPos);
		map = new MiniMap(2, 2, new Player(0f, 0f, blockMap, World.DAY));
		correct = false;
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

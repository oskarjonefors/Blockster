package edu.chalmers.blockster.core.objects;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.util.GridMap;

/**
 * A class representing a grid layer of blocks and players in a map.
 */

public class BlocksterMap implements GridMap, BlockMap {

	private static final Logger LOG = Logger
			.getLogger(BlocksterMap.class.getName());
	private Block[][] blockMap;
	private List<Point> playerStartingPositions;
	private final float blockWidth, blockHeight;

	private Set<Block> activeBlocks;
	private List<BlockMapListener> listeners;
	private List<ActiveBlockListener> activeBlockListeners;

	public BlocksterMap(int width, int height, float blockWidth, float blockHeight,
			List<Point> playerStartingPositions) {
		String posi = "must be positive";
		
		if (width <= 0) {
			throw new IllegalArgumentException("Width of map "+posi);
		}
		
		if (height <= 0) {
			throw new IllegalArgumentException("Height of map "+posi);
		}
		
		if (blockWidth <= 0) {
			throw new IllegalArgumentException("Width of blocks "+posi);
		}
		
		if (height <= 0) {
			throw new IllegalArgumentException("Height of blocks "+posi);
		}
		
		if (playerStartingPositions.isEmpty()) {
			throw new IllegalArgumentException("There must be at least one player on the map");
		}
		
		for (Point p : playerStartingPositions) {
			if (p.x < 0 || p.x >= width) {
				throw new IllegalArgumentException("Player is not on map (x-axis): "+p.x);
			}
			if (p.y < 0 || p.y >= height) {
				throw new IllegalArgumentException("Player is not on map (y-axis): "+p.y);
			}
		}

		this.listeners = new ArrayList<BlockMapListener>();
		this.activeBlockListeners = new ArrayList<ActiveBlockListener>();
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		this.playerStartingPositions = playerStartingPositions;

		blockMap = new Block[width][height];
		activeBlocks = new HashSet<Block>();

		// Fills the whole map with empty blocks
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				blockMap[x][y] = EmptyBlock.getInstance();
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#addActiveBlockListener(edu.chalmers.blockster.core.objects.ActiveBlockListener)
	 */
	@Override
	public void addActiveBlockListener(ActiveBlockListener listener) {
		activeBlockListeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getActiveBlockListener()
	 */
	@Override
	public List<ActiveBlockListener> getActiveBlockListener(){
		return activeBlockListeners;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#removeActiveBlockListener(edu.chalmers.blockster.core.objects.ActiveBlockListener)
	 */
	@Override
	public void removeActiveBlockListener(ActiveBlockListener listener) {
		activeBlockListeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#addListener(edu.chalmers.blockster.core.objects.BlockMapListener)
	 */
	@Override
	public void addListener(BlockMapListener listener) {
		listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getListeners()
	 */
	@Override
	public List<BlockMapListener> getListeners(){
		return listeners;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#removeListener(edu.chalmers.blockster.core.objects.BlockMapListener)
	 */
	@Override
	public void removeListener(BlockMapListener listener) {
		listeners.remove(listener);
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#removeBlock(edu.chalmers.blockster.core.objects.Block)
	 */
	@Override
	public void removeBlock(Block block) {
		assert block != null : "Block does not exist, cannot be removed.";

		final int x = Math.round(block.getX());
		final int y = Math.round(block.getY());
		setBlock(x, y, EmptyBlock.getInstance());

		for (BlockMapListener listener : listeners) {
			listener.blockRemoved(block);
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#insertBlock(edu.chalmers.blockster.core.objects.Block)
	 */
	@Override
	public void insertBlock(Block block) {
		assert block != null : "Block does not exist, cannot be inserted.";

		final int x = Math.round(block.getX());
		final int y = Math.round(block.getY());
		setBlock(x, y, block);

		for (BlockMapListener listener : listeners) {
			listener.blockInserted(block);
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getBlockWidth()
	 */
	@Override
	public float getBlockWidth() {
		return blockWidth;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getBlockHeight()
	 */
	@Override
	public float getBlockHeight() {
		return blockHeight;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getHeight()
	 */
	@Override
	public int getHeight() {
		return blockMap[0].length;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getWidth()
	 */
	@Override
	public int getWidth() {
		return blockMap.length;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#setBlock(int, int, edu.chalmers.blockster.core.objects.Block)
	 */
	@Override
	public void setBlock(int x, int y, Block block) {
		if(x < getWidth() && y < getHeight()
				&& x >= 0 && y >= 0) {
			blockMap[x][y] = block;
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getBlock(int, int)
	 */
	@Override
	public Block getBlock(int x, int y) {
		if(x >= getWidth() || y >= getHeight()) {
			return EmptyBlock.getInstance();
		}
		return blockMap[x][y];
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#hasBlock(int, int)
	 */
	@Override
	public boolean hasBlock(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			return false;
		}
		if (y < 0 || y >= getHeight()) {
			return false;
		}
		return !(blockMap[x][y] instanceof EmptyBlock);
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getBlocks()
	 */
	@Override
	public Set<Block> getBlocks() {
		Set<Block> blocks = new HashSet<Block>();
		for (int i = 0; i < blockMap.length; i++) {
			Collections.addAll(blocks, blockMap[i]);
		}
		return blocks;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getActiveBlocks()
	 */
	@Override
	public Set<Block> getActiveBlocks() {
		return new HashSet<Block>(activeBlocks);
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#getPlayerStartingPositions()
	 */
	@Override
	public List<Point> getPlayerStartingPositions() {
		return new ArrayList<Point>(playerStartingPositions);
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#updateActiveBlocks(float)
	 */
	@Override
	public void updateActiveBlocks(float deltaTime) {
		for (final Block block : new HashSet<Block>(activeBlocks)) {
			AnimationState anim = block.getAnimationState();
			anim.updatePosition(deltaTime);
			LOG.log(Level.FINE, "Updating " + block);
			if (anim.isDone()) {
				LOG.log(Level.FINE, "Animation on " + block + " is done");
				block.moveToNextPosition();
				insertFinishedBlock(block);
			}
		}
	}

	private void insertFinishedBlock(Block block) {
		block.setAnimationState(AnimationState.NONE);
		if (!hasBlock((int) block.getX(), (int) (block.getY() - 1))) {
			block.fallDown();
		} else {
			insertBlock(block);
			activeBlocks.remove(block);

			for (ActiveBlockListener listener : activeBlockListeners) {
				listener.blockDeactivated(block);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.blockster.core.objects.BlockMap#addActiveBlock(edu.chalmers.blockster.core.objects.Block)
	 */
	@Override
	public void addActiveBlock(Block block) {
		activeBlocks.add(block);

		for (ActiveBlockListener listener : activeBlockListeners) {
			listener.blockActivated(block);
		}
	}
}
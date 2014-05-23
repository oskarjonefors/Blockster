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

public class BlockMap implements GridMap {

	private static final Logger LOG = Logger
			.getLogger(BlockMap.class.getName());
	private Block[][] blockMap;
	private List<Point> playerStartingPositions;
	private final float blockWidth, blockHeight;

	private Set<Block> activeBlocks;
	private List<BlockMapListener> listeners;
	private List<ActiveBlockListener> activeBlockListeners;

	public BlockMap(int width, int height, float blockWidth, float blockHeight,
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

	public void addActiveBlockListener(ActiveBlockListener listener) {
		activeBlockListeners.add(listener);
	}
	
	public List<ActiveBlockListener> getActiveBlockListener(){
		return activeBlockListeners;
	}

	public void removeActiveBlockListener(ActiveBlockListener listener) {
		activeBlockListeners.remove(listener);
	}

	public void addListener(BlockMapListener listener) {
		listeners.add(listener);
	}
	
	public List<BlockMapListener> getListeners(){
		return listeners;
	}

	public void removeListener(BlockMapListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Replaces the specific block with an empty block in the block map, i.e.
	 * removes a block. Alerts the listeners that a block has been removed.
	 * 
	 * @param block
	 *            The block that is to be removed.
	 */
	public void removeBlock(Block block) {
		assert block != null : "Block does not exist, cannot be removed.";

		final int x = Math.round(block.getX());
		final int y = Math.round(block.getY());
		setBlock(x, y, EmptyBlock.getInstance());

		for (BlockMapListener listener : listeners) {
			listener.blockRemoved(block);
		}
	}

	/**
	 * Inserts the specific block with it's coordinates in the block map. Alerts
	 * the listeners that a block has been inserted.
	 * 
	 * @param block
	 *            That is to be inserted.
	 */
	public void insertBlock(Block block) {
		assert block != null : "Block does not exist, cannot be inserted.";

		final int x = Math.round(block.getX());
		final int y = Math.round(block.getY());
		setBlock(x, y, block);

		for (BlockMapListener listener : listeners) {
			listener.blockInserted(block);
		}
	}

	/**
	 * Get the width of the block in pixels.
	 * 
	 * @return Block width in pixels.
	 */
	public float getBlockWidth() {
		return blockWidth;
	}

	/**
	 * Get the height of the block in pixels.
	 * 
	 * @return Block height in pixels.
	 */
	public float getBlockHeight() {
		return blockHeight;
	}

	/**
	 * Get the height of the layer in blocks.
	 * 
	 * @return The width of the layer in blocks.
	 */
	public int getHeight() {
		return blockMap[0].length;
	}

	/**
	 * Get the width of the layer in blocks.
	 * 
	 * @return The width of the layer in blocks.
	 */
	public int getWidth() {
		return blockMap.length;
	}

	/**
	 * Insert the given block into the layer at the given coordinates.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param block
	 */
	public void setBlock(int x, int y, Block block) {
		if(x < getWidth() && y < getHeight()
				&& x >= 0 && y >= 0) {
			blockMap[x][y] = block;
		}
	}

	/**
	 * Get the Block at the given coordinates, or null if there is none.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return A Block, or null if no Block is found at the given coordinates.
	 */
	public Block getBlock(int x, int y) {
		if(x >= getWidth() || y >= getHeight()) {
			return EmptyBlock.getInstance();
		}
		return blockMap[x][y];
	}

	/**
	 * Check to see if there is a block at the given coordinates.
	 * 
	 * @param x
	 *            X coordinate to check
	 * @param y
	 *            Y coordinate to check
	 * @return True if there is a block at (x,y), otherwise false.
	 */
	public boolean hasBlock(int x, int y) {
		if (x < 0 || x >= getWidth()) {
			return false;
		}
		if (y < 0 || y >= getHeight()) {
			return false;
		}
		return !(blockMap[x][y] instanceof EmptyBlock);
	}

	public Set<Block> getBlocks() {
		Set<Block> blocks = new HashSet<Block>();
		for (int i = 0; i < blockMap.length; i++) {
			Collections.addAll(blocks, blockMap[i]);
		}
		return blocks;
	}

	public Set<Block> getActiveBlocks() {
		return new HashSet<Block>(activeBlocks);
	}

	/**
	 * Get a float array of the player starting positions in the map. They are
	 * structured as such: float[playerNo][n] where n:0 = X coordinate, n:1 = Y
	 * coordinate.
	 * 
	 * @return
	 */
	public List<Point> getPlayerStartingPositions() {
		return new ArrayList<Point>(playerStartingPositions);
	}

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

	public void addActiveBlock(Block block) {
		activeBlocks.add(block);

		for (ActiveBlockListener listener : activeBlockListeners) {
			listener.blockActivated(block);
		}
	}
}
package edu.chalmers.blockster.core.objects;

import java.awt.Point;
import java.util.List;
import java.util.Set;

import edu.chalmers.blockster.core.util.GridMap;

public interface BlockMap extends GridMap{

	public abstract void addActiveBlockListener(ActiveBlockListener listener);

	public abstract List<ActiveBlockListener> getActiveBlockListener();

	public abstract void removeActiveBlockListener(ActiveBlockListener listener);

	public abstract void addListener(BlockMapListener listener);

	public abstract List<BlockMapListener> getListeners();

	public abstract void removeListener(BlockMapListener listener);

	/**
	 * Replaces the specific block with an empty block in the block map, i.e.
	 * removes a block. Alerts the listeners that a block has been removed.
	 * 
	 * @param block
	 *            The block that is to be removed.
	 */
	public abstract void removeBlock(Block block);

	/**
	 * Inserts the specific block with it's coordinates in the block map. Alerts
	 * the listeners that a block has been inserted.
	 * 
	 * @param block
	 *            That is to be inserted.
	 */
	public abstract void insertBlock(Block block);

	/**
	 * Get the width of the block in pixels.
	 * 
	 * @return Block width in pixels.
	 */
	public abstract float getBlockWidth();

	/**
	 * Get the height of the block in pixels.
	 * 
	 * @return Block height in pixels.
	 */
	public abstract float getBlockHeight();

	/**
	 * Get the height of the layer in blocks.
	 * 
	 * @return The width of the layer in blocks.
	 */
	public abstract int getHeight();

	/**
	 * Get the width of the layer in blocks.
	 * 
	 * @return The width of the layer in blocks.
	 */
	public abstract int getWidth();

	/**
	 * Insert the given block into the layer at the given coordinates.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param block
	 */
	public abstract void setBlock(int x, int y, Block block);

	/**
	 * Get the Block at the given coordinates, or null if there is none.
	 * 
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @return A Block, or null if no Block is found at the given coordinates.
	 */
	public abstract Block getBlock(int x, int y);

	/**
	 * Check to see if there is a block at the given coordinates.
	 * 
	 * @param x
	 *            X coordinate to check
	 * @param y
	 *            Y coordinate to check
	 * @return True if there is a block at (x,y), otherwise false.
	 */
	public abstract boolean hasBlock(int x, int y);

	public abstract Set<Block> getBlocks();

	public abstract Set<Block> getActiveBlocks();

	/**
	 * Get a float array of the player starting positions in the map. They are
	 * structured as such: float[playerNo][n] where n:0 = X coordinate, n:1 = Y
	 * coordinate.
	 * 
	 * @return
	 */
	public abstract List<Point> getPlayerStartingPositions();

	public abstract void updateActiveBlocks(float deltaTime);

	public abstract void addActiveBlock(Block block);

}
package edu.chalmers.Blockster.core.objects;

import java.util.Set;

import edu.chalmers.Blockster.core.util.GridMap;

/**
 * An interface representing a grid layer of blocks in a BlockMap.
 */

public interface BlockMap extends GridMap {

	/**
	 * Get the width of the block in pixels.
	 * @return Block width in pixels.
	 */
	public float getBlockWidth();

	/**
	 * Get the height of the block in pixels.
	 * @return Block height in pixels.
	 */
	public float getBlockHeight();
	
	/**
	 * Get the height of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	public int getHeight();
	
	/**
	 * Get the width of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	public int getWidth();

	/**
	 * Insert the given block into the layer at the given coordinates.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @param block
	 */
	public void setBlock(int x, int y, Block block);

	/**
	 * Get the Block at the given coordinates, or null if there is none.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @return	A Block, or null if no Block is found at the given coordinates.
	 */
	public Block getBlock(int x, int y);
	
	/**
	 * Check to see if there is a block at the given coordinates.
	 * @param x	X coordinate to check
	 * @param y	Y coordinate to check
	 * @return	True if there is a block at (x,y), otherwise false. 
	 */
	public boolean hasBlock(int x, int y);
	
	public Set<Block> getBlocks();
	
	public Set<Block> getActiveBlocks();
	
	public void insertFinishedBlocks();
	
	public void updateActiveBlocks(float deltaTime);
	
	public void addActiveBlock(Block block);
	
}
package edu.chalmers.blockster.core.objects;

import java.util.Set;

import edu.chalmers.blockster.core.util.GridMap;

/**
 * An interface representing a grid layer of blocks in a BlockMap.
 */

public interface BlockMap extends GridMap {

	/**
	 * Get the width of the block in pixels.
	 * @return Block width in pixels.
	 */
	float getBlockWidth();

	/**
	 * Get the height of the block in pixels.
	 * @return Block height in pixels.
	 */
	float getBlockHeight();
	
	/**
	 * Get the height of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	int getHeight();
	
	/**
	 * Get the width of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	int getWidth();

	/**
	 * Insert the given block into the layer at the given coordinates.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @param block
	 */
	void setBlock(int x, int y, Block block);

	/**
	 * Get the Block at the given coordinates, or null if there is none.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @return	A Block, or null if no Block is found at the given coordinates.
	 */
	Block getBlock(int x, int y);
	
	/**
	 * Check to see if there is a block at the given coordinates.
	 * @param x	X coordinate to check
	 * @param y	Y coordinate to check
	 * @return	True if there is a block at (x,y), otherwise false. 
	 */
	boolean hasBlock(int x, int y);
	
	Set<Block> getBlocks();
	
	Set<Block> getActiveBlocks();
	
	/**
	 * Get a float array of the player starting positions in the map.
	 * They are structured as such: float[playerNo][n] 
	 * where n:0 = X coordinate, n:1 = Y coordinate.
	 * @return
	 */
	float[][] getPlayerStartingPositions();
	
	void updateActiveBlocks(float deltaTime);
	
	void addActiveBlock(Block block);

	
}
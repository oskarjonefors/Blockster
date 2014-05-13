package edu.chalmers.blockster.core.objects;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.util.GridMap;

/**
 * An interface representing a grid layer of blocks in a BlockMap.
 */

public class BlockMap implements GridMap {

	private final Logger log = Logger.getLogger(this.getClass().getName());
	
	private Block[][] blockMap;
	private float[][] playerStartingPositions;
	private final float blockWidth, blockHeight;
	
	private Set<Block> activeBlocks;
	private BlockMapListener listener;
	
	public BlockMap(int width, int height, float blockWidth, float blockHeight, int[][] playerStartingPositions) {
		assert width > 0 && height > 0;
		assert blockWidth > 0 && blockHeight > 0;
		assert playerStartingPositions.length > 0;

		this.listener = new NoListener();
		this.blockWidth = blockWidth;
		this.blockHeight = blockHeight;
		this.playerStartingPositions = new float[playerStartingPositions.length][2];
		
		for (int i = 0; i < playerStartingPositions.length; i++) {
			int[] startPosition = playerStartingPositions[i];
			assert startPosition.length == 2;
			
			int startPositionX = startPosition[0];
			int startPositionY = startPosition[1];
			
			assert startPositionX >= 0 && startPositionX < width;
			assert startPositionY >= 0 && startPositionY < height;
			
			this.playerStartingPositions[i][0] = startPositionX;
			this.playerStartingPositions[i][1] = startPositionY;
		}
		
		
		blockMap = new Block[width][height];
		activeBlocks = new HashSet<Block>();
		
		for (int x=0; x<width; x++) {
			for (int y=0; y<height; y++) {
				blockMap[x][y] = new EmptyBlock(x, y, this);
			}
		}
	}
	
	public void setListener(BlockMapListener listener) {
		this.listener = listener;
	}
	
	public void removeBlock(Block block) {
		if (block != null) {
			final int x = Math.round(block.getX());
			final int y = Math.round(block.getY());
			setBlock(x, y, new EmptyBlock(x, y, this));
			listener.blockRemoved(x, y);
		}
	}
	
	public void insertBlock(Block block) {
		if (block != null) {
			final int x = Math.round(block.getX());
			final int y = Math.round(block.getY());
			setBlock(x, y, block);
			listener.blockInserted(x, y, block);
		}
	}
	
	/**
	 * Get the width of the block in pixels.
	 * @return Block width in pixels.
	 */
	public float getBlockWidth() {
		return blockWidth;
	}

	/**
	 * Get the height of the block in pixels.
	 * @return Block height in pixels.
	 */
	public float getBlockHeight() {
		return blockHeight;
	}
	
	/**
	 * Get the height of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	public int getHeight() {
		return blockMap[0].length;
	}
	
	/**
	 * Get the width of the layer in blocks.
	 * @return The width of the layer in blocks.
	 */
	public int getWidth() {
		return blockMap.length;
	}

	/**
	 * Insert the given block into the layer at the given coordinates.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @param block
	 */
	public void setBlock(int x, int y, Block block) {
		blockMap[x][y] = block;
	}

	/**
	 * Get the Block at the given coordinates, or null if there is none.
	 * @param x	X coordinate
	 * @param y	Y coordinate
	 * @return	A Block, or null if no Block is found at the given coordinates.
	 */
	public Block getBlock(int x, int y) {
		return blockMap[x][y];
	}
	
	/**
	 * Check to see if there is a block at the given coordinates.
	 * @param x	X coordinate to check
	 * @param y	Y coordinate to check
	 * @return	True if there is a block at (x,y), otherwise false. 
	 */
	public boolean hasBlock(int x, int y) {
		if (x < 0 || x >= getWidth())
			return false;
		if (y < 0 || y >= getHeight())
			return false;
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
	 * Get a float array of the player starting positions in the map.
	 * They are structured as such: float[playerNo][n] 
	 * where n:0 = X coordinate, n:1 = Y coordinate.
	 * @return
	 */
	public float[][] getPlayerStartingPositions() {
		return playerStartingPositions;
	}
	
	public void updateActiveBlocks(float deltaTime) {
		for (final Block block : new HashSet<Block>(activeBlocks)) {
			block.getAnimationState().updatePosition(deltaTime);
			log.log(Level.INFO, "Updating " + block);
			if(block.getAnimationState().isDone()) {
				log.log(Level.INFO, "Animation on " + block + " is done");
				block.moveToNextPosition();
				insertFinishedBlock(block);
			}
		}
	}
	
	private void insertFinishedBlock(Block block) {
		block.setAnimationState(AnimationState.NONE);
		if (!hasBlock((int)block.getX(), (int)(block.getY() - 1)) &&
				!block.isLifted() && block.hasWeight()) {
			block.fallDown();
		} else {
			insertBlock(block);
			activeBlocks.remove(block);
		}
	}
	
	public void addActiveBlock(Block block) {
		activeBlocks.add(block);
	}
	
	private class NoListener implements BlockMapListener {

		@Override
		public void blockInserted(int x, int y, Block block) {
			// Does nothing
		}

		@Override
		public void blockRemoved(int x, int y) {
			// Really does nothing
		}
		
	}
	
}
package edu.chalmers.Blockster.core;


public interface BlockLayer {

	public float getBlockWidth();

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

	public void setBlock(int x, int y, Block block);

	public Block getBlock(int x, int y);

}
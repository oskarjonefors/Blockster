package edu.chalmers.Blockster.core;


public interface BlockLayer {

	public float getBlockWidth();

	public float getBlockHeight();

	public void setBlock(int x, int y, Block block);

	public Block getBlock(int x, int y);

}
package edu.chalmers.Blockster.core;


public interface BlockLayer {

	public float getTileWidth();

	public float getTiledHeight();

	public void setBlock(int x, int y, Block block);

	public Block getBlock(int x, int y);

	public BlockLayer getLayer(int index);

}
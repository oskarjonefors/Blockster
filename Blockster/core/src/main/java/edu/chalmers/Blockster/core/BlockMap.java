package edu.chalmers.Blockster.core;

/**
 * A interface to represent a map with layers.
 * @author Mia
 */
public interface BlockMap {
	
	/**
	 * A method to get a layer with blocks.
	 * @param index specifies which block layer to return. Starts with 0.
	 * @return the specified block layer.
	 */
	public BlockLayer getBlockLayer();
}

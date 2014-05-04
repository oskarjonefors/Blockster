package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockLayer;

/**
 * Decorator class
 *
 */
public class GdxBlockLayer implements BlockLayer {
	private TiledMapTileLayer layer;
	
	/**
	 * Only fetches the first layer
	 * @param map
	 */
	public GdxBlockLayer(TiledMapTileLayer layer){
		this.layer = layer;
	}
	
	@Override
	public float getBlockWidth(){
		return layer.getTileWidth();
	}

	@Override
	public float getBlockHeight(){
		return layer.getTileHeight();
	}
	
	@Override
	public Block getBlock(int x, int y){
		return (Block)layer.getCell(x, y).getTile();
	}
	
	@Override
	public void setBlock(int x, int y, Block block){
		layer.getCell(x, y).setTile((Block) block);
	}

	@Override
	public int getHeight() {
		return layer.getHeight();
	}

	@Override
	public int getWidth() {
		return layer.getWidth();
	}
	
	/**
	 * Removes block from layer.
	 * @param block the block to remove
	 */
	public void removeBlock(Block block) {
		layer.setCell((int)block.getX(), (int)block.getY(), null);
	}
	
	/**
	 * Inserts block into layer.
	 * @param block the block to insert
	 */
	public void insertBlock(Block block) {
		try {
			block.setX(Math.round(block.getX()));
			block.setY(Math.round(block.getY()));
			if (layer.getCell((int) block.getX(), (int) block.getY()) == null) {
				layer.setCell((int) block.getX(), (int) block.getY(), new Cell());
				Gdx.app.log("GdxBlockLayer", "Inserted block at " + block.getX() + " " + block.getY());
			}
			
			if (layer.getCell((int) block.getX(), (int) block.getY()).getTile() == null) {
				layer.getCell((int) block.getX(), (int) block.getY()).setTile(block);
			}
		} catch (NullPointerException e) {
			Gdx.app.log("Layer", "NullPointer");
		}
	}

	@Override
	public boolean hasBlock(int x, int y) {
		return layer.getCell(x, y) != null && layer.getCell(x, y).getTile() != null;
	}
}

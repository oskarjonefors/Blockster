package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockLayer;

public class GdxBlockLayer implements BlockLayer {
	private TiledMapTileLayer layer;
	
	/**
	 * Only fetches the first layer
	 * @param map
	 */
	public GdxBlockLayer(TiledMap map){
		layer = (TiledMapTileLayer)map.getLayers().get(0);
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
		layer.getCell(x, y).setTile((GdxBlock) block);
	}

	@Override
	public int getHeight() {
		return layer.getHeight();
	}

	@Override
	public int getWidth() {
		return layer.getWidth();
	}
	
}

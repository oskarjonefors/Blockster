package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

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
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.BlockLayer#getTileWidth()
	 */
	@Override
	public float getTileWidth(){
		return layer.getTileWidth();
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.BlockLayer#getTiledHeight()
	 */
	@Override
	public float getTiledHeight(){
		return layer.getTileHeight();
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.BlockLayer#setBlock(int, int, edu.chalmers.Blockster.core.gdx.view.Block)
	 */
	@Override
	public void setBlock(int x, int y, Block block){
		layer.getCell(x, y).setTile((GdxBlock) block);
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.BlockLayer#getBlock(int, int)
	 */
	@Override
	public Block getBlock(int x, int y){
		return (Block)layer.getCell(x, y).getTile();
				}
	
	
	
}

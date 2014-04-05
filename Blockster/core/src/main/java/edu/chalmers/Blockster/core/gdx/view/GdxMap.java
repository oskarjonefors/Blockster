package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.BlockMap;

/**
 * 
 * @author Mia
 *
 */
public class GdxMap implements BlockMap {

	private BlockLayer blockLayer;
	private TiledMap map;

	public GdxMap (TiledMap map) {
		this.blockLayer = new GdxBlockLayer(map);
		this.map = map;
		
		for (MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				for (int x = 0; x < tileLayer.getWidth(); x++) {
					for (int y = 0; y < tileLayer.getHeight(); y++) {
						Cell cell = tileLayer.getCell(x, y);
						if (cell != null) {
							TiledMapTile tile = cell.getTile();
							GdxBlock block = new GdxBlock(tile);
							tileLayer.getCell(x,y).setTile(block);
							block.setX(x);
							block.setY(y);
						}
					}
				}
			}
		}
	}

	@Override
	public BlockLayer getBlockLayer() {
		return blockLayer;
	}
	
}

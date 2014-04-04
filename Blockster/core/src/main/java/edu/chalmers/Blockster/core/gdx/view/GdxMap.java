package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import edu.chalmers.Blockster.core.BlockMap;

/**
 * 
 * @author Mia
 *
 */
public class GdxMap implements BlockMap {

	private BlockLayer blockLayer;

	public GdxMap (TiledMap map) {
		this.blockLayer = new BlockLayer(map);
	}

	@Override
	public BlockLayer getBlockLayer(int index) {
		return this.blockLayer.getLayer(index);
	}
	
}

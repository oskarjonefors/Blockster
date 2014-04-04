package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.maps.tiled.TiledMap;
import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.BlockMap;

/**
 * 
 * @author Mia
 *
 */
public class GdxMap implements BlockMap {

	private BlockLayer blockLayer;

	public GdxMap (TiledMap map) {
		this.blockLayer = new GdxBlockLayer(map);
	}

	@Override
	public BlockLayer getBlockLayer() {
		return this.blockLayer.getLayer();
	}
	
}

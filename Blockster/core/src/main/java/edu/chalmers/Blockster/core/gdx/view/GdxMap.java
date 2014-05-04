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
 * @author Eric Bjuhr
 *
 */
public class GdxMap extends TiledMap implements BlockMap {

	private BlockLayer blockLayer;

	public GdxMap (TiledMap map) {
		super();
		
		for (MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				TiledMapTileLayer nTileLayer = new TiledMapTileLayer(
						tileLayer.getWidth(), 
						tileLayer.getHeight(),
						(int) tileLayer.getTileWidth(), 
						(int) tileLayer.getTileHeight());
				
				getLayers().add(nTileLayer);
				for (int x = 0; x < tileLayer.getWidth(); x++) {
					for (int y = 0; y < tileLayer.getHeight(); y++) {
						Cell cell = tileLayer.getCell(x, y);
						if (cell != null) {
							TiledMapTile tile = cell.getTile();
							Block block = new Block(tile);

							block.setX(x);
							block.setY(y);
							
							cell.setTile(block);
							nTileLayer.setCell(x, y, cell);
						}
					}
				}
			}
		}
		

		this.blockLayer = new GdxBlockLayer((TiledMapTileLayer) getLayers().get(0));
	}

	@Override
	public BlockLayer getBlockLayer() {
		return blockLayer;
	}
	
}

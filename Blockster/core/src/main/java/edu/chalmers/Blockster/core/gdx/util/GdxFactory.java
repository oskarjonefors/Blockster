package edu.chalmers.Blockster.core.gdx.util;


import com.badlogic.gdx.maps.tiled.TiledMap;

import edu.chalmers.Blockster.core.gdx.view.GdxMap;
import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.Player;
import edu.chalmers.Blockster.core.Factory;

public class GdxFactory implements Factory {

	protected TiledMap map;
	
	public GdxFactory(TiledMap map) {
		this.map = map;
	}
	
	public BlockMap createMap() {
		GdxMap gMap = new GdxMap(map);
		return gMap;
	}
	
	
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockLayer);
	}

}

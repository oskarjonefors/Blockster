package edu.chalmers.blockster.core.gdx.util;


import com.badlogic.gdx.maps.tiled.TiledMap;

import edu.chalmers.blockster.core.Factory;
import edu.chalmers.blockster.core.gdx.view.GdxMap;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;

public class GdxFactory implements Factory {

	protected TiledMap map;
	
	public GdxFactory(TiledMap map) {
		this.map = map;
	}
	
	@Override
	public BlockMap createMap() {
		return new GdxMap(map);
	}
	
	
	
	@Override
	public Player createPlayer(float startX, float startY, BlockMap blockLayer) {
		return new Player(startX, startY, blockLayer);
	}

}

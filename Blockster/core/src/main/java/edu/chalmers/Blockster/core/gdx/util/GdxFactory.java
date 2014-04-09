package edu.chalmers.Blockster.core.gdx.util;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;

import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Factory;
import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.gdx.view.GdxMap;
import edu.chalmers.Blockster.core.gdx.view.GdxPlayer;

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
	public Player createPlayer(String spritePath, Model model) {
		return new GdxPlayer(new TextureRegion(new Texture((spritePath))), model);
	}

}

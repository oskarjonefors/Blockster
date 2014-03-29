package edu.chalmers.Blockster.core;

import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors
 *
 */
public class Stage {
	private TiledMap map;
	
	public Stage(TiledMap map) {
		this.map = map;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public void update(float deltaTime) {
		//Set animation state etc
	}
}

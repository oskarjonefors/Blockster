package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr
 * 
 */
public class Block extends Actor implements TiledMapTile {
	
	private TiledMapTile tile;
	
	public Block(TiledMapTile tile) {
		this.tile = tile;
	}
	
	public TiledMapTile getTile() {
		return tile;
	}

	@Override
	public int getId() {
		return tile.getId();
	}

	@Override
	public void setId(int id) {
		tile.setId(id);
	}

	@Override
	public BlendMode getBlendMode() {
		return tile.getBlendMode();
	}

	@Override
	public void setBlendMode(BlendMode blendMode) {
		tile.setBlendMode(blendMode);
	}

	@Override
	public TextureRegion getTextureRegion() {
		return tile.getTextureRegion();
	}

	@Override
	public MapProperties getProperties() {
		return tile.getProperties();
	}
	
}

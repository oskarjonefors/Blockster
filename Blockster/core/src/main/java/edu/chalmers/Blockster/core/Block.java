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
	private boolean solid;
	private boolean liftable;
	private Animation activeAnimation;
	private float animationTime;
	
	public Block(TiledMapTile tile) {
		this.tile = tile;
		
		MapProperties props = tile.getProperties();
		solid = props.containsKey("Collision");
		liftable = !solid && props.containsKey("Liftable");
	}
	


	@Override
	public BlendMode getBlendMode() {
		return tile.getBlendMode();
	}

	@Override
	public int getId() {
		return tile.getId();
	}

	@Override
	public MapProperties getProperties() {
		return tile.getProperties();
	}

	@Override
	public TextureRegion getTextureRegion() {
		return tile.getTextureRegion();
	}

	public TiledMapTile getTile() {
		return tile;
	}
	
	public boolean isLiftable() {
		return liftable;
	}

	public boolean isSolid() {
		return solid;
	}
	
	@Override
	public void setBlendMode(BlendMode blendMode) {
		tile.setBlendMode(blendMode);
	}
	
	@Override
	public void setId(int id) {
		tile.setId(id);
	}
	
	public Animation getAnimation() {
		return activeAnimation;
	}
	
	public float getAnimationTime() {
		return animationTime;
	}
	
	public enum Animation {
		NONE, PUSH_LEFT, PUSH_RIGHT, PULL_LEFT, PULL_RIGHT, DESTROY, LIFT
	}
	
}

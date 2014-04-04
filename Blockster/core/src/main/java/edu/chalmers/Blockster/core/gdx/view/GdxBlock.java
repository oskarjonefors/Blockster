package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.Blockster.core.Block;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr, Oskar Jönefors
 * 
 */
public class GdxBlock implements Block, TiledMapTile {
	
	private boolean solid;
	private boolean liftable;
	private boolean movable;
	private Animation activeAnimation = Animation.NONE;
	private float animationTime;
	private TiledMapTile tile;
	
	public GdxBlock(TiledMapTile tile) {
		this.tile = tile;
		
		MapProperties props = tile.getProperties();
		solid = props.containsKey("Solid");
		liftable = !solid && props.containsKey("Liftable");
		movable = props.containsKey("Movable");
	}
	
	public int getId() {
		return tile.getId();
	}

	public MapProperties getProperties() {
		return tile.getProperties();
	}

	public TextureRegion getTextureRegion() {
		return tile.getTextureRegion();
	}

	public TiledMapTile getTile() {
		return tile;
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isLiftable()
	 */
	@Override
	public boolean isLiftable() {
		return liftable;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isMovable()
	 */
	@Override
	public boolean isMovable() {
		return movable;
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isSolid()
	 */
	@Override
	public boolean isSolid() {
		return solid;
	}
	
	public void setId(int id) {
		tile.setId(id);
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#getAnimation()
	 */
	@Override
	public Animation getAnimation() {
		return activeAnimation;
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#setAnimation(edu.chalmers.Blockster.core.gdx.view.GdxBlock.Animation)
	 */
	@Override
	public void setAnimation(Animation anim) {
		this.activeAnimation = anim;
	}

	@Override
	public float getElapsedAnimationTime() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public float getAnimationDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public BlendMode getBlendMode() {
		// TODO Auto-generated method stub
		return tile.getBlendMode();
	}

	@Override
	public void setBlendMode(BlendMode arg0) {
		// TODO Auto-generated method stub
		tile.setBlendMode(arg0);
	}	
}

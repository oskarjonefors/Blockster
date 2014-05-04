package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr, Oskar Jönefors, Emilia Nilsson
 * 
 */
public class Block extends BlocksterObject implements TiledMapTile {
	
	private boolean solid;
	private boolean liftable;
	private boolean movable;
	private boolean weight;

	private AnimationState activeAnimation = AnimationState.NONE;

	private float animationTime;
	private TiledMapTile tile;
	private float posX;
	private float posY;
	private TextureRegion region;
	private AnimationState animState;
	private BlockLayer blockLayer;
	
	public Block(float posX, float posY, TiledMapTile tile, BlockLayer blockLayer) {
		super(posX, posY, blockLayer);
		this.tile = tile;
		//this.animState = animState;
		MapProperties props = tile.getProperties();
		solid = props.containsKey("Solid");
		liftable = props.containsKey("Liftable");
		movable = props.containsKey("Movable");
		weight = props.containsKey("Weight");
		region = tile.getTextureRegion();
		posX = 0;
		posY = 0;
		this.blockLayer = blockLayer;
	}
	
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, getX()*region.getRegionWidth(), getY()*region.getRegionHeight(),
				getOriginX(), getOriginY(), region.getRegionWidth(), region.getRegionHeight(),
				getScaleX(), getScaleY(), getRotation());
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
	
	public boolean hasWeight() {
		return weight;
	}	
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isLiftable()
	 */
	public boolean isLiftable() {
		return liftable;
	}

	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isMovable()
	 */
	public boolean isMovable() {
		return movable;
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#isSolid()
	 */
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

	public AnimationState getAnimationState() {

		return activeAnimation;
	}
	
	/* (non-Javadoc)
	 * @see edu.chalmers.Blockster.core.gdx.view.Block#setAnimation(edu.chalmers.Blockster.core.gdx.view.GdxBlock.Animation)
	 */
	@Override

	public void setAnimationState(AnimationState anim) {

		this.activeAnimation = anim;
	}

	public float getElapsedAnimationTime() {
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

	@Override
	public float getX() {
		if(animState != AnimationState.NONE) {
			return posX + animState.getRelativePosition().x;
		} else {
			return posX;
		}
	}

	@Override
	public float getY() {
		if(animState != AnimationState.NONE){
			return posX + animState.getRelativePosition().y;
		}
		return posY;
	}

	@Override
	public void setX(float x) {
		super.setX(x);
	}

	@Override
	public void setY(float y) {
		super.setY(y);
	}

	public void moveToNextPosition() {
		posX += activeAnimation.getMovement().getDirection().deltaX;
		posY += activeAnimation.getMovement().getDirection().deltaY;
		
		setX(posX);
		setY(posY);
	}
}

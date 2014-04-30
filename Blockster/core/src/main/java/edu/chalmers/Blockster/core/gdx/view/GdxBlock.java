package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.scenes.scene2d.Actor;
import edu.chalmers.Blockster.core.AnimationState;
import edu.chalmers.Blockster.core.Movement;
import edu.chalmers.Blockster.core.Block;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr, Oskar Jönefors
 * 
 */
public class GdxBlock extends Actor implements Block, TiledMapTile {
	
	private boolean solid;
	private boolean liftable;
	private boolean movable;
	private boolean weight;

	private AnimationState activeAnimation = AnimationState.NONE;

	private float animationTime;
	private TiledMapTile tile;
	private float x;
	private float y;
	private TextureRegion region;
	
	public GdxBlock(TiledMapTile tile) {
		this.tile = tile;
		MapProperties props = tile.getProperties();
		solid = props.containsKey("Solid");
		liftable = props.containsKey("Liftable");
		movable = props.containsKey("Movable");
		weight = props.containsKey("Weight");
		region = tile.getTextureRegion();
		x = 0;
		y = 0;
	}
	
	@Override
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
	
	@Override
	public boolean hasWeight() {
		return weight;
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

	@Override
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
		return super.getX();
	}

	@Override
	public float getY() {
		return super.getY();
	}

	@Override
	public void setX(float x) {
		super.setX(x);
	}

	@Override
	public void setY(float y) {
		super.setY(y);
	}
}

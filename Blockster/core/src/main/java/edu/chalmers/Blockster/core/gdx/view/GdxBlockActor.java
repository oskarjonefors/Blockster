package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.Blockster.core.Block;

/**
 * A class to represent a Block as a Scene2D actor.
 * @author Oskar Jönefors
 *
 */
public class GdxBlockActor extends Actor {
	private GdxBlock block;
	private TextureRegion region;
	
	/**
	 * Create an actor representing the given block.
	 * @param block	The block to represent.
	 */
	public GdxBlockActor(GdxBlock block) {
		this.block = block;
		this.region = block.getTile().getTextureRegion();
		setX(block.getX());
		setY(block.getY());
	}
	
	/**
	 * Return the GdxBlock that the GdxBlockActor represents.
	 * @return a GdxBlock
	 */
	public GdxBlock getBlock() {
		return block;
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, getX()*region.getRegionWidth(), getY()*region.getRegionHeight(),
				getOriginX(), getOriginY(), region.getRegionWidth(), region.getRegionHeight(),
				getScaleX(), getScaleY(), getRotation());
	}
}
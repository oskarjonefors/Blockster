package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * A class to represent a stage background image as an actor.
 * @author Oskar Jönefors
 *
 */

public class GdxBackgroundActor extends Actor {

	private TextureRegion region;
	
	/**
	 * 
	 * @param texture
	 */
	public GdxBackgroundActor(TextureRegion region) {
		this.region = region; 
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
				region.getRegionWidth(), region.getRegionHeight(),
				getScaleX(), getScaleY(), getRotation());
	}
}

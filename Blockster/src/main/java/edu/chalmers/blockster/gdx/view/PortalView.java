package edu.chalmers.blockster.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PortalView {
	private Sprite sprite;
	private TextureRegion region;
	private final Animation animations;
	private float time, posX, posY;
	
	public PortalView(float posX, float posY, Animation animations) {
		this.posX = posX;
		this.posY = posY;
		this.animations = animations;
		
		sprite = new Sprite(); 
	}
	public void draw(SpriteBatch batch) {
		TextureRegion region = chooseAnimation();
		int width = region.getRegionWidth();
		int height = region.getRegionHeight();
		
		sprite.setRegion(region);
		sprite.setSize(width, height);
		
		batch.draw(sprite, posX, posY);
	}

	private TextureRegion chooseAnimation() {
		time += Gdx.graphics.getDeltaTime();
			return animations.getKeyFrame(time, true);
	}
}

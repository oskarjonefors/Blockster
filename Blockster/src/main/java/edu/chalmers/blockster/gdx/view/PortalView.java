package edu.chalmers.blockster.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.Block;

public class PortalView {
	private Block teleporter;
	private TextureRegion region;
	private final Animation animations;
	private float time, posX, posY;
	
	public PortalView(float posX, float posY, Animation animations) {
		this.posX = posX;
		this.posY = posY;
		this.animations = animations;
	}
	public void draw(SpriteBatch batch) {
		region = chooseAnimation();
		batch.draw(region, posX, posY);
	}

	private TextureRegion chooseAnimation() {
		time += Gdx.graphics.getDeltaTime();
		return animations.getKeyFrame(time);
	}

}

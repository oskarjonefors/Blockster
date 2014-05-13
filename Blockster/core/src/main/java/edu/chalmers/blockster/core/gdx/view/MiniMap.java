package edu.chalmers.blockster.core.gdx.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class MiniMap extends Actor {

	private Pixmap map;
	private Sprite sprite;
	private final float scaleX;
	private final float scaleY;
	
	public MiniMap (Pixmap map, float scaleX, float scaleY) {
		this.map = map;
		sprite = new Sprite(new Texture(map));
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public void setViewportBounds(int lowX, int highX, int lowY, int highY) {
		
	}

	public void draw(SpriteBatch batch) {
		batch.draw(sprite, 5, 5, sprite.getRegionWidth()*scaleX, sprite.getRegionHeight()*scaleY);
	}
	
}

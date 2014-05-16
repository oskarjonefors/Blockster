package edu.chalmers.blockster.gdx.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * A class used to draw background images.
 * @author Oskar Jönefors
 *
 */

public class BackgroundImage {
	
	private TextureRegion dayRegion;
	private TextureRegion nightRegion;
	private TextureRegion currentRegion;
	private float width;
	private float height;
	private float scaleX;
	private float scaleY;
	private float x;
	private float y;
	private GameMode mode;
	
	public enum GameMode {
		DAY, NIGHT;
	}
	
	public BackgroundImage(TextureRegion day, TextureRegion night) {
		dayRegion = day;
		nightRegion = night;
		currentRegion = dayRegion;
		width = dayRegion.getRegionWidth();
		height = dayRegion.getRegionHeight();
		scaleX = 1f;
		scaleY = 1f;
		mode = GameMode.DAY;
	}
	
	private void setHeight(float height) {
		this.height = height;
	}
	
	private void setWidth(float width) {
		this.width = width;
	}

	public float getScaleX() {
		return scaleX;
	}

	public void draw(SpriteBatch batch) {
		batch.draw(currentRegion, x, y, width*scaleX, height*scaleY);
	}
	
	public void setScale(float scaleXY) {
		scaleX = scaleXY;
		scaleY = scaleXY;
	}
	
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}

	public float getScaleY() {
		return scaleY;
	}

	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public GameMode getMode() {
		return mode;
	}

	public void setMode(GameMode mode) {
		this.mode = mode;
		currentRegion = (mode == GameMode.DAY ? dayRegion : nightRegion);
		setWidth(currentRegion.getRegionWidth());
		setHeight(currentRegion.getRegionHeight());
	}

	public void switchMode() {
		setMode(mode == GameMode.DAY ? GameMode.NIGHT : GameMode.DAY);
	}
	public float getWidth() {
		return width;
	}

	public float getHeight() {
		return height;
	}
}

package edu.chalmers.blockster.core.gdx.view;

import java.nio.ByteBuffer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;

public class MiniMap implements BlockMapListener {

	private final Pixmap pixmap;
	private float scaleX;
	private float scaleY;
	
	public static final int NO_BLOCK = Color.rgba8888(0, 0, 0, 1f);
	public static final int SOLID_BLOCK = Color.rgba8888(0.8f, 0.8f, 0.8f, 1f);
	public static final int LIFTABLE_BLOCK = Color.rgba8888(0.8f, 0, 0, 1f); 
	
	public MiniMap (Pixmap pixmap) {
		this.pixmap = pixmap;
		this.scaleX = 1f;
		this.scaleY = 1f;
	}
	
	private int getColor(Block block) {
		if (block.isLiftable()) {
			return LIFTABLE_BLOCK;
		} else if (block.isSolid()) {
			return SOLID_BLOCK;
		} else {
			return NO_BLOCK;
		}
	}
	
	private int getPixMapY(int y) {
		return pixmap.getHeight() - y - 1;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setScaleX(float scaleX) {
		this.scaleX = scaleX;
	}
	
	public void setScaleY(float scaleY) {
		this.scaleY = scaleY;
	}
	
	public void setViewportBounds(int lowX, int highX, int lowY, int highY) {
		
	}

	public void draw(SpriteBatch batch) {
		Sprite sprite = new Sprite(new Texture(pixmap));
		Color previousColor = batch.getColor();
		batch.setColor(previousColor.r, previousColor.g, previousColor.b, 0.6f);
		batch.draw(sprite, 5, 5, sprite.getRegionWidth()*scaleX, sprite.getRegionHeight()*scaleY);
	}

	@Override
	public void blockInserted(int x, int y, Block block) {
		pixmap.drawPixel(x, getPixMapY(y), 0);
		pixmap.drawPixel(x, getPixMapY(y), getColor(block));
	}

	@Override
	public void blockRemoved(int x, int y) {
		
		pixmap.drawPixel(x, getPixMapY(y), 0);
		pixmap.drawPixel(x, getPixMapY(y), NO_BLOCK);
	}
	
}

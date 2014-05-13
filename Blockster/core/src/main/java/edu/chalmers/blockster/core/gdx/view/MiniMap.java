package edu.chalmers.blockster.core.gdx.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.blockster.core.objects.ActiveBlockListener;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;

public class MiniMap implements BlockMapListener, ActiveBlockListener {

	private final Pixmap pixmap;
	private final Map<Block, int[]> activeBlocks;
	private float scaleX;
	private float scaleY;
	
	
	public static final int NO_BLOCK = Color.rgba8888(0, 0, 0, 1f);
	public static final int SOLID_BLOCK = Color.rgba8888(0.8f, 0.8f, 0.8f, 1f);
	public static final int LIFTABLE_BLOCK = Color.rgba8888(0.8f, 0, 0, 1f); 
	
	public MiniMap (Pixmap pixmap) {
		this.pixmap = pixmap;
		this.scaleX = 1f;
		this.scaleY = 1f;
		activeBlocks = new HashMap<Block, int[]>();
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
		pixmap.drawPixel(x, getPixMapY(y), getColor(block));
	}

	@Override
	public void blockRemoved(int x, int y) {
		
	}

	@Override
	public void blockActivated(Block block) {
		final int[] blockPosition = {(int)block.getOriginX(), (int)block.getOriginY()};
		activeBlocks.put(block, blockPosition);
	}

	@Override
	public void blockDeactivated(Block block) {
		final int[] blockPosition = activeBlocks.get(block);
		pixmap.drawPixel(blockPosition[0], getPixMapY(blockPosition[1]), NO_BLOCK);
		activeBlocks.remove(block);
	}
	
}

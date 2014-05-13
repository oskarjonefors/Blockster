package edu.chalmers.blockster.core.gdx.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.blockster.core.objects.ActiveBlockListener;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;

public class MiniMap implements BlockMapListener, ActiveBlockListener {


	
	private final List<Block> activeBlockList;
	private final List<Block> staticBlockList;
	
	private float scaleX;
	private float scaleY;
	
	private final int width, height;
	
	
	public static final int NO_BLOCK = Color.rgba8888(0, 0, 0, 1f);
	public static final int SOLID_BLOCK = Color.rgba8888(0.8f, 0.8f, 0.8f, 1f);
	public static final int LIFTABLE_BLOCK = Color.rgba8888(0.8f, 0, 0, 1f); 
	
	public MiniMap (int width, int height) {
		this.scaleX = 1f;
		this.scaleY = 1f;
		this.width = width;
		this.height = height;
		activeBlockList = new ArrayList<Block>();
		staticBlockList = new ArrayList<Block>();
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
	
	private int getPixMapY(Pixmap pixmap, int y) {
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
		Sprite staticBlockSprite = new Sprite(new Texture(getPixmap()));
		Color previousColor = batch.getColor();
		batch.setColor(previousColor.r, previousColor.g, previousColor.b, 0.6f);
		batch.draw(staticBlockSprite, 5, 5, staticBlockSprite.getRegionWidth()*scaleX, staticBlockSprite.getRegionHeight()*scaleY);
	}
	
	private Pixmap getPixmap() {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(NO_BLOCK);
		pixmap.fill();
		
		for (Block block : staticBlockList) {
			pixmap.drawPixel((int) block.getOriginX(), 
					getPixMapY(pixmap, (int) block.getOriginY()), getColor(block));
		}
		
		for (Block block : activeBlockList) {
			pixmap.drawPixel((int) block.getOriginX(), 
					getPixMapY(pixmap, (int) block.getOriginY()), getColor(block));
		}
		
		return pixmap;
	}

	@Override
	public void blockInserted(Block block) {
		staticBlockList.add(block);
	}

	@Override
	public void blockRemoved(Block block) {
		staticBlockList.remove(block);
	}

	@Override
	public void blockActivated(Block block) {
		activeBlockList.add(block);
	}

	@Override
	public void blockDeactivated(Block block) {
		activeBlockList.remove(block);
	}
	
}

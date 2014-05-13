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
	
	private int scaleX;
	private int scaleY;
	
	private final int mapWidth, mapHeight;
	private int width, height;
	private float[][] playerPos;
	private float viewX, viewY, viewportWidth, viewportHeight;
	
	public static final int NO_BLOCK = Color.rgba8888(0, 0, 0, 1f);
	public static final int SOLID_BLOCK = Color.rgba8888(0.8f, 0.8f, 0.8f, 1f);
	public static final int LIFTABLE_BLOCK = Color.rgba8888(0.8f, 0, 0, 1f); 
	public static final int VIEWPORT = Color.rgba8888(1f, 1f, 1f, 1f);
	public static final int ACTIVE_PLAYER = Color.rgba8888(1f, 1f, 0, 1f);
	public static final int INACTIVE_PLAYER = Color.rgba8888(0, 0, 1f, 1f);
	
	public MiniMap (int mapWidth, int mapHeight) {
		this.scaleX = 1;
		this.scaleY = 1;
		this.mapWidth = mapWidth;
		this.mapHeight = mapHeight;
		
		viewportWidth = 0;
		viewportHeight = 0;
		
		activeBlockList = new ArrayList<Block>();
		staticBlockList = new ArrayList<Block>();
		
		playerPos = new float[0][0];
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
		return pixmap.getHeight() - y;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
		this.width = this.mapWidth * this.scaleX; 
	}
	
	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
		this.height = this.mapHeight * this.scaleY; 
	}
	
	public void setPlayerLocations(float[][] locations) {
		playerPos = locations;
	}
	
	public void setViewportBounds(float x, float y, float width, float height) {

		viewX = x;
		viewY = -y;
		viewportWidth = width;
		viewportHeight = height;
	}

	public void draw(SpriteBatch batch) {
		Sprite staticBlockSprite = new Sprite(new Texture(getPixmap()));
		Color previousColor = batch.getColor();
		batch.setColor(previousColor.r, previousColor.g, previousColor.b, 0.6f);
		
		Pixmap viewport = new Pixmap(width, height, Format.RGBA8888);
		viewport.setColor(VIEWPORT);
		viewport.drawRectangle((int)viewX*scaleX, (int)viewY * scaleY,
				(int)viewportWidth*scaleX, (int)viewportHeight*scaleY);
		
		viewport.setColor(INACTIVE_PLAYER);
		for(int i = 0; i < playerPos.length; i++) {
			final int x =  (int)playerPos[i][0]*scaleX;
			final int y = (int)(height - playerPos[i][1]*scaleY);
			final int r = (int)(scaleX*0.5);
			
			viewport.fillCircle(x, y, r);
		}
		
		Sprite viewportSprite = new Sprite(new Texture(viewport));
		batch.draw(staticBlockSprite, 5, 5, staticBlockSprite.getRegionWidth(), 
				staticBlockSprite.getRegionHeight());
		batch.draw(viewportSprite, 5, 5, viewportSprite.getRegionWidth(),
				viewportSprite.getRegionHeight());
		
	}
	
	private Pixmap getPixmap() {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(NO_BLOCK);
		pixmap.fill();
		
		for (Block block : staticBlockList) {

		
			for (int x = 0; x < scaleX; x++) {
				for (int y = 0; y < scaleY; y++) {
					pixmap.drawPixel(Math.round(block.getX() * scaleX + x), 
							getPixMapY(pixmap, Math.round(block.getY() 
									* scaleY + y)), getColor(block));
				}
			}
		}
		
		for (Block block : activeBlockList) {
			for (int x = 0; x < scaleX; x++) {
				for (int y = 0; y < scaleY; y++) {
					pixmap.drawPixel(Math.round(block.getX() * scaleX + x), 
							getPixMapY(pixmap, Math.round(block.getY() 
									* scaleY + y)), getColor(block));
				}
			}
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

package edu.chalmers.blockster.core.gdx.view;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.chalmers.blockster.core.objects.ActiveBlockListener;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;

public class MiniMap implements BlockMapListener, ActiveBlockListener {


	
	private final List<Block> activeBlockList;
	private final List<Block> staticBlockList;
	
	private int scaleX;
	private int scaleY;
	
	private final Sprite backgroundSprite;
	private final Sprite blockSprite;
	private final Sprite playerSprite;
	private final Sprite viewportSprite;
	
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
		
		blockSprite = new Sprite();
		playerSprite = new Sprite();
		backgroundSprite = new Sprite();
		viewportSprite = new Sprite();
		
		blockSprite.setColor(1, 1, 1, 1);
		playerSprite.setColor(1, 1, 1, 1);
		backgroundSprite.setColor(1, 1, 1, 1);
		viewportSprite.setColor(1, 1, 1, 1);
	}
	
	@Override
	public void blockActivated(Block block) {
		activeBlockList.add(block);
	}
	
	@Override
	public void blockDeactivated(Block block) {
		activeBlockList.remove(block);
	}
	
	@Override
	public void blockInserted(Block block) {
		staticBlockList.add(block);
	}
	
	@Override
	public void blockRemoved(Block block) {
		staticBlockList.remove(block);
	}
	
	public void draw(SpriteBatch batch) {
		prepareSprite(backgroundSprite, new Texture(getBackgroundPixmap()));
		prepareSprite(blockSprite, new Texture(getBlockPixmap()));
		prepareSprite(playerSprite, new Texture(getPlayerPixmap()));
		prepareSprite(viewportSprite, new Texture(getViewportPixmap()));
		
		Color previousColor = batch.getColor();
		batch.setColor(previousColor.r, previousColor.g, previousColor.b, 0.6f);
		
		batch.draw(backgroundSprite, 5, 5);
		batch.draw(blockSprite, 5, 5);
		batch.draw(playerSprite, 5, 5);
		batch.draw(viewportSprite, 5, 5);
		
	}
	
	private void drawActiveBlocks(Pixmap pixmap) {
		pixmap.setColor(INACTIVE_PLAYER);
		for (Block block : activeBlockList) {
			for (int x = 0; x < scaleX; x++) {
				for (int y = 0; y < scaleY; y++) {
					pixmap.drawPixel(Math.round(block.getX() * scaleX + x), 
							getPixMapY(pixmap, Math.round(block.getY() 
									* scaleY + y)), getColor(block));
				}
			}
		}
	}
	
	private void drawPlayers(Pixmap pixmap) {
		for(int i = 0; i < playerPos.length; i++) {
			final int x =  (int)(playerPos[i][0]*scaleX);
			final int y = (int)(height - playerPos[i][1]*scaleY);
			final int r = (int)(scaleX*0.5);
			
			if(Math.abs(x - scaleX*(viewX + viewportWidth / 2f)) <= scaleX*0.2f &&
					Math.abs(y - scaleY*(viewY + viewportHeight / 2f)) <= scaleY*2f) {
				pixmap.setColor(ACTIVE_PLAYER);
			} else {
				pixmap.setColor(INACTIVE_PLAYER);
			}
			pixmap.fillCircle(x, y, r);
		}
	}
	
	private void drawStaticBlocks(Pixmap pixmap) {
		for (Block block : staticBlockList) {
			for (int x = 0; x < scaleX; x++) {
				for (int y = 0; y < scaleY; y++) {
					pixmap.drawPixel(Math.round(block.getX() * scaleX + x), 
							getPixMapY(pixmap, Math.round(block.getY() 
									* scaleY + y)), getColor(block));
				}
			}
		}
	}

	private Pixmap getBackgroundPixmap() {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(NO_BLOCK);
		pixmap.fill();
		return pixmap;
	}
	
	private Pixmap getBlockPixmap() {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		drawStaticBlocks(pixmap);
		drawActiveBlocks(pixmap);
		return pixmap;
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
	
	private Pixmap getPlayerPixmap() {
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		pixmap.setColor(INACTIVE_PLAYER);
		drawPlayers(pixmap);
		return pixmap;
	}
	
	public float getScaleX() {
		return scaleX;
	}
	
	public float getScaleY() {
		return scaleY;
	}
	
	private Pixmap getViewportPixmap() {
		Pixmap viewport = new Pixmap(width, height, Format.RGBA8888);
		viewport.setColor(VIEWPORT);
		viewport.drawRectangle((int)(viewX*scaleX), (int)(viewY * scaleY),
				(int)(viewportWidth*scaleX), (int)(viewportHeight*scaleY));

		
		return viewport;
	}
	
	private void prepareSprite(Sprite sprite, Texture texture) {
		int width = texture.getWidth(), height = texture.getHeight();
		
		sprite.setTexture(texture);
		sprite.setRegion(0, 0, width, height);
		sprite.setSize(Math.abs(width), Math.abs(height));
	}

	public void setPlayerLocations(float[][] locations) {
		playerPos = locations;
	}

	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
		this.width = this.mapWidth * this.scaleX; 
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
		this.height = this.mapHeight * this.scaleY; 
	}

	public void setViewportBounds(float x, float y, float width, float height) {

		viewX = x;
		viewY = -y;
		viewportWidth = width;
		viewportHeight = height;
	}
	
}

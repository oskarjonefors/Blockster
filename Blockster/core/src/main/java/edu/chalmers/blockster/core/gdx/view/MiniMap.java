package edu.chalmers.blockster.core.gdx.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import edu.chalmers.blockster.core.objects.ActiveBlockListener;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;
import edu.chalmers.blockster.core.objects.EmptyBlock;

public class MiniMap implements BlockMapListener, ActiveBlockListener {


	
	private final Set<Block> activeBlocks;
	private final Set<Block> staticBlocks;
	
	private int scaleX;
	private int scaleY;
	
	private final Sprite minimapSprite;
	private Texture previousTexture;
	
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
		
		activeBlocks = new HashSet<Block>();
		staticBlocks = new HashSet<Block>();
		
		playerPos = new float[0][0];
		
		minimapSprite = new Sprite();
		minimapSprite.setColor(1, 1, 1, 1);
	}
	
	@Override
	public void blockActivated(Block block) {
		if (!(block instanceof EmptyBlock)) {
			activeBlocks.add(block);
		}
	}
	
	@Override
	public void blockDeactivated(Block block) {
		if (!(block instanceof EmptyBlock)) {
			activeBlocks.remove(block);
		}
	}
	
	@Override
	public void blockInserted(Block block) {
		if (!(block instanceof EmptyBlock)) {
			staticBlocks.add(block);
		}
	}
	
	@Override
	public void blockRemoved(Block block) {
		if (!(block instanceof EmptyBlock)) {
			staticBlocks.remove(block);
		}
	}
	
	public void draw(SpriteBatch batch) {
		Texture spriteTexture = new Texture(getMinimapPixmap());
		
		if (previousTexture != null) {
			previousTexture.dispose();
		}
		
		prepareSprite(minimapSprite, spriteTexture);
		
		Color previousColor = batch.getColor();
		batch.setColor(previousColor.r, previousColor.g, previousColor.b, 0.6f);
		
		batch.draw(minimapSprite, 5, 5);
		previousTexture = spriteTexture;
	}
	
	private void drawActiveBlocks(Pixmap pixmap, Bounds bounds) {
		for (Block block : activeBlocks) {
			float x = block.getX();
			float y = block.getY();
			if (bounds.contains(x, y)) {
				pixmap.setColor(getColor(block));
				pixmap.fillRectangle(Math.round((x - bounds.x) * scaleX), 
						getPixMapY(pixmap, Math.round((y + bounds.y + 1) * scaleY)), 
						scaleX, scaleY);
				
			}
		}
	}
	
	private void drawPlayers(Pixmap pixmap, Bounds bounds) {
		pixmap.setColor(INACTIVE_PLAYER);
		for(int i = 0; i < playerPos.length; i++) {
			final float x =  playerPos[i][0];
			final float y = playerPos[i][1];
			final int r = (int) Math.round(scaleX*0.5);

			if (bounds.contains(x, y)) {
				pixmap.setColor(ACTIVE_PLAYER);
				pixmap.fillCircle(Math.round((x - bounds.x) * scaleX), 
						getPixMapY(pixmap, Math.round((y + bounds.y) * scaleY) + 1), 
								r);
			}

		}
	}
	
	private void drawStaticBlocks(Pixmap pixmap, Bounds bounds) {
		for (Block block : staticBlocks) {
			float x = block.getX();
			float y = block.getY() + 1;
			if (bounds.contains(x, y)) {
				pixmap.setColor(getColor(block));
				pixmap.fillRectangle(Math.round((x - bounds.x) * scaleX), 
						getPixMapY(pixmap, Math.round((y + bounds.y) * scaleY)), 
						scaleX, scaleY);
				
			}
		}
	}

	private Pixmap getMinimapPixmap() {
		float minimapWidth = viewportWidth + 10;
		float minimapHeight = viewportHeight + 10;
		float minimapX = viewX - 5;
		float minimapY = viewY - 5;
		Bounds bounds = new Bounds(minimapX, minimapY, minimapWidth, minimapHeight);
		
		
		Pixmap pixmap = new Pixmap((int) (minimapWidth*scaleX),
				(int) (minimapHeight*scaleY), Format.RGBA8888);
		
		
		drawBackground(pixmap);
		drawStaticBlocks(pixmap, bounds);
		drawActiveBlocks(pixmap, bounds);
		drawPlayers(pixmap, bounds);
		drawViewport(pixmap, bounds);
		
		return pixmap;
	}
	
	private void drawBackground(Pixmap pixmap) {
		pixmap.setColor(NO_BLOCK);
		pixmap.fill();
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
	
	private void drawViewport(Pixmap pixmap, Bounds bounds) {
		pixmap.setColor(VIEWPORT);
		int x = 5;
		int y = 5;
		pixmap.drawRectangle(x * scaleX, y * scaleY, 
				(int) ((bounds.width - 10) * scaleX), 
				(int) ((bounds.height - 10) * scaleY));
	}
	
	private void prepareSprite(Sprite sprite, Texture texture) {
		int texWidth = texture.getWidth(), height = texture.getHeight();
		
		sprite.setTexture(texture);
		sprite.setRegion(0, 0, texWidth, height);
		sprite.setSize(Math.abs(texWidth), Math.abs(height));
	}

	public void setPlayerLocations(float[][] locations) {
		playerPos = locations.clone();
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
	
	private class Bounds {
		
		public final float x, y, width, height;
		
		public Bounds(float x, float y, float width, float height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public boolean contains(float x, float y) {
			return x >= this.x - 3 && x < this.x + width + 3
					&& y >= this.y - 3 && y < this.y + height + 3;
		}
		
	}
	
	
}

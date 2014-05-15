package edu.chalmers.blockster.gdx.view;

import java.util.HashSet;
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
import edu.chalmers.blockster.core.objects.Player;

public class MiniMap implements BlockMapListener, ActiveBlockListener {


	
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
	private final Set<Block> activeBlocks;
	
	private final Set<Block> staticBlocks;
	private int scaleX;
	

	private int scaleY;
	private final float offsetX = 16f;
	
	
	private final float offsetY = 16f;
	private Player activePlayer;
	private final Sprite minimapSprite;
	
	private Texture previousTexture;
	private final int width, height;
	private float[][] playerPos;
	
	private float viewX, viewY, viewportWidth, viewportHeight;
	public static final int NO_BLOCK = Color.rgba8888(0, 0, 0, 1f);
	public static final int SOLID_BLOCK = Color.rgba8888(0.8f, 0.8f, 0.8f, 1f); 
	public static final int LIFTABLE_BLOCK = Color.rgba8888(0.8f, 0, 0, 1f);
	public static final int VIEWPORT = Color.rgba8888(1f, 1f, 1f, 0.15f);
	public static final int ACTIVE_PLAYER = Color.rgba8888(1f, 1f, 0, 1f);
	
	public static final int INACTIVE_PLAYER = Color.rgba8888(0, 0, 1f, 1f);
	
	public MiniMap (int mapWidth, int mapHeight) {
		this.scaleX = 1;
		this.scaleY = 1;
		this.width = mapWidth;
		this.height = mapHeight;
		
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
		Texture spriteTexture = new Texture(getMinimapPixmap(getDrawBounds()));
		
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
				pixmap.fillRectangle(Math.round(x * scaleX), 
						getPixMapY(pixmap, Math.round((y+1) * scaleY)), 
						scaleX, scaleY);
				
			}
		}
	}
	
	private void drawBackground(Pixmap pixmap) {
		pixmap.setColor(NO_BLOCK);
		pixmap.fill();
	}
	
	private void drawPlayers(Pixmap pixmap, Bounds bounds) {
		pixmap.setColor(INACTIVE_PLAYER);
		for(int i = 0; i < playerPos.length; i++) {
			final float x =  playerPos[i][0];
			final float y = playerPos[i][1];
			final int r = (int) Math.round(scaleX*0.5);

			if (bounds.contains(x, y)) {
				pixmap.setColor(ACTIVE_PLAYER);
				pixmap.fillCircle(Math.round(x * scaleX), 
						getPixMapY(pixmap, Math.round((y+0.3f) * scaleY)), 
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
				pixmap.fillRectangle(Math.round(x * scaleX), 
						getPixMapY(pixmap, Math.round(y * scaleY)), 
						scaleX, scaleY);
				
			}
		}
	}

	private void drawViewport(Pixmap pixmap, Bounds bounds) {
		pixmap.setColor(VIEWPORT);
		int x = Math.max(1, Math.round(viewX * scaleX));
		int y = Math.max(1, Math.round(viewY * scaleY));
		int width = Math.round(viewportWidth * scaleX);
		int height = Math.round(viewportHeight * scaleY);

		if (width + x >= this.width * scaleX) {
			width = this.width * scaleX - x - 1;
		}
		
		if (height + y >= this.height * scaleY) {
			height = this.height * scaleY - y - 1;
		}
		
		
		pixmap.fillRectangle(x, y, width, height);
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
	
	private Bounds getDrawBounds() {
		//Make the side two times the offset long (one time for each side of the center)
		float width = 2 * offsetX;
		float height = 2 * offsetY;
		
		//Get the center point of the player, then subtract by offset
		float x = (activePlayer.getX() + activePlayer.getWidth() / 2f) 
				/ activePlayer.getScaleX() - offsetX;
		float y = (activePlayer.getY() + activePlayer.getHeight() / 2f) 
				/ activePlayer.getScaleY() - offsetY + 1;
		
		Bounds bounds = new Bounds(x, y, width, height);
		return bounds;
	}
	
	private Pixmap getMinimapPixmap(Bounds bounds) {
		
		
		Pixmap pixmap = new Pixmap(width*scaleX,
				height*scaleY, Format.RGBA8888);
		
		
		drawBackground(pixmap);
		drawStaticBlocks(pixmap, bounds);
		drawActiveBlocks(pixmap, bounds);
		drawPlayers(pixmap, bounds);
		drawViewport(pixmap, bounds);
		
		return pixmap;
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
	
	private Bounds getTextureBounds() {
		//Make the side two times the offset long (one time for each side of the center)
		float width = 2 * offsetX;
		float height = 2 * offsetY;
		
		//Get the center point of the player, then subtract by offset
		float x = (activePlayer.getX() + activePlayer.getWidth() / 2f) 
				/ activePlayer.getScaleX() - offsetX;
		float y = (activePlayer.getY() - activePlayer.getHeight() / 2f) 
				/ activePlayer.getScaleY() + offsetY + 1;
		
		Bounds bounds = new Bounds(x, y, width, height);
		return bounds;
	}

	private void prepareSprite(Sprite sprite, Texture texture) {
		Bounds bounds = getTextureBounds();
		float textX = bounds.x * scaleX;
		float textY = (height - bounds.y) * scaleY;
		float textWidth = bounds.width * scaleX;
		float textHeight = bounds.height * scaleY;
		
		sprite.setTexture(texture);
		sprite.setRegion((int) textX, (int) textY, (int) textWidth, (int) textHeight);
		sprite.setSize(Math.abs(textWidth), Math.abs(textHeight));
	}

	public void setActivePlayer(Player player) {
		this.activePlayer = player;
	}

	public void setPlayerLocations(float[][] locations) {
		playerPos = locations.clone();
	}
	
	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}
	
	public void setViewportBounds(float x, float y, float width, float height) {

		viewX = x;
		viewY = -y;
		viewportWidth = width;
		viewportHeight = height;
	}
	
	
}

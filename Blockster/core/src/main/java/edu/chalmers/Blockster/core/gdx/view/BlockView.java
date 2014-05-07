package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.Blockster.core.objects.Block;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr, Oskar Jönefors, Emilia Nilsson
 * 
 */
public class BlockView implements TiledMapTile {
	
	private TiledMapTile tile;
	private TextureRegion region;
	private Block block;
	
	public BlockView(Block block, TiledMapTile tile) {
		this.block = block;
		this.tile = tile;
		region = tile.getTextureRegion();
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(region, getX()*128, getY()*128);
	}
	
	public int getId() {
		return tile.getId();
	}

	public MapProperties getProperties() {
		return tile.getProperties();
	}

	public TextureRegion getTextureRegion() {
		return tile.getTextureRegion();
	}

	public TiledMapTile getTile() {
		return tile;
	}
	
	public void setId(int id) {
		tile.setId(id);
	}

	@Override
	public BlendMode getBlendMode() {
		// TODO Auto-generated method stub
		return tile.getBlendMode();
	}

	@Override
	public void setBlendMode(BlendMode arg0) {
		// TODO Auto-generated method stub
		tile.setBlendMode(arg0);
	}

	public float getX() {
		return block.getX();
	}

	public float getY() {
		return block.getY();
	}
	
}

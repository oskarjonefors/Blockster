package edu.chalmers.blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;

/**
 * A block
 * TODO: Add block specific attributes
 * @author Eric Bjuhr, Oskar Jönefors, Emilia Nilsson
 * 
 */
public class BlockView implements TiledMapTile {
	
	private final TiledMapTile tile;
	private final TextureRegion region;
	private final Block block;
	
	public Block getBlock() {
		return block;
	}

	public BlockView(Block block, TiledMapTile tile) {
		this.block = block;
		this.tile = tile;
		region = tile.getTextureRegion();
		
	}
	
	public void draw(SpriteBatch batch) {
		Sprite sprite = new Sprite(region);
		sprite.setPosition(getX()*128, getY()*128);
		if (!block.getAnimationState().isDone()) {
			Direction dir = block.getAnimationState().getMovement().getDirection();
			if (Math.abs(dir.deltaX) + Math.abs(dir.deltaY) == 2) {
				AnimationState anim = block.getAnimationState();
				sprite.setRotation(- dir.deltaX * 90f * (anim.getElapsedTime() 
						/ anim.getMovement().getDuration()));
			}
		}
		
		sprite.draw(batch);
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

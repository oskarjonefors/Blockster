package edu.chalmers.blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

/**
 * A graphical representation of a Block.
 * @author Eric Bjuhr, Oskar Jönefors, Emilia Nilsson
 * 
 */
public class BlockView implements TiledMapTile {
	
	private final TiledMapTile tile;
	private final TextureRegion region;
	private final Block block;
	private final Sprite sprite;
	
	private float rotation;
	
	public BlockView(Block block, TiledMapTile tile) {
		this.block = block;
		this.tile = tile;
		region = tile.getTextureRegion();
		sprite = new Sprite(region);
	}

	public void draw(SpriteBatch batch) {
		
		sprite.setPosition(getX()*128, getY()*128);
		setRotation();
		
		sprite.draw(batch);
	}
	
	@Override
	public BlendMode getBlendMode() {
		return tile.getBlendMode();
	}
	
	public Block getBlock() {
		return block;
	}
	
	public int getId() {
		return tile.getId();
	}
	
	public MapProperties getProperties() {
		return tile.getProperties();
	}
	
	private float getRotation() {

		AnimationState anim = block.getAnimationState();
		Direction dir = anim.getMovement().getDirection();
		return - dir.deltaX * 90f * (anim.getElapsedTime() 
				/ anim.getMovement().getDuration());
	}

	public TextureRegion getTextureRegion() {
		return tile.getTextureRegion();
	}

	public TiledMapTile getTile() {
		return tile;
	}

	public float getX() {
		return block.getX();
	}
	
	public float getY() {
		return block.getY();
	}

	private boolean isMovementAnimationDiagonal() {
		Direction dir = block.getAnimationState().getMovement().getDirection();
		return Math.abs(dir.deltaX) + Math.abs(dir.deltaY) == 2;
	}

	private boolean shouldRotate() {
		return isMovementAnimationDiagonal() && !isClimbMovement();
	}
	
	private boolean isClimbMovement() {
		Movement movement = block.getAnimationState().getMovement();
		return movement == Movement.CLIMB_LEFT || movement == Movement.CLIMB_RIGHT;
	}
	
	@Override
	public void setBlendMode(BlendMode arg0) {
		tile.setBlendMode(arg0);
	}

	public void setId(int id) {
		tile.setId(id);
	}

	private void setRotation() {
		if (!block.getAnimationState().isDone()) {
			if (shouldRotate()) {
				sprite.setRotation(getRotation());
			} else {
				sprite.setRotation(rotation);
			}
		} else {
			rotation = sprite.getRotation();
		}
	}
	
}

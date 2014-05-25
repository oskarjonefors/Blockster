package edu.chalmers.blockster.gdx.view;

import javax.vecmath.Vector2f;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.interactions.AbstractPlayerInteraction;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.Calculations;

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
	private float previousRotation = 0;
	private boolean hasSetPreviousRotation = false;

	public BlockView(Block block, TiledMapTile tile) {
		this.block = block;
		this.tile = tile;
		region = tile.getTextureRegion();
		sprite = new Sprite(region);
	}

	public void draw(SpriteBatch batch) {
		sprite.setPosition(getX()*region.getRegionWidth(), getY()*region.getRegionHeight());
		evaluateRotation();

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

	public TextureRegion getTextureRegion() {
		if (!hasSetPreviousRotation) {
			previousRotation = getTotalRotation();
			rotation = 0;
			hasSetPreviousRotation = true;
		}

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

	private boolean playerHasSameMovement() {
		Movement blockMovement = block.getAnimationState().getMovement();
		Movement playerMovement = Movement.NONE;
		AbstractPlayerInteraction interaction = block.getInteraction();
		if (interaction != AbstractPlayerInteraction.NONE) {
			playerMovement = ((Player) interaction.getInteractor()).getAnimationState().getMovement();
		}
		
		return blockMovement != Movement.NONE && blockMovement == playerMovement;
	}

	private boolean isMovementAnimationDiagonal() {
		Direction dir = block.getAnimationState().getMovement().getDirection();
		return Math.abs(dir.getDeltaX()) + Math.abs(dir.getDeltaY()) == 2;
	}

	@Override
	public void setBlendMode(BlendMode arg0) {
		tile.setBlendMode(arg0);
	}

	public void setId(int id) {
		tile.setId(id);
	}

	private void evaluateRotation() {
		if (shouldRotate()) {
			updateRotation();
			sprite.setRotation(getTotalRotation());
			hasSetPreviousRotation = false;
		} else {
			sprite.setRotation(getOrtagonalDegrees());
		}

	}

	private float getTotalRotation() {
		return previousRotation + rotation;
	}

	public int getOrtagonalDegrees() {
		int dif = 45, targetVal = Math.round(getTotalRotation()) % 360;
		int[] ortagonalDegrees = { 0, 90, 180, 270 };
		return  Calculations.getClosestNumber(targetVal, dif, 0, ortagonalDegrees);
	}

	private void updateRotation() {
		final AnimationState anim = block.getAnimationState();
		Vector2f v = anim.getRelativePosition();
		switch (anim.getMovement().getDirection()) {
		case UP_LEFT:
			rotation = (float) (360d * Math.atan2(v.y, 1 + v.x) / (2 * Math.PI));
			break;
		case UP_RIGHT:
			rotation = (float) (360d * Math.atan2(-v.y, 1 - v.x) / (2 * Math.PI));
			break;
		case DOWN_LEFT:
			rotation = (float) (360d * Math.atan2(-v.y, 1 + v.x) / (2 * Math.PI));
			break;
		case DOWN_RIGHT:
			rotation = (float) (360d * Math.atan2(v.y, 1 - v.x) / (2 * Math.PI));
			break;
		default: break;
		}
	}

	private boolean shouldRotate() {

		
		
		return isMovementAnimationDiagonal() && !playerHasSameMovement();
	}

}

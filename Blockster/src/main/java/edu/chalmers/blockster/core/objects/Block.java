package edu.chalmers.blockster.core.objects;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.interactions.Interactable;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridObject;

public class Block extends BlocksterObject implements GridObject, Interactable {

	private static final Logger LOG = Logger.getLogger(Block.class.getName());

	private final Set<String> properties;
	private boolean lifted;

	public Block(float startX, float startY, BlockMap blockLayer) {
		super(startX, startY, blockLayer, 1, 1);
		properties = new TreeSet<String>();
		lifted = false;
	}

	public void setProperty(String property) {
		properties.add(property.toLowerCase(Locale.ENGLISH));
	}

	public boolean canMove(Direction dir) {
		final BlockMap blockLayer = getBlockLayer();
		final float mapHeight = blockLayer.getHeight();
		final float mapWidth = blockLayer.getWidth();
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();
		final boolean collision = blockLayer.hasBlock(checkX,checkY);
		
		return checkX >= 0 && checkX <= mapWidth - 1 && !collision;

	}

	public boolean isLifted() {
		return lifted;
	}
	
	public boolean isTeleporter(){
		return properties.contains("teleporter");
	}
	
	@Override
	public boolean isSolid() {
		return properties.contains("solid");
	}

	@Override
	public boolean isLiftable() {
		return properties.contains("liftable");
	}

	@Override
	public boolean isMovable() {
		return properties.contains("movable");
	}

	@Override
	public boolean hasWeight() {
		return properties.contains("weight");
	}

	public void fallDown() {
		if (!blockMap.hasBlock((int) getX(), (int) (getY() - 1))) {
			AnimationState anim = new AnimationState(Movement.FALL_DOWN);
			this.setAnimationState(anim);
		}
	}

	@Override
	public void moveToNextPosition() {
		LOG.log(Level.INFO, "Removing" + this);
		super.moveToNextPosition();
	}

	@Override
	public void setAnimationState(AnimationState anim) {
		super.setAnimationState(anim);
		if (anim != AnimationState.NONE) {
			blockMap.addActiveBlock(this);
		}
	}

	public void setLifted(boolean lifted) {
		this.lifted = lifted;
	}

	public void removeFromGrid() {
		blockMap.removeBlock(this);
	}

	public boolean canBeClimbed() {

		final int blockX = (int) getX();
		final int blockY = (int) getY();

		return !blockMap.hasBlock(blockX, blockY + 1);
	}

	public boolean canBeGrabbed() {
		return isMovable() || isLiftable();
	}
	
	public boolean removeProperty(String string) {
		return properties.remove(string.toLowerCase(Locale.ENGLISH));
	}
}

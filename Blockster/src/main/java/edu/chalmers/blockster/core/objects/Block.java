package edu.chalmers.blockster.core.objects;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import edu.chalmers.blockster.core.objects.interactions.AbstractPlayerInteraction;
import edu.chalmers.blockster.core.objects.interactions.Interactable;
import edu.chalmers.blockster.core.objects.interactions.Interactor;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridObject;

public class Block extends AbstractBlocksterObject implements GridObject,
		Interactable {

	private final Set<String> properties;

	private AbstractPlayerInteraction interaction;

	private boolean lifted;

	public Block(float startX, float startY, BlockMap blockLayer) {
		super(startX, startY, blockLayer, 1, 1);
		properties = new TreeSet<String>();
		lifted = false;
	}

	public boolean canBeClimbed() {

		final int blockX = (int) getX();
		final int blockY = (int) getY();

		return !blockMap.hasBlock(blockX, blockY + 1);
	}

	public boolean canBeGrabbed() {
		return isMovable() || isLiftable();
	}

	public boolean canMove(Direction dir) {
		final BlockMap blockLayer = getBlockMap();
		final float mapWidth = blockLayer.getWidth();
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();
		final boolean collision = blockLayer.hasBlock(checkX, checkY);
		final boolean animationDone = getAnimationState().isDone();

		return checkX >= 0 && checkX <= mapWidth - 1 && !collision
				&& animationDone;
	}

	public void fallDown() {
		final boolean collisionBelow = blockMap.hasBlock((int) getX(),
				(int) (getY() - 1))
				&& blockMap.getBlock((int) getX(), (int) (getY() - 1))
						.isSolid();

		if (!collisionBelow && hasWeight()) {
			if (isLifted()) {
				final Interactor interactor = getInteraction().getInteractor();
				if (!interactor.collisionBeneathNext(Direction.NONE)) {
					interactor.setAnimationState(new AnimationState(
							Movement.FALL_DOWN));
					setAnimationState(new AnimationState(Movement.FALL_DOWN));
				}
			} else {
				setAnimationState(new AnimationState(Movement.FALL_DOWN));
			}
		}
	}

	public AbstractPlayerInteraction getInteraction() {
		return interaction;
	}

	@Override
	public boolean hasWeight() {
		return properties.contains("weight");
	}

	@Override
	public boolean isLiftable() {
		return properties.contains("liftable");
	}

	public boolean isLifted() {
		return lifted;
	}

	@Override
	public boolean isMovable() {
		return properties.contains("movable");
	}

	@Override
	public boolean isSolid() {
		return properties.contains("solid");
	}

	public boolean isTeleporter() {
		return properties.contains("teleporter");
	}

	public void removeFromGrid() {
		blockMap.removeBlock(this);
	}

	public boolean removeProperty(String string) {
		return properties.remove(string.toLowerCase(Locale.ENGLISH));
	}

	@Override
	public void setAnimationState(AnimationState anim) {
		super.setAnimationState(anim);
		if (anim != AnimationState.NONE) {
			blockMap.addActiveBlock(this);
		}
	}

	public void setInteraction(AbstractPlayerInteraction interaction) {
		this.interaction = interaction;
	}

	public void setLifted(boolean lifted) {
		this.lifted = lifted;
	}

	public void setProperty(String property) {
		properties.add(property.toLowerCase(Locale.ENGLISH));
	}

	public boolean canBeLifted() {
		return isLiftable()
				&& !blockMap.hasBlock((int) getX(), (int) getY() + 1);
	}
}
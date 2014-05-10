package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockClimbInteraction extends PlayerInteraction {
	
	private final Interactor interactor;
	private final Interactable interactable;
	private final GridMap gridMap;
	
	public BlockClimbInteraction(Interactor interactor, Interactable interactable, GridMap gridMap) {
		this.interactor = interactor;
		this.interactable = interactable;
		this.gridMap = gridMap;
	}
	
	@Override
	public void interact(Direction dir) {
		final AnimationState anim = interactor.getAnimationState();
		if (anim != AnimationState.NONE && anim.isDone()) {
			interactor.setAnimationState(AnimationState.NONE);
		}
	}

	@Override
	public void endInteraction() {
		// TODO Auto-generated method stub

	}

	@Override
	public void startInteraction() {
		Direction dir = Direction.getDirection(
				interactor.getX() / interactor.getScaleX(), interactable.getX());
		AnimationState climb = new AnimationState(Movement.getClimbMovement(dir));
		interactor.setAnimationState(climb);
	}

}

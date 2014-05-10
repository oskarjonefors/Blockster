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
		
	}

	@Override
	public void endInteraction() {
		interactor.setClimbing(false);
	}

	@Override
	public void startInteraction() {
		Direction dir = Direction.getDirection(
				interactor.getX() / interactor.getScaleX(), interactable.getX());
		AnimationState climb = new AnimationState(Movement.getClimbMovement(dir));
		interactor.setAnimationState(climb);
		interactor.setClimbing(true);
	}

}

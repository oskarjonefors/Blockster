package edu.chalmers.blockster.core.objects.interactions;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockGrabbedInteraction extends PlayerInteraction {
	
	private final Interactable interactable; 
	private final GridMap blockLayer;
	private final Interactor interactor;
	
	public BlockGrabbedInteraction(Interactor interactor, 
			Interactable interactable, GridMap blockLayer) {
		this.interactable = interactable;
		this.blockLayer = blockLayer;
		this.interactor = interactor;
	}
	
	@Override
	public void endInteraction() {
		interactor.setGrabbing(false);
	}
	
	public List<Interactable> getMoveableInteractables(Direction dir) {
		/* Create a list to put the block to be moved in. */
		final List<Interactable> movingBlocks = new ArrayList<Interactable>();
		
		/* Origin of add process */
		final int origY = (int) interactable.getY();
		
		if (origY >= blockLayer.getHeight()) {
			return movingBlocks;
		}
		
		final int origX = (int) interactable.getX();

		/* We've already established that the move is okay,
			so we don't need to change the Y coordinate. */
		int checkX = origX;

		/* Loop to add all the blocks to be moved to the list. */
		while (blockLayer.hasBlock(checkX, origY) && checkX > 0 
				&& checkX < blockLayer.getWidth()) {
			if(!blockLayer.hasBlock(checkX, origY + 1) && blockLayer.
					getBlock(checkX, origY).isMovable()) {
				movingBlocks.add((Interactable) blockLayer.getBlock(checkX, origY));
				checkX += dir.deltaX;
			} else {
				movingBlocks.clear();
				break;
			}
		}
		
		return movingBlocks;
	}
	
	@Override
	public void interact(Direction dir) {

		System.out.println("Interacting: " + dir.name());
		final float relativePosition = interactable.getX() 
				- interactor.getX() / blockLayer.getBlockWidth();
		

		final int checkX = (int)interactable.getX();
		final int checkY = (int)interactable.getY() + 1;

		if (!blockLayer.hasBlock(checkX, checkY) ||
				!blockLayer.getBlock(checkX, checkY).hasWeight()) { 

			if(isInReach()) {
				if (Movement.isPullMovement(relativePosition, dir)) {
					pullBlock(dir);
				} else {
					pushBlocks(dir);
				}
			} else {
				endInteraction();
			}
		}
	}
	
	private boolean isInReach() {
		return Math.abs(interactable.getX() - (Math.round(interactor.getX()) / 
				interactor.getScaleX())) <= 1.1f &&
				Math.abs(interactable.getY() - (Math.round(interactor.getY()) / 
						interactor.getScaleY())) <= 1.1f;
	}
	
	private void pullBlock(Direction dir) {
		Movement movement = Movement.getPullMovement(dir);
		if (interactor.canMove(movement.getDirection())) {
			System.out.println("Can pull");
			interactable.setAnimationState(new AnimationState(movement));
			interactor.setAnimationState(new AnimationState(movement));
			interactable.removeFromGrid();
		}
	}

	private void pushBlocks(Direction dir) {
		Movement movement = Movement.getPushMovement(dir);
		List<Interactable> moveableInteractables = 
				getMoveableInteractables(movement.getDirection());
		
		if (!moveableInteractables.isEmpty()) {
			System.out.println("Can push");
			for (final Interactable interactable : moveableInteractables) {
				interactable.setAnimationState(new AnimationState(movement));
				interactable.removeFromGrid();
			}
			interactor.setAnimationState(new AnimationState(movement));
		}
	}

	@Override
	public void startInteraction() {
		interactor.setGrabbing(true);
	}
	


}

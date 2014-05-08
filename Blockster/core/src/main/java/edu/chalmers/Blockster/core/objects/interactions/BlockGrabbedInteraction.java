package edu.chalmers.Blockster.core.objects.interactions;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.Player;
import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.objects.movement.Direction;
import edu.chalmers.Blockster.core.objects.movement.Movement;
import edu.chalmers.Blockster.core.util.GridMap;

public class BlockGrabbedInteraction extends PlayerInteraction {
	
	private Interactable interactable; 
	private GridMap blockLayer;
	private Interactor interactor;
	
	public BlockGrabbedInteraction(Interactor interactor, 
			Interactable interactable, GridMap blockLayer) {
		this.interactable = interactable;
		this.blockLayer = blockLayer;
		this.interactor = interactor;
	}
	
	@Override
	public void interact(Direction dir) {
		
		System.out.println("Interacting: " + dir.name());
		float relativePosition = interactable.getX() 
				- interactor.getX() / blockLayer.getBlockWidth();
		Movement movement = Movement.getPushPullMovement(dir, relativePosition);
		List<Interactable> moveableInteractables;
		

		System.out.println((movement.isPullMovement() ? "IS" : "ISN'T")+ " PULL MOVEMENT ("+movement.name()+")");
		if (movement.isPullMovement()) {
			if (interactor.canMove(dir)) {
				System.out.println("Can pull");
				interactable.setAnimationState(new AnimationState(movement));
				interactor.setAnimationState(new AnimationState(movement));
				interactable.removeFromGrid();
			}
		} else {
			moveableInteractables = getMoveableInteractables(dir);
			for (Interactable interactable : moveableInteractables) {
				interactable.setAnimationState(new AnimationState(movement));
				interactable.removeFromGrid();
			}
			
			if (!moveableInteractables.isEmpty()) {
				System.out.println("Can push");
				interactor.setAnimationState(new AnimationState(movement));
			}
		}

		
	}
	
	public List<Interactable> getMoveableInteractables(Direction dir) {
		/* Create a list to put the block to be moved in. */
		List<Interactable> movingBlocks = new ArrayList<Interactable>();
		
		/* Origin of add process */
		int origX = (int) interactable.getX();
		int origY = (int) interactable.getY();
		
		if (origY >= blockLayer.getHeight()) {
			return movingBlocks;
		}

		/* We've already established that the move is okay,
			so we don't need to change the Y coordinate. */
		int checkX = (origX);

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
	public void endInteraction() {
		interactor.setGrabbing(false);
	}

	@Override
	public void startInteraction() {
		interactor.setGrabbing(true);
	}
	


}

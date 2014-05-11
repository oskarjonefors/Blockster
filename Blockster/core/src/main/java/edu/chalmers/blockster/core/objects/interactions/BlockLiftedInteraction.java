package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.Calculations;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockLiftedInteraction extends PlayerInteraction {
	
	private final Interactor interactor;
	private final Interactable interacted;
	private final GridMap blockMap;
	
	public BlockLiftedInteraction(Interactor interactor, Interactable interacted,
			GridMap blockMap) {
		this.interactor = interactor;
		this.interacted = interacted;
		this.blockMap = blockMap; 
	}

	@Override
	public void interact(Direction dir) {
		System.out.println("Interacting: " + dir.name());
		
		if(canPerformMove(dir)) {
			System.out.println("Can move");
			Movement move = Movement.getMoveMovement(dir);
			interactor.setAnimationState(new AnimationState(move));
			interacted.setAnimationState(new AnimationState(move));
			interacted.removeFromGrid();
		}
	}

	@Override
	public void endInteraction() {
		final Direction dir = interactor.getDirection();
		AnimationState anim = new AnimationState(Movement.getPlaceMovement(dir));
		
		if (interacted.canMove(anim.getMovement().getDirection())) {
			interactor.setLifting(false);
			interacted.setAnimationState(anim);
			interacted.setLifted(false);
			interacted.removeFromGrid();
		} else {
			System.out.println("COULD NOT END INTERACTION");
		}
	}

	@Override
	public void startInteraction() {
		final Direction dir = Direction.getDirection(interactor.getX(),
				interacted.getX() * blockMap.getBlockWidth());
		AnimationState anim = new AnimationState(Movement.getLiftMovement(dir));
		
		if (interacted.canMove(anim.getMovement().getDirection())) {
			interactor.setLifting(true);
			interacted.setAnimationState(anim);
			interacted.setLifted(true);
			interacted.removeFromGrid();
		}
	}
	
	public boolean canPerformMove(Direction dir) {
		return interactor.canMove(dir) && interacted.canMove(dir);
		
	}

}

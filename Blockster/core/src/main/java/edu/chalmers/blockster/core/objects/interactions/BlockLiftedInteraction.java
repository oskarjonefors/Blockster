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
			final AnimationState anim = 
					new AnimationState(Movement.getMoveMovement(dir));
			interactor.setAnimationState(anim);
			interacted.setAnimationState(anim);
			interacted.removeFromGrid();
		}
	}

	@Override
	public void endInteraction() {
		final Direction dir = interactor.getDirection();
		interacted.setAnimationState(new AnimationState(Movement.getPlaceMovement(dir)));
		interactor.setLifting(false);
	}

	@Override
	public void startInteraction() {
		final Direction dir = Direction.getDirection(interactor.getX(),
				interacted.getX() * blockMap.getBlockWidth());
		interacted.setAnimationState(new AnimationState(Movement.getLiftMovement(dir)));
		interactor.setLifting(true);
	}
	
	public boolean canPerformMove(Direction dir) {
		int flag = 0;
		if (dir == Direction.LEFT) {
			flag = Calculations.CHECK_LEFT_FLAG | Calculations.CHECK_DOWN_LEFT_FLAG;
		} else {
			flag = Calculations.CHECK_RIGHT_FLAG | Calculations.CHECK_DOWN_RIGHT_FLAG;
		}
		return !Calculations.collisionSurroundingBlocks(interacted, blockMap, flag);
	}

}

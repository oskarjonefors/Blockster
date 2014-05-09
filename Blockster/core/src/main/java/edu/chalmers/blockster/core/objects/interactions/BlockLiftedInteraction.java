package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.Calculations;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockLiftedInteraction extends PlayerInteraction {
	
	private Interactor interactor;
	private Interactable interacted;
	private GridMap blockMap;
	
	public BlockLiftedInteraction(Interactor interactor, Interactable interacted,
			GridMap blockMap) {
		this.interactor = interactor;
		this.interacted = interacted;
		this.blockMap = blockMap; 
	}

	@Override
	public void interact(Direction dir) {
		if(canPerformMove(dir)) {
			AnimationState anim = new AnimationState(Movement.getMoveMovement(dir));
			interactor.setAnimationState(anim);
			interacted.setAnimationState(anim);
		}
	}

	@Override
	public void endInteraction() {
		interactor.setLifting(false);
	}

	@Override
	public void startInteraction() {
		interacted.setAnimationState(new AnimationState(Movement.getLiftMovement
				(Direction.getDirection(interactor.getX(),
						interacted.getX() * blockMap.getBlockWidth()))));
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

package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockLiftedInteraction extends AbstractPlayerInteraction {

	private final Interactor interactor;
	private final Interactable interacted;
	private final GridMap blockMap;
	
	private static final int CANNOT_MOVE = 0;
	private static final int CAN_MOVE = 1;
	private static final int CAN_CLIMB_DOWN = 2;

	public BlockLiftedInteraction(Interactor interactor,
			Interactable interacted, GridMap blockMap) {
		super(interactor, interacted);
		this.interactor = interactor;
		this.interacted = interacted;
		this.blockMap = blockMap;
	}

	@Override
	public void interact(Direction dir) {
		Movement move;
		
		switch (getMovePerformType(dir)) {
			case CAN_MOVE:
				move = Movement.getMoveMovement(dir);
				break;
			case CAN_CLIMB_DOWN:
				move = Movement.getClimbDownMovement(dir);
				break;
			default: return;
		}

		interactor.setAnimationState(new AnimationState(move));
		interacted.setAnimationState(new AnimationState(move));
		interacted.removeFromGrid();
	}

	@Override
	public void endInteraction() {
		final Direction dir = interactor.getDirection();
		final Movement placeMovement = Movement.getPlaceMovement(dir);
		final AnimationState placeDown = new AnimationState(placeMovement);
		boolean done = false;
		
		if (interacted.canMove(placeMovement.getDirection())) {
			interacted.setAnimationState(placeDown);
			done = true;
		} else if (interacted.canMove(dir)) {
			interacted.setAnimationState(new AnimationState(Movement.getMoveMovement(dir)));
			done = true;
		}
		
		if (done) {
			interactor.setAnimationState(new AnimationState(
					Movement.getPlayerPlaceMovement(dir)));
			interactor.setLifting(false);
			interacted.setLifted(false);
			interacted.removeFromGrid();
		}
	}

	@Override
	public void startInteraction() {
		final float interactorX = interactor.getX();
		final float interactedX = interacted.getX();
		final Direction dir = Direction.getDirection(interactorX, interactedX
				* blockMap.getBlockWidth());
		final AnimationState anim = new AnimationState(Movement.getLiftMovement(dir));
		
		if (interacted.canMove(anim.getMovement().getDirection())
				&& !blockMap.hasBlock((int) interactedX,
						(int) interacted.getY() + 1)) {
			interactor.setLifting(true);
			interacted.setAnimationState(anim);
			interacted.setLifted(true);
			interacted.removeFromGrid();
		}
	}

	public int getMovePerformType(Direction dir) {
		final boolean interactorCanMove = interactor.canMove(dir);
		final boolean interactedCanMove = interacted.canMove(dir);
		final boolean collisionBeneathNext = interactor.collisionBeneathNext(dir);
		if (interactorCanMove && interactedCanMove && collisionBeneathNext) {
			return CAN_MOVE;
		} else if (interactorCanMove && interactedCanMove){
			return CAN_CLIMB_DOWN;
		} else {
			return CANNOT_MOVE;
		}

	}
}
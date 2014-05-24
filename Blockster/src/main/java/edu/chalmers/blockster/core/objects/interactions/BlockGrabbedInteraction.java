package edu.chalmers.blockster.core.objects.interactions;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockGrabbedInteraction extends PlayerInteraction {

	private static final Logger LOG = Logger
			.getLogger(BlockGrabbedInteraction.class.getName());

	private final GridMap blockLayer;
	private final Interactable interacted;
	private final Interactor interactor;

	public BlockGrabbedInteraction(Interactor interactor,
			Interactable interacted, GridMap blockLayer) {
		super(interactor, interacted);
		this.interacted = interacted;
		this.interactor = interactor;
		this.blockLayer = blockLayer;
	}

	@Override
	public void endInteraction() {
		interactor.setGrabbing(false);
	}

	private List<Interactable> getMoveableInteractables(Direction dir) {
		final List<Interactable> movingBlocks = new ArrayList<Interactable>();
		final int origY = (int) interacted.getY();
		
		int checkX = (int) interacted.getX();
		
		while (blockLayer.hasBlock(checkX, origY)) {
			boolean noBlockAbove = !blockLayer.hasBlock(checkX, origY + 1);
			boolean noWeightAbove = !blockLayer.getBlock(checkX, origY + 1).hasWeight();
			boolean isMovable = blockLayer.getBlock(checkX, origY).isMovable();
			boolean notCrossingBounds = checkX > 0  && checkX < blockLayer.getWidth() - 1;
			
			if ((noBlockAbove || noWeightAbove) && isMovable && notCrossingBounds) {
				movingBlocks.add((Interactable) blockLayer.getBlock(checkX, origY));
				checkX += dir.getDeltaX();
			} else {
				movingBlocks.clear();
				return movingBlocks;
			}
		}

		return movingBlocks;
	}

	@Override
	public void interact(Direction dir) {

		LOG.log(Level.FINE, "Interacting: " + dir.name());
		final float relativePosition = interacted.getX() - interactor.getX()
				/ blockLayer.getBlockWidth();

		if (isInReach()) {
			if (Movement.checkIfPullMovement(relativePosition, dir)) {
				pullBlock(dir);
			} else {
				pushBlocks(dir);
			}
		} else {
			endInteraction();
		}
	}

	private boolean isInReach() {
		return Math.abs(interacted.getX()
				- (Math.round(interactor.getX()) / interactor.getScaleX())) <= 1.1f
				&& Math.abs(interacted.getY()
						- (Math.round(interactor.getY()) / interactor
								.getScaleY())) <= 0.2f;
	}

	private void pullBlock(Direction dir) {
		Movement movement = Movement.getPullMovement(dir);
		if (canPerformMove(dir)) {
			LOG.log(Level.INFO, "Can pull");
			interacted.setAnimationState(new AnimationState(movement));
			interactor.setAnimationState(new AnimationState(movement));
			interacted.removeFromGrid();
		}
	}

	private void pushBlocks(Direction dir) {
		Movement movement = Movement.getPushMovement(dir);
		List<Interactable> moveableInteractables = getMoveableInteractables(movement
				.getDirection());

		if (!moveableInteractables.isEmpty()) {
			LOG.log(Level.INFO, "Can push");
			for (final Interactable interactable : moveableInteractables) {
				interactable.setAnimationState(new AnimationState(movement));
				interactable.removeFromGrid();
			}
			interactor.setAnimationState(new AnimationState(movement));
		}
	}
	
	public boolean canPerformMove(Direction dir) {
		boolean interactorCanMove = interactor.canMove(dir);
		boolean collisionBeneathNext = interactor.collisionBeneathNext(dir);
		boolean noBlockAbove = !blockLayer.hasBlock((int) interacted.getX(),
				(int) interacted.getY() + 1);
		boolean weightless = !blockLayer.getBlock((int) interacted.getX(),
				(int) interacted.getY() + 1).hasWeight();
		return collisionBeneathNext && interactorCanMove && (noBlockAbove 
				|| weightless);

	}

	@Override
	public void startInteraction() {
		interactor.setGrabbing(true);
	}

}
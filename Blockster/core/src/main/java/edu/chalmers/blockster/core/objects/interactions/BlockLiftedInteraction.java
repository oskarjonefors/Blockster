package edu.chalmers.blockster.core.objects.interactions;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;
import edu.chalmers.blockster.core.util.GridMap;

public class BlockLiftedInteraction extends PlayerInteraction {

    private static final Logger LOG = Logger.getLogger(BlockLiftedInteraction.class.getName());

    private final Interactor interactor;
    private final Interactable interacted;
    private final GridMap blockMap;

    public BlockLiftedInteraction(Interactor interactor, Interactable interacted, GridMap blockMap) {
        this.interactor = interactor;
        this.interacted = interacted;
        this.blockMap = blockMap;
    }

    @Override
    public void interact(Direction dir) {
        LOG.log(Level.INFO, "Interacting: " + dir.name());

        if (canPerformMove(dir)) {
            LOG.log(Level.INFO, "Can move");
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
            LOG.log(Level.INFO, "Could not end interaction");
        }
    }

    @Override
    public void startInteraction() {
        final Direction dir = Direction.getDirection(interactor.getX(), interacted.getX() * blockMap.getBlockWidth());
        AnimationState anim = new AnimationState(Movement.getLiftMovement(dir));

        if (interacted.canMove(anim.getMovement().getDirection())
                && !blockMap.hasBlock((int) interacted.getX(), (int) interacted.getY() + 1)) {
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
package edu.chalmers.blockster.core.objects;

import static edu.chalmers.blockster.core.util.Calculations.collisionEitherCorner;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.vecmath.Vector2f;

import edu.chalmers.blockster.core.objects.interactions.BlockGrabbedInteraction;
import edu.chalmers.blockster.core.objects.interactions.BlockLiftedInteraction;
import edu.chalmers.blockster.core.objects.interactions.Interactor;
import edu.chalmers.blockster.core.objects.interactions.PlayerInteraction;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

/**
 * The model representing a player in the game Blockster
 * 
 * @author Emilia Nilsson and Eric Bjuhr
 * 
 */
public class Player extends BlocksterObject implements Interactor {

	private static final Logger LOG = Logger.getLogger(Player.class.getName());

	private boolean dancing = false;

	private final Block none;
	private Block processedBlock;
	private boolean grabbingBlock;
	private boolean liftingBlock;
	private PlayerInteraction interaction = PlayerInteraction.NONE;
	private boolean horizontalCollision;
	private boolean verticalCollision;
	private int wait = 0;
	private boolean hasMovedBlock = false;

	public Player(float startX, float startY, BlockMap blockMap) {
		super(startX, startY, blockMap, blockMap.getBlockWidth(),
				blockMap.getBlockHeight());
		defaultVelocity = new Vector2f(8 * blockMap.getBlockWidth(),
				55 * blockMap.getBlockHeight());
		none = new EmptyBlock(0,0,blockMap);
		processedBlock = none;
	}

	private boolean canClimbBlock(Block block) {
		assert block != null;
		if (block instanceof EmptyBlock) {
			return false;
		}
		return isNextToBlock(block) && !climbingCollision();
	}

	private boolean climbingCollision() {
		final int playerGridX = (int) (getX() / getScaleX());
		final int playerGridY = (int) (getY() / getScaleY());
		if (blockMap.hasBlock(playerGridX, playerGridY + 1)) {
			Block blockAbove = blockMap.getBlock(playerGridX, playerGridY + 1);
			return blockAbove != processedBlock && !blockAbove.isLiftable();
		} else {
			return false;
		}
	}

	private boolean canGrabBlock(Block block) {
		assert block != null;
		return !isBusy() && isNextToBlock(block) && !isLiftingBlock();
	}

	private boolean canLiftBlock(Block block) {
		return block != null && grabbingBlock && isNextToBlock(block)
				&& block.isLiftable();
	}

	public boolean canMove(Direction dir) {
		final BlockMap bLayer = getBlockLayer();
		final float blockWidth = bLayer.getBlockWidth();
		final int checkX = (int) (getOriginX() / getScaleX());
		final int checkY = (int) (getOriginY() / getScaleY());

		return checkX >= 1
				&& checkX < blockWidth
				&& !bLayer.hasBlock(checkX + dir.getDeltaX(), checkY + dir.getDeltaY())
				&& bLayer
						.hasBlock(checkX + dir.getDeltaX(), checkY + dir.getDeltaY() - 1);

	}

	public void climbBlock() {
		final Block block = getAdjacentBlock();
		LOG.log(Level.INFO, "Can we climb block?");
		if (!isGrabbingBlock() && canClimbBlock(block) && block.canBeClimbed()) {
			LOG.log(Level.INFO, "We can climb block!");
			Direction dir = Direction.getDirection(
					getX() / blockMap.getBlockWidth(), block.getX());
			setAnimationState(new AnimationState(Movement.getClimbMovement(dir)));
			processedBlock.removeFromGrid();
			processedBlock.setAnimationState(new AnimationState(Movement.
												getClimbMovement(dir)));
		}
	}

	public boolean collidedHorisontally() {
		return horizontalCollision;
	}

	public boolean collidedVertically() {
		return verticalCollision;
	}

	public void endInteraction() {
		interaction.endInteraction();
		LOG.log(Level.INFO, "Ending interaction");
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}

	public void grabBlock() {
		final Block block = getAdjacentBlock();
		if (block != null && canGrabBlock(block) && block.canBeGrabbed()) {
			LOG.log(Level.INFO, "Can grab block at " + block.getX() + " "
					+ block.getY());
			processedBlock = block;
			grabbingBlock = true;
			interaction = new BlockGrabbedInteraction(this, block, blockMap);
			interaction.startInteraction();
		}
	}

	public boolean hasMovedBlock() {
		return hasMovedBlock;
	}
	
	public void interact() {
		if (isInteracting()) {
			if (getAnimationState().isDone()) {
				if (!directionChanged || !isLiftingBlock()) {
					interaction.interact(getDirection());
					hasMovedBlock = true;
					directionChanged = false;
				} else {
					wait++;
					if (wait > 2) {
						directionChanged = false;
						wait = 0;
					}
				}

			}
		} else {
			setDefaultVelocity(getDirection());
		}
	}

	public boolean isBusy() {
		return isInteracting() || getAnimationState() != AnimationState.NONE;
	}

	public boolean isDancing() {
		return dancing;
	}

	public boolean isGrabbingBlock() {
		return grabbingBlock;
	}

	public boolean isInteracting() {
		return interaction != PlayerInteraction.NONE;
	}

	public boolean isLiftingBlock() {
		return liftingBlock;
	}

	public boolean isNextToBlock(Block block) {
		return block != null
				&& Math.abs(block.getX()
						- (Math.round(getX()) / blockMap.getBlockWidth())) <= 1.1f
				&& Math.abs(block.getY() - (getY() / blockMap.getBlockHeight())) <= 0.2f;
	}

	public void liftBlock() {
		LOG.log(Level.INFO, "Trying to lift block " + processedBlock);
		if (canLiftBlock(processedBlock)) {
			LOG.log(Level.INFO, "Can lift block at " + processedBlock.getX()
					+ " " + processedBlock.getY());
			// Lift process
			interaction = new BlockLiftedInteraction(this, processedBlock,
					blockMap);
			interaction.startInteraction();
			grabbingBlock = false;
		}
	}

	public void move(Vector2f distance) {
		float[] previousPosition = { getX(), getY() };

		if (Math.abs(distance.x) > 0) {
			setX(getX() + distance.x);
			if (collisionEitherCorner(this, getBlockLayer())) {
				setX(previousPosition[0]);
				horizontalCollision = true;
			} else {
				horizontalCollision = false;
				verticalCollision = false;
			}
		}

		if (Math.abs(distance.y) > 0) {
			setY(getY() + distance.y);
			if (collisionEitherCorner(this, getBlockLayer())) {
				setY(previousPosition[1]);
				if (distance.y < 0) {
					setY(((int) getY() / getBlockLayer().getBlockHeight())
							* getBlockLayer().getBlockHeight());
				}
				verticalCollision = true;
			} else {
				verticalCollision = false;
				horizontalCollision = false;
			}
		}
	}

	public void setDancing(boolean b) {
		dancing = true;
	}

	public void setGrabbing(boolean b) {
		grabbingBlock = b;
		if (b) {
			setAnimationState(getDirection() == Direction.LEFT ? AnimationState.GRAB_LEFT
					: AnimationState.GRAB_RIGHT);
		} else {
			setAnimationState(AnimationState.NONE);
			interaction = PlayerInteraction.NONE;
			hasMovedBlock = false;
			processedBlock = none;
		}
	}

	public void setLifting(boolean lift) {
		liftingBlock = lift;
		if (lift) {
			setAnimationState(getDirection() == Direction.LEFT ? AnimationState.LIFT_LEFT
					: AnimationState.LIFT_RIGHT);
		} else {
			setAnimationState(getDirection() == Direction.LEFT ? AnimationState.PLACE_LEFT
					: AnimationState.PLACE_RIGHT);
			interaction = PlayerInteraction.NONE;
			hasMovedBlock = false;
			processedBlock = none;
		}
	}

	public void updatePosition(float deltaTime) {
		final AnimationState anim = getAnimationState();
		if (anim == AnimationState.NONE) {
			final Vector2f velocity = getVelocity();
			final Vector2f distance = new Vector2f(velocity.x * deltaTime,
					velocity.y * deltaTime);
			if (Math.abs(Math.hypot(distance.x, distance.y)) > 0) {
				move(distance);
			}

		} else {
			LOG.log(Level.INFO, "Moving " + this);
			anim.updatePosition(deltaTime);
		}
	}
}
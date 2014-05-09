package edu.chalmers.blockster.core.objects;

import javax.vecmath.*;

import edu.chalmers.blockster.core.objects.interactions.BlockGrabbedInteraction;
import edu.chalmers.blockster.core.objects.interactions.BlockLiftedInteraction;
import edu.chalmers.blockster.core.objects.interactions.Interactor;
import edu.chalmers.blockster.core.objects.interactions.PlayerInteraction;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import static edu.chalmers.blockster.core.util.Calculations.*;

/**
 * The model representing a player in the game Blockster
 * @author Emilia Nilsson and Eric Bjuhr
 *
 */
public class Player extends BlocksterObject implements Interactor {
	

	private boolean dancing = false;
	
	private Block processedBlock;
	private boolean grabbingBlock;
	private boolean liftingBlock;
	private PlayerInteraction interaction = PlayerInteraction.NONE;
	private boolean horizontalCollision;
	private boolean verticalCollision;
	
	public Player(float startX, float startY, BlockMap blockLayer) {
		super(startX, startY, blockLayer, blockLayer.getBlockWidth(), blockLayer.getBlockHeight());
		defaultVelocity = new Vector2f(700, 55 * blockMap.getBlockHeight());
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}
	
	public void grabBlock() {
		final Block block = getAdjacentBlock();
		if (canGrabBlock(block)) {
			System.out.println("Can grab block at " + block.getX() + " " + block.getY());
			processedBlock = block;
			grabbingBlock = true;
			interaction = new BlockGrabbedInteraction(this, block, blockMap);
			interaction.startInteraction();
		}
	}
	
	private boolean canGrabBlock(Block block) {
		return block != null && !isLiftingBlock() && !isInteracting() && isNextToBlock(block) &&
				(block.isMovable() || block.isLiftable());
	}
	
	public void setGrabbing(boolean b) {
		if(b) {
			setAnimationState(getDirection() == Direction.LEFT ? AnimationState.GRAB_LEFT : AnimationState.GRAB_RIGHT);
		} else {
			setAnimationState(AnimationState.NONE);
			processedBlock = null;
		}
	}
	
	public boolean isGrabbingBlock() {
		return grabbingBlock;
	}
	
	public void liftBlock() {
		Block processedBlock = getProcessedBlock();
		System.out.println("Trying to lift block " + processedBlock);
		if (canLiftBlock(processedBlock)) {
			System.out.println("Can lift block at " + processedBlock.getX() + " " + processedBlock.getY());
			//Lift process			
			interaction = new BlockLiftedInteraction(this, processedBlock, blockMap);
			interaction.startInteraction();
			grabbingBlock = false;
		}
	}
	
	private boolean canLiftBlock(Block block) {
		return block != null && grabbingBlock &&
				isNextToBlock(block) && block.isLiftable();
	}
	
	public void setLifting(boolean lift) {
		liftingBlock = lift;
		
	}
	
	public boolean isLiftingBlock() {
		return liftingBlock;
	}
	
	public boolean isNextToBlock(Block block) {
		return block != null &&
		Math.abs(block.getX() - ((int) getX() / blockMap.getBlockWidth())) <= 1.1f &&
		Math.abs(block.getY() - ((int) getY() / blockMap.getBlockHeight())) <= 1.1f;
	}
	
	public void interact() {
		if (isInteracting()) {
			if (getAnimationState().isDone()) {
				interaction.interact(getDirection());
			}
		} else {
			setDefaultVelocity(getDirection());
		}
	}
	
	public boolean isInteracting() {
		return (interaction != PlayerInteraction.NONE
			&&  getAnimationState() == AnimationState.NONE);
	}
	
	public void endInteraction() {
		interaction.endInteraction();
		interaction = PlayerInteraction.NONE;
		System.out.println("ENDING INTERACTION");
	}
	
	public void move(Vector2f distance) {
		float[] previousPosition = { getX(), getY() };
		
		if (Math.abs(distance.x) > 0 ) {
			setX(getX() + distance.x);
			if (collisionEitherCorner(this, getBlockLayer())) {
				setX(previousPosition[0]);
				horizontalCollision = true;
			} else {
				horizontalCollision = false;
				verticalCollision = false;
			}
		}

		if (Math.abs(distance.y) > 0 ) {
			setY(getY() + distance.y);
			if (collisionEitherCorner(this, getBlockLayer())) {
				setY(previousPosition[1]);
				if (distance.y < 0) {
					setY(((int)getY()/getBlockLayer().getBlockHeight())
								* getBlockLayer().getBlockHeight());
				}
				verticalCollision = true;
			} else {
				verticalCollision = false;
				horizontalCollision = false;
			}
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
			System.out.println("Moving "+this);
			anim.updatePosition(deltaTime);
		}
	}
	
	/**
	 * This method is used when pulling a block and checks if the player
	 * can continue to pull it or if there is something blocking the way
	 * (this is usually taken care of by the collision avoidance, but when
	 * moving a block then this isn't available).
	 * 
	 * @param movement
	 * @return true if nothing is blocking the way behind player, else false.
	 */
	public boolean canMove(Direction dir) {
		final BlockMap bLayer = getBlockLayer();
		final float blockWidth = bLayer.getBlockWidth();
		final int checkX = (int) (getX() / blockWidth);
		final int checkY = (int) (getY() / blockWidth);
			
		return checkX >= 1 && checkX < blockWidth && !bLayer.
				hasBlock(checkX + dir.deltaX, checkY);
			
	}
	
	public boolean collidedHorisontally() {
		return horizontalCollision;
	}
	
	public boolean collidedVertically() {
		return verticalCollision;
	}

	public void setDancing(boolean b) {
		dancing = true;
	}
	
	public boolean isDancing() {
		return dancing;
	}
}

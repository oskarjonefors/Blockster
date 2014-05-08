package edu.chalmers.Blockster.core.objects;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.objects.interactions.BlockGrabbedInteraction;
import edu.chalmers.Blockster.core.objects.interactions.BlockLiftedInteraction;
import edu.chalmers.Blockster.core.objects.interactions.PlayerInteraction;
import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.objects.movement.Direction;
import edu.chalmers.Blockster.core.util.PhysicalObject;
import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * The model representing a player in the game Blockster
 * @author Emilia Nilsson and Eric Bjuhr
 *
 */
public class Player extends BlocksterObject implements PhysicalObject{
	

	public boolean DANCE = false;
	
	private Block processedBlock;
	private boolean isGrabbingBlock;
	private boolean isLiftingBlock;
	private PlayerInteraction interaction = PlayerInteraction.NONE;
	private boolean collidedHorisontally = false;
	private boolean collidedVertically = false;
	
	public Player(float startX, float startY, BlockMap blockLayer) {
		super(startX, startY, blockLayer, blockLayer.getBlockWidth(), blockLayer.getBlockHeight());
		defaultVelocity = new Vector2f(700, 55 * blockMap.getBlockHeight());
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}
	
	public void grabBlock() {
		Block block = getAdjacentBlock();
		if (canGrabBlock(block)) {
			System.out.println("Can grab block at " + block.getX() + " " + block.getY());
			processedBlock = block;
			isGrabbingBlock = true;
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
			/*Direction dir = Calculations.getDirection(getX(), getProcessedBlock()

			.getX() * blockMap.getBlockWidth());*/
			setAnimationState(getDirection() == Direction.LEFT ? AnimationState.GRAB_LEFT : AnimationState.GRAB_RIGHT);
		} else {
			setAnimationState(AnimationState.NONE);
			processedBlock = null;
		}
	}
	
	public boolean isGrabbingBlock() {
		return isGrabbingBlock;
	}
	
	public void liftBlock() {
		if (canLiftBlock(getProcessedBlock())) {
			System.out.println("Can lift block at " + getProcessedBlock().getX() + " " + getProcessedBlock().getY());
			//Lift process			
			interaction = new BlockLiftedInteraction(this, getProcessedBlock(), blockMap);
			interaction.startInteraction();
			isGrabbingBlock = false;
		}
	}
	
	private boolean canLiftBlock(Block block) {
		return block != null && !isInteracting() &&
				isNextToBlock(block) && block.isLiftable();
	}
	
	public void setLifting(boolean lift) {
		isLiftingBlock = lift;
		
	}
	
	public boolean isLiftingBlock() {
		return isLiftingBlock;
	}
	
	public boolean isNextToBlock(Block block) {
		if(block != null) {
		System.out.println("X: " + Math.abs(block.getX() - (getX() / blockMap.getBlockWidth())) + " Y: " + 
				Math.abs(block.getY() - (getY() / blockMap.getBlockHeight())));
		}
		return block != null &&
		Math.abs(block.getX() - (getX() / blockMap.getBlockWidth())) < 1f &&
		Math.abs(block.getY() - (getY() / blockMap.getBlockHeight())) < 1f;
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
				collidedHorisontally = true;
			} else {
				collidedHorisontally = false;
				collidedVertically = false;
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
				collidedVertically = true;
			} else {
				collidedVertically = false;
				collidedHorisontally = false;
			}
		}
	}

	public void updatePosition(float deltaTime) {
		if (getAnimationState() != AnimationState.NONE) {
			System.out.println("Moving "+this);
			getAnimationState().updatePosition(deltaTime);
		} else {
			Vector2f distance = new Vector2f();
			distance.x = getVelocity().x * deltaTime;
			distance.y = getVelocity().y * deltaTime;
			if (Math.abs(Math.hypot(distance.x, distance.y)) > 0)
				move(distance);
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
		BlockMap bLayer = getBlockLayer();
		int checkX = (int) (getX() / bLayer.getBlockWidth());
		int checkY = (int) (getY() / bLayer.getBlockWidth());
			
		return checkX >= 1 && checkX < bLayer.getWidth() && !bLayer.
				hasBlock(checkX + dir.deltaX, checkY);
			
	}
	
	public boolean collidedHorisontally() {
		return collidedHorisontally;
	}
	
	public boolean collidedVertically() {
		return collidedVertically;
	}
}

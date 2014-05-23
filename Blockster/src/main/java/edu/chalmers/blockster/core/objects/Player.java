package edu.chalmers.blockster.core.objects;

import static edu.chalmers.blockster.core.util.Calculations.collisionEitherCorner;

import java.util.ArrayList;
import java.util.List;
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

	public enum World {
		DAY, NIGHT;
	}
	
	private static final Logger LOG = Logger.getLogger(Player.class.getName());
	private final Block none;
	private Block processedBlock;
	private boolean grabbingBlock;
	private boolean liftingBlock;
	private PlayerInteraction interaction = PlayerInteraction.NONE;
	private boolean horizontalCollision;
	private boolean verticalCollision;
	private World world;
	private int wait = 0;
	private boolean hasMovedBlock = false;
	private List<GameEventListener> listeners;
	private boolean switchFromMe = false;

	private boolean isMoving = false;
	private boolean activeAniRight;
	private boolean activeAniLeft;

	public Player(float startX, float startY, BlockMap blockMap, World world) {
		super(startX, startY, blockMap, blockMap.getBlockWidth(),
				blockMap.getBlockHeight());
		defaultVelocity = new Vector2f(4 * blockMap.getBlockWidth(),
				55 * blockMap.getBlockHeight());
		none = EmptyBlock.getInstance();
		processedBlock = none;
		listeners = new ArrayList<GameEventListener>();
		this.world = world;
	}
	
	private boolean canClimbBlock(Block block) {
		if (block instanceof EmptyBlock || block == null) {
			return false;
		}
		
		return isNextToBlock(block) && !climbingCollision();
	}

	private boolean climbingCollision() {
		final int playerGridX = (int) (getX() / getScaleX());
		final int playerGridY = (int) (getY() / getScaleY());
		if (blockMap.hasBlock(playerGridX, playerGridY + 1)) {
			Block blockAbove = blockMap.getBlock(playerGridX, playerGridY + 1);
			return blockAbove != processedBlock;
		} else {
			return false;
		}
	}

	private boolean canGrabBlock(Block block) {
		return !isBusy() && isNextToBlock(block) && !isLiftingBlock();
	}

	private boolean canLiftBlock(Block block) {
		final int x = (int)block.getX();
		final int y = (int)block.getY();
		return grabbingBlock && isNextToBlock(block)
				&& block.isLiftable() && !blockMap.hasBlock(x, y+1);
	}

	public boolean canMove(Direction dir) {
		final BlockMap bLayer = getBlockLayer();
		final float mapWidth = bLayer.getWidth();
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();
		final boolean collisionForward = bLayer.hasBlock(checkX , checkY);

		return checkX >= 0 && checkX <= mapWidth - 1 && !collisionForward;

	}
	
	public boolean collisionBeneathNext(Direction dir) {
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();
	
		return checkY >= 0 && getBlockLayer().hasBlock(checkX, checkY - 1);
	}		


	public void climbBlock() {
		final Block block = getAdjacentBlock(); 
		LOG.log(Level.FINE, "Can we climb block?");
		if (!isGrabbingBlock() && canClimbBlock(block) && block.canBeClimbed()) {
			LOG.log(Level.FINE, "We can climb block!");
			Direction dir = Direction.getDirection(
					getX() / blockMap.getBlockWidth(), block.getX());
			if (isLiftingBlock()) {
				Movement state = getDirection() == Direction.LEFT ?
						Movement.LIFTING_CLIMB_LEFT : Movement.LIFTING_CLIMB_RIGHT;
		
				setAnimationState(new AnimationState(state));
				
				processedBlock.setAnimationState(new AnimationState(state));
			} else {
			setAnimationState(new AnimationState(Movement.getClimbMovement(dir)));
			processedBlock.removeFromGrid();
			processedBlock.setAnimationState(new AnimationState(Movement.
												getClimbMovement(dir)));
			}
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

	public void startInteraction() {
		final Block block = getAdjacentBlock();
		if (block.isTeleporter()){
			enterTeleport();
		} else if (canGrabBlock(block) && block.canBeGrabbed()) {
			LOG.log(Level.INFO, "Can grab block at " + block.getX() + " "
					+ block.getY());
			processedBlock = block;
			grabbingBlock = true;
			setInteraction(new BlockGrabbedInteraction(this, block, blockMap));
			interaction.startInteraction();
		}
	}

	public World getWorld() {
		return world;
	}
	
	public boolean hasMovedBlock() {
		return hasMovedBlock;
	}

	public void interact() {
		if (isInteracting() && getAnimationState().isDone()) {
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
		} else {
			setDefaultVelocity(getDirection());
		}
	}

	public boolean isBusy() {
		return isInteracting() || getAnimationState() != AnimationState.NONE;
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
		if (getDirection().getDeltaX() < 0) { 
			return block != null
					&& Math.abs(block.getX() + 1
							- (Math.round(getX()) / blockMap.getBlockWidth())) <= 0.1f
					&& Math.abs(block.getY() - (getY() / blockMap.getBlockHeight())) <= 0.2f;
		} else {
			return block != null
					&& Math.abs(block.getX()
							- (getX() + getWidth()) / getScaleX()) <= 0.25f
					&& Math.abs(block.getY() - (getY() / blockMap.getBlockHeight())) <= 0.2f;
		}
	}

	public void liftBlock() {
		LOG.log(Level.INFO, "Trying to lift block " + processedBlock);
		if (canLiftBlock(processedBlock)) {
			LOG.log(Level.INFO, "Can lift block at " + processedBlock.getX()
					+ " " + processedBlock.getY());
			setInteraction(new BlockLiftedInteraction(this, processedBlock,
					blockMap));
			interaction.startInteraction();
		} else {
			setInteraction(PlayerInteraction.NONE);
		}
		grabbingBlock = false;
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

	public void setGrabbing(boolean b) {
		grabbingBlock = b;
		if (b) {
			Movement move = getDirection() == Direction.LEFT ? Movement.GRAB_LEFT : Movement.GRAB_RIGHT;
			setAnimationState(new AnimationState(move));
		} else {
			setAnimationState(AnimationState.NONE);
			setInteraction(PlayerInteraction.NONE);
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
			setInteraction(PlayerInteraction.NONE);
			hasMovedBlock = false;
			processedBlock = none;
		}
	}

	public void updatePosition(float deltaTime) {
		isMoving = !(getVelocity().x == 0);
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

	public void switchingToMe(boolean bool) {
		switchFromMe = bool;
	}
	
	public boolean isSwitchingToMe() {
		return switchFromMe;
	}
	
	public void addGameEventListener(GameEventListener e){
		listeners.add(e);
	}
	
	public void enterTeleport(){
		if (getDirection() == Direction.LEFT) {
			setAnimationState(new AnimationState(Movement.MOVE_LEFT));
		} else {
			setAnimationState(new AnimationState(Movement.MOVE_RIGHT));
		}
		
		for (GameEventListener listener : listeners) {
			listener.playerReachedGoal();
		}	
	}
	
	public boolean isMoving() {
		return isMoving;
	}
	
	public boolean getActiveAniRight() {
		return activeAniRight;
	}

	public boolean getActiveAniLeft() {
		return activeAniLeft;
	}

	public void setActiveAniLeft(boolean b) {
		activeAniLeft = b;
		
	}

	public void setActiveAniRight(boolean b) {
		activeAniRight = b;
		
	}
	
	private void setInteraction(PlayerInteraction interaction) {
		this.interaction = interaction;
		processedBlock.setInteraction(interaction);
	}
}


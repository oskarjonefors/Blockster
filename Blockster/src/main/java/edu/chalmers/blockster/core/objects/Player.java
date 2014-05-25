package edu.chalmers.blockster.core.objects;

import static edu.chalmers.blockster.core.util.Calculations.collisionEitherCorner;

import java.awt.geom.Point2D;
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
public class Player extends AbstractBlocksterObject implements Interactor {
	
	private static final Logger LOG = Logger.getLogger(Player.class.getName());
	private final Block none;
	private Block processedBlock;
	private boolean grabbingBlock;
	private boolean liftingBlock;
	private PlayerInteraction interaction = PlayerInteraction.NONE;
	private boolean horizontalCollision;
	private boolean verticalCollision;
	private final World world;
	private int wait;
	private boolean movedBlock;
	private final List<GameEventListener> listeners;
	private boolean switchFromMe;
	private boolean moving;

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
		if (block instanceof EmptyBlock) {
			return false;
		}
		
		return isNextToBlock(block) && !climbingCollision();
	}

	private boolean climbingCollision() {
		final int playerGridX = (int) (getX() / getScaleX());
		final int playerGridY = (int) (getY() / getScaleY());
		if (blockMap.hasBlock(playerGridX, playerGridY + 1)) {
			final Block blockAbove = blockMap.getBlock(playerGridX, playerGridY + 1);
			return !blockAbove.equals(processedBlock);
		} else {
			return false;
		}
	}

	private boolean canGrabBlock(Block block) {
		return !isBusy() && isNextToBlock(block);
	}

	private boolean canLiftBlock(Block block) {
		return grabbingBlock && isNextToBlock(block);
	}

	public boolean canMove(Direction dir) {
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();

		return !getBlockMap().collisionAt(checkX , checkY);
	}
	
	public boolean collisionBeneathNext(Direction dir) {
		final int checkX = (int) (getOriginX() / getScaleX()) + dir.getDeltaX();
		final int checkY = (int) (getOriginY() / getScaleY()) + dir.getDeltaY();
	
		return checkY >= 0 && getBlockMap().hasBlock(checkX, checkY - 1);
	}		


	public void climbBlock() {
		final Block block = getAdjacentBlock(); 
		LOG.log(Level.FINE, "Can we climb block?");
		if (!isGrabbingBlock() && canClimbBlock(block) && block.canBeClimbed()) {
			LOG.log(Level.FINE, "We can climb block!");
			final Direction dir = Direction.getDirection(
					getX() / blockMap.getBlockWidth(), block.getX());
			if (isLiftingBlock()) {
				final Movement state = getDirection() == Direction.LEFT ?
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
		LOG.log(Level.FINE, "Ending interaction");
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}

	public void startInteraction() {
		final Block block = getAdjacentBlock();
		if (block.isTeleporter()){
			enterTeleport();
		} else if (canGrabBlock(block) && block.canBeGrabbed()) {
			LOG.log(Level.FINE, "Can grab block at " + block.getX() + " "
					+ block.getY());
			processedBlock = block;
			grabbingBlock = true;
			setInteraction(new BlockGrabbedInteraction(this, block, blockMap));
			interaction.startInteraction();
		}
	}
	
	public Block getAdjacentBlock() {
		final Direction dir = getDirection();
		if (dir == Direction.LEFT || dir == Direction.RIGHT) {
			return blockMap.getBlock((int) (getX() / getScaleX()) + dir.getDeltaX(),
					(int) ((2 * getY() + getHeight()) / 2 / getScaleY()));
		} else {
			return EmptyBlock.getInstance();
		}
	}

	public World getWorld() {
		return world;
	}
	
	public boolean hasMovedBlock() {
		return movedBlock;
	}

	public void interact() {
		if (isInteracting() && getAnimationState().isDone() 
				&& processedBlock.getAnimationState().isDone()) {
			if (directionChanged && isLiftingBlock()) {
				wait++;
				if (wait > 2) {
					directionChanged = false;
					wait = 0;
				}
			} else {
				interaction.interact(getDirection());
				movedBlock = true;
				directionChanged = false;
			}
		} else if (!isInteracting()) {
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

	public boolean isNextToBlock(AbstractBlocksterObject block) {
		if (block == null) {
			return false;
		}
		
		boolean isNextToBlock = isLiftingBlock()
				&& Math.abs((int) block.getX()
						- (int) (getX() / blockMap.getBlockWidth())) <= 1
				&& Math.abs(block.getY() - getY() / blockMap.getBlockHeight()) <= 0.2f;
		isNextToBlock |= getDirection().getDeltaX() < 0
				&& Math.abs(block.getX() + 1
						- Math.round(getX()) / blockMap.getBlockWidth()) <= 0.1f
				&& Math.abs(block.getY() - getY() / blockMap.getBlockHeight()) <= 0.2f;
		isNextToBlock |= Math.abs(block.getX() - (getX() + getWidth())
				/ getScaleX()) <= 0.25f && Math.abs(block.getY() - getY() /
						blockMap.getBlockHeight()) <= 0.2f;
		
		return isNextToBlock;
	}

	public void liftBlock() {
		LOG.log(Level.FINE, "Trying to lift block " + processedBlock);
		if (canLiftBlock(processedBlock) && processedBlock.canBeLifted()) {
			LOG.log(Level.FINE, "Can lift block at " + processedBlock.getX()
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
		final Point2D.Float previousPosition = new Point2D.Float(getX(), getY());

		if (Math.abs(distance.x) > 0) {
			setX(getX() + distance.x);
			if (collisionEitherCorner(this, getBlockMap())) {
				setX(previousPosition.x);
				horizontalCollision = true;
			} else {
				horizontalCollision = false;
				verticalCollision = false;
			}
		}

		if (Math.abs(distance.y) > 0) {
			setY(getY() + distance.y);
			if (collisionEitherCorner(this, getBlockMap())) {
				setY(previousPosition.y);
				if (distance.y < 0) {
					setY(((int) getY() / getBlockMap().getBlockHeight())
							* getBlockMap().getBlockHeight());
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
			final Movement move = getDirection() == Direction.LEFT ? Movement.GRAB_LEFT : Movement.GRAB_RIGHT;
			setAnimationState(new AnimationState(move));
		} else {
			setAnimationState(AnimationState.NONE);
			setInteraction(PlayerInteraction.NONE);
			movedBlock = false;
			processedBlock = none;
		}
	}

	public void setLifting(boolean lift) {
		liftingBlock = lift;
		if (lift) {
			setAnimationState(getDirection() == Direction.LEFT ? new AnimationState(Movement.PLAYER_LIFT_LEFT)
					: new AnimationState(Movement.PLAYER_LIFT_RIGHT));
		} else {
			setAnimationState(getDirection() == Direction.LEFT ? new AnimationState(Movement.PLAYER_PLACE_LEFT)
					: new AnimationState(Movement.PLAYER_PLACE_RIGHT));
			setInteraction(PlayerInteraction.NONE);
			movedBlock = false;
			processedBlock = none;
		}
	}
	
	public boolean isLiftingOrPlacing() {
		final Movement move = getAnimationState().getMovement();
		return move == Movement.PLAYER_PLACE_LEFT || move == Movement.PLAYER_PLACE_RIGHT ||
				move == Movement.PLAYER_LIFT_LEFT || move == Movement.PLAYER_LIFT_RIGHT;
	}

	public void updatePosition(float deltaTime) {
		moving = getVelocity().x != 0;
		final AnimationState anim = getAnimationState();
		if (anim == AnimationState.NONE) {
			final Vector2f velocity = getVelocity();
			final Vector2f distance = new Vector2f(velocity.x * deltaTime,
					velocity.y * deltaTime);
			if (Math.abs(Math.hypot(distance.x, distance.y)) > 0) {
				move(distance);
			}

		} else {
			LOG.log(Level.FINE, "Moving " + this);
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
		
		for (final GameEventListener listener : listeners) {
			listener.playerReachedGoal();
		}	
	}
	
	public boolean isMoving() {
		return moving;
	}
	
	private void setInteraction(PlayerInteraction interaction) {
		this.interaction = interaction;
		processedBlock.setInteraction(interaction);
	}
}


package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Calculations.*;
import static edu.chalmers.Blockster.core.util.Direction.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Vector2f;

import edu.chalmers.Blockster.core.util.Calculations;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors, Eric Bjuhr, Emilia Nilsson
 * 
 */
public class Model implements Comparable<Model> {


	private BlockMap map;
	private Player activePlayer;
	private List<Player> players;
	private Set<Block> activeBlocks;
	private Direction lastDirection;
	private HashMap<Player, Block> liftedBlocks;

	private boolean isGrabbingBlockAnimation = false; //for animations
	private boolean isMovingBlockAnimation = false;
	private boolean isLiftingBlockAnimation = false;

	private boolean isGrabbingBlock = false; 
	private boolean isLiftingBlock = false;
	public boolean isSwitchChar;
	
	private Factory factory;
	private final String name;
	
	private Vector2f cameraMoveVector;
	

	//TODO Change this
	private final static String PLAYER_IMAGE_ADDRESS = "Player/still2.png";



	public Model(Factory factory, String name) {
		this.factory = factory;
		this.name = name;
		init();
	}
	
	public void init() {
		this.map = factory.createMap();
		players = new ArrayList<Player>();
		activeBlocks = Collections.synchronizedSet(new HashSet<Block>());
		liftedBlocks = new HashMap<Player, Block>();
		setStartPositions();

		activePlayer = players.get(0);
	}
	
	public boolean canGrabBlock(Block block) {
		return block != null && getProcessedBlock() == null 
				&& !isGrabbingBlockAnimation && !isLiftingBlockAnimation 
				&& !isMovingBlockAnimation && (block.isMovable() || block.isLiftable());
	}
	
	public boolean canLiftBlock(Block block) {
		//TODO: Add additional tests
		return block != null && block.isLiftable();
				//&& isGrabbingBlock && !isGrabbingBlockAnimation &&
				//!isLiftingBlockAnimation && !isMovingBlockAnimation;
	}
	
	/**
	 * This method is used when pulling a block and checks if the player
	 * can continue to pull it or if there is something blocking the way
	 * (this is usually taken care of by the collision avoidance, but when
	 * moving a block then this isn't available).
	 * 
	 * @param dir
	 * @return true if nothings blocking the way behind player, else false.
	 */
	public boolean canMovePlayer(Direction dir, Movement movement) {
		
		/* If the player isn't grabbing a block, then
		 * there will be no need to check if you can
		 * move player and therefore return false */
		if (getProcessedBlock() == null) {
			return false;
		}
		
		/* Save the players position so we can check
		 * the surroundings later.
		 * Divide with 128(?) to get the right scale */
		int checkX = (int)activePlayer.getX() / 128;
		int checkY = (int)activePlayer.getY() / 128;
		
		/* Variable to track and set if it's possible to move */
		boolean canMove = false;
		
		BlockLayer blockLayer = map.getBlockLayer();
		
		/* Check so that we are inside the bounds */
		if (checkX >= 1 && checkX < blockLayer.getWidth()) {
			
			/* Check so that we actually are pulling it left or right
			 * and if so, check if there is a block in the way behind
			 * the player */
			if (movement == Movement.PULL_LEFT) {
				canMove = !(blockLayer.hasBlock(checkX - 1, checkY));
			}
			else if (movement == Movement.PULL_RIGHT) {
				canMove = !(blockLayer.hasBlock(checkX + 1, checkY));
			}
		} else {
			return false;
		}
		return canMove;
	}
	
	/**
	 * Checks to see if the player can move the block that's currently grabbed
	 * in the given direction.
	 * 
	 * @param dir	The direction to push the block
	 * @return		True if block can be moved, false otherwise.
	 */
	public boolean canMoveBlock(Direction dir) {
		
		/* If the player is not grabbing a block, return false */
		if (getProcessedBlock() == null) {
			return false;
			
		/* If the grabbed block is not movable, return false */
		} else if (!getProcessedBlock().isMovable()) {
			return false;
		}
		
		/* Save the original coordinates for brevity (korthet) */
		int origX = (int)getProcessedBlock().getX();
		int origY = (int)getProcessedBlock().getY();
		
		/* These coordinates will be changed when checking adjacent blocks */ 
		int checkX = origX;
		int checkY = origY;
		
		/* Will be set to true when the check is done and the loop can end */
		boolean isDone = false;
		
		/* Result of the check when the loop is finished*/
		boolean canMove = false;
		
		/* The layer that the blocks are in */
		BlockLayer blockLayer = map.getBlockLayer();
		
		/* Keep checking while not done, and make sure not to step out of bounds */
		while (!isDone && checkX >= 0 && checkX < blockLayer.getWidth()) {

			/* Check if there is a block above the one at (checkX, checkY).
			 * Blocks can only be moved if there are no other blocks
			 * on top of them. First statement avoids going out of bounds. */
			if(checkY < blockLayer.getHeight() && blockLayer.hasBlock(checkX, checkY + 1)) {
				canMove = false;
				isDone = true;
			
			/* Check if there is an empty space (no block) here. If so, the
			 * previously checked blocks can be pushed/pulled to
			 * fill up the empty space */
			} else if(!blockLayer.hasBlock(checkX, checkY)) {
				canMove = true;
				isDone = true;
				
			/* If the checked block is movable, we can check the next block
			 * in the given direction in the next iteration of the loop.
			 */
			} else if (blockLayer.getBlock(checkX, checkY).isMovable()) {
				checkX = checkX + dir.deltaX;
				
			/* If the block is not movable, we cannot move neither this block
			 * nor the previously checked ones.
			 */
			} else {
				canMove = false;
				isDone = true;
			}
		}
		return canMove;
	}

	@Override
	public int compareTo(Model model) {
		return name.compareTo(model.getName());
	}

	/**
	 * Return the blocks that are currently out of the grid and moving.
	 * @return A list of blocks that are out of the grid. Empty if there are none.
	 */
	public Set<Block> getActiveBlocks() {
		return activeBlocks;
	}
	
	/**
	 * Get the currently controller Player.
	 * @return A Player
	 */
	public Player getActivePlayer() {
		return activePlayer;
	}
	
	public Block getAdjacentBlock(Direction dir) {
		return Calculations.getAdjacentBlock(dir, activePlayer, map.getBlockLayer());
	}
	
	/**
	 * Get the blocks that are currently being lifted by a player.
	 * @return A set of blocks that are being lifted. Empty if there are none.
	 */
	public Map<Player, Block> getLiftedBlocks() {
		return liftedBlocks;
	}
	
	public BlockMap getMap() {
		return map;
	}

	public String getName() {
		return name;
	}

	public List<Player> getPlayers() {
		return players;
	}

	private float[][] getPlayerStartingPositions(BlockMap map) {
		return new float[][] {{700, 1000}, {2500, 1000}};
	}

	public Block getProcessedBlock() {
		return liftedBlocks.get(activePlayer);
	}

	public void grabBlock(Block block) {

		if (canGrabBlock(block)) {

			liftedBlocks.put(activePlayer,block);


			isGrabbingBlockAnimation = true;
			isGrabbingBlock = true;

			//TODO: Start grab animation
		}
	}

	public boolean isGrabbingBlock() {
		return isGrabbingBlock;
	}

	public boolean isLiftingBlock() {
		return isLiftingBlock;
	}

	public void liftBlock() {
		if (canLiftBlock(getProcessedBlock())) {
			//If we are not already lifting a block, do so.
			isLiftingBlockAnimation = true;
			isLiftingBlock = true;
			isGrabbingBlock = false;
			BlockLayer blockLayer = map.getBlockLayer();
			float blockWidth = blockLayer.getBlockWidth();
			
			//Lift process
			float relativePositionSignum = getProcessedBlock().getX()
					- activePlayer.getX() / blockWidth;

			AnimationState anim = relativePositionSignum > 0 ? 
						new AnimationState(Movement.LIFT_LEFT) : 
						new AnimationState(Movement.LIFT_RIGHT);

						
			getProcessedBlock().setAnimationState(anim);
			activeBlocks.add(getProcessedBlock());
			liftedBlocks.put(activePlayer, getProcessedBlock());
		}
	}

	public void setActivePlayerDefaultVelocity(Direction dir) {
		//Wrapper method
		setPlayerDefaultVelocity(dir, activePlayer);
	}

	/**
	 * Attempt to move the currently grabbed block in the given direction.
	 * @param dir	Direction to move the block in.
	 * @return		True if the move was successful, otherwise false.
	 */
	public boolean attemptMoveBlock(Direction dir) {
		
			/* If there is no move currently being grabbed, return false */
			if (getProcessedBlock() == null) {
				return false;
			}
			
			if (canMoveBlock(dir)) {

				AnimationState anim = AnimationState.NONE;

				isMovingBlockAnimation = true;

				/* Get a reference to the BlockLayer for brevity. */
				BlockLayer blockLayer = map.getBlockLayer();

				/* Create a list to put the block to be moved in. */
				List<Block> movingBlocks = new ArrayList<Block>();

				/* End condition for block adding loop */
				boolean isDone = false;

				/* Origin of add process */
				int origX = (int)getProcessedBlock().getX();
				int origY = (int)getProcessedBlock().getY();

				/* We've already established that the move is okay,
				so we don't need to change the Y coordinate. */
				int checkX = (origX);

				/* Loop to add all the blocks to be moved to the list. */
				while (!isDone) {
					if (blockLayer.hasBlock(checkX, origY)) {
						movingBlocks.add(blockLayer.getBlock(checkX, origY));
						checkX += dir.deltaX;
					} else {
						isDone = true;
					}
				}

				if(isLiftingBlock) {

					anim = new AnimationState(Movement.getPullMovement(dir));

				} else {
					float blockWidth = blockLayer.getBlockWidth();
					float relativePositionSignum = getProcessedBlock().getX()
							- activePlayer.getX() / blockWidth;

					anim = new AnimationState(Movement.getPushPullMovement(dir, relativePositionSignum));
				}
				
				if (!canMovePlayer(dir, anim.getMovement())) {
					return false;
				}
				
				/* Add the blocks to be moved to the list */
				for (Block block : movingBlocks) {
					activeBlocks.add(block);
					block.setAnimationState(anim);
				}
				lastDirection = dir;
				activePlayer.setAnimationState(anim);
				return true;
			} else {
				return false;
			}
	}

	private void setPlayerDefaultVelocity(Direction dir, Player player) {
		lastDirection = dir;
		player.setDefaultVelocity(dir);
	}

	
	/**
	 * Start controlling the next player.
	 */
	public void nextPlayer() {
		int pIndex = getPlayers().indexOf(activePlayer);
		int nbrPlayers = getPlayers().size();
		int nPlayer = (pIndex + 1) % nbrPlayers;
		activePlayer = getPlayers().get(nPlayer);
		isSwitchChar = true;
	}


	public void resetStartPositions() {
		float[][] startPositions = getPlayerStartingPositions(map);
		for (int i = 0; i < startPositions.length; i++) {
			players.get(i).setX(startPositions[i][0]);
			players.get(i).setY(startPositions[i][1]);
			players.get(i).setVelocityX(0);
			players.get(i).setVelocityY(0);
			players.get(i).resetGravity();
		}
	}

	private void setStartPositions() {
		for (float[] startPosition : getPlayerStartingPositions(map)) {
			Player player = factory.createPlayer(startPosition[0], 
					startPosition[1], map.getBlockLayer());
			players.add(player);
		}
	}

	public void stopProcessingBlock() {
		System.out.println("Stop processing block");
		if (isLiftingBlock) {
			System.out.println("Stop lifting block");
			BlockLayer blockLayer = map.getBlockLayer();
			float blockWidth = blockLayer.getBlockWidth();
			float blockHeight = blockLayer.getBlockHeight();
			boolean hasBlock = blockLayer.hasBlock(
					(int) (activePlayer.getX() / blockWidth) - lastDirection.deltaX,
					(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / blockHeight));
			
			if (!hasBlock && getProcessedBlock() != null) {
				System.out.println("Didn't have block");

				AnimationState anim = new AnimationState(Movement.getPlaceMovement(lastDirection));

				getProcessedBlock().setAnimationState(anim);
				activeBlocks.add(getProcessedBlock());
				liftedBlocks.remove(activePlayer);
			}
		}
		
		liftedBlocks.remove(activePlayer);
		isGrabbingBlock = isLiftingBlock = isGrabbingBlockAnimation 
				= isLiftingBlockAnimation = isMovingBlockAnimation = false;
	}

	public void update(float deltaTime) {

		for (Player player : players) {
			boolean notcollision;
			
			
			if (player.getAnimationState().isDone()) {
				player.moveToNextPosition();
				player.setAnimationState(AnimationState.NONE);
			}
			
			notcollision = player.updatePosition(deltaTime);
			player.setVelocityY(0);
			player.setVelocityX(0);
			
			if (notcollision) {
				player.increaseGravity(deltaTime);
				System.out.println("increasing gravity");
			} else {
				player.resetGravity();
			}
			

		}
	}
}

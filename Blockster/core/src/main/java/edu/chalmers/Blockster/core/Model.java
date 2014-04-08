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

import edu.chalmers.Blockster.core.util.Direction;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors, Eric Bjuhr, Emilia Nilsson
 * 
 */
public class Model implements Comparable<Model> {


	private BlockMap map;
	private BlockLayer blockLayer;
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
	
	private Factory factory;
	private final String name;
	

	//TODO Change this
	private final static String PLAYER_IMAGE_ADDRESS = "Player/still2.png";


	public Model(BlockMap map, Factory factory, String name) {
		this.map = map;
		this.blockLayer = this.map.getBlockLayer();
		this.factory = factory;
		this.name = name;
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
	
	public boolean canMovePlayer(int flags) {
		return collisionSurroundingBlocks(activePlayer, blockLayer, flags);
	}
	
	/**
	 * Checks to see if the player can move the block that's currently grabbed
	 * in the given direction.
	 * 
	 * @param dir	The direction to push the block
	 * @param anim	//TODO WHY???
	 * @return		True if block can be moved, false otherwise.
	 */
	public boolean canMoveBlock(Direction dir, Animation anim) {
		
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
		BlockLayer layer = map.getBlockLayer();
		
		/* Keep checking while not done, and make sure not to step out of bounds */
		while (!isDone && checkX >= 0 && checkX < layer.getWidth()) {

			/* Check if there is a block above the one at (checkX, checkY).
			 * Blocks can only be moved if there are no other blocks
			 * on top of them. First statement avoids going out of bounds. */
			if(checkY < layer.getHeight() && layer.hasBlock(checkX, checkY + 1)) {
				canMove = false;
				isDone = true;
			
			/* Check if there is an empty space (no block) here. If so, the
			 * previously checked blocks can be pushed/pulled to
			 * fill up the empty space */
			} else if(!layer.hasBlock(checkX, checkY)) {
				canMove = true;
				isDone = true;
				
			/* If the checked block is movable, we can check the next block
			 * in the given direction in the next iteration of the loop.
			 */
			} else if (layer.getBlock(checkX, checkY).isMovable()) {
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
				/* && !isMovingBlockAnimation && !isLiftingBlockAnimation && !isGrabbingBlockAnimation; */
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

	public float getActivePlayerVelocity() {
		return activePlayer.getMaximumMovementSpeed();
	}
	
	public Block getAdjacentBlock(Direction dir) {
		float blockWidth = blockLayer.getBlockWidth();
		float blockHeight = blockLayer.getBlockHeight();
		Block block = null;

		try {
			if (dir == LEFT) {
				Block adjacentBlockLeft = blockLayer.getBlock(
						(int) (activePlayer.getX() / blockWidth) - 1,
						(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / blockHeight));
				block = adjacentBlockLeft;
			}

			if (dir == RIGHT) {
				Block adjacentBlockRight = blockLayer.getBlock(
						(int) ((activePlayer.getX() + activePlayer.getWidth()) / blockWidth) + 1,
						(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / blockHeight));

				block = adjacentBlockRight;
			}
		} catch (NullPointerException e) {
			block = null;
		}
		return block;
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
		//TODO
		return new float[][] {{600, 500, 2500}};
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
			float blockWidth = blockLayer.getBlockWidth();
			
			//Lift process
			float relativePositionSignum = getProcessedBlock().getX()
					- activePlayer.getX() / blockWidth;
			Animation anim = Animation.getLiftAnimation(relativePositionSignum);
			getProcessedBlock().setAnimation(anim);
			activeBlocks.add(getProcessedBlock());
			liftedBlocks.put(activePlayer, getProcessedBlock());
		}
	}

	public void moveActivePlayer(Direction dir, float distance) {
		//Wrapper method
		movePlayer(dir, activePlayer, distance);
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
			
			//TODO Why???
			Animation anim = Animation.NONE;
			isMovingBlockAnimation = true;
			//TODO Why???
			
			/* Get a reference to the BlockLayer for brevity. */
			BlockLayer layer = map.getBlockLayer();
			
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
				if (layer.hasBlock(checkX, origY)) {
					movingBlocks.add(layer.getBlock(checkX, origY));
					checkX += dir.deltaX;
				} else {
					isDone = true;
				}
			}
			
			if(isLiftingBlock) {
				anim = Animation.getPullAnimation(dir);
			} else {
				float blockWidth = blockLayer.getBlockWidth();
				float relativePositionSignum = getProcessedBlock().getX()
						- activePlayer.getX() / blockWidth;
				anim = Animation.getMoveAnimation(dir, relativePositionSignum);
			}
			
			/*TODO Shouldn't this check come earlier to avoid adding blocks to
			 * the list if the move isn't possible?
			 */
			if (canMoveBlock(dir, anim)){
				/* Add the blocks to be moved to the list */
				for (Block block : movingBlocks) {
					activeBlocks.add(block);
					block.setAnimation(anim);
				}
				lastDirection = dir;
				activePlayer.setAnimation(anim);
			}
			return true;
	}

	private void movePlayer(Direction dir, Player player, float distance) {
		lastDirection = dir;
		player.move(dir, distance);
	}

	/**
	 * Start controlling the next player.
	 */
	public void nextPlayer() {
		int pIndex = getPlayers().indexOf(activePlayer);
		int nbrPlayers = getPlayers().size();
		int nPlayer = (pIndex + 1) % nbrPlayers;
		activePlayer = getPlayers().get(nPlayer);
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
			Player player = factory.createPlayer(PLAYER_IMAGE_ADDRESS, this);
			player.setX(startPosition[0]);
			player.setY(startPosition[1]);
			
			Player player2 = factory.createPlayer(PLAYER_IMAGE_ADDRESS, this);
			player2.setX(startPosition[2]);
			player2.setY(startPosition[1]);
			players.add(player);
			players.add(player2);
		}
	}

	public void stopProcessingBlock() {
		System.out.println("Stop processing block");
		if (isLiftingBlock) {
			System.out.println("Stop lifting block");
			float blockWidth = blockLayer.getBlockWidth();
			float blockHeight = blockLayer.getBlockHeight();
			boolean hasBlock = blockLayer.hasBlock(
					(int) (activePlayer.getX() / blockWidth) - lastDirection.deltaX,
					(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / blockHeight));
			
			if (!hasBlock) {
				System.out.println("Didn't have block");
				Animation anim = Animation.getPutDownAnimation(lastDirection);
				getProcessedBlock().setAnimation(anim);
				activeBlocks.add(getProcessedBlock());
				liftedBlocks.remove(activePlayer);
			}
		}
		

		liftedBlocks.remove(activePlayer);
		isGrabbingBlock = isLiftingBlock = isGrabbingBlockAnimation 
				= isLiftingBlockAnimation = isMovingBlockAnimation = false;
	}

	public void update(float deltaTime) {
		//Set animation state etc		

		if (getProcessedBlock() != null && getProcessedBlock().getAnimation() != Animation.NONE) {
			
		}

		for (Player player : players) {
			if (!collisionVertically(player, blockLayer)) {
				player.increaseGravity(deltaTime);
				player.move(FALL, player.getGravity().y);
			}

			float[] previousPosition = { player.getX(), player.getY() };

			player.setX(player.getX() + player.getVelocity().x);
			if (collisionHorisontally(player, blockLayer)) {
				player.setX(previousPosition[0]);
			}

			player.setY(player.getY() + player.getVelocity().y);
			if (collisionVertically(player, blockLayer)) {
				player.setY(previousPosition[1]);
				player.resetGravity();

				float y2 = player.getY();
				float blockHeight = blockLayer.getBlockHeight();

				player.setVelocityY(0);
				player.move(FALL,  + Math.abs(y2 - ((int) (y2 / blockHeight)) * blockHeight));
				player.setY(player.getY() + player.getVelocity().y);
			}


			player.setVelocityY(0);
			player.setVelocityX(0);

		}
	}
}

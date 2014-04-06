package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Calculations.*;
import static edu.chalmers.Blockster.core.util.Direction.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.Gdx;

import edu.chalmers.Blockster.core.util.Direction;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors, Eric Bjuhr
 * 
 */
public class Model implements Comparable<Model> {


	private BlockMap map;
	private BlockLayer blockLayer;
	private Block processedBlock;
	private Player activePlayer;
	private List<Player> players;
	private Set<Block> activeBlocks;

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
		setStartPositions();

		activePlayer = players.get(0);
	}
	
	public boolean canGrabBlock(Block block) {
		return block != null && processedBlock == null 
				&& !isGrabbingBlockAnimation && !isLiftingBlockAnimation 
				&& !isMovingBlockAnimation && (block.isMovable() || block.isLiftable());
	}
	
	public boolean canLiftBlock(Block block) {
		//TODO: Add additional tests
		return block != null && block.isLiftable();
				//&& isGrabbingBlock && !isGrabbingBlockAnimation &&
				//!isLiftingBlockAnimation && !isMovingBlockAnimation;
	}
	
	public boolean canMoveBlock(Direction dir) {
		//TODO: Add checks for collision etc
		return processedBlock != null;
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
		return new float[][] {{600, 500}};
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}

	public void grabBlock(Block block) {

		if (canGrabBlock(block)) {

			processedBlock = block;


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
		if (canLiftBlock(processedBlock)) {
			//If we are not already lifting a block, do so.
			isLiftingBlockAnimation = true;
			isLiftingBlock = true;
			isGrabbingBlock = false;
			
			//Lift process
			float relativePositionSignum = activePlayer.getX()
					- processedBlock.getX();
			Animation anim = Animation.getLiftAnimation(relativePositionSignum);
			processedBlock.setAnimation(anim);
			activeBlocks.add(processedBlock);
		}
	}

	public void moveActivePlayer(Direction dir, float distance) {
		//Wrapper method
		movePlayer(dir, activePlayer, distance);
	}

	public boolean moveBlock(Direction dir) {
		if (canMoveBlock(dir)) {
			Animation anim = Animation.NONE;
			isMovingBlockAnimation = true;
			
			//TODO: move block in grid, animation, etc
			activeBlocks.add(processedBlock);
			
			
			
			if(isLiftingBlock) {
				anim = Animation.getPullAnimation(dir);
			} else {
				float relativePositionSignum = activePlayer.getX() 
						- processedBlock.getX() * blockLayer.getBlockWidth();
				anim = Animation.getMoveAnimation(dir, relativePositionSignum);
			}
			processedBlock.setAnimation(anim);
			activePlayer.setAnimation(anim);
			return true;
		}
		return false;
	}

	private void movePlayer(Direction dir, Player player, float distance) {
		player.move(dir, distance);
	}

	public void nextPlayer() {

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
			players.add(player);
		}
	}

	public void stopProcessingBlock() {
		//TODO put down block animation, etc
		processedBlock = null;
		isGrabbingBlock = isLiftingBlock = isGrabbingBlockAnimation 
				= isLiftingBlockAnimation = isMovingBlockAnimation = false;
	}

	public void update(float deltaTime) {
		//Set animation state etc		

		if (processedBlock != null && processedBlock.getAnimation() != Animation.NONE) {
			
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

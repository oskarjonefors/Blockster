package edu.chalmers.Blockster.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	private HashMap<Player, Block> liftedBlocks;
	private List<Block> blocks;


	private boolean isGrabbingBlock = false; 
	private boolean isLiftingBlock = false;
	public boolean isSwitchChar;
	
	private Factory factory;
	private final String name;



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
		blocks = new ArrayList<Block>();
		setStartPositions();
		activePlayer = players.get(0);
		
		
		BlockLayer blockLayer = map.getBlockLayer();
		for (int x = 0; x < blockLayer.getWidth(); x++) {
			for(int y = 0; y < blockLayer.getHeight(); y++) {
				if (blockLayer.hasBlock(x, y)) {
					blocks.add(blockLayer.getBlock(x, y));
				}
			}
		}
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
		if (activePlayer.canGrabBlock(block)) {
			liftedBlocks.put(activePlayer,block);
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
		if (activePlayer.canLiftBlock(getProcessedBlock())) {
			//If we are not already lifting a block, do so.
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
	
	public void interactPlayer(Direction dir) {
		activePlayer.interact(dir);
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
		activePlayer.endInteraction();
	}

	public void update(float deltaTime) {
		
		updateBlocks(deltaTime);
		updatePlayers(deltaTime);

	}
	
	private void updateBlocks(float deltaTime) {
		for (Block block : blocks) {
			if (block.getAnimationState() != AnimationState.NONE) {
				block.getAnimationState().updatePosition(deltaTime);
				if(block.getAnimationState().isDone()) {
					block.moveToNextPosition();
					block.setAnimationState(AnimationState.NONE);
				}
			}
		}
	}
	
	private void updatePlayers(float deltaTime) {
		for (Player player : players) {
			boolean notcollision;
			
			
			if (player.getAnimationState() != AnimationState.NONE 
					&& player.getAnimationState().isDone()) {
				player.moveToNextPosition();
				player.setAnimationState(AnimationState.NONE);
			}
			
			notcollision = player.updatePosition(deltaTime);
			player.setVelocityY(0);
			player.setVelocityX(0);
			
			if (notcollision) {
				player.increaseGravity(deltaTime);
			} else {
				player.resetGravity();
			}
			

		}
	}
}

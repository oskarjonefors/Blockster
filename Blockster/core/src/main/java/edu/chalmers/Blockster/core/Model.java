package edu.chalmers.Blockster.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.chalmers.Blockster.core.util.AnimationState;
import edu.chalmers.Blockster.core.util.Calculations;
import edu.chalmers.Blockster.core.util.Direction;
import edu.chalmers.Blockster.core.util.Factory;

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
	private List<Block> blocks;
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
		setStartPositions();
		activePlayer = players.get(0);
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
		Map liftedBlocks = new HashMap<Player, Block>();
		for (Player player : getPlayers()) {
			if (player.isLiftingBlock()) {
				liftedBlocks.put(player, player.getProcessedBlock());
			}
		}
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
		return activePlayer.getProcessedBlock();
	}
	
	public boolean isGrabbingBlock() {
		return activePlayer.isGrabbingBlock();
	}

	public boolean isLiftingBlock() {
		return activePlayer.isLiftingBlock();
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
					startPosition[1], map);
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
		for (Block block : map.getBlocks()) {
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
			if (player.getAnimationState() != AnimationState.NONE 
					&& player.getAnimationState().isDone()) {
				player.moveToNextPosition();
				player.setAnimationState(AnimationState.NONE);
			}
			
			player.updatePosition(deltaTime);
			player.setVelocityY(0);
			player.setVelocityX(0);
			
			if (player.collidedVertically()) {
				player.resetGravity();
			} else {
				player.increaseGravity(deltaTime);
			}
		}
	}
}

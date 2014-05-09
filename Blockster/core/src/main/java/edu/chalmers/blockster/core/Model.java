package edu.chalmers.blockster.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;

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
	public boolean isSwitchChar;
	private Factory factory;
	private final String name;

	public Model(Factory factory, String name) {
		this.factory = factory;
		this.name = name;
		init();
	}
	
	public final void init() {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		return prime * result + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == getClass()) {
			Model mod = (Model)obj;
			return name.equals(mod.getName());
		} else {
			return false;
		}
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
		return map.getPlayerStartingPositions();
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
	
	public void interactPlayer() {
		activePlayer.interact();
	}
	
	/**
	 * Start controlling the next player.
	 */
	public void nextPlayer() {
		int pIndex = players.indexOf(activePlayer);
		activePlayer = players.get((pIndex + 1) % players.size());
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
		map.updateActiveBlocks(deltaTime);
		map.insertFinishedBlocks();
	}
	
	private void updatePlayers(float deltaTime) {
		for (Player player : players) {
			player.updatePosition(deltaTime);
			if (player.getAnimationState() != AnimationState.NONE 
					&& player.getAnimationState().isDone()) {
				player.moveToNextPosition();
				player.setAnimationState(AnimationState.NONE);
			}
			
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

package edu.chalmers.blockster.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMap;
import edu.chalmers.blockster.core.objects.GameEventListener;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Movement;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors, Eric Bjuhr, Emilia Nilsson
 * 
 */
public class Model implements Comparable<Model>, GameEventListener {

	private static final Logger LOG = Logger.getLogger(Model.class.getName());
	
	private BlockMap map;
	private Player activePlayer;
	private List<Player> players;
	private Set<Block> activeBlocks;
	private final Factory factory;
	private final String name;
	private GameState gameState = GameState.GAME_RUNNING;
	private boolean activePlayerEnteringTeleporter = false;

	public Model(Factory factory, String name) {
		this.factory = factory;
		this.name = name;
		init();
	}
	
	public final void init() {
		factory.createMap();
		this.map = factory.getMap();
		players = new ArrayList<Player>();
		activeBlocks = Collections.synchronizedSet(new HashSet<Block>());
		setStartPositions();
		activePlayer = players.get(0);
		
		for(Player player : players){
			player.addGameEventListener(this);
		}
	}

	@Override
	public int compareTo(Model model) {
		return name.compareTo(model.getName());
	}

	@Override
	public int hashCode() {
		return 31 + ((name == null) ? 0 : name.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj.getClass() == getClass()) {
			final Model mod = (Model)obj;
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
	
	/**
	 * Start controlling the next player.
	 */
	public void nextPlayer() {
		if (activePlayer.getAnimationState().getMovement() == Movement.NONE) {
			final int pIndex = players.indexOf(activePlayer);
			activePlayer = players.get((pIndex + 1) % players.size());
			activePlayer.switchingToMe(true);
		}
	}

	public void resetStartPositions() {
		final float[][] startPositions = map.getPlayerStartingPositions();
		for (int i = 0; i < startPositions.length; i++) {
			players.get(i).setX(startPositions[i][0]);
			players.get(i).setY(startPositions[i][1]);
			players.get(i).setVelocityX(0);
			players.get(i).setVelocityY(0);
			players.get(i).resetGravity();
		}
	}

	private void setStartPositions() {
		for (final float[] startPosition : map.getPlayerStartingPositions()) {
			final Player player = factory.createPlayer(startPosition[0], 
					startPosition[1], map);
			players.add(player);
		}
	}

	public void update(float deltaTime) {
		if(deltaTime <= 0) {
			throw new IllegalArgumentException("Update method does not accept"
					+ "deltatimes that are negative or zero.");
		}
		

		checkGoals();
		updateBlocks(deltaTime);
		updatePlayers(deltaTime);
	}
	
	private void checkGoals() {
		if (activePlayerEnteringTeleporter) {
			Player previousActivePlayer = activePlayer;
			nextPlayer();
			didWeWin();
			if (previousActivePlayer != activePlayer) {
				players.remove(previousActivePlayer);
				activePlayerEnteringTeleporter = false;
			}
		}
	}
	
	private void didWeWin() {
		if(getActivePlayer().getAnimationState().getMovement() == Movement.NONE && players.size() == 1){
			setGameState(GameState.GAME_WON);
		}
		
	}

	private void updateBlocks(float deltaTime) {
		map.updateActiveBlocks(deltaTime);
	}
	
	private void updatePlayers(float deltaTime) {
		for (final Player player : players) {
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

	@Override
	public void playerReachedGoal() {
		activePlayerEnteringTeleporter = true;
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}
	
}


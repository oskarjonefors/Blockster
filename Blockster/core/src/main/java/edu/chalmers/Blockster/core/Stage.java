package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.Direction.*;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors, Eric Bjuhr
 * 
 */
public class Stage {

	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();
	
	private TiledMap map;
	private TiledMapTileLayer collisionLayer;
	private Block processedBlock;
	private StageView stageView;
	private Player activePlayer;
	private List<Player> players;
	
	private boolean isGrabbingBlockAnimation = false; //for animations
	private boolean isMovingBlockAnimation = false;
	private boolean isLiftingBlockAnimation = false;
	
	private boolean isGrabbingBlock = false; 
	private boolean isLiftingBlock = false;
	
	private Sprite PlayerImg = new Sprite(new Texture("Player/still2.png"));
	

	public Stage(TiledMap map) {
		this.map = map;
		this.collisionLayer = (TiledMapTileLayer)map.getLayers().get(0);
		players = new ArrayList<Player>();
		
		for (float[] startPosition : getPlayerStartingPositions(map)) {
			Player player = new Player(PlayerImg);
			player.setX(startPosition[0]);
			player.setY(startPosition[1]);
			players.add(player);
		}
		
		activePlayer = players.get(0);
	}
	
	public boolean canGrabBlock(Block block) {
		//TODO: Add additional tests (type of block etc)
		return block != null && processedBlock == null 
				&& !isGrabbingBlockAnimation && !isLiftingBlockAnimation 
				&& !isMovingBlockAnimation;
	}
	
	public boolean canLiftBlock(Block block) {
		//TODO: Add additional tests
		return block != null && isGrabbingBlock && !isGrabbingBlockAnimation &&
				!isLiftingBlockAnimation && !isMovingBlockAnimation;
	}
	
	public boolean canMoveBlock(Direction dir) {
		//TODO: Add checks for collision etc
		return processedBlock != null && !isMovingBlockAnimation 
				&& !isLiftingBlockAnimation && !isGrabbingBlockAnimation;
	}
	
	private boolean collisionBothXAxis(Player player) {
		boolean collisionXLeft = false;
		boolean collisionXRight = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();;
		
		collisionXLeft = collisionLayer.getCell(
				(int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeigth))
				.getTile().getProperties().containsKey("Collision");

		// checking the tile to the left of the player
		collisionXLeft |= collisionLayer.getCell(
				(int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / 2 / tileHeigth))
				.getTile().getProperties().containsKey("Collision");

		// checking the tile to the left and under the player
		collisionXLeft |= collisionLayer.getCell(
				(int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeigth)).getTile()
				.getProperties().containsKey("Collision");

		collisionXRight = collisionLayer.getCell(
				(int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeigth))
				.getTile().getProperties().containsKey("Collision");

		// checking the tile to the right of the player
		collisionXRight |= collisionLayer.getCell(
				(int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getWidth()) / 2 / tileHeigth))
				.getTile().getProperties().containsKey("Collision");

		// checking the tile to the right and UNDER the player
		collisionXRight |= collisionLayer.getCell(
				(int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeigth)).getTile()
				.getProperties().containsKey("Collision");
		
		return collisionXLeft && collisionXRight;
	}
	
	private boolean collisionX(Player player) {
		boolean collisionX = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();;
		
		if (player.getVelocity().x < 0) {
			//System.out.println("Left");

			// checking the tile to the left and above the player
			collisionX = collisionLayer.getCell(
							(int) (player.getX() / tileWidth),
							(int) ((player.getY() + player.getHeight()) / tileHeigth))
					.getTile().getProperties().containsKey("Collision");

			// checking the tile to the left of the player
			collisionX |= collisionLayer.getCell(
							(int) (player.getX() / tileWidth),
							(int) ((player.getY() + player.getHeight()) / 2 / tileHeigth))
					.getTile().getProperties().containsKey("Collision");

			// checking the tile to the left and under the player
			collisionX |= collisionLayer.getCell(
							(int) (player.getX() / tileWidth),
							(int) (player.getY() / tileHeigth)).getTile()
					.getProperties().containsKey("Collision");

			/*
			 * Player moving to the right
			 */
		} else if (player.getVelocity().x > 0) {
			//System.out.println("Right");

			// checking the tile to the right and ABOVE the player
			collisionX = collisionLayer.getCell(
							(int) ((player.getX() + player.getWidth()) / tileWidth),
							(int) ((player.getY() + player.getHeight()) / tileHeigth))
					.getTile().getProperties().containsKey("Collision");

			// checking the tile to the right of the player
			collisionX |= collisionLayer.getCell(
							(int) ((player.getX() + player.getWidth()) / tileWidth),
							(int) ((player.getY() + player.getWidth()) / 2 / tileHeigth))
					.getTile().getProperties().containsKey("Collision");

			// checking the tile to the right and UNDER the player
			collisionX |= collisionLayer.getCell(
							(int) ((player.getX() + player.getWidth()) / tileWidth),
							(int) (player.getY() / tileHeigth)).getTile()
					.getProperties().containsKey("Collision");
		}
		return collisionX;
	}
	
	private boolean collisionY(Player player) {
		boolean collisionY = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();;
		


		if (player.getVelocity().y < 0) {
			// checking tile to the left and under player
			collisionY = collisionLayer.getCell(
							(int) (player.getX() / tileWidth),
							(int) (player.getY() / tileHeigth)).getTile()
					.getProperties().containsKey("Collision");

			// checking tile under player
			collisionY |= collisionLayer.getCell(
							(int) ((player.getX() + player.getWidth()) / 2 / tileWidth),
							(int) (player.getY() / tileHeigth)).getTile()
					.getProperties().containsKey("Collision");

			// checking to the right and under the player
			collisionY |= collisionLayer.getCell(
							(int) ((player.getX() + player.getWidth()) / tileWidth),
							(int) (player.getY() / tileHeigth)).getTile()
					.getProperties().containsKey("Collision");

		}
		return collisionY;
	}
	
	public float getActivePlayerVelocity() {
		return activePlayer.getMaximumMovementSpeed();
	}
	
	public Block getAdjacentBlock(Direction dir) {
		if (dir != NONE) {
			//TODO: Get adjacent block from map
		}
		return null;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	private float[][] getPlayerStartingPositions(TiledMap map) {
		//TODO
		return new float[][] {{600, 1500}};
	}
	
	public Block getProcessedBlock() {
		return processedBlock;
	}
	
	public StageView getStageView() {
		return stageView;
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
			//TODO: Start lift animation
		}
	}
	
	public void moveActivePlayer(Direction dir, float distance) {
		//Wrapper method
		movePlayer(dir, activePlayer, distance);
	}
	
	public boolean moveBlock(Direction dir) {
		if (canMoveBlock(dir)) {
			isMovingBlockAnimation = true;
			//TODO: move block in grid, animation, etc
			return true;
		}
		return false;
	}
	
	
	private void movePlayer(Direction dir, Player player, float distance) {
		player.move(dir, distance);
	}
	 
	
	public void nextPlayer() {
		
	}
	
	public void setStageView(StageView stageView) {
		this.stageView = stageView;
	}
	
	public void stopProcessingBlock() {
		//TODO put down block animation, etc
		processedBlock = null;
	}
	
	public void update(float deltaTime) {
		//Set animation state etc
		
		if (isGrabbingBlockAnimation) {
			//Set the block grabbing animation timer to t+deltaTime
		}
		
		if (isMovingBlockAnimation) {
			//Set the block moving animation timer to t+deltaTime
		}
		
		if (isLiftingBlockAnimation) {
			//Set the block lifting animation timer to t+deltaTime
		}
		
		for (Player player : players) {
			if (!collisionY(player)) {
				player.increaseGravity(deltaTime);
				player.move(FALL, player.getGravity().y);
			} else {
				player.resetGravity();
			}
			
			float[] previousPosition = { player.getX(), player.getY() };
			
			player.setX(player.getX() + player.getVelocity().x);
			if (collisionX(player)) {
				player.setX(previousPosition[0]);
			}
			
			player.setY(player.getY() + player.getVelocity().y);
			if (collisionY(player) || collisionBothXAxis(player)) {
				player.setY(previousPosition[1]);
			}
			
			player.setVelocityX(0);
			player.setVelocityY(0);
		}
	}
}

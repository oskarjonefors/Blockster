package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.Direction.*;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import edu.chalmers.Blockster.core.Block.Animation;

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
	
	public void setBlocks() {
		for (MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				for (int x = 0; x < tileLayer.getWidth(); x++) {
					for (int y = 0; y < tileLayer.getHeight(); y++) {
						TiledMapTile tile = tileLayer.getCell(x, y).getTile();
						Block block = new Block(tile);
						tileLayer.getCell(x,y).setTile(block);
					}
				}
			}
		}
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
	
	private boolean collisionX(Player player) {
		try {
			if (player.getVelocity().x < 0) {
				return collisionXAxisLeft(player);
			} else {
				return collisionXAxisRight(player);
			}
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	private boolean collisionXAxisBoth(Player player) {
		try {
			return collisionXAxisLeft(player) && collisionXAxisRight(player);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	private boolean collisionXAxisLeft(Player player) {
		boolean collisionX = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();
		
		// checking upper left corner
		collisionX = collisionLayer.getCell(
				(int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeigth))
				.getTile().getProperties().containsKey("Collision");


		// checking lower left corner
		collisionX |= collisionLayer.getCell(
				(int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeigth)).getTile()
				.getProperties().containsKey("Collision");
		return collisionX;
	}
	
	private boolean collisionXAxisRight(Player player) {
		boolean collisionX = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();
		
		// checking upper right corner
		collisionX = collisionLayer.getCell(
				(int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeigth))
				.getTile().getProperties().containsKey("Collision");

		// checking lower right corner
		collisionX |= collisionLayer.getCell(
				(int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeigth)).getTile()
				.getProperties().containsKey("Collision");

		return collisionX;
	}
	
	private boolean collisionY(Player player) {
		try {
			if (player.getVelocity().y < 0) {
				return collisionYAxisDown(player);
			}
		} catch (NullPointerException e) {
			return false;
		}
		

		//TODO: What if blocks are above the character??
		return false;
	}
	
	private boolean collisionYAxisDown(Player player) {
		boolean collisionY = false;
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();

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

		return collisionY;
	}
	
	public float getActivePlayerVelocity() {
		return activePlayer.getMaximumMovementSpeed();
	}
	
	public Block getAdjacentBlock(Direction dir) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();
		Block block = null;
		
		if (dir == LEFT) {
			TiledMapTile adjacentTileLeft = collisionLayer.getCell(
					(int) ((activePlayer.getX() + activePlayer.getWidth()) / 2 / tileWidth),
					(int) (activePlayer.getY() / tileHeigth)).getTile();
			
			block = (Block) adjacentTileLeft;
		}
		
		if (dir == RIGHT) {
			TiledMapTile adjacentTileRight = collisionLayer.getCell(
					(int) ((activePlayer.getX() + activePlayer.getWidth()) / tileWidth),
					(int) ((activePlayer.getY() + activePlayer.getWidth()) / 2 / tileHeigth))
					.getTile();
			block = (Block) adjacentTileRight;
		}
		
		return block;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public List<Player> getPlayers() {
		return players;
	}
	
	private float[][] getPlayerStartingPositions(TiledMap map) {
		//TODO
		return new float[][] {{600, 500}};
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
		
		if (processedBlock != null && processedBlock.getAnimation() != Animation.NONE) {
			if(AnimationHandler.handleBlockPlayerAnimation(processedBlock, activePlayer, map)) {
				//TODO: sätt alla animationer av, blocks animationer till none
			}
		}
		
		for (Player player : players) {
			if (!collisionY(player)) {
				player.increaseGravity(deltaTime);
				player.move(FALL, player.getGravity().y);
			}
			
			float[] previousPosition = { player.getX(), player.getY() };
			
			player.setX(player.getX() + player.getVelocity().x);
			if (collisionX(player)) {
				player.setX(previousPosition[0]);
			}
			
			player.setY(player.getY() + player.getVelocity().y);
			if (collisionY(player) || collisionXAxisBoth(player)) {
				player.setY(previousPosition[1]);
				player.resetGravity();
				
				float y2 = player.getY();
				float tileHeigth = collisionLayer.getTileHeight();

				player.setVelocityY(0);
				player.move(FALL,  + Math.abs(y2 - ((int) (y2 / tileHeigth)) * tileHeigth));
				player.setY(player.getY() + player.getVelocity().y);
			}
			

			player.setVelocityY(0);
			player.setVelocityX(0);
			
		}
	}
}

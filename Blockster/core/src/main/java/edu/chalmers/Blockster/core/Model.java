package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.util.Calculations.*;
import static edu.chalmers.Blockster.core.util.Direction.*;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

import edu.chalmers.Blockster.core.gdx.view.Blockster;
import edu.chalmers.Blockster.core.gdx.view.GdxBlock;
import edu.chalmers.Blockster.core.gdx.view.GdxPlayer;
import edu.chalmers.Blockster.core.gdx.view.View;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * A class to represent a stage.
 * @author Oskar J��nefors, Eric Bjuhr
 * 
 */
public class Model {

	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();

	private TiledMap map;
	private TiledMapTileLayer collisionLayer;
	private Block processedBlock;
	private View stageView;
	private GdxPlayer activePlayer;
	private List<GdxPlayer> players;

	private boolean isGrabbingBlockAnimation = false; //for animations
	private boolean isMovingBlockAnimation = false;
	private boolean isLiftingBlockAnimation = false;

	private boolean isGrabbingBlock = false; 
	private boolean isLiftingBlock = false;

	private Sprite PlayerImg = new Sprite(new Texture("Player/still2.png"));


	public Model(TiledMap map) {
		this.map = map;
		this.collisionLayer = (TiledMapTileLayer)map.getLayers().get(0);
		players = new ArrayList<GdxPlayer>();
		setStartPositions();
		
		activePlayer = players.get(0);
		setBlocks();
	}
	
	private void setStartPositions() {
		for (float[] startPosition : getPlayerStartingPositions(map)) {
			GdxPlayer player = new GdxPlayer(PlayerImg);
			player.setX(startPosition[0]);
			player.setY(startPosition[1]);
			players.add(player);
		}
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

	public boolean canGrabBlock(Block block) {
		//TODO: Add additional tests (type of block etc)

		return block != null && processedBlock == null 
				&& !isGrabbingBlockAnimation && !isLiftingBlockAnimation 
				&& !isMovingBlockAnimation && (block.isMovable() || block.isLiftable());
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

	@SuppressWarnings("unused")
	private boolean collisionAbove(GdxPlayer player) {
		try {
			return collisionUpperLeft(player, collisionLayer) ||
					collisionUpperRight(player, collisionLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean collisionBelow(GdxPlayer player) {
		try {
			return collisionLowerLeft(player, collisionLayer) ||
					collisionLowerRight(player, collisionLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean collisionHorisontally(GdxPlayer player) {
		if (player.getVelocity().x < 0) {
			return collisionLeft(player);
		} else if (player.getVelocity().x > 0) {
			return collisionRight(player);
		} else {
			return false;
		}
	}

	private boolean collisionLeft(GdxPlayer player) {
		try {
			return collisionUpperLeft(player, collisionLayer) 
					|| collisionLowerLeft(player, collisionLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean collisionRight(GdxPlayer player) {
		try {
			return collisionUpperRight(player, collisionLayer)
					|| collisionLowerRight(player, collisionLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private boolean collisionVertically(GdxPlayer player) {
		if (player.getVelocity().y < 0) {
			return collisionBelow(player);
		}

		//TODO: What if blocks are above the character??
		return false;
	}
	
	public float getActivePlayerVelocity() {
		return activePlayer.getMaximumMovementSpeed();
	}

	public Block getAdjacentBlock(Direction dir) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeigth = collisionLayer.getTileHeight();
		Block block = null;

		try {
			if (dir == LEFT) {
				TiledMapTile adjacentTileLeft = collisionLayer.getCell(
						(int) (activePlayer.getX() / tileWidth) - 1,
						(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / tileHeigth)).getTile();
				block = (Block) adjacentTileLeft;
			}

			if (dir == RIGHT) {
				TiledMapTile adjacentTileRight = collisionLayer.getCell(
						(int) ((activePlayer.getX() + activePlayer.getWidth()) / tileWidth) + 1,
						(int) ((2 * activePlayer.getY() + activePlayer.getHeight()) / 2 / tileHeigth))
						.getTile();

				block = (Block) adjacentTileRight;
			}
		} catch (NullPointerException e) {
			block = null;
		}
		return block;
	}

	public TiledMap getMap() {
		return map;
	}

	public List<GdxPlayer> getPlayers() {
		return players;
	}

	private float[][] getPlayerStartingPositions(TiledMap map) {
		//TODO
		return new float[][] {{600, 500}};
	}

	public Block getProcessedBlock() {
		return processedBlock;
	}

	public View getStageView() {
		return stageView;
	}

	public void grabBlock(Block block) {

		if (canGrabBlock(block)) {

			processedBlock = block;


			isGrabbingBlockAnimation = true;
			isGrabbingBlock = true;

			Gdx.app.log("Stage", "Can grab block!");
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

	private void movePlayer(Direction dir, GdxPlayer player, float distance) {
		player.move(dir, distance);
	}


	public void nextPlayer() {

	}


	public void setBlocks() {
		for (MapLayer layer : map.getLayers()) {
			if (layer instanceof TiledMapTileLayer) {
				TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				for (int x = 0; x < tileLayer.getWidth(); x++) {
					for (int y = 0; y < tileLayer.getHeight(); y++) {
						Cell cell = tileLayer.getCell(x, y);
						if (cell != null) {
							TiledMapTile tile = cell.getTile();
							System.out.println("("+x+", "+y+") from "
									+tile.getClass().getSimpleName()+" to "+Block.class.getSimpleName());
							GdxBlock block = new GdxBlock(tile);
							tileLayer.getCell(x,y).setTile(block);
						}
					}
				}
			}
		}
	}

	public void setStageView(View stageView) {
		this.stageView = stageView;
	}

	public void stopProcessingBlock() {
		//TODO put down block animation, etc
		processedBlock = null;
	}

	public void update(float deltaTime) {
		//Set animation state etc		

		if (processedBlock != null && processedBlock.getAnimation() != Block.Animation.NONE) {
			
		}

		for (GdxPlayer player : players) {
			if (!collisionVertically(player)) {
				player.increaseGravity(deltaTime);
				player.move(FALL, player.getGravity().y);
			}

			float[] previousPosition = { player.getX(), player.getY() };

			player.setX(player.getX() + player.getVelocity().x);
			if (collisionHorisontally(player)) {
				player.setX(previousPosition[0]);
			}

			player.setY(player.getY() + player.getVelocity().y);
			if (collisionVertically(player)) {
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

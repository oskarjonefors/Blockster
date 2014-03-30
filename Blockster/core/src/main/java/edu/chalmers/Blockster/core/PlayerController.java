package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;

public class PlayerController implements InputProcessor {
	private Player player;
	private TiledMapTileLayer collisionLayer;
	private float maximumMovmentSpeed = 700, gravity = 2000;
	private Sprite PlayerImg = new Sprite(new Texture("Player/still2.png"));
	private boolean collisionX, collisionY;


	public PlayerController(TiledMapTileLayer collisionLayer){
		player = new Player(PlayerImg);
		this.collisionLayer = collisionLayer;
		Gdx.input.setInputProcessor(this);
		
		/**
		 * Set the startposition for the player.
		 */
		player.setX(600);
		player.setY(1500);
		
	}
	
	/**
	 * Update the players movement
	 * @param delta
	 */
	public void update(float delta){
		
		Vector2 playerVelocity = player.getVelocity();
	
		/**
		 * add the gravity to the player and check´s that the speed wont reach infinity
		 */
		playerVelocity.y -= gravity * delta;
		
		if(playerVelocity.y > maximumMovmentSpeed){
			playerVelocity.y = maximumMovmentSpeed;
			
		}else if(playerVelocity.y < -maximumMovmentSpeed){
			playerVelocity.y =-maximumMovmentSpeed;
		}else{}
	
		//Store the just resent position
		float oldX = player.getX(), oldY = player.getY(), 
				tileWidth = collisionLayer.getTileWidth(), tileHeigth = collisionLayer.getTileHeight();;
		
		/**
		 * ******Collision handler****** 
		 *     (far from optimized)
		 */
				
		//Move player in X-direction
		player.setX((player.getX() + playerVelocity.x * delta));
		
		/**
		 * Player moving to the left
		 */
		if(playerVelocity.x < 0){
			System.out.println("Left");
			
//			// checking the tile to the left and above the player
//			collisionX = collisionLayer.getCell((int)(player.getX() / tileWidth), (int)((player.getY() + player.getHeight()) / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
//			
//			// checking the tile to the left of the player
//			if(!collisionX)
//			collisionX = collisionLayer.getCell((int)(player.getX() / tileWidth), (int)((player.getY() + player.getHeight()) / 2 / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
//			
//			// checking the tile to the left and under the player
//			if(!collisionX)		
//					collisionX = collisionLayer.getCell((int)(player.getX() / tileWidth), (int)(player.getY() / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
			
		/**
		 *  Player moving to the right
		 */
		}else if(playerVelocity.x > 0){
			System.out.println("Right");
			
//			// checking the tile to the right and ABOVE the player
//			collisionX = collisionLayer.getCell((int)((player.getX() + player.getWidth()) / tileWidth), (int)((player.getY() + player.getHeight()) / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
//			
//			// checking the tile to the right of the player
//			if(!collisionX)
//				collisionX = collisionLayer.getCell((int)((player.getX() + player.getWidth()) / tileWidth), (int)((player.getY() + player.getWidth()) / 2 / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
//			
//			// checking the tile to the right and UNDER the player
//			if(!collisionX)
//				collisionX = collisionLayer.getCell((int)((player.getX() + player.getWidth()) / tileWidth), (int)(player.getY() / tileHeigth))
//					.getTile().getProperties().containsKey("Collision");
		}
		
		/**
		 * Deal with potentially collisions on X-axis
		 */
		if(collisionX){
			player.setX(oldX);
			player.setVelocityX(0);
		}
		
		//Move player in Y-direction
		player.setY((player.getY() + playerVelocity.y * delta));
		
		/**
		 * Player falling
		 */
		if(playerVelocity.y < 0){
			
			// checking tile to the left and under player
			collisionY = collisionLayer.getCell((int)(player.getX() / tileWidth), (int)(player.getY() / tileHeigth))
					.getTile().getProperties().containsKey("Collision");
			
			// checking tile under player
			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((player.getX() + player.getWidth()) / 2 / tileWidth), (int)(player.getY() / tileHeigth))
					.getTile().getProperties().containsKey("Collision");
			
			//checking to the right and under the player
			if(!collisionY)
				collisionY = collisionLayer.getCell((int)((player.getX() + player.getWidth()) / tileWidth), (int)(player.getY() / tileHeigth))
				.getTile().getProperties().containsKey("Collision");
			
		}
		
		/**
		 * Deal with potentially collisions in Y-axis
		 */
		
		if(collisionY){
			player.setY(oldY);
			player.setVelocityY(0);
		}
	}
	public Player getPlayer(){
		return player;
	}

	@Override
	public boolean keyDown(int keycode) {
		switch(keycode){
		case Keys.W:
			break;
		case Keys.A:
			System.out.println("Pressed Left");
			player.setVelocityX(-maximumMovmentSpeed);
			break;
		case Keys.D:
			System.out.println("Predded Right");
			player.setVelocityX(maximumMovmentSpeed);
			break;
		}
	
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch(keycode){
		case Keys.A:
			player.setVelocityX(0);
			break;
		case Keys.D:
			player.setVelocityX(0);
		}
		return true;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}

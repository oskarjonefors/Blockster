package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.math.Vector2;

public class PlayerController {
	private Player player;
	private MapLayers mapLayers;
	private float maximumMovmentSpeed = 700, gravity = 2000 ;
	private Sprite PlayerImg = new Sprite(new Texture("Player/still2.png"));

	public PlayerController(MapLayers mapLayers){
		player = new Player(PlayerImg);
		this.mapLayers = mapLayers;
		
		/**
		 * Set the startposition for the player.
		 */
		player.setX(100);
		player.setY(900);
		
	}
	
	/**
	 * Update the players movement
	 * @param delta
	 */
	public void update(float delta){
		
		Vector2 playerVelocity = player.getVelocity();
	
		//add the gravity to the player and check´s that the speed wont reach infinity
		playerVelocity.y -= gravity * delta;
		
		if(playerVelocity.y > maximumMovmentSpeed){
			playerVelocity.y = maximumMovmentSpeed;
			
		}else if(playerVelocity.y < -maximumMovmentSpeed){
			playerVelocity.y =-maximumMovmentSpeed;
		}else{
		
			
			//player.setVelocityY(gravity * delta);
		}
	
		player.setX((player.getX() + playerVelocity.x * delta));
		player.setY((player.getY() + playerVelocity.y * delta));
		
	}
	public Player getPlayer(){
		return player;
	}
}

package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {
	
	
	private Vector2 velocity = new Vector2();
	
	

	public Player (Sprite img){
		super(img);
	}
	
	
	/**
	 * sets the velocity of the player
	 * @param velY
	 */
	public void setVelocityY(float velY){
		velocity.y = velY;
	}
	public void setVelocityX(float velX){
		velocity.x = velX;
	}
	public Vector2 getVelocity(){
		return velocity;
	}
}

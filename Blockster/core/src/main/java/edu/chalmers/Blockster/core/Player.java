package edu.chalmers.Blockster.core;

import static java.lang.Math.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Player extends Sprite {
	
	private float maximumMovementSpeed = 700;
	private Vector2 velocity = new Vector2();
	private Vector2 gravity = new Vector2();
	private float gravityTime = 0;
	

	public Player (Sprite img){
		super(img);
	}
	
	
	public float getMaximumMovementSpeed() {
		return maximumMovementSpeed;
	}
	public Vector2 getVelocity(){
		return velocity;
	}
	
	public void move(Direction dir, float distance) {
		setVelocityX(getVelocity().x + dir.deltaX * distance);
		setVelocityY(getVelocity().y + dir.deltaY * distance);
	}
	
	public void setVelocityX(float velX){
		velocity.x = min(velX, maximumMovementSpeed);
	}
	
	/**
	 * sets the velocity of the player
	 * @param velY
	 */
	public void setVelocityY(float velY){
		velocity.y = min(velY, maximumMovementSpeed);
	}
	
	public void increaseGravity(float deltaTime) {
		gravityTime += deltaTime;
		gravity.y = 10 * gravityTime;
	}
	
	public void resetGravity() {
		gravity.y = 0;
		gravityTime = 0;
	}
	
	public Vector2 getGravity() {
		return gravity;
	}
}

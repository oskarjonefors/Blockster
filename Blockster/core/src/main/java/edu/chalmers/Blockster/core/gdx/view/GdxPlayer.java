package edu.chalmers.Blockster.core.gdx.view;

import static java.lang.Math.*;

import javax.vecmath.Vector2f;

import com.badlogic.gdx.graphics.g2d.Sprite;

import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.util.Direction;

public class GdxPlayer extends Sprite implements Player {
	
	private float maximumMovementSpeed = 700;
	private Vector2f velocity = new Vector2f();
	private Vector2f gravity = new Vector2f();
	private float gravityTime = 0;
	

	public GdxPlayer (Sprite img) {
		super(img);
	}
	
	
	@Override
	public Vector2f getGravity() {
		return gravity;
	}
	@Override
	public float getMaximumMovementSpeed() {
		return maximumMovementSpeed;
	}
	
	@Override
	public Vector2f getVelocity(){
		return velocity;
	}
	
	@Override
	public void increaseGravity(float deltaTime) {
		gravityTime += deltaTime;
		gravity.y = 9.82F * gravityTime;
	}
	
	@Override
	public void move(Direction dir, float distance) {
		setVelocityX(getVelocity().x + dir.deltaX * distance);
		setVelocityY(getVelocity().y + dir.deltaY * distance);
	}
	
	@Override
	public void resetGravity() {
		gravity.y = 0;
		gravityTime = 0;
	}
	
	@Override
	public void setVelocityX(float velX){
		velocity.x = min(velX, maximumMovementSpeed);
	}
	
	/**
	 * sets the velocity of the player
	 * @param velY
	 */
	@Override
	public void setVelocityY(float velY){
		velocity.y = min(velY, maximumMovementSpeed);
	}
}

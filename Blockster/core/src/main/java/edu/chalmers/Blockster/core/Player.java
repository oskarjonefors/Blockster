package edu.chalmers.Blockster.core;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.util.Direction;

public interface Player {

	public Vector2f getGravity();
	
	public float getMaximumMovementSpeed();
	
	public Vector2f getVelocity();
	
	public void increaseGravity(float deltaTime);
	
	public void move(Direction dir, float distance);
	
	public void resetGravity();
	
	public void setVelocityX(float velX);
	
	public void setVelocityY(float velX);
	
	public float getX();
	
	public float getY();

	public float getWidth();
	
	public float getHeight();
	
	
}

package edu.chalmers.Blockster.core;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.util.Direction;

public interface Player {

	public Vector2f getGravity();
	
	public float getHeight();
	
	public float getMaximumMovementSpeed();
	
	public Vector2f getVelocity();
	
	public float getWidth();
	
	public float getX();
	
	public float getY();
	
	public void increaseGravity(float deltaTime);
	
	public void move(Direction dir, float distance);
	
	public void resetGravity();

	public void setVelocityX(float velX);
	
	public void setVelocityY(float velX);
	
	public void setX(float x);
	
	public void setY(float y);
}

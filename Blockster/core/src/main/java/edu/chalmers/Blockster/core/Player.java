package edu.chalmers.Blockster.core;

import javax.vecmath.*;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * The model representing a player in the game Blockster
 * @author Emilia
 *
 */
public class Player {
	private float posX;
	private float posY;
	private final float height;
	private final float width;
	private AnimationState anim;
	private Vector2f velocityX;
	private Vector2f velocityY;
	
	public Player(float startX, float startY, float height, float width) {
		this.posX = startX;
		this.posY = startY;
		this.height = height;
		this.width = width;
	}
	
	public void updatePosition(Direction direction) {
		posX += direction.deltaX * 128;
		posY += direction.deltaY * 128;
	}

	public float getX() {
		//If there is an animation currently running then
		//we want to return the relative position
		if (anim != AnimationState.NONE) {
			return posX + anim.getRelativePosition().x;
		}
		return posX;
	}
	
	public float getY() {
		//If there is an animation currently running then
		//we want to return the relative position
		if (anim != AnimationState.NONE) {
			return posY + anim.getRelativePosition().y;
		}
		return posY;
	}
	
	public void setX(float x) {
		posX = x;
	}
	
	public void setY(float y) {
		posY = y;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public AnimationState getAnimation() {
		return anim;
	}
	
	public void setAnimation(AnimationState anim) {
		this.anim = anim;
	}
	
	public Vector2f getVelocityX() {
		return velocityX;
	}
	
	public Vector2f getVelocityY() {
		return velocityY;
	}
	
	public void setVelocityX(Vector2f velocityX) {
		this.velocityX = velocityX;
	}
	
	public void setVelocityY(Vector2f velocityY) {
		this.velocityY = velocityY;
	}
	
	public boolean move(Direction dir, float distance) {
		setVelocityX(getVelocityX().x );
	}
	
	public void collision() {
		
	}
	
}

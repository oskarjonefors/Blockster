package edu.chalmers.Blockster.core;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.util.Direction;
import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * The model representing a player in the game Blockster
 * @author Emilia
 *
 */
public class Player implements BlocksterObject{
	private float posX;
	private float posY;
	private final float height;
	private final float width;
	private AnimationState anim;
	private Vector2f velocity;
	private Vector2f defaultVelocity = new Vector2f(700, 700);
	private float totalTime = 0;
	
	public Player(float startX, float startY, float height, float width) {
		this.posX = startX;
		this.posY = startY;
		this.height = height;
		this.width = width;
	}
	
	public void updatePosition(Direction dir) {
		posX += dir.deltaX * 128;
		posY += dir.deltaY * 128;
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
	
	public void increaseVelocityY(float deltaTime) {
		totalTime += deltaTime;
		setVelocityY(9.82F * totalTime);
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
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public void setVelocityX(float velocityX) {
		this.velocity.x = Math.min(velocityX, defaultVelocity.x);
	}
	
	public void setVelocityY(float velocityY) {
		this.velocity.y = Math.min(velocityY, defaultVelocity.y);
	}
	
	public void setDefaultVelocity(Direction dir) {
		setVelocityX(getVelocity().x + dir.deltaX * defaultVelocity.x);
		setVelocityY(getVelocity().y + dir.deltaY * defaultVelocity.y);
	}
	
	public boolean move(Vector2f distance, BlockLayer blockLayer) {
		float[] previousPosition = { getX(), getY() };
		boolean collision = false;

		setX(getX() + distance.x);
		if (getVelocity().x > 0) {
			if (collisionRight(this, blockLayer)) {
				setX(previousPosition[0]);
				collision = true;
			}
		} else {
			if (collisionLeft(this, blockLayer)) {
				setX(previousPosition[0]);
				collision = true;
			}
		}

		setY(getY() + getVelocity().y);
		if(collisionBelow(this, blockLayer)) {
			setY(getY() - Math.abs(getY() - ((int)getY()/blockLayer.getBlockHeight())
														* blockLayer.getBlockHeight()));
			collision = true;
		} else {
			setY(getY() + getVelocity().y);
		}
		
		return !collision;
	}
	
	public void collision(float deltaTime, BlockLayer blockLayer) {


	}
	
}

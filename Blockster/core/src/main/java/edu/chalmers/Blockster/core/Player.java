package edu.chalmers.Blockster.core;

import javax.vecmath.*;

import edu.chalmers.Blockster.core.util.Direction;
import static edu.chalmers.Blockster.core.util.Calculations.*;

/**
 * The model representing a player in the game Blockster
 * @author Emilia and Eric Bjuhr
 *
 */
public class Player implements BlocksterObject{
	
	private float posX;
	private float posY;
	private float height;
	private float width;
	private AnimationState anim;
	private Vector2f velocity;
	private Vector2f defaultVelocity = new Vector2f(700, 700);
	private float totalTime = 0;
	private BlockLayer blockLayer;
	
	private InteractionState state = InteractionState.NONE;
	
	public Player(float startX, float startY, BlockLayer blockLayer) {
		this.posX = startX;
		this.posY = startY;
		this.blockLayer = blockLayer;
		anim = AnimationState.NONE;
		velocity = new Vector2f(0, 0);
	}
	
	public void interact(Direction dir) {
		if (isInteracting()) {
			state.getInteraction().interact(dir);
		} else {
			setDefaultVelocity(dir);
		}
	}
	
	public void endInteraction() {
		state.getInteraction().endInteraction();
		state = InteractionState.NONE;
	}

	public AnimationState getAnimationState() {
		return anim;
	}
	
	public float getHeight() {
		return height;
	}
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public float getWidth() {
		return width;
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
	
	public void increaseGravity(float deltaTime) {
		totalTime += deltaTime;
		setVelocityY(-9.82F * totalTime * blockLayer.getBlockHeight());
	}
	
	public boolean isInteracting() {
		return (state != InteractionState.NONE);
	}
	
	public boolean move(Vector2f distance) {
		float[] previousPosition = { getX(), getY() };
		boolean collision = false;

		setX(getX() + distance.x);
		if (collisionEitherCorner(this, blockLayer)) {
			setX(previousPosition[0]);
			collision = true;
		}

		setY(getY() + distance.y);
		if (collisionEitherCorner(this, blockLayer)) {
			setY(previousPosition[1]);
			if (distance.y < 0) {
				setY(((int)getY()/blockLayer.getBlockHeight())
							* blockLayer.getBlockHeight());
			}
			collision = true;
		}
		
		return !collision;
	}
	
	public void moveToNextPosition() {
		posX += anim.getMovement().getDirection().deltaX 
					* blockLayer.getBlockWidth();
		posY += anim.getMovement().getDirection().deltaY 
					* blockLayer.getBlockHeight();
	}
	
	public void resetGravity() {
		totalTime = 0;
	}
	
	public void setAnimationState(AnimationState anim) {
		this.anim = anim;
	}
	
	public void setDefaultVelocity(Direction dir) {
		setVelocityX(getVelocity().x + dir.deltaX * defaultVelocity.x);
		setVelocityY(getVelocity().y + dir.deltaY * defaultVelocity.y);
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setVelocityX(float velocityX) {
		if (Math.abs(velocityX) > Math.abs(defaultVelocity.x)) {
			velocity.x = Math.signum(velocityX) * defaultVelocity.x;
		} else {
			velocity.x = velocityX;
		}
	}
	
	public void setVelocityY(float velocityY) {
		if (Math.abs(velocityY) > Math.abs(defaultVelocity.y)) {
			velocity.y = Math.signum(velocityY) * defaultVelocity.y;
		} else {
			velocity.y = velocityY;
		}
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public void setX(float x) {
		posX = x;
	}

	public void setY(float y) {
		posY = y;
	}

	public boolean updatePosition(float deltaTime) {
		if (anim != AnimationState.NONE) {
			anim.updatePosition(deltaTime);
			return false;
		} else {
			Vector2f distance = new Vector2f();
			distance.x = velocity.x * deltaTime;
			distance.y = velocity.y * deltaTime;
			
			return move(distance);
		}
	}
	
}

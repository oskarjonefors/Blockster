package edu.chalmers.Blockster.core.objects;

import javax.vecmath.Vector2f;

import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.objects.movement.Direction;

public abstract class BlocksterObject extends ScaledObject {
	private float totalTime = 0;
	private Vector2f velocity;
	protected Vector2f defaultVelocity;
	private Direction dir = Direction.NONE;
	protected AnimationState anim;
	protected BlockMap blockMap;

	public BlocksterObject(float startX, float startY, BlockMap blockMap, float scaleX, float scaleY) {
		super(startX, startY, scaleX, scaleY);
		this.blockMap = blockMap;
		anim = AnimationState.NONE;
		velocity = new Vector2f(0, 0);
	}
	
	public Block getAdjacentBlock() {
		Block block = null;

		try {
			if (dir == Direction.LEFT) {
				block = blockMap.getBlock(
						(int) (getX() / scaleX) - 1,
						(int) ((2 * getY() + getHeight()) / 2 / scaleY));
				
			}

			if (dir == Direction.RIGHT) {
				block = blockMap.getBlock(
						(int) ((getX() + getWidth()) / scaleX) + 1,
						(int) ((2 * getY() + getHeight()) / 2 / scaleY));

			}
		} catch (NullPointerException e) {
			block = null;
		}
		return block;
	}
	
	public BlockMap getBlockLayer(){
		return blockMap;
	}
	
	public float getOriginX() {
		return x;
	}
	
	public float getOriginY() {
		return y;
	}
	
	public float getX() {
		//If there is an animation currently running then
		//we want to return the relative position
		if ( anim != AnimationState.NONE) {
			return x + anim.getRelativePosition().x * scaleX;
		}
		return x;
	}
	
	
	public float getY() {
		if (anim != AnimationState.NONE){
			return y + anim.getRelativePosition().y * scaleY;
		}
		return y;
	}
	
	public void moveToNextPosition(){
		x += anim.getMovement().getDirection().deltaX * scaleX;
		y += anim.getMovement().getDirection().deltaY * scaleY;
		
		System.out.println("Moved "+this+" ("+anim.getMovement().getDirection()
				+") (" + anim.getMovement() + ") (" + anim + ")");
	}
	
	public AnimationState getAnimationState() {
		return anim;
	}
	
	public void setAnimationState(AnimationState anim) {
		if (this.anim.isDone()) {
			System.out.println("Giving animation state "+anim+" to "+this);
			this.anim = anim;
		}
	}
	
	public void setDirection(Direction dir){
		this.dir = dir;
	}
	
	public Direction getDirection(){
		return dir;
	}	
	
	public Vector2f getVelocity() {
		return velocity;
	}
	
	public void setVelocityX(float velocityX) {
		if(Math.abs(velocityX) > defaultVelocity.x) {
			velocity.x = Math.signum(velocityX) * defaultVelocity.x;
		} else {
			velocity.x = velocityX;
		}
	}
	
	public void setVelocityY(float velocityY) {
		if(Math.abs(velocityY) > defaultVelocity.y) {
			velocity.y = Math.signum(velocityY) * defaultVelocity.y;
		} else {
			velocity.y = velocityY;
		}
	}
	
	public void setDefaultVelocity(Direction dir) {
		setVelocityX(getVelocity().x + dir.deltaX * defaultVelocity.x);
		setVelocityY(getVelocity().y + dir.deltaY * defaultVelocity.y);
	}
	
	public void increaseGravity(float deltaTime) {
		totalTime += deltaTime;
		setVelocityY(-9.82F * totalTime * getBlockLayer().getBlockHeight());
	}
	
	public void resetGravity() {
		totalTime = 0;
	}
	
	@Override
	public String toString() {
		return (this.getClass().getSimpleName()+": (" 
							+ Math.round(getX() * 10) / (10 * scaleX)  + ", " 
							+ Math.round(getY() * 10) / (10 * scaleY) + ")");
	}
	
	public void removeFromGrid() {
		blockMap.setBlock((int) getX(), (int) getY(), null);
	}
	
}

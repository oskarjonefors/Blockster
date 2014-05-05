package edu.chalmers.Blockster.core;

import edu.chalmers.Blockster.core.util.AnimationState;

public abstract class BlocksterObject {
	private float posX;
	private float posY;
	private float height;
	private float width;
	private AnimationState anim;
	private BlockMap blockMap;

	public BlocksterObject(float startX, float startY, BlockMap blockMap) {
		this.posX = startX;
		this.posY = startY;
		this.blockMap = blockMap;
		anim = AnimationState.NONE;
	}
	
	public BlockMap getBlockLayer(){
		return blockMap;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getWidth() {
		return width;
	}
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getX() {
		//If there is an animation currently running then
		//we want to return the relative position
		if ( anim != AnimationState.NONE) {
			return posX + anim.getRelativePosition().x;
		}
		return posX;
	}
	
	
	public float getY() {
		if (anim != AnimationState.NONE){
			return posY + anim.getRelativePosition().y;
		}
		return posY;
	}
	
	public void setX(float posX) {
		this.posX = posX;
	}
	
	public void setY(float posY) {
		this.posY = posY;
	}
	public void moveToNextPosition(){
		posX += anim.getMovement().getDirection().deltaX 
				* blockMap.getBlockWidth();
		
		posY += anim.getMovement().getDirection().deltaY 
				* blockMap.getBlockHeight();
	}
	
	public AnimationState getAnimationState() {
		return anim;
	}
	
	public void setAnimationState(AnimationState anim) {
		this.anim = anim;
	}
	
}

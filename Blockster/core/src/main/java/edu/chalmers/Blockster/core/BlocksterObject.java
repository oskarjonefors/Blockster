package edu.chalmers.Blockster.core;

public abstract class BlocksterObject {
	private float posX;
	private float posY;
	private float height;
	private float width;
	private AnimationState anim;
	private BlockLayer blockLayer;
	
	public BlocksterObject(float startX, float startY, BlockLayer blockLayer) {
		this.posX = posX;
		this.posY = posY;
		this.blockLayer = blockLayer;
		anim = AnimationState.NONE;
	}
	
	public BlockLayer getBlockLayer(){
		return blockLayer;
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
				* blockLayer.getBlockWidth();
		
		posY += anim.getMovement().getDirection().deltaY 
				* blockLayer.getBlockHeight();
	}
	
	public AnimationState getAnimationState() {
		return anim;
	}
	
	public void setAnimationState(AnimationState anim) {
		this.anim = anim;
	}
	
}

package edu.chalmers.Blockster.core;

public class BlocksterObject {
	private float posX;
	private float posY;
	private float height;
	private float width;
	private AnimationState anim;
	private BlockLayer blockLayer;
	
	public BlocksterObject(float startX, float startY, BlockLayer blockLayer) {
		
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
		return posX;
	}
	
	public float getY() {
		return posY;
	}
	
	public void setX(float posX) {
		this.posX = posX;
	}
	
	public void setY(float posY) {
		this.posY = posY;
	}
	
	public AnimationState getAnimationState() {
		return anim;
	}
	
	public void setAnimationState(AnimationState anim) {
		this.anim = anim;
	}
	
}

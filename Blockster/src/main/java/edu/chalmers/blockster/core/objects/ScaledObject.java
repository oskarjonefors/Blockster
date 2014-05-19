package edu.chalmers.blockster.core.objects;

import edu.chalmers.blockster.core.util.PhysicalObject;

public class ScaledObject implements PhysicalObject {
	

	private float x;
	private float y;
	protected float height;
	protected float width;
	private float scaleX;
	private float scaleY;
	
	public ScaledObject(float x, float y, float scaleX, float scaleY) {
		this.x = x * scaleX;
		this.y = y * scaleY;
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}

	@Override
	public float getX() {
		return x;
	}
	
	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getScaleX() {
		return scaleX;
	}

	@Override
	public float getScaleY() {
		return scaleY;
	}

	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	public void setX(float posX) {
		this.x = posX;
	}
	
	public void setY(float posY) {
		this.y = posY;
	}
	
	
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	public void setWidth(float width) {
		this.width = width;
	}
	
}

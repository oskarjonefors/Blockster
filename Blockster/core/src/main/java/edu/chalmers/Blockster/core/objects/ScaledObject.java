package edu.chalmers.Blockster.core.objects;

import edu.chalmers.Blockster.core.util.PhysicalObject;

public class ScaledObject implements PhysicalObject {
	

	protected float x;
	protected float y;
	protected float height;
	protected float width;
	protected float scaleX;
	protected float scaleY;
	
	public ScaledObject(float x, float y, float scaleX, float scaleY) {
		this.x = x;
		this.y = y;
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
		return 0;
	}

	@Override
	public float getScaleY() {
		return 0;
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

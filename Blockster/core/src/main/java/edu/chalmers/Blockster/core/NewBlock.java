package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMapTile;

import edu.chalmers.Blockster.core.util.Direction;

public class NewBlock implements BlocksterObject{

	private float posX, posY, height, width;
	private TiledMapTile tile;
	private AnimationState animState;
	private boolean animationActive;
	
	
	public NewBlock(TiledMapTile tile, float x, float y){
		this.tile = tile;
		posX= x;
		posY = y;
		
	}
	/**
	 * Moves the block according to the player´s position
	 * @param playerX
	 * @param playerY
	 */
	public void moveLiftedBlock(float playerX, float playerY){
			posX = playerX;
			posY = playerY + tile.getTextureRegion().getRegionHeight();
		}
	
	public void moveBlock(Direction dir, float playerX, Float playerY){
		posX = playerX + dir.deltaX * tile.getTextureRegion().getRegionWidth();
		posY = playerY;
	}
		  
	public void SetAnimation(AnimationState animState){
		this.animState = animState;
	}
	
	public void runAnimation(Direction dir){
		animationActive = true;
		 
		while(animationActive){
			posX = animState.getRelativePosition().x;
			posY = animState.getRelativePosition().y;
			
			if(animState.isDone()) {
				animationActive = false;
			}
		}
		
	}
	public void setHeight(float height){
		this.height = height;
	}
	
	public void setWidth(float width){
		this.width = width;
	}
	
	@Override
	public float getHeight() {
		return height;
	}
	
	@Override
	public float getWidth() {
		return width;
	}
	
	@Override
	public float getX() {
		return posX;
	}
	
	@Override
	public float getY() {
		return posY;
	}
	
}


package edu.chalmers.Blockster.core;

import static edu.chalmers.Blockster.core.Direction.*;
import com.badlogic.gdx.maps.tiled.TiledMap;

/**
 * A class to represent a stage.
 * @author Oskar Jönefors
 * 
 */
public class Stage {
	private TiledMap map;
	private Block processedBlock;
	
	private boolean isGrabbingBlockAnimation = false; //for animations
	private boolean isMovingBlockAnimation = false;
	private boolean isLiftingBlockAnimation = false;
	
	private boolean isGrabbingBlock = false; 
	private boolean isLiftingBlock = false;
	

	public Stage(TiledMap map) {
		this.map = map;
	}
	
	public boolean canGrabBlock(Block block) {
		//TODO: Add additional tests (type of block etc)
		return block != null && !isGrabbingBlockAnimation && 
				!isLiftingBlockAnimation && !isMovingBlockAnimation;
	}
	
	public boolean canLiftBlock(Block block) {
		//TODO: Add additional tests
		return block != null && isGrabbingBlock && !isGrabbingBlockAnimation &&
				!isLiftingBlockAnimation && !isMovingBlockAnimation;
	}
	
	public Block getAdjacentBlock(Direction dir) {
		if (dir != NONE) {
			//TODO: Get adjacent block from map
		}
		return null;
	}
	
	public TiledMap getMap() {
		return map;
	}
	
	public Block getProcessedBlock() {
		return processedBlock;
	}
	
	public void grabBlock(Block block) {
		
		if (canGrabBlock(block)) {
			
			processedBlock = block;
			isGrabbingBlockAnimation = true;
			isGrabbingBlock = true;
			//TODO: Start grab animation
		}
	}
	
	public boolean isGrabbingBlock() {
		return isGrabbingBlock;
	}
	
	public boolean isLiftingBlock() {
		return isLiftingBlock;
	}
	
	public void liftBlock() {
		if (canLiftBlock(processedBlock)) {
			//If we are not already lifting a block, do so.
			isLiftingBlockAnimation = true;
			isLiftingBlock = true;
			isGrabbingBlock = false;
			//TODO: Start lift animation
		}
	}
	
	public void nextPlayer() {
		
	}
	
	public void moveBlock(Direction dir) {
		if (processedBlock != null && !isMovingBlockAnimation 
				&& !isLiftingBlockAnimation && !isGrabbingBlockAnimation) {
			isMovingBlockAnimation = true;
			//TODO: set character position, move block in grid, animation, etc
		}
	}
	
	public void moveCharacter(Direction dir, float distance) {
		//TODO Move the active playable character
		//Note: make sure to check if there is a collision beneath the character
	}
	
	public void stopProcessingBlock() {
		//TODO put down block animation, etc
		processedBlock = null;
	}
	
	public void update(float deltaTime) {
		//Set animation state etc
		
		if (isGrabbingBlockAnimation) {
			//Set the block grabbing animation timer to t+deltaTime
		}
		
		if (isMovingBlockAnimation) {
			//Set the block moving animation timer to t+deltaTime
		}
		
		if (isLiftingBlockAnimation) {
			//Set the block lifting animation timer to t+deltaTime
		}
		
	}
}

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
	private boolean isGrabbingBlock = false; //for animations
	private boolean isMovingBlock = false;
	private boolean isLiftingBlock = false;
	
	public Stage(TiledMap map) {
		this.map = map;
	}
	
	private boolean canGrabBlock(Block block) {
		//TODO: Add tests
		return true;
	}
	
	private boolean canLiftBlock(Block block) {
		//TODO: Add tests
		return true;
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
		
		if (!isGrabbingBlock && !isLiftingBlock && 
				!isMovingBlock && canGrabBlock(block)) {
			
			processedBlock = block;
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
		if (isGrabbingBlock && !isLiftingBlock && 
				!isMovingBlock && canLiftBlock(processedBlock)) {
			//If we are not already lifting a block, do so.
			isLiftingBlock = true;
			isGrabbingBlock = false;
			//TODO: Start lift animation
		}
	}
	
	public void moveBlock(Direction dir) {
		if (processedBlock != null && !isMovingBlock 
				&& !isLiftingBlock && !isGrabbingBlock) {
			isMovingBlock = true;
			//TODO
		}
	}
	
	public void moveCharacter(Direction dir, float distance) {
		//TODO
	}
	
	public void stopGrabbingBlock() {
		//TODO
	}
	
	public void update(float deltaTime) {
		//Set animation state etc
		
		if (isMovingBlock) {
			
		}
		
		if (isLiftingBlock) {
			
		}
		
	}
}

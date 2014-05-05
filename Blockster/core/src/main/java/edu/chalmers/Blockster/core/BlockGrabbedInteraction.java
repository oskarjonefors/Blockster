package edu.chalmers.Blockster.core;

import java.util.ArrayList;
import java.util.List;

import edu.chalmers.Blockster.core.util.AnimationState;
import edu.chalmers.Blockster.core.util.Direction;
import edu.chalmers.Blockster.core.util.Movement;

public class BlockGrabbedInteraction implements PlayerInteraction {
	
	private Block activeBlock; 
	private BlockMap blockLayer;
	private Player player;
	
	public BlockGrabbedInteraction(Player player, 
			Block activeBlock, BlockMap blockLayer) {
		this.activeBlock = activeBlock;
		this.blockLayer = blockLayer;
		this.player = player;
	}
	
	@Override
	public void interact(Direction dir) {

		float relativePosition = activeBlock.getX() 
				- player.getX() / blockLayer.getBlockWidth();
		Movement movement = Movement.getPushPullMovement(dir, relativePosition);
		List<Block> moveableBlocks;
		
		if (movement.isPullMovement()) {
			if (player.canMove(movement)) {
				activeBlock.setAnimationState(new AnimationState(movement));
				player.setAnimationState(new AnimationState(movement));
			}
		} else {
			moveableBlocks = getMoveableBlocks(dir);
			for (Block block : moveableBlocks) {
				block.setAnimationState(new AnimationState(movement));
			}
			
			if (!moveableBlocks.isEmpty()) {
				player.setAnimationState(new AnimationState(movement));
			}
		}

		
	}
	
	public List<Block> getMoveableBlocks(Direction dir) {
		/* Create a list to put the block to be moved in. */
		List<Block> movingBlocks = new ArrayList<Block>();

		/* Origin of add process */
		int origX = (int) activeBlock.getX();
		int origY = (int) activeBlock.getY();
		
		if (origY >= blockLayer.getHeight()) {
			return movingBlocks;
		}

		/* We've already established that the move is okay,
			so we don't need to change the Y coordinate. */
		int checkX = (origX);

		/* Loop to add all the blocks to be moved to the list. */
		while (blockLayer.hasBlock(checkX, origY) && checkX > 0 
				&& checkX < blockLayer.getWidth()) {
			if(!blockLayer.hasBlock(checkX, origY + 1) && blockLayer.
					getBlock(checkX, origY).isMovable()) {
				movingBlocks.add(blockLayer.getBlock(checkX, origY));
				checkX += dir.deltaX;
			} else {
				movingBlocks.clear();
				break;
			}
		}
		
		return movingBlocks;
	}

	@Override
	public void endInteraction() {

	}
	


}

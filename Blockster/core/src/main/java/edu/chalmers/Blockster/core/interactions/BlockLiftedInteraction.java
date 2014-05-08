package edu.chalmers.Blockster.core.interactions;

import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.Player;
import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.objects.movement.Direction;
import edu.chalmers.Blockster.core.objects.movement.Movement;
import edu.chalmers.Blockster.core.util.Calculations;

public class BlockLiftedInteraction extends PlayerInteraction {
	
	private Player player;
	private Block block;
	private BlockMap blockMap;
	
	public BlockLiftedInteraction(Player player, Block block,
			BlockMap blockMap) {
		this.player = player;
		this.block = block;
		this.blockMap = blockMap; 
	}

	@Override
	public void interact(Direction dir) {
		if(canPerformMove(dir)) {
			AnimationState anim = new AnimationState(Movement.getMoveMovement(dir));
			player.setAnimationState(anim);
			block.setAnimationState(anim);
		}
	}

	@Override
	public void endInteraction() {
		player.setLifting(false);
	}

	@Override
	public void startInteraction() {
		block.setAnimationState(new AnimationState(Movement.getLiftMovement
				(Direction.getDirection(player.getX(),
						block.getX() * blockMap.getBlockWidth()))));
		player.setLifting(true);
	}
	
	public boolean canPerformMove(Direction dir) {
		int flag = 0;
		if (dir == Direction.LEFT) {
			flag = Calculations.CHECK_LEFT_FLAG | Calculations.CHECK_DOWN_LEFT_FLAG;
		} else {
			flag = Calculations.CHECK_RIGHT_FLAG | Calculations.CHECK_DOWN_RIGHT_FLAG;
		}
		return !Calculations.collisionSurroundingBlocks(block, blockMap, flag);
	}

}

package edu.chalmers.Blockster.core.interactions;

import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.Player;
import edu.chalmers.Blockster.core.util.AnimationState;
import edu.chalmers.Blockster.core.util.Direction;
import edu.chalmers.Blockster.core.util.Movement;

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

	}

	@Override
	public void endInteraction() {
	}

	@Override
	public void startInteraction() {
		block.setAnimationState(anim);
	}

}

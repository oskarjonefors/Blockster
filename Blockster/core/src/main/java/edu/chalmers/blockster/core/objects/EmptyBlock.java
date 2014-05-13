package edu.chalmers.blockster.core.objects;

import edu.chalmers.blockster.core.objects.movement.AnimationState;

public class EmptyBlock extends Block {
	
	public EmptyBlock(int x, int y, BlockMap blockMap) {
		super(x, y, blockMap);
	}
	
	@Override
	public void setAnimationState(AnimationState state) {
		//DO NOTHING!!
	}
}
package edu.chalmers.blockster.core.objects;

import edu.chalmers.blockster.core.objects.movement.AnimationState;

public class EmptyBlock extends Block {
	private static volatile EmptyBlock instance = null;
	
	private EmptyBlock() {
		super(0, 0, null);
	}
	
	public static EmptyBlock getInstance() {
		if(instance == null) {
			synchronized (EmptyBlock.class) {
				instance = new EmptyBlock();
			}
		}
		return instance;
	}

	@Override
	public void removeFromGrid() {
		// DO NOTHING!
	}
	
	@Override
	public void setAnimationState(AnimationState state) {
		//DO NOTHING!!
	}
}
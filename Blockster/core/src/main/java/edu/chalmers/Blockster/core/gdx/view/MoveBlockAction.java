package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import edu.chalmers.Blockster.core.Block;

public class MoveBlockAction extends MoveByAction {
	
	private final Block block;
	
	public MoveBlockAction(float x, float y, float duration, Block block) {
		super();
		this.block = block;
		setAmount(x,y);
		setDuration(duration);
		
	}
	
	public Block getBlock() {
		return block;
	}

}

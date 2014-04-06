package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.util.Direction;

public class MoveBlockAction extends MoveByAction {
	
	private final Direction dir;
	private final BlockMap map;
	private final Model model;
	private GdxBlock block;
	
	public MoveBlockAction(Direction dir, float duration, BlockMap map, Model model) {
		super();
		this.dir = dir;
		this.map = map;
		this.model = model;
		setAmount(dir.deltaX, dir.deltaY);
		System.out.println("Direction:" + dir.deltaX + " " + dir.deltaY);
		setDuration(duration);
	}
	
	public Block getBlock() {
		return block;
	}
	
	public boolean act(float delta) {
		if(getActor() != null) {
			block = (GdxBlock)getActor();
		}
		boolean done = super.act(delta);
		
		if (done) {
			model.getActiveBlocks().remove(block);
			//if(!model.getLiftedBlocks().containsValue(block)) {
				getActor().remove();
				getActor().removeAction(this);
				((GdxBlockLayer)map.getBlockLayer()).insertBlock(block);
			//}
		}
		return done;
	}
}

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
	
	public MoveBlockAction(Direction dir, float duration, BlockMap map, Model model) {
		super();
		this.dir = dir;
		this.map = map;
		this.model = model;
		setAmount(dir.deltaX, dir.deltaY);
		setDuration(duration);
		
	}
	
	public Block getBlock() {
		return ((GdxBlockActor) getActor()).getBlock();
	}
	
	public boolean act(float delta) {
		boolean done = super.act(delta);
		
		if (done) {
			GdxBlock block = (GdxBlock) getBlock();
			Actor actor = getActor();
			model.getActiveBlocks().remove(block);
			actor.removeAction(this);
			actor.remove();
			
			block.setX((int) Math.round(block.getX() + dir.deltaX));
			block.setY((int) Math.round(block.getY() + dir.deltaY));
			
			((GdxBlockLayer)map.getBlockLayer()).insertBlock(block);
		}
		
		return done;
	}

}

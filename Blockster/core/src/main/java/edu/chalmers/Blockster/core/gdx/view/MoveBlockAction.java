package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.util.Calculations;
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
		GdxBlockLayer layer = (GdxBlockLayer)map.getBlockLayer();
		
		if (done) {
			
			/* If there is a block below the new position, insert the block
			 * into the map.
			 */
			if (layer.hasBlock((int)block.getX(), (int)(block.getY() - 1)) ||
					model.isLiftingBlock()) {
				model.getActiveBlocks().remove(block);
				getActor().remove();
				getActor().removeAction(this);
				layer.insertBlock(block);
				
			/* If not, let it fall */
			} else {
				getActor().addAction(new MoveBlockAction(Direction.DOWN, 
						Calculations.STANDARD_MOVE_DURATION, map, model));
			}
		}
		return done;
	}
}

package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Model;

public class MoveBlockPlayerAction extends MoveByAction {
	
	private final Model model;
	
	public MoveBlockPlayerAction(Vector2 distance, float duration, Model model) {
		super();
		this.model = model;
		
		Gdx.app.log("MoveBlockPlayerAction", "Moving player by distance: ("
							+distance.x +", "+distance.y+")");
		setAmount(distance.x, distance.y);
		setDuration(duration);
	}
	
	public boolean act(float delta) {
		boolean done = super.act(delta);
		
		
		if (done) {
			
			getActor().removeAction(this);
			
		}
		
		return done;
	}

}

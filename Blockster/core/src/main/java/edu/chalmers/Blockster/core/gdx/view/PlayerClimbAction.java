package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;

public class PlayerClimbAction extends MoveByAction{
	private float duration;
	private Vector2 distance;
	
	public PlayerClimbAction(Vector2 distance, float duration){
		this.distance = distance;
		this.duration = duration;
		
		setAmount(distance.x, distance.y);
		setDuration(duration);
	}
	
	public boolean act(float delta){
		Boolean isDone = super.act(delta);
		
		if(isDone){
			getActor().removeAction(this);
		}
		return isDone;
	}
	
}

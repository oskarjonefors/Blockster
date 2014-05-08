package edu.chalmers.Blockster.core.objects.interactions;

import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.objects.movement.Direction;
import edu.chalmers.Blockster.core.util.PhysicalObject;

public interface Interactor extends PhysicalObject {

	public void setAnimationState(AnimationState anim);
	
	public void setLifting(boolean b);

	public boolean canMove(Direction dir);

	public void setGrabbing(boolean b);
	
}

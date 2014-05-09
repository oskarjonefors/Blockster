package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.util.PhysicalObject;

public interface Interactor extends PhysicalObject {

	public void setAnimationState(AnimationState anim);
	
	public void setLifting(boolean b);

	public boolean canMove(Direction dir);

	public void setGrabbing(boolean b);
	
}

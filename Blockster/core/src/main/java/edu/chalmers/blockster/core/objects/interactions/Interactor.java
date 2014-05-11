package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.util.PhysicalObject;

public interface Interactor extends PhysicalObject {
	
	Direction getDirection();
	
	AnimationState getAnimationState();

	boolean canMove(Direction dir);
	
	void setAnimationState(AnimationState anim);
	
	void setLifting(boolean b);

	void setGrabbing(boolean b);
	
}

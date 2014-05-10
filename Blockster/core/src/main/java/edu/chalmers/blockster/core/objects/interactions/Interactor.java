package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.util.PhysicalObject;

public interface Interactor extends PhysicalObject {
	
	Direction getDirection();
	
	AnimationState getAnimationState();

	void setAnimationState(AnimationState anim);
	
	void setClimbing(boolean b);
	
	void setLifting(boolean b);

	boolean canMove(Direction dir);

	void setGrabbing(boolean b);
	
}

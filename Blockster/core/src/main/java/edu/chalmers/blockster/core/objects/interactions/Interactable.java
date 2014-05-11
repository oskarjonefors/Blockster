package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.util.GridObject;
import edu.chalmers.blockster.core.util.PhysicalObject;

public interface Interactable extends PhysicalObject, GridObject {

	void setAnimationState(AnimationState anim);

	void removeFromGrid();
	
	void setLifted(boolean lifted);
	
	boolean canMove(Direction dir);
	
	boolean hasWeight();
	
	boolean isLifted();
	
}

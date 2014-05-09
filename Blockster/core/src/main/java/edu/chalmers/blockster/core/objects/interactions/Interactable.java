package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.util.GridObject;
import edu.chalmers.blockster.core.util.PhysicalObject;

public interface Interactable extends PhysicalObject, GridObject {

	public void setAnimationState(AnimationState anim);

	public void removeFromGrid();
	
}

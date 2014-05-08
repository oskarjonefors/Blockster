package edu.chalmers.Blockster.core.objects.interactions;

import edu.chalmers.Blockster.core.objects.movement.AnimationState;
import edu.chalmers.Blockster.core.util.GridObject;
import edu.chalmers.Blockster.core.util.PhysicalObject;

public interface Interactable extends PhysicalObject, GridObject {

	public void setAnimationState(AnimationState anim);

	public void removeFromGrid();
	
}

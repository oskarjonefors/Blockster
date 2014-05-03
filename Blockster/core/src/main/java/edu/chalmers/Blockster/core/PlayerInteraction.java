package edu.chalmers.Blockster.core;

import edu.chalmers.Blockster.core.util.Direction;

public interface PlayerInteraction {
	
	public void interact(Direction dir);

	public void endInteraction();
	
}

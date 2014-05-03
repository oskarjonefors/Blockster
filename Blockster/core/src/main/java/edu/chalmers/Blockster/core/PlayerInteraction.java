package edu.chalmers.Blockster.core;

import edu.chalmers.Blockster.core.util.Direction;

public interface PlayerInteraction {
	
	public boolean attemptInteraction(Direction dir);

	public void endInteraction();
	
}

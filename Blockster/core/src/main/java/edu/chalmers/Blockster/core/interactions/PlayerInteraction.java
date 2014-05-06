package edu.chalmers.Blockster.core.interactions;

import edu.chalmers.Blockster.core.util.Direction;

public abstract class PlayerInteraction {

	public final static PlayerInteraction NONE = new NoInteraction();
	
	private static class NoInteraction extends PlayerInteraction{

		@Override
		public void interact(Direction dir) {
			
		}

		@Override
		public void endInteraction() {
			
		}
		
	}
	
	public abstract void interact(Direction dir);

	public abstract void endInteraction();
	
}

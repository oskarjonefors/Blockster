package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.Direction;

public abstract class PlayerInteraction {

	public final static PlayerInteraction NONE = new NoInteraction();
	
	private static class NoInteraction extends PlayerInteraction{

		@Override
		public void interact(Direction dir) {
			
		}

		@Override
		public void endInteraction() {
			
		}

		@Override
		public void startInteraction() {
			// TODO Auto-generated method stub
			
		}
	}
	
	public abstract void interact(Direction dir);

	public abstract void endInteraction();
	
	public abstract void startInteraction();
}

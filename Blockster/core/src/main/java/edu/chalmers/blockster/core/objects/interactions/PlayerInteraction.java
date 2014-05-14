package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.Direction;

public abstract class PlayerInteraction {

	public static final PlayerInteraction NONE = new NoInteraction();
	
	private static class NoInteraction extends PlayerInteraction{

		@Override
		public void interact(Direction dir) {
			/* Does nothing */
		}

		@Override
		public void endInteraction() {
			/* Does nothing */
		}

		@Override
		public void startInteraction() {
			/* Does nothing */
		}
	}
	
	public abstract void interact(Direction dir);

	public abstract void endInteraction();
	
	public abstract void startInteraction();
}

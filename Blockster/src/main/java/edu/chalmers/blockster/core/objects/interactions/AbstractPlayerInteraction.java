package edu.chalmers.blockster.core.objects.interactions;

import edu.chalmers.blockster.core.objects.movement.Direction;

public abstract class AbstractPlayerInteraction {

	public static final AbstractPlayerInteraction NONE = new NoInteraction();
	private final Interactor interactor;
	private final Interactable interacted;
	
	
	public AbstractPlayerInteraction(Interactor interactor, Interactable interacted) {
		this.interactor = interactor;
		this.interacted = interacted;
	}
	
	private static class NoInteraction extends AbstractPlayerInteraction{

		public NoInteraction() {
			super(null, null);
		}
		
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
	
	public final Interactor getInteractor() {
		return interactor;
	}
	
	public final Interactable getInteracted() {
		return interacted;
	}
	
}

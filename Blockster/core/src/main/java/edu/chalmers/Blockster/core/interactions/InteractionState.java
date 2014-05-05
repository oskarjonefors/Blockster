package edu.chalmers.Blockster.core.interactions;

import edu.chalmers.Blockster.core.util.Direction;

public abstract class InteractionState {
	
	public final static InteractionState NONE = NoInteractionState.singleton;
	
	public abstract PlayerInteraction getInteraction();
	
	private static class NoInteractionState extends InteractionState {

		public final static NoInteractionState singleton = new NoInteractionState();
		private final static PlayerInteraction noInteraction = new NoInteraction();;
		
		private NoInteractionState() {
			
		}
		
		@Override
		public PlayerInteraction getInteraction() {
			return noInteraction;
		}
		
		private static class NoInteraction implements PlayerInteraction {

			@Override
			public void interact(Direction dir) {
				
			}

			@Override
			public void endInteraction() {
				
			}
			
		}
	}

}

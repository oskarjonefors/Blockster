package edu.chalmers.blockster.core.objects;

/**
 * An interface for a Listener for active blocks.
 * @author Oskar Jönefors
 *
 */

public interface ActiveBlockListener {
	void blockActivated(Block block);
	void blockDeactivated(Block block);
}

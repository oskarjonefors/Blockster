package edu.chalmers.Blockster.core.gdx.controller;

import static edu.chalmers.Blockster.core.util.Direction.*;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.MapChangeListener;
import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * Class to handle input and updating the model.
 * @author Eric Bjuhr, Oskar Jönefors
 *
 */
public class Controller extends InputAdapter implements Disposable {

	private volatile int keyFlags = 0;

	private final static int LEFT_BUTTON_DOWN_FLAG = 1 << 0;
	private final static int RIGHT_BUTTON_DOWN_FLAG = 1 << 1;
	private final static int GRAB_BUTTON_DOWN_FLAG = 1 << 2;
	private final static int GRAB_BUTTON_UP_FLAG = 1 << 3;
	private final static int MENU_BUTTON_UP_FLAG = 1 << 4;
	private final static int SWITCH_CHARACTER_BUTTON_UP_FLAG = 1 << 5;
	private final static int RESTART_STAGE_BUTTON_R_FLAG = 1 << 6;
	private final static int LEFT_BUTTON_UP_FLAG = 1 << 7;
	private final static int RIGHT_BUTTON_UP_FLAG = 1 << 8;

	private Model model;

	private Direction lastDirection = NONE;
	private boolean hasMovedBlock = false;

	private final ArrayList<MapChangeListener> stageListenerList = new ArrayList<MapChangeListener>();

	/**
	 * Creates a new controller for the Blockster application. The controller
	 * is an InputAdapter.
	 */
	public Controller() {
		init();
	}

	/**
	 * Adds a MapChangedListener
	 * @param sl
	 */
	public void addMapChangeListener(MapChangeListener sl) {
		stageListenerList.add(sl);
	}

	/**
	 * Removes the listener
	 */
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		Gdx.input.setInputProcessor(null);
	}

	/**
	 * Initiates the listener.
	 */
	private void init() {
		Gdx.input.setInputProcessor(this);
	}

	/**
	 * This method is called each time a key has been pressed
	 * @see com.badlogic.gdx.InputAdapter#keyDown(int)
	 */
	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Keys.LEFT) {
			/* Try to go left. If block is grabbed, try to push or pull it */

			//Override rightwards movement. Can only move one direction at a time
			keyFlags &= ~RIGHT_BUTTON_DOWN_FLAG; 
			keyFlags |= LEFT_BUTTON_DOWN_FLAG;
		}

		if (keyCode == Keys.RIGHT) {
			/* Try to go right. If block is grabbed, try to push or pull it */

			//Override leftwards movement. Can only move one direction at a time
			keyFlags &= ~LEFT_BUTTON_DOWN_FLAG; 
			keyFlags |= RIGHT_BUTTON_DOWN_FLAG;
		}

		if (keyCode == Keys.SPACE) {
			//Grab block
			keyFlags |= GRAB_BUTTON_DOWN_FLAG;
		}
		if (keyCode == Keys.R) {
			//Restart level
			keyFlags |= RESTART_STAGE_BUTTON_R_FLAG;
		}
		return false;
	}

	/**
	 * This method is called each time a key as been released.
	 * 
	 * @see com.badlogic.gdx.InputAdapter#keyUp(int)
	 */
	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Keys.LEFT) {
			//Stop going/pushing/pulling left.
			keyFlags &= ~LEFT_BUTTON_DOWN_FLAG;
			keyFlags |= LEFT_BUTTON_UP_FLAG;
		}

		if (keyCode == Keys.RIGHT) {
			//Stop going/pushing/pulling right.
			keyFlags &= ~RIGHT_BUTTON_DOWN_FLAG;
			keyFlags |= RIGHT_BUTTON_UP_FLAG;
		}

		if (keyCode == Keys.SPACE) {
			//If block is grabbed and no other keys are pushed down, lift the block.
			keyFlags &= ~GRAB_BUTTON_DOWN_FLAG; //This is how you set the flag to false
			keyFlags |= GRAB_BUTTON_UP_FLAG; //This is how you set the flag to true
		}

		if (keyCode == Keys.ESCAPE){
			//Level menu
			keyFlags |= MENU_BUTTON_UP_FLAG;
		}

		if (keyCode == Keys.SHIFT_LEFT || keyCode == Keys.SHIFT_RIGHT) {
			//Switch character
			keyFlags |= SWITCH_CHARACTER_BUTTON_UP_FLAG;
		}
		return false;
	}


	/**
	 * Tells each listener that the model has changed.
	 */
	public void setModel(Model model) {
		this.model = model;
		for (MapChangeListener sl : stageListenerList) {
			sl.stageChanged(model);
		}
	}

	/**
	 * Updates the game flow.
	 * @param deltaTime The time between the current frame and the last one.
	 */
	public void update(float deltaTime) {
		Block adjacentBlock = model.getActivePlayer().getAdjacentBlock(lastDirection);

		if ((keyFlags & GRAB_BUTTON_DOWN_FLAG) != 0) {
			//Try to grab the adjacent block if possible and there is one.
			model.getActivePlayer().grabBlock(adjacentBlock);
		} 

		if ((keyFlags & LEFT_BUTTON_DOWN_FLAG) != 0) {
			// Character is moving left

			lastDirection = LEFT;
			model.interactPlayer(lastDirection);
			model.getActivePlayer().setDirection(lastDirection);
		}
		
		if ((keyFlags & LEFT_BUTTON_UP_FLAG) != 0) {
			model.getActivePlayer().setDefaultVelocity(Direction.NONE);
			keyFlags &= ~LEFT_BUTTON_UP_FLAG;
		}
		
		if ((keyFlags & RIGHT_BUTTON_UP_FLAG) != 0) {
			model.getActivePlayer().setDefaultVelocity(Direction.NONE);
			keyFlags &= ~RIGHT_BUTTON_UP_FLAG;

		}

		if ((keyFlags & RIGHT_BUTTON_DOWN_FLAG) != 0) {
			// Character is moving right
			lastDirection = RIGHT;
			model.interactPlayer(lastDirection);
			model.getActivePlayer().setDirection(lastDirection);
		}

		if ((keyFlags & GRAB_BUTTON_UP_FLAG) != 0) {
			//Grab button was released
			if (!hasMovedBlock && !model.isLiftingBlock()) {
				model.getActivePlayer().liftBlock();
			} else {
				model.stopProcessingBlock();
				hasMovedBlock = false;
			}
			keyFlags &= ~GRAB_BUTTON_UP_FLAG;
			System.out.println("Removing flag: "+GRAB_BUTTON_UP_FLAG);
		}

		if ((keyFlags & MENU_BUTTON_UP_FLAG) != 0) {
			// Opening the level menu
			keyFlags &= ~MENU_BUTTON_UP_FLAG;
			System.out.println("Removing flag: "+MENU_BUTTON_UP_FLAG);
		}

		if ((keyFlags & SWITCH_CHARACTER_BUTTON_UP_FLAG) != 0) {
			// Switching active character
			keyFlags &= ~SWITCH_CHARACTER_BUTTON_UP_FLAG;
			
			model.nextPlayer();
		}
		
		if ((keyFlags & RESTART_STAGE_BUTTON_R_FLAG) != 0) {
			//Restart stage
			keyFlags &= ~RESTART_STAGE_BUTTON_R_FLAG;
			model.init();
			setModel(model);
		}

	}


}
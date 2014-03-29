package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

/**
 * Class to handle input and updating the model.
 * @author Oskar JÃ¶nefors
 *
 */
public class StageController extends InputAdapter implements Disposable {

	private volatile int keyFlags = 0;
	public final static int VELOCITY = 4;	
	
	private final static int LEFT_BUTTON_DOWN_FLAG = 1 << 0;
	private final static int RIGHT_BUTTON_DOWN_FLAG = 1 << 1;
	private final static int GRAB_BUTTON_DOWN_FLAG = 1 << 2;
	private final static int GRAB_BUTTON_UP_FLAG = 1 << 3;
	private final static int MENU_BUTTON_UP_FLAG = 1 << 4;
	private final static int SWITCH_CHARACTER_BUTTON_UP_FLAG = 1 << 5;
	
	public StageController() {
		
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
	private void init() {
		Gdx.input.setInputProcessor(this);
	}
	
	/**
	 * Updates the game flow.
	 * @param deltaTime The time between the current frame and the last one.
	 */
	public void update(float deltaTime) {
		float distanceMoved = deltaTime * VELOCITY;
		
		if ((keyFlags & LEFT_BUTTON_DOWN_FLAG) != 0) {
			// Character is moving left
			
		}
		if ((keyFlags & RIGHT_BUTTON_DOWN_FLAG) != 0) {
			// Character is moving right
			
		}
		if ((keyFlags & GRAB_BUTTON_DOWN_FLAG) != 0) {
			// Character is grabbing a block
			
		}
		if ((keyFlags & GRAB_BUTTON_UP_FLAG) != 0) {
			// Character is lifting a block
			
		}
		if ((keyFlags & MENU_BUTTON_UP_FLAG) != 0) {
			// Opening the level menu
			
		}
		if ((keyFlags & SWITCH_CHARACTER_BUTTON_UP_FLAG) != 0) {
			// Switching active character
			
		}
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Keys.LEFT) {
			/* Try to go left. If block is grabbed, try to push or pull it */
			
			keyFlags |= LEFT_BUTTON_DOWN_FLAG;
		}
		
		if (keyCode == Keys.RIGHT) {
			/* Try to go right. If block is grabbed, try to push or pull it */
			keyFlags |= RIGHT_BUTTON_DOWN_FLAG;
		}
		
		if (keyCode == Keys.SPACE) {
			//Grab block
			keyFlags |= GRAB_BUTTON_DOWN_FLAG;
		}
				
		return false;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Keys.LEFT) {
			//Stop going/pushing/pulling left.
			keyFlags &= ~LEFT_BUTTON_DOWN_FLAG;
		}
		
		if (keyCode == Keys.RIGHT) {
			//Stop going/pushing/pulling right.
			keyFlags &= ~RIGHT_BUTTON_DOWN_FLAG;
		}
		
		if (keyCode == Keys.SPACE) {
			//If block is grabbed and no other keys are pushed down, lift the block.
			keyFlags &= ~GRAB_BUTTON_DOWN_FLAG;
			
			//TODO: check if a block is grabbed
			if (keyFlags == 0) { //No keys are being pushed down
				keyFlags |= GRAB_BUTTON_UP_FLAG;
			}
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
}
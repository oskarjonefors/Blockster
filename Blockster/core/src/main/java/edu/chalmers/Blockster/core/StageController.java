package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.utils.Disposable;

/**
 * Class to handle input and updating the model.
 * @author Oskar Jönefors
 *
 */
public class StageController extends InputAdapter implements Disposable {

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
		
	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if (keyCode == Keys.LEFT) {
			/* Try to go left. If block is grabbed, try to push or pull it */
		}
		
		if (keyCode == Keys.RIGHT) {
			/* Try to go right. If block is grabbed, try to push or pull it */
		}
		
		if (keyCode == Keys.SPACE) {
			//Grab block
		}
				
		return false;
	}
	
	@Override
	public boolean keyUp(int keyCode) {
		if (keyCode == Keys.LEFT) {
			//Stop going/pushing/pulling left.
		}
		
		if (keyCode == Keys.RIGHT) {
			//Stop going/pushing/pulling right.
		}
		
		if (keyCode == Keys.SPACE) {
			//If block is grabbed and no other keys are pushed down, lift the block.
		}
		
		if (keyCode == Keys.ESCAPE){
			//Level menu
		}
		
		if (keyCode == Keys.SHIFT_LEFT || keyCode == Keys.SHIFT_RIGHT) {
			//Switch character
		}
		
		return false;
	}
}

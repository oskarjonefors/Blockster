package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Blockster extends Game implements ApplicationListener {
	
	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();

	//A libgdx helper class that logs current FPS each second
	private FPSLogger fpsLogger;
	private StageController controller;
	private StageView view;
	
	@Override
	public void create () {
		Gdx.app.log(Blockster.LOG, "Creating game");
		StageController controller = new StageController();
		StageView view = new StageView(controller);
	}

	@Override
	public void resize (int width, int height) {
		Gdx.app.log(Blockster.LOG, "Resizing game to: " + width + " x " + height);
	}

	@Override
	public void render () {
		/*Update the world controller with the time
			elapsed between the last two frames. */ 
		controller.update(Gdx.graphics.getDeltaTime());
		
		/* Clear screen */
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		/* Render the new frame */
		view.render();
	}

	@Override
	public void pause () {
		Gdx.app.log(Blockster.LOG, "Pausing game");
	}

	@Override
	public void resume () {
		Gdx.app.log(Blockster.LOG, "Resuming game");
	}

	@Override
	public void dispose () {
		Gdx.app.log(Blockster.LOG,  "Disposing game");
	}
}

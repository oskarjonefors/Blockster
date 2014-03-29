package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

public class Blockster extends Game implements ApplicationListener {
	Texture texture;
	SpriteBatch batch;
	float elapsed;
	
	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();

	//A libgdx helper class that logs current FPS each second
	private FPSLogger fpsLogger;
	
	@Override
	public void create () {
		Gdx.app.log(Blockster.LOG, "Creating game");
		texture = new Texture(Gdx.files.internal("libgdx-logo.png"));
		batch = new SpriteBatch();
		StageController controller = new StageController();
		StageView view = new StageView(controller);
	}

	@Override
	public void resize (int width, int height) {
		Gdx.app.log(Blockster.LOG, "Resizing game to: " + width + " x " + height);
	}

	@Override
	public void render () {
		elapsed += Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(texture, 100+100*(float)Math.cos(elapsed), 100+25*(float)Math.sin(elapsed));
		batch.end();
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

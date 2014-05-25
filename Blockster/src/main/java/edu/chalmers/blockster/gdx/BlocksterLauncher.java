package edu.chalmers.blockster.gdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * Application to launch an instance of the game.
 */
public final class BlocksterLauncher {

	private BlocksterLauncher() {
	}

	public static void main(String [] args) {
		final ApplicationListener listener = new Blockster();
		
		final LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = 800;
		config.height = 600;
		config.resizable = false;
		config.useGL20 = true;
		config.title = "Blockster";
		
		new LwjglApplication(listener, config);
	}
}

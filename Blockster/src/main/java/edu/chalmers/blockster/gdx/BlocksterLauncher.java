package edu.chalmers.blockster.gdx;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

/**
 * Application to launch an instance of the game.
 */
public final class BlocksterLauncher {

	private BlocksterLauncher() {
	}

	public static void main(String [] args) {
		final ApplicationListener listener = new Blockster();
		final String title = "Blockster";
		
		final int width = 800;
		final int height = 600;
		
		final boolean useOpenGLES2 = true;
		new LwjglApplication(listener, title, width, height, useOpenGLES2);
	}
}

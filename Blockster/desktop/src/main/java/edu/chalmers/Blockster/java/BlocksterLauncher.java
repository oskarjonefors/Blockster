package edu.chalmers.Blockster.java;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import edu.chalmers.Blockster.core.Blockster;

/**
 * Application to launch an instance of the game.
 */
public class BlocksterLauncher {

	public static void main(String [] args) {
		ApplicationListener listener = new Blockster();
		String title = "Blockster";
		
		int width = 800;
		int height = 600;
		
		boolean useOpenGLES2 = true;
		new LwjglApplication(listener, title, width, height, useOpenGLES2);
	}
}

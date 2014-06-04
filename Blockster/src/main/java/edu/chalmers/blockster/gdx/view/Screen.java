package edu.chalmers.blockster.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;

public final class Screen {
	
	private Screen() {
        throw new UnsupportedOperationException("Instantiation of Screen "
        		+ "class is not allowed");
	}

	public final static void setFullScreenMode() {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
	}

	public final static void setWindowMode() {
		final DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
		Gdx.graphics.setDisplayMode((int) (desktop.width * 0.75),
				(int) (desktop.height * 0.75), false);
	}
	
}

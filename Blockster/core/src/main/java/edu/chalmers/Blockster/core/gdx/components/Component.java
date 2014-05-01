package edu.chalmers.Blockster.core.gdx.components;

import com.badlogic.gdx.InputProcessor;

public interface Component extends InputProcessor {

	public void draw();
	
	public int getX();
	
	public int getY();
	
	public int getWidth();
	
	public int getHeight();
	
}

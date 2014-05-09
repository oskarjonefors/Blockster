package edu.chalmers.blockster.core.gdx.components;

import java.util.List;

import com.badlogic.gdx.InputProcessor;

public class Button implements Component, InputProcessor {
	
	private List<ButtonListener> listeners;
	
	private int x, y, width, height;
	private String text;
	
	private boolean hover = false;
	
	private boolean touchdown = false;
	
	public Button(int x, int y, int width, int height, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
	}
	
	public void addButtonListener(ButtonListener bl) {
		listeners.add(bl);
	}
	

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (screenX >= x && screenX < x + width &&
				screenY >= y && screenY < y + height) {
			touchdown = true;
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if (touchdown && screenX >= x && screenX < x + width &&
				screenY >= y && screenY < y + height) {
			for (ButtonListener bl : listeners) {
				bl.buttonClicked(this);
			}
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		if (screenX >= x && screenX < x + width &&
				screenY >= y && screenY < y + height) {
			hover = true;
		}
		
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void draw() {
		
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}
	
	

}

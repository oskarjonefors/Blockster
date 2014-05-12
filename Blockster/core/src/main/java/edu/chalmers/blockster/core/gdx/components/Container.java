package edu.chalmers.blockster.core.gdx.components;

import java.util.ArrayList;
import java.util.List;

public class Container implements Component {

	private int x, y, width, height;
	private List<Component> content;
	
	public Container(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		content = new ArrayList<Component>();
	}
	
	public void add(Component c) {
		content.add(c);
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
		for (Component c : content) {
			c.touchDown(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		for (Component c : content) {
			c.touchUp(screenX, screenY, pointer, button);
		}
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		for (Component c : content) {
			c.touchDragged(screenX, screenY, pointer);
		}
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		for (Component c : content) {
			c.mouseMoved(screenX, screenY);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		for (Component c : content) {
			c.scrolled(amount);
		}
		return false;
	}

}

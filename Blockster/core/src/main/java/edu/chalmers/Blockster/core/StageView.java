package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;

public class StageView implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private StageController controller;
	
	public StageView(StageController controller) {
		this.controller = controller;
	}
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub	
	}
	
	/**
	 * Render the view.
	 */
	public void render() {
	}
	
	/**
	 * Initialize the view.
	 */
	public void init() {
		
	}

}

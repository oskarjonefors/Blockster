package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

public class StageView implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private StageController controller;
	private PlayerController pController;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	
	public StageView(StageController controller, PlayerController pController) {
		this.controller = controller;
		this.pController = pController;
	}
	
	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();
	}
	
	/**
	 * Render the view.
	 */
	public void render(){
		/**
		 *  renders the Stage
		 */
		renderer.setView(camera);
		renderer.render();
		
		/**
		 * render the player
		 */
		pController.update(Gdx.graphics.getDeltaTime());
		renderer.getSpriteBatch().begin();
		pController.getPlayer().draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
	}
	
	/**
	 * Initialize the view.
	 */
	public void init(Map map) {
		this.map = (TiledMap)map;
		camera = new OrthographicCamera();
		renderer = new OrthogonalTiledMapRenderer((TiledMap)map);
		
		
		//Move the camera to a good start position
		camera.translate(600, 600);
	}
	public void resize(int width, int height){
		camera.viewportHeight = height*2;
		camera.viewportWidth = width*2;
		camera.update();
	}
	
}

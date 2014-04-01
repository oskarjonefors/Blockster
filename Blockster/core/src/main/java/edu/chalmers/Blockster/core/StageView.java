package edu.chalmers.Blockster.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

/**
 * @author Joel Tegman
 */
public class StageView implements Disposable {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private StageController controller;
	private PlayerController pController;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	
	/**
	 * test
	 */
	private Sprite bg = new Sprite(new Texture("maps/background-11.jpg"));
	
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
		
		
		
		/**
		 * render the player
		 */
		pController.update(Gdx.graphics.getDeltaTime());
		renderer.getSpriteBatch().begin();
		bg.draw(renderer.getSpriteBatch());
		pController.getPlayer().draw(renderer.getSpriteBatch());
		renderer.getSpriteBatch().end();
		renderer.render();
	}
	
	/**
	 * Initialize the view.
	 */
	public void init(Map map) {
		this.map = (TiledMap)map;
		camera = new OrthographicCamera();
		renderer = new OrthogonalTiledMapRenderer((TiledMap)map);
		bg.scale(3);
		bg.setPosition(1100, 500);
		
		
		//Move the camera to a good start position
		camera.translate(1600, 900);
	}
	public void resize(int width, int height){
		camera.viewportHeight = height*5;
		camera.viewportWidth = width*5;
		camera.update();
	}
	
	public void setBatch() {
		//TODO: parameters, body
	}
	
}

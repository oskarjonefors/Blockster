package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.View;

/**
 * @author Joel Tegman
 */
public class GdxView implements Disposable, View {

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private Model stage;
	private OrthogonalTiledMapRenderer renderer;
	private TiledMap map;
	
	/**
	 * test
	 */
	private Sprite bg = new Sprite(new Texture("maps/background-11.jpg"));
	
	public GdxView(Model stage) {
		this.stage = stage;
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
		
		renderer.getSpriteBatch().begin();
		bg.draw(renderer.getSpriteBatch());
		for (Player player : stage.getPlayers()) {
			((GdxPlayer)player).draw(renderer.getSpriteBatch());
		}
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
	
}

package edu.chalmers.Blockster.core.gdx.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.Animation;
import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.BlockMap;
import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.util.Direction;

/**
 * @author Joel Tegman, Oskar Jönefors
 */
public class GdxView implements ApplicationListener, Disposable {

	private OrthographicCamera camera;
	private Model model;
	private OrthogonalTiledMapRenderer renderer;
	private BlockMap blockMap;
	private Stage stage;
	private List<Player> players;
	private Player activePlayer;
	private HashMap<Block, GdxBlockActor> activeBlocks;
	private Actor background;
	
	public GdxView(Model model) {
		this.model = model;
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		renderer.dispose();
	}
	
	/**
	 * Render the view.
	 */
	public void render(){
		
		/* Follow the active player */
		camera.position.set(activePlayer.getX(), activePlayer.getY(), 0);
		
		/* Move the background with the player */
		background.setPosition((activePlayer.getX()*0.9f - background.getScaleX()*(background.getWidth())),
				(activePlayer.getY()*0.9f - (background.getHeight() / 2)));
		
		for (Block block : model.getActiveBlocks()) {
			if (!activeBlocks.keySet().contains(block) || !activeBlocks.get(block).hasParent()) {
				Animation anim = block.getAnimation();
				Direction dir = anim.direction;
				float duration = anim.duration;
				GdxBlockActor actor = new GdxBlockActor((GdxBlock)block);
				
				activeBlocks.put(block, actor);
				stage.addActor(actor);
				actor.addAction(new MoveBlockAction(dir, duration, blockMap, model));
				((GdxBlockLayer) blockMap.getBlockLayer()).removeBlock(block);
				Gdx.app.log("GdxView", "Added actor.");
			}
		}
		
		/**
		 *  renders the stage
		 */
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();		
		renderer.setView(camera);
		renderer.render();
	}
	
	/**
	 * Initialize the view.
	 */
	public void init(Map map) {
		blockMap = new GdxMap((TiledMap)map);
		BlockLayer layer = blockMap.getBlockLayer();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		camera = new OrthographicCamera();
		renderer = new OrthogonalTiledMapRenderer((TiledMap)map);
		stage.setCamera(camera);

		/* Add the background */
		Texture tex = new Texture("maps/background-11.jpg");
		background = new GdxBackgroundActor(new TextureRegion(tex));
		background.setScale(5);
		stage.addActor(background);
		
		players = new ArrayList<Player>();
		for (Player player : model.getPlayers()) {
			stage.addActor((GdxPlayer)player);
			players.add(player);
			Gdx.app.log("GdxView", "Added actor.");
		}
		
		activePlayer = players.get(0);
		
		activeBlocks = new HashMap<Block, GdxBlockActor>();
		
		/*    Makes actors out of all the blocks in the map.
		float blockWidth = layer.getBlockWidth();
		float blockHeight = layer.getBlockHeight();
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				try {
					GdxBlock block = (GdxBlock)layer.getBlock(x, y);
					Actor actor = new GdxBlockActor(block);
					actor.setBounds(x*blockWidth, y*blockHeight, blockWidth, blockHeight);
					stage.addActor(actor);
					Gdx.app.log("GdxView", "Added block at x = " + block.getX() + " y =" + block.getY());
				} catch (NullPointerException e) { }
			}
		} */
		
		//Move the camera to a good start position
		camera.translate(1600, 900);
	}
	public void resize(int width, int height){
		camera.viewportHeight = height*5;
		camera.viewportWidth = width*5;
		camera.update();
	}

	@Override
	public void create() {
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}
	
}

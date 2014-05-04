package edu.chalmers.Blockster.core.gdx.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Vector2f;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.Blockster.core.AnimationState;
import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.Movement;
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
	private Stage stage;
	private List<Block> activeBlocks;
	private List<Block> liftedBlocks;
	private Map<Player, PlayerView> players;
	private Actor background;
	private Vector3 cameraMoveVector;

	
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

		/* Checks if the camera should transit between the players */
		if (model.isSwitchChar) {
			transitCamera();
		}

		/* Follow the active player */
		camera.position.set(model.getActivePlayer().getX(),
				model.getActivePlayer().getY(), 0);

		/* Move the background with the player */
		background.setPosition(
				(model.getActivePlayer().getX()*0.7f - 
						background.getScaleX()*background.getWidth() - 
						camera.viewportWidth / 2),
						(model.getActivePlayer().getY()*0.7f - 
								(background.getHeight() / 2) - 
								camera.viewportHeight / 2));

	
		for (Block block : model.getActiveBlocks()) {
			if (!activeBlocks.contains(block) || !((Block)block).hasParent()) {
				Block gBlock = (Block)block;

				AnimationState anim = block.getAnimationState();

				
				activeBlocks.add(gBlock);
				stage.addActor(gBlock);
				gBlock.setOrigin(gBlock.getX(), gBlock.getY());
				((GdxBlockLayer) model.getMap().getBlockLayer()).removeBlock(block);
				Gdx.app.log("GdxView", "Added actor. Coordinates:" + gBlock.getX() + " " + gBlock.getY());
			}
		}
				

		
		/**
		 *  renders the stage
		 */
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		drawPlayers();
		
		renderer.setView(camera);
		renderer.render();
	}

	/**
	 * Initialize the view.
	 */
	public void init() {
		players = new HashMap<Player, PlayerView>();
		for (Player player : model.getPlayers()) {
			players.put(player, createPlayerView(player));
		}
		
		
		//BlockLayer layer = model.getMap().getBlockLayer();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		camera = new OrthographicCamera();
		renderer = new OrthogonalTiledMapRenderer((GdxMap) model.getMap());
		stage.setCamera(camera);
		

		/* Add the background */
		Texture tex = new Texture("maps/background-11.jpg");
		background = new GdxBackgroundActor(new TextureRegion(tex));
		background.setScale(5);
		stage.addActor(background);
		
		
		activeBlocks = new ArrayList<Block>();
		liftedBlocks = new ArrayList<Block>();
	}
	
	public void transitCamera(){
		cameraMoveVector = new Vector3(model.getActivePlayer().getX(), model.getActivePlayer().getY(), 0);

		/* Set the "direction" of the cameraMoveVector towards the active player */
		cameraMoveVector.sub(camera.position);

		/* Set the vector to a proper size. 
		 * This will decide how fast the camera moves */
		cameraMoveVector.nor();
		cameraMoveVector.mul(50f);

		camera.translate(cameraMoveVector);

		background.setPosition(
				(camera.position.x*0.7f - 
						background.getScaleX()*background.getWidth() - 
						camera.viewportWidth / 2),
						(camera.position.y*0.7f - 
								(background.getHeight() / 2) - 
								camera.viewportHeight / 2));


		boolean cameraInPlace = camera.position.epsilonEquals(model.getActivePlayer().getX(), model.getActivePlayer().getY(), 0, 30f);

		if (cameraInPlace) {
			model.isSwitchChar = false;
		}
	}

	
	public void refreshRenderer() {
		renderer.setMap((GdxMap) model.getMap());
	}
	
	public void refreshStage() {
		stage.clear();
		stage.setCamera(camera);
		stage.addActor(background);
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
	
	public void drawPlayers() {
		camera.update();
		SpriteBatch batch = stage.getSpriteBatch();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		for (Player player : model.getPlayers()) {
			PlayerView pView = players.get(player);
			if (pView == null) {
				players.put(player, pView = createPlayerView(player));
			}
			pView.draw(batch);
		}
		
		batch.end();
	}
	
	public PlayerView createPlayerView(Player player) {

		Texture tex = new Texture("Player/still2.png");
		TextureRegion texr = new TextureRegion(tex);
		PlayerView pView = new PlayerView(player, null, texr);
		
		return pView;
	}
	
	
}

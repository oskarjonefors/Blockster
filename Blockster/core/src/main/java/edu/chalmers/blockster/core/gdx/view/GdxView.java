package edu.chalmers.blockster.core.gdx.view;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.blockster.core.Model;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationFactory;

/**
 * @author Joel Tegman, Oskar Jönefors
 */
public class GdxView implements ApplicationListener, Disposable {

	private final Logger log = Logger.getLogger(this.getClass().getName());
	
	private OrthographicCamera camera;
	private SpriteBatch hudBatch;
	private final Model model;
	private OrthogonalTiledMapRenderer renderer;
	private Stage stage;
	private Map<Player, PlayerView> players;
	private Actor background;
	private GdxFactory factory;
	private GdxMap gdxMap;
	private MiniMap miniMap;
	
	public GdxView(Model model, GdxFactory factory) {
		this.model = model;
		this.factory = factory;
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		renderer.dispose();
	}
	
	/**
	 * Render the view.
	 */
	@Override
	public void render(){

		/* Checks if the camera should transit between the players */
		if (model.isSwitchChar) {
			transitCamera();
		} else {

		/* Follow the active player */
		final Player activePlayer = model.getActivePlayer();
		final float activePlayerX = activePlayer.getX();
		final float activePlayerY = activePlayer.getY();
		
		camera.position.set(activePlayerX, activePlayerY, 0);

		/* Move the background with the player */
		background.setPosition(activePlayerX * 0.7f - background.getWidth() * 2,
				activePlayerY * 0.7f - background.getHeight() * 2);
		}
		
		/**
		 *  renders the stage
		 */
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		
		renderer.setView(camera);
		renderer.render();
		
		drawObjects();
	}

	/**
	 * Initialize the view.
	 */
	public void init() {
		players = new HashMap<Player, PlayerView>();
		for (final Player player : model.getPlayers()) {
			players.put(player, createPlayerView(player));
		}
		
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		camera = new OrthographicCamera();
		gdxMap = factory.getGdxMap();
		renderer = new OrthogonalTiledMapRenderer(gdxMap);
		stage.setCamera(camera);
		
		/* Set the window size to match the screen resolution */
		setWindowMode();

		/* Add the background */
		final Texture tex = new Texture("maps/background-11.jpg");
		background = new GdxBackgroundActor(new TextureRegion(tex));
		background.setScale(5);
		stage.addActor(background);
		
		hudBatch = new SpriteBatch();
		miniMap = factory.getMiniMap();
		miniMap.setScaleX(8);
		miniMap.setScaleY(8);
	}
	
	public void transitCamera(){
		Vector3 cameraMoveVector = new Vector3(model.getActivePlayer().getX(), model.getActivePlayer().getY(), 0);

		/* Set the "direction" of the cameraMoveVector towards the active player */
		cameraMoveVector.sub(camera.position);

		/* Set the vector to a proper size. 
		 * This will decide how fast the camera moves */
		cameraMoveVector.nor();
		cameraMoveVector.mul(50f);

		camera.translate(cameraMoveVector);

		background.setPosition(camera.position.x * 0.7f - background.getWidth() * 2,
				camera.position.y * 0.7f - background.getHeight() * 2);


		final boolean cameraInPlace = camera.position.epsilonEquals(model.getActivePlayer().getX(), model.getActivePlayer().getY(), 0, 30f);

		if (cameraInPlace) {
			model.isSwitchChar = false;
		}
	}

	
	public void refreshRenderer() {
		gdxMap = factory.getGdxMap();
		renderer.setMap(gdxMap);
	}
	
	public void refreshStage() {
		stage.clear();
		stage.setCamera(camera);
		stage.addActor(background);
	}
	
	
	@Override
	public void resize(int width, int height){
		camera.viewportHeight = height*5;
		camera.viewportWidth = width*5;
		camera.update();
		log.log(Level.INFO, "Resizing to " + width + " x " + height);
		}
	
	private void setFullScreenMode() {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
		setZoom();
	}
	
	private void setWindowMode() {
		final DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
		Gdx.graphics.setDisplayMode((int)(desktop.width * 0.75), (int)(desktop.height * 0.75), false);
		setZoom();
	}
	
	private void setZoom() {
		camera.zoom = 700f / Gdx.graphics.getWidth();
	}
	
	public void toggleFullScreen() {
		if(Gdx.graphics.isFullscreen()) {
			setWindowMode();
		} else {
			setFullScreenMode();
		}
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
	
	public void drawPlayers(SpriteBatch batch) {

		
		for (final Player player : model.getPlayers()) {
			PlayerView pView = players.get(player);
			if (pView == null) {
				pView = createPlayerView(player);
				players.put(player, pView);
			}
			pView.draw(batch);
		}
		
		
	}
	
	public void drawBlocks(SpriteBatch batch) {
		
		for (final Block block : model.getMap().getActiveBlocks()) {
			final BlockView bView = gdxMap.getBlockView(block);
			if (bView != null) {
				bView.draw(batch);
			}
		}
	}
	
	public void drawHud(SpriteBatch batch) {
		miniMap.draw(batch);
	}
	
	public void drawObjects() {
		camera.update();

		hudBatch.begin();
		drawHud(hudBatch);
		hudBatch.end();
		
		final SpriteBatch batch = stage.getSpriteBatch();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		
		drawPlayers(batch);
		drawBlocks(batch);
		
		batch.end();
	}
 	
	public PlayerView createPlayerView(Player player) {

		final Texture tex = new Texture("Player/still2.png");
		final TextureRegion texr = new TextureRegion(tex);
		final AnimationFactory animF = new AnimationFactory();
		return new PlayerView(player, animF.getArrayOfAnimations(),
				animF.getWalkAnimations(), texr);
	}
}

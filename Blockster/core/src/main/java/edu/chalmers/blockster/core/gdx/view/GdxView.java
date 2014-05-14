package edu.chalmers.blockster.core.gdx.view;

import java.util.HashMap;
import java.util.List;
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

import edu.chalmers.blockster.core.GameState;
import edu.chalmers.blockster.core.Model;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationFactory;

/**
 * @author Joel Tegman, Oskar Jönefors
 */
public class GdxView implements ApplicationListener, Disposable {

	private static final Logger LOG = Logger.getLogger(GdxView.class.getName());

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
	public void render() {

		if (model.getGameState() == GameState.GAME_RUNNING) {

			/* Checks if the camera should transit between the players */
			if (model.getActivePlayer().isSwitchingToMe()) {
				transitCamera();
			} else {

				/* Follow the active player */
				final Player activePlayer = model.getActivePlayer();
				final float activePlayerX = activePlayer.getX();
				final float activePlayerY = activePlayer.getY();

				camera.position.set(activePlayerX, activePlayerY, 0);

				/* Move the background with the player */
				background.setPosition(activePlayerX * 0.7f - background.getWidth()
						* 2, activePlayerY * 0.7f - background.getHeight() * 2);
			}

			/**
			 * renders the stage
			 */
			stage.act(Gdx.graphics.getDeltaTime());
			stage.draw();

			renderer.setView(camera);
			renderer.render();

			drawObjects();
			
		} else if (model.getGameState() == GameState.GAME_WON) {
			Texture winPic = new Texture("menuPics/winPic.jpg");
			SpriteBatch batch = new SpriteBatch();
			
			batch.begin();
			batch.draw(winPic, 370, 150);
			batch.end();
		}
	}

	/**
	 * Initialize the view.
	 */
	public void init() {
		players = new HashMap<Player, PlayerView>();
		for (final Player player : model.getPlayers()) {
			players.put(player, createPlayerView(player));
		}

		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);
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

	public void transitCamera() {
		Vector3 cameraMoveVector = new Vector3(model.getActivePlayer().getX(),
				model.getActivePlayer().getY(), 0);

		/* Set the "direction" of the cameraMoveVector towards the active player */
		cameraMoveVector.sub(camera.position);

		/*
		 * Set the vector to a proper size. This will decide how fast the camera
		 * moves
		 */
		cameraMoveVector.nor();
		cameraMoveVector.mul(50f);

		camera.translate(cameraMoveVector);

		background.setPosition(camera.position.x * 0.7f - background.getWidth()
				* 2, camera.position.y * 0.7f - background.getHeight() * 2);

		final boolean cameraInPlace = camera.position.epsilonEquals(model
				.getActivePlayer().getX(), model.getActivePlayer().getY(), 0,
				30f);

		if (cameraInPlace) {
			model.getActivePlayer().switchingToMe(false);
		}
	}

	public void refreshRenderer() {
		gdxMap = factory.getGdxMap();
		miniMap = factory.getMiniMap();
		miniMap.setScaleX(8);
		miniMap.setScaleY(8);
		renderer.setMap(gdxMap);
	}

	public void refreshStage() {
		stage.clear();
		stage.setCamera(camera);
		stage.addActor(background);
	}

	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height * 5;
		camera.viewportWidth = width * 5;
		camera.update();
		LOG.log(Level.INFO, "Resizing to " + width + " x " + height);
	}

	private void setFullScreenMode() {
		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
		setZoom();
	}

	private void setWindowMode() {
		final DisplayMode desktop = Gdx.graphics.getDesktopDisplayMode();
		Gdx.graphics.setDisplayMode((int) (desktop.width * 0.75),
				(int) (desktop.height * 0.75), false);
		setZoom();
	}

	private void setZoom() {
		camera.zoom = 700f / Gdx.graphics.getWidth();
	}

	public void toggleFullScreen() {
		if (Gdx.graphics.isFullscreen()) {
			setWindowMode();
		} else {
			setFullScreenMode();
		}
	}

	@Override
	public void create() {
		/* Currently unused */
	}

	@Override
	public void pause() {
		/* Currently unused */
	}

	@Override
	public void resume() {
		/* Currently unused */
	}

	private void drawPlayers(SpriteBatch batch) {

		for (final Player player : model.getPlayers()) {
			PlayerView pView = players.get(player);
			if (pView == null) {
				pView = createPlayerView(player);
				players.put(player, pView);
			}
			pView.draw(batch);
		}

	}

	private void drawBlocks(SpriteBatch batch) {

		for (final Block block : model.getMap().getActiveBlocks()) {
			final BlockView bView = gdxMap.getBlockView(block);
			if (bView != null) {
				bView.draw(batch);
			}
		}
	}

	private void drawHud(SpriteBatch batch) {
		updateHud();
		miniMap.draw(batch);
	}

	private void drawObjects() {
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
	
	private void updateHud() {
		final Player activePlayer = model.getActivePlayer();
		final float scaleX = activePlayer.getScaleX();
		final float scaleY = activePlayer.getScaleY();
		final float pX = activePlayer.getX() / scaleX;
		final float pY = activePlayer.getY() / scaleY;
		final float viewWidth = camera.viewportWidth * camera.zoom / scaleX;
		final float viewHeight = camera.viewportHeight * camera.zoom / scaleY;
		final float vX = pX - viewWidth / 2;
		final float vY = pY - viewHeight;
		
		final List<Player> playerList = model.getPlayers();
		final float[][] playerPos = new float[playerList.size()][2];
		for (int i = 0; i < playerList.size(); i++) {
			final Player player = playerList.get(i);
			playerPos[i][0] = player.getX() / scaleX;
			playerPos[i][1] = (player.getY() + player.getHeight() / 2f) / scaleY;
		}
		
		miniMap.setPlayerLocations(playerPos);
		miniMap.setViewportBounds(vX, vY, viewWidth, viewHeight);
	}

	public PlayerView createPlayerView(Player player) {

		final Texture tex = new Texture("Player/still2.png");
		final TextureRegion texr = new TextureRegion(tex);
		final AnimationFactory animF = new AnimationFactory();
		return new PlayerView(player, animF.getArrayOfAnimations(),
				animF.getWalkAnimations(), texr);
	}
}

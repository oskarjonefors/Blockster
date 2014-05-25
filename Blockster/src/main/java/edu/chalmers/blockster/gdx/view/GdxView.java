package edu.chalmers.blockster.gdx.view;

import java.awt.geom.Point2D;
import java.util.ArrayList;
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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Disposable;

import edu.chalmers.blockster.core.GameState;
import edu.chalmers.blockster.core.Model;
import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;

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
	private BackgroundImage background;
	private GdxFactory factory;
	private GdxMap gdxMap;
	private MiniMap miniMap;

	private PortalView bluePortalView;
	private PortalView yellowPortalView;

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
			
			final SpriteBatch batch = stage.getSpriteBatch();
			batch.begin();
			drawBackground(batch);
			batch.end();

			renderer.setView(camera);
			renderer.render();

			drawObjects();
			
		} else if (model.getGameState() == GameState.GAME_WON) {
			Texture winPic = new Texture("menuPics/logo.png");
			SpriteBatch batch = new SpriteBatch();
			
			batch.begin();
			batch.draw(winPic, (Gdx.graphics.getWidth() - winPic.getWidth())/2,
							(Gdx.graphics.getHeight() - winPic.getHeight())/2);
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
		background = new BackgroundImage(new TextureRegion(new Texture("images/background_day.jpg")),
				new TextureRegion(new Texture("images/background_night.jpg")));
		

		hudBatch = new SpriteBatch();
		miniMap = factory.getMiniMap();
		miniMap.setScaleX(8);
		miniMap.setScaleY(8);
		
		bluePortalView = factory.getPortalView(0);
		yellowPortalView = factory.getPortalView(1);
		
	}
	
	public void transitCamera() {
		final Player activePlayer = model.getActivePlayer();
		final float playerX = activePlayer.getX();
		final float playerY = activePlayer.getY();
		
		final Vector3 cameraMoveVector = new Vector3(playerX, playerY, 0);

		/* Set the "direction" of the cameraMoveVector towards the active player */
		cameraMoveVector.sub(camera.position);

		/*
		 * Set the vector to a proper size. This will decide how fast the camera
		 * moves
		 */
		cameraMoveVector.nor();
		cameraMoveVector.scl(30f);

		camera.translate(cameraMoveVector);

		final boolean cameraInPlace = camera.position.epsilonEquals(
				playerX, playerY, 0, 30f);

		if (cameraInPlace) {
			activePlayer.switchingToMe(false);
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
		camera.zoom = 4 * model.getMap().getBlockWidth() / Gdx.graphics.getWidth();
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

	private void drawBackground(SpriteBatch batch) {
		background.setScale(1);
		background.setPosition(camera.position.x - background.getWidth() / 2,
				camera.position.y - background.getHeight() / 2);
		background.draw(batch, model.getCurrentWorld());
	}
	
	private void drawPlayers(SpriteBatch batch) {

		for (final Player player : model.getPlayers()) {
			PlayerView pView = players.get(player);
			if (pView == null) {
				pView = createPlayerView(player);
				players.put(player, pView);
			}
			final boolean active = model.getActivePlayer().equals(
					pView.getPlayer()) ? true : false;
			
			pView.draw(batch, active);
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
		drawPortals(batch);

		batch.end();
	}
	
	private void drawPortals(SpriteBatch batch) {
			bluePortalView.draw(batch);
			yellowPortalView.draw(batch);
	}

	private void updateHud() {
		final Player activePlayer = model.getActivePlayer();
		miniMap.setActivePlayer(activePlayer);
		final float scaleX = model.getMap().getBlockWidth();
		final float scaleY = model.getMap().getBlockHeight();
		final float pX = activePlayer.getX() / scaleX;
		final float pY = activePlayer.getY() / scaleY;
		final float viewWidth = camera.viewportWidth * camera.zoom / scaleX;
		final float viewHeight = camera.viewportHeight * camera.zoom / scaleY;
		final float vX = pX - viewWidth / 2;
		final float vY = pY - viewHeight - 1;
		
		final List<Player> playerList = model.getPlayers();
		final List<Point2D.Float> playerPos = new ArrayList<Point2D.Float>();
		for (Player player : playerList) {
			playerPos.add(new Point2D.Float(player.getX() / scaleX, (player.getY() + (player.getHeight() / 2f)) / scaleY));
		}
		
		miniMap.setPlayerLocations(playerPos);
		miniMap.setViewportBounds(vX, vY, viewWidth, viewHeight);
	}

	public PlayerView createPlayerView(Player player) {

		final AnimationFactory animF = new AnimationFactory();
		final World pWorld = player.getWorld();
		return new PlayerView(player, animF.getArrayOfAnimations(pWorld),
				animF.getWalkAnimations(pWorld));
	}
}

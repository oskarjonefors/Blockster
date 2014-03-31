package edu.chalmers.Blockster.core;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
/**
 * 
 * @author Oskar Jönefors, Eric Bjuhr
 *
 */
public class Blockster extends Game implements ApplicationListener, StageListener {
	
	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();

	//A libgdx helper class that logs current FPS each second
	private FPSLogger fpsLogger;
	private StageController controller;
	private PlayerController playerController;
	private StageView viewer;
	private Stage stage;
	private List<Stage> stageList;
	
	private void addStagesToList(List<Stage> list, File[] maps) {
		TmxMapLoader loader = new TmxMapLoader();
		for (File mapFile : maps) {
			Gdx.app.log(Blockster.LOG, "Stage found: "+mapFile.getName());
			TiledMap map = loader.load("maps/"+mapFile.getName());
			playerController = new PlayerController((TiledMapTileLayer)map.getLayers().get(0));
			Stage stage = new Stage(map);
			
			StageView view = new StageView(controller, playerController);
			view.init(map);
			stage.setStageView(view);
			
			list.add(stage);
		}
	}
	
	@Override
	public void create () {
		Gdx.app.log(Blockster.LOG, "Creating game");
		StageController controller = new StageController();
		controller = new StageController();
		controller.addStageListener(this);
		try {

			Gdx.app.log(Blockster.LOG, "Loading stages");
			loadStages();

			Gdx.app.log(Blockster.LOG, "Setting stage");
			controller.setStage(stageList.get(0));
		} catch (SecurityException | IOException e) {
			Gdx.app.log(Blockster.LOG, e.getClass().getName());
		}
	}
	
	@Override
	public void dispose () {
		Gdx.app.log(Blockster.LOG,  "Disposing game");
		viewer.dispose();
	
	}
	
	private File[] listFilesInDirectory(File directory, final String fileEnding) {
		
		FileFilter ff = new FileFilter() {
			@Override
			public boolean accept(File f) {
				return f.getName().endsWith(fileEnding);
			}
		};
		
		return directory.listFiles(ff);
	}

	private void loadStages() throws SecurityException, IOException {
		
		ArrayList<Stage> stages = new ArrayList<Stage>();
		addStagesToList(stages, listFilesInDirectory(new File("assets/maps/"), ".tmx"));
		stageList = Collections.synchronizedList(stages);
	}

	@Override
	public void pause () {
		Gdx.app.log(Blockster.LOG, "Pausing game");
	}

	@Override
	public void render () {
		/*Update the world controller with the time
			elapsed between the last two frames. */ 
		controller.update(Gdx.graphics.getDeltaTime());
		
		/* Update the model with the time elapsed between
		 * the last two frames.
		 */
		stage.update(Gdx.graphics.getDeltaTime());
		
		/* Clear screen */
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		/* Render the new frame */
		viewer.render();
	}

	@Override
	public void resize (int width, int height) {
		Gdx.app.log(Blockster.LOG, "Resizing game to: " + width + " x " + height);
		
		/**
		 * set the camera view according to the new size
		 */
		viewer.resize(width, height);
		}

	@Override
	public void resume () {
		Gdx.app.log(Blockster.LOG, "Resuming game");
	}
	
	@Override
	public void stageChanged(Stage stage) {
		this.stage = stage;
		viewer = stage.getStageView();
		Gdx.app.log(Blockster.LOG, "Recieved a stage changed event");
	}
}

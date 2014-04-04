package edu.chalmers.Blockster.core.gdx;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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

import edu.chalmers.Blockster.core.Model;
import edu.chalmers.Blockster.core.MapChangeListener;
import edu.chalmers.Blockster.core.gdx.controller.Controller;
import edu.chalmers.Blockster.core.gdx.view.View;
/**
 * 
 * @author Oskar Jönefors, Eric Bjuhr
 *
 */
public class Blockster extends Game implements ApplicationListener, MapChangeListener {
	
	//Constant useful for logging.
	public static final String LOG = Blockster.class.getSimpleName();

	//A libgdx helper class that logs current FPS each second
	private FPSLogger fpsLogger;
	private Controller controller;
	private View viewer;
	private Model stage;
	private List<Model> stageList;
	
	private void addStagesToList(List<Model> list, File[] maps) {
		TmxMapLoader loader = new TmxMapLoader();
		for (File mapFile : maps) {
			Gdx.app.log(Blockster.LOG, "Stage found: "+mapFile.getName());
			TiledMap map = loader.load("maps/"+mapFile.getName());
			Model stage = new Model(map);
			
			View view = new View(stage);
			view.init(map);
			stage.setStageView(view);
			
			list.add(stage);
		}
	}
	
	@Override
	public void create () {
		Gdx.app.log(Blockster.LOG, "Creating game");
		controller = new Controller();
		controller.addStageListener(this);
		try {
			FileHandle fh = new FileHandle(new File(new File("assets"), "Gourmet Race.mp3"));
			Music music = Gdx.audio.newMusic(fh);
			music.play();
			music.setLooping(true);
			
			Gdx.app.log(Blockster.LOG, "Loading stages");
			loadStages();

			Gdx.app.log(Blockster.LOG, "Setting stage");
			controller.setStage(stageList.get(1));
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
		
		ArrayList<Model> stages = new ArrayList<Model>();
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
	public void stageChanged(Model stage) {
		this.stage = stage;
		viewer = stage.getStageView();
		Gdx.app.log(Blockster.LOG, "Recieved a stage changed event");
	}
}

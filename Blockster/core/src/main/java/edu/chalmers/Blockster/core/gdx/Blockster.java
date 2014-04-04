package edu.chalmers.Blockster.core.gdx;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
import edu.chalmers.Blockster.core.gdx.util.GdxFactory;
import edu.chalmers.Blockster.core.gdx.view.GdxMap;
import edu.chalmers.Blockster.core.gdx.view.GdxView;
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
	private GdxView view;
	private Model stage;
	private Map<Model, GdxView> stages;
	
	private void addStagesToMap(Map<Model, GdxView> list, File[] maps) {
		TmxMapLoader loader = new TmxMapLoader();
		for (File mapFile : maps) {
			Gdx.app.log(Blockster.LOG, "Stage found: "+mapFile.getName());
			TiledMap map = loader.load("maps/"+mapFile.getName());
			GdxMap gMap = new GdxMap(map);
			Model stage = new Model(gMap, new GdxFactory());
			
			GdxView view = new GdxView(stage);
			view.init(map);
			
			list.put(stage, view);
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
			
			Iterator<Model> modelIterator = stages.keySet().iterator();
			modelIterator.next();
			Model model = modelIterator.next();
			
			controller.setStage(model);
		} catch (SecurityException | IOException e) {
			Gdx.app.log(Blockster.LOG, e.getClass().getName());
		}
	}
	
	@Override
	public void dispose () {
		Gdx.app.log(Blockster.LOG,  "Disposing game");
		view.dispose();
	
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
		stages = Collections.synchronizedMap(new HashMap<Model, GdxView>());
		addStagesToMap(stages, listFilesInDirectory(new File("assets/maps/"), ".tmx"));
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
		view.render();
	}

	@Override
	public void resize (int width, int height) {
		Gdx.app.log(Blockster.LOG, "Resizing game to: " + width + " x " + height);
		
		/**
		 * set the camera view according to the new size
		 */
		view.resize(width, height);
		}

	@Override
	public void resume () {
		Gdx.app.log(Blockster.LOG, "Resuming game");
	}
	
	@Override
	public void stageChanged(Model stage) {
		this.stage = stage;
		view = stages.get(stage);
		Gdx.app.log(Blockster.LOG, "Recieved a stage changed event");
	}
}

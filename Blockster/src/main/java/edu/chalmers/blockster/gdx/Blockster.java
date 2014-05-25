package edu.chalmers.blockster.gdx;

import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

import edu.chalmers.blockster.core.MapChangeListener;
import edu.chalmers.blockster.core.Model;
import edu.chalmers.blockster.gdx.controller.Controller;
import edu.chalmers.blockster.gdx.view.GdxFactory;
import edu.chalmers.blockster.gdx.view.GdxView;

public final class Blockster extends Game implements MapChangeListener {
	
	private Controller controller;
	private GdxView viewer;
	private Model stage;
	private Map<Model, GdxView> stages;
	
	public Blockster() {
		super();
		create();
	}
	
	private void addStagesToMap(Map<Model, GdxView> stageMap, FileHandle... maps) {
		final TmxMapLoader loader = new TmxMapLoader();
		for (final FileHandle mapFile : maps) {
			final TiledMap map = loader.load(mapFile.path());
			final GdxFactory factory = new GdxFactory(map);
			final Model model = new Model(factory, mapFile.name());
			final GdxView view = new GdxView(model, factory);
			view.init();
			
			stageMap.put(model, view);
		}
	}
	
	@Override
	public final void create () {
		controller = new Controller();
		controller.addMapChangeListener(this);
		try {
			final FileHandle file = Gdx.files.internal("music/gourmet_race.mp3");
			final Music music = Gdx.audio.newMusic(file);
			music.setLooping(true);
			/* add music.play() here to make the music start. */

			loadStages();

			final Iterator<Model> modelIterator = stages.keySet().iterator();
			final Model model = modelIterator.next();
			
			controller.setModel(model);
		} catch (IOException e) {
		}
	}
	
	@Override
	public void dispose () {
		viewer.dispose();
	}

	private void loadStages() throws IOException {
		final FileHandle[] maps = {Gdx.files.internal("maps/stage1.tmx")};
		
		stages = Collections.synchronizedSortedMap(new TreeMap<Model, GdxView>());
		addStagesToMap(stages, maps);
	}

	@Override
	public void pause () {
	}

	@Override
	public void render () {
		/*Update the world controller with the time
			elapsed between the last two frames. */ 
		controller.update();
		
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
		/**
		 * set the camera view according to the new size
		 */
		viewer.resize(width, height);
		}

	@Override
	public void resume () {
	}
	
	@Override
	public void stageChanged(Model stage) {
		this.stage = stage;
		viewer = stages.get(stage);
		controller.setView(viewer);
		viewer.refreshRenderer();
		viewer.refreshStage();
	}
}
package edu.chalmers.blockster.gdx;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.badlogic.gdx.ApplicationListener;
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
import edu.chalmers.blockster.gdx.view.AbstractView;
import edu.chalmers.blockster.gdx.view.GdxFactory;
import edu.chalmers.blockster.gdx.view.GdxView;
import edu.chalmers.blockster.gdx.view.menu.MainMenu;
import edu.chalmers.blockster.gdx.view.menu.MainMenuParent;

public final class Blockster extends Game implements MapChangeListener, MainMenuParent {
	
	private Controller controller;
	private ApplicationListener view;
	private Model model;
	private Map<String, AbstractView> views;
	private Map<String, Model> models;
	private MainMenu mainMenu;
	
	private void addStagesToMap(FileHandle... maps) {
		views = Collections.synchronizedSortedMap(new TreeMap<String, AbstractView>());
		models = Collections.synchronizedSortedMap(new TreeMap<String, Model>());
		
		final TmxMapLoader loader = new TmxMapLoader();
		for (final FileHandle mapFile : maps) {
			final TiledMap map = loader.load(mapFile.path());
			final GdxFactory factory = new GdxFactory(map);
			final Model model = new Model(factory, mapFile.name());
			final GdxView view = new GdxView(model, factory);
			view.init();
			
			views.put(mapFile.name(), view);
			models.put(mapFile.name(), model);
		}
		
		views.put("Menu", mainMenu);
	}
	
	@Override
	public final void create () {
		controller = new Controller();
		mainMenu = new MainMenu(this);
		
		mainMenu.create();
		controller.addMapChangeListener(this);
		try {
			final FileHandle file = Gdx.files.internal("music/gourmet_race.mp3");
			final Music music = Gdx.audio.newMusic(file);
			music.setLooping(true);
			music.play();

			loadStages();

			/*final Iterator<Model> modelIterator = stages.keySet().iterator();
			final Model model = modelIterator.next();*/
			
			controller.setModel(null);
		} catch (IOException e) {
			Logger.getGlobal().throwing("Blockster", "create", e);
		}
	}
	
	@Override
	public void dispose () {
		for (AbstractView view : views.values()) {
			view.dispose();
		}
	}

	private void loadStages() throws IOException {
		final FileHandle[] maps = {Gdx.files.internal("maps/stage1.tmx")};
		addStagesToMap(maps);
	}

	
	@Override
	public void render () {
		/*Update the world controller with the time
			elapsed between the last two frames. */ 
		controller.update();
		
		/* Update the model with the time elapsed between
		 * the last two frames.
		 */
		if (model != null) {
			model.update(Gdx.graphics.getDeltaTime());
		}
		
		/* Clear screen */
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		/* Render the new frame */
		view.render();
	}

	@Override
	public void resize (int width, int height) {
		/**
		 * set the camera view according to the new size
		 */
		view.resize(width, height);
		}
	
	@Override
	public void stageChanged(String name) {
		model = models.get(name);
		view = views.get(name);
		
		if (view instanceof GdxView) {
			GdxView gdxView = (GdxView) view;
			controller.setView(gdxView);
			gdxView.refreshRenderer();
			gdxView.refreshStage();
		}
	}

	@Override
	public Set<String> getModelNames() {
		return models.keySet();
	}

	@Override
	public void setModel(String name) {
		controller.setModel(models.get(name));
	}
}
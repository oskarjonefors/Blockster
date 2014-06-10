package edu.chalmers.blockster.gdx.view.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import edu.chalmers.blockster.gdx.view.AbstractView;

public class MainMenu extends AbstractView {

	private Table table;
	
	public MainMenu(MainMenuParent parent) {
		super();
		createStage();
        table = new MenuTable(parent);
	}
	
	public void addDisposeListener() {
		
	}

	@Override
	public void dispose() {
		stage.dispose();
	}
	
	public void resize (int width, int height) {
        stage.setViewport(width, height, true);
	}

	@Override
	public void create() {
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        stage.addActor(table);
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void render() {
		stage.draw();
	}

	@Override
	public void resume() {
		
	}
	
}

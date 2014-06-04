package edu.chalmers.blockster.gdx.view;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Disposable;

public abstract class AbstractView extends WidgetGroup 
		implements ApplicationListener, Disposable {

	protected Stage stage;

	public AbstractView() {
		super();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);
	}
	
	protected final void createStage() {
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
				true);
	}
	
}

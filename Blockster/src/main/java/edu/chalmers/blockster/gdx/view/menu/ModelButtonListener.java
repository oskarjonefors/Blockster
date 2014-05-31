package edu.chalmers.blockster.gdx.view.menu;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class ModelButtonListener extends ClickListener {

	private final String name;
	private final ModelButtonClickedStrategy strategy;
	
	public ModelButtonListener(String name, ModelButtonClickedStrategy strategy) {
		super();
		this.name = name;
		this.strategy = strategy;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		super.clicked(event, x, y);
		strategy.clickedModelButton(name);
	}
	
}

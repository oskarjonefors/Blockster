package edu.chalmers.blockster.gdx.view.menu;

import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;

public class MenuTable extends Table implements ButtonClickedStrategy,
			ModelButtonClickedStrategy {
	
	private MenuLocation location;
	private final ButtonListener buttonListener;
	private final MainMenuParent parent;
	
	public MenuTable(MainMenuParent parent) {
		super();
		buttonListener = new ButtonListener(this);
		this.parent = parent;
		
		setMenuLocation(MenuLocation.START);
		setLayoutEnabled(true);
	}
	
	private void setMenuLocation(MenuLocation location) {
		this.location = location;
		clear();
		setMenuContent();
	}
	
	private void setMenuContent() {
		switch(location) {
			case OPTIONS:
				fillMenuWithOptions();
				break;
			case SELECT_STAGE:
				fillMenuWithStages();
				break;
			default:
				break;
		}
		fillMenuWithButtons();		
	}
	
	private void fillMenuWithOptions() {
		
	}
	
	private void fillMenuWithStages() {
		Set<String> modelNames = parent.getModelNames();
		

		final TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = new BitmapFont();
		buttonStyle.overFontColor = new Color(0.8f, 0.8f, 0, 1f);
		
		for (String name : modelNames) {
			final TextButton button = new TextButton(name, buttonStyle);
			button.setName(name);
			button.addListener(new ModelButtonListener(name, this));
			add(button);
			row();
		}
	}
	
	private void fillMenuWithButtons() {
		final TextButtonStyle buttonStyle = new TextButtonStyle();
		buttonStyle.font = new BitmapFont();
		buttonStyle.overFontColor = new Color(0.8f, 0.8f, 0, 1f);
		
		for (final String buttonText : location.getMenuButtons()) {
			final TextButton button = new TextButton(buttonText, buttonStyle);
			button.setName(buttonText);
			button.addListener(buttonListener);
			add(button);
			row();
		}
	}

	@Override
	public void actionPerformed(MenuLocation newLocation) {
		if (newLocation != MenuLocation.QUIT) { 
			setMenuLocation(newLocation);
		} else {
			Gdx.app.exit();
		}
	}

	@Override
	public void clickedModelButton(String name) {
		parent.setModel(name);
	}
	

	
}

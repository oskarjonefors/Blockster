package edu.chalmers.blockster.gdx.view.menu;

import java.util.Locale;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import edu.chalmers.blockster.gdx.view.menu.MenuTable.MenuLocation;

class ButtonListener extends ClickListener {
	
	private final ButtonClickedStrategy strategy;
	
	public ButtonListener(ButtonClickedStrategy strategy) {
		this.strategy = strategy;
	}
	
	@Override
	public void clicked(InputEvent event, float x, float y) {
		super.clicked(event, x, y);
		
		final String buttonText = event.getListenerActor().getName();
		final String locationName;
		if (buttonText.equals("Back")) {
			locationName = "START";
		} else{
			locationName = buttonText.replaceAll(" ", "_")
					.toUpperCase(Locale.ENGLISH);
		}

		strategy.actionPerformed(MenuLocation.valueOf(locationName));
	}
	
}
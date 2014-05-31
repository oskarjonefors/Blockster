package edu.chalmers.blockster.gdx.view.menu;
import edu.chalmers.blockster.gdx.view.menu.MenuTable.MenuLocation;

interface ButtonClickedStrategy {

	void actionPerformed(MenuLocation newLocation);
	
}

package edu.chalmers.blockster.gdx.view.menu;
import java.util.Map;

import edu.chalmers.blockster.core.Model;
import edu.chalmers.blockster.gdx.view.menu.MenuTable.MenuLocation;

interface ButtonClickedStrategy {

	void actionPerformed(MenuLocation newLocation);
	
}

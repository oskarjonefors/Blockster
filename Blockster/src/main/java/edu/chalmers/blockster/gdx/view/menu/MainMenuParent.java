package edu.chalmers.blockster.gdx.view.menu;

import java.util.Map;
import java.util.Set;

import edu.chalmers.blockster.core.Model;

public interface MainMenuParent {
	
	Set<String> getModelNames();
	
	void setModel(String name);
	
}

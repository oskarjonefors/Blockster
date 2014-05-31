package edu.chalmers.blockster.gdx.view.menu;

import java.util.Set;


public interface MainMenuParent {
	
	Set<String> getModelNames();
	
	void setModel(String name);
	
}

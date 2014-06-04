package edu.chalmers.blockster.gdx.view.menu;

public enum MenuLocation {
	START("Select Stage", "Options", "Quit"),
	SELECT_STAGE("Back"),
	OPTIONS("Back"),
	QUIT();
	
	private final String[] content;
	private MenuLocation(String...content) {
		this.content = content;
	}
	
	public String[] getMenuButtons() {
		return content.clone();
	}
}
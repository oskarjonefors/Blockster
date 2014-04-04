package edu.chalmers.Blockster.core.gdx.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import edu.chalmers.Blockster.core.Factory;
import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.gdx.view.GdxPlayer;

public class GdxFactory implements Factory {

	@Override
	public Player createPlayer(String spritePath) {
		return new GdxPlayer(new Sprite(new Texture(spritePath)));
	}

}

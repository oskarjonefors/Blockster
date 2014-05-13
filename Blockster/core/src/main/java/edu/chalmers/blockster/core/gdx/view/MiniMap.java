package edu.chalmers.blockster.core.gdx.view;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.blockster.core.objects.Block;
import edu.chalmers.blockster.core.objects.BlockMapListener;

public class MiniMap implements BlockMapListener {

	private Pixmap pixmap;
	private Sprite sprite;
	private final float scaleX;
	private final float scaleY;
	
	public MiniMap (Pixmap pixmap, float scaleX, float scaleY) {
		this.pixmap = pixmap;
		sprite = new Sprite(new Texture(pixmap));
		this.scaleX = scaleX;
		this.scaleY = scaleY;
	}
	
	public void setViewportBounds(int lowX, int highX, int lowY, int highY) {
		
	}

	public void draw(SpriteBatch batch) {
		batch.draw(sprite, 5, 5, sprite.getRegionWidth()*scaleX, sprite.getRegionHeight()*scaleY);
	}

	@Override
	public void blockInserted(int x, int y, Block block) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void blockRemoved(int x, int y) {
		// TODO Auto-generated method stub
		
	}
	
}

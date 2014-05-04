package edu.chalmers.Blockster.core.gdx.view;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.Blockster.core.NewBlock;

public class NewBlockView extends Actor {

	private NewBlock block;
	private TextureRegion region;
	
	public NewBlockView(NewBlock block){
		this.block = block;
		this.region = block.getTile().getTextureRegion();
		
	}
	
	public void draw(SpriteBatch batch, float alpha){
		batch.draw(region, block.getX(), block.getY());
		
	}
}

package edu.chalmers.Blockster.core.gdx.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.Blockster.core.AnimationState;
import edu.chalmers.Blockster.core.Movement;
import edu.chalmers.Blockster.core.Player;

public class PlayerView extends Actor{
	private Player player;
	private SpriteBatch batch;
	private TextureRegion activeSprite;
	private HashMap<Movement, Animation> arrayOfAnimation;
	
	public PlayerView(Player player, HashMap hashMap, TextureRegion texture){
		this.player = player;
		arrayOfAnimation = hashMap;
		activeSprite = texture; 
		
		float width = activeSprite.getRegionWidth();
		float height = activeSprite.getRegionHeight();
		setWidth(width);
		setHeight(height);
		setBounds(0, 0, width, height);
	}
	public void draw(SpriteBatch batch){
			activeSprite = chooseAnimation();
			batch.draw(activeSprite, player.getX(), player.getY(), getOriginX(), getOriginY(),
					getWidth(), getHeight(), getScaleX(), getScaleY(), getRotation());
			
	}
			
		
	public TextureRegion chooseAnimation(){
		AnimationState animState = player.getAnimationState();
		Movement move = animState.getMovement();
		
		return getCurrentAnimation(move, animState.getElapsedTime());
	}
	
	public TextureRegion getCurrentAnimation(Movement move, Float time){
		 
		return arrayOfAnimation.get(move).getKeyFrame(time, true);  
		
	}
	

}

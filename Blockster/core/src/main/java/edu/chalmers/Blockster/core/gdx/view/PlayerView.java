package edu.chalmers.Blockster.core.gdx.view;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.Blockster.core.objects.Player;
import edu.chalmers.Blockster.core.util.AnimationState;
import edu.chalmers.Blockster.core.util.Direction;
import edu.chalmers.Blockster.core.util.Movement;

public class PlayerView {
	private Player player;
	private Sprite sprite;
	private TextureRegion defaultSprite;
	private HashMap<Movement, Animation> arrayOfAnimation;
	private HashMap<Direction, Animation> walkAnimations;
	
	private float walkAnimTime;
	
	public PlayerView(Player player, HashMap<Movement, Animation> arrayOfAnimation,
			HashMap<Direction, Animation> walkAnimations, TextureRegion texture) {
		this.player = player;
		this.arrayOfAnimation = arrayOfAnimation;
		this.walkAnimations = walkAnimations;
		defaultSprite = texture; 
		
		sprite = new Sprite();
		setWidth(defaultSprite.getRegionWidth());
		setHeight(defaultSprite.getRegionHeight());
	}
	
	public void draw(SpriteBatch batch){
		sprite.setRegion(chooseAnimation());
		batch.draw(sprite, getX(), getY());
	}
			
		
	public TextureRegion chooseAnimation(){
		AnimationState animState = player.getAnimationState();
		Movement move = animState.getMovement();
		walkAnimTime += Gdx.graphics.getDeltaTime();
		if (move == Movement.NONE) {
			if (player.getDirection() == Direction.NONE) {
				return defaultSprite;
			} else {
				return walkAnimations.get(player.getDirection()).getKeyFrame(walkAnimTime, true);
			} 
		} else {
			/**
			return getCurrentAnimation(move, animState.getElapsedTime());
			*/
			return defaultSprite;
		}
	}
	
	public TextureRegion getCurrentAnimation(Movement move, Float time){
		return arrayOfAnimation.get(move).getKeyFrame(time, true);
	}
	
	public void setHeight(float height) {
		player.setHeight(height);
	}
	
	public void setWidth(float width) {
		player.setWidth(width);
	}
	
	public float getX() {
		return player.getX();
	}
	
	public float getY() {
		return player.getY();
	}

}

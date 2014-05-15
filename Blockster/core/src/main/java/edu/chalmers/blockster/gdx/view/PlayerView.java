package edu.chalmers.blockster.gdx.view;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerView {
	private final Player player;
	private final Sprite sprite;
	private final TextureRegion defaultSprite;
	private final Map<Movement, Animation> arrayOfAnimation;
	private final Map<Direction, Animation> walkAnimations;
	private Animation danceAnimation;
	
	private float walkAnimTime;
	
	public PlayerView(Player player, Map<Movement, Animation> arrayOfAnimation,
			Map<Direction, Animation> walkAnimations, TextureRegion texture) {
		this.player = player;
		this.arrayOfAnimation = arrayOfAnimation;
		this.walkAnimations = walkAnimations;
		defaultSprite = texture;
		
		sprite = new Sprite();
	}
	
	public void draw(SpriteBatch batch){
		TextureRegion region = chooseAnimation();
		int width = region.getRegionWidth(), height = region.getRegionHeight();
		
		sprite.setRegion(region);
		setSize(width, height);
		batch.draw(sprite, getX(), getY());
	}
			
		
	public TextureRegion chooseAnimation(){
		final AnimationState animState = player.getAnimationState();
		final Movement move = animState.getMovement();
		walkAnimTime += Gdx.graphics.getDeltaTime();
		if (move == Movement.NONE) {
			if (player.getDirection() == Direction.NONE ) {
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
		/*if (player.DANCE == true) {
			return danceAnimation.getKeyFrame(stateTime);
		}*/
	}
	
	public TextureRegion getCurrentAnimation(Movement move, Float time){
		return arrayOfAnimation.get(move).getKeyFrame(time, true);
	}
	
	public final void setSize(float width, float height) {
		sprite.setSize(width, height);
		player.setWidth(width);
		player.setHeight(width);
	}
	
	public float getX() {
		return player.getX();
	}
	
	public float getY() {
		return player.getY();
	}

}
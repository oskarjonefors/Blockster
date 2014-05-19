package edu.chalmers.blockster.gdx.view;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;

public class PlayerView {
	private final Player player;
	private final Sprite sprite;
	private TextureRegion standLeft, standRight;
	private final Map<AnimationState, Animation> arrayOfAnimation;
	private final Map<Direction, Animation> walkAnimations;
	
	private float animTime;
	
	public PlayerView(Player player, Map<AnimationState, Animation> arrayOfAnimation,
			Map<Direction, Animation> walkAnimations, TextureRegion texture) {
		this.player = player;
		this.arrayOfAnimation = arrayOfAnimation;
		this.walkAnimations = walkAnimations;
		standLeft = new TextureRegion(new Texture("Animations/standLeft.png"));
		standRight = new TextureRegion(new Texture("Animations/standRight.png"));
		
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
		animTime += Gdx.graphics.getDeltaTime();
		if (!player.isGrabbingBlock() && animState == AnimationState.NONE) {
			if (player.isMoving()) {
				return getWalkingPic();
			} else {
				return getStillPic();
			} 
		} else {
			return getAnimations(animState);
		}
	}
	private TextureRegion getAnimations(AnimationState state) {
		if (player.isGrabbingBlock() && state == AnimationState.NONE) {
			System.out.println("grabbing");
			
			return player.getDirection() == Direction.LEFT ?
					arrayOfAnimation.get(AnimationState.GRAB_RIGHT).getKeyFrame(animTime) :
					arrayOfAnimation.get(AnimationState.GRAB_LEFT).getKeyFrame(animTime) ;
		} else {
			return standRight;
		}
	}

	private TextureRegion getWalkingPic() {
		return walkAnimations.get(player.getDirection()).getKeyFrame(animTime, true);
	}
	
	private TextureRegion getStillPic() {
		switch (player.getDirection()) {
		
			case LEFT:	return standLeft; 
			case RIGHT:	return standRight;
			default:	return standRight;
			
		}
		
	}

	public TextureRegion getCurrentAnimation(AnimationState anim, Float time){
		return arrayOfAnimation.get(anim).getKeyFrame(time, true);
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

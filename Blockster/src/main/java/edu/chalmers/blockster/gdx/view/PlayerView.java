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
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerView {
	private final Player player;
	private final Sprite sprite;
	private TextureRegion standLeft, standRight;
	private final Map<Movement, Animation> arrayOfAnimation;
	private final Map<Direction, Animation> walkAnimations;
	
	private float walkAnimTime;
	
	public PlayerView(Player player, Map<Movement, Animation> arrayOfAnimation,
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
		final Movement move = animState.getMovement();
		walkAnimTime += Gdx.graphics.getDeltaTime();
		if (move == Movement.NONE) {
			if (player.isMoving()) {
				return walkAnimations.get(player.getDirection()).getKeyFrame(walkAnimTime, true);
			} else {
				return stillPic();
			} 
		} else {
			return standRight;
		}
	}
	
	private TextureRegion stillPic() {
		switch (player.getDirection()) {
		
			case LEFT:	return standLeft; 
		
			case RIGHT:	return standRight;
			
			default:	return standRight;
			
		}
		
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

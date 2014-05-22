package edu.chalmers.blockster.gdx.view;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.Player.World;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerView {

	private static final Logger LOG = Logger.getLogger(PlayerView.class.getName());

	private final Player player;
	private final Sprite sprite;
	private TextureRegion standLeft, standRight;
	private final Map<Movement, Animation> arrayOfAnimation;
	private final Map<Direction, Animation> walkAnimations;
	private float animTime;
	private Movement lastMovement;
	private Animation lastAnimation;
	
	public PlayerView(Player player, Map<Movement, Animation> arrayOfAnimation,
			Map<Direction, Animation> walkAnimations, TextureRegion texture) {
		this.player = player;
		this.arrayOfAnimation = arrayOfAnimation;
		this.walkAnimations = walkAnimations;
		
		final String prefix = player.getWorld() == World.DAY ? "" : "night_";
		Texture standPic = new Texture ("Animations/" + prefix + "stand.png");
		TextureRegion[][] standPics = TextureRegion.split(standPic, standPic.getWidth(), standPic.getHeight()/2);
		
		
		standLeft = standPics[1][0];
		standRight = standPics[0][0];

		sprite = new Sprite();
	}

	public void draw(SpriteBatch batch, boolean isActive){
		TextureRegion region = chooseAnimation();
		int width = region.getRegionWidth(), height = region.getRegionHeight();

		sprite.setRegion(region);
		setSize(width, height);

		if (!isActive) {
			final Color color = batch.getColor();
			final float a = color.a;
			color.a = a * 0.3f;
			batch.setColor(color);
			batch.draw(sprite, getX(), getY(), getX(), getY(), width, height, 1f, 1f, 0);
			color.a = a;
			batch.setColor(color);
		} else {
			batch.draw(sprite,  getX(),  getY());
		}
	}


	public TextureRegion chooseAnimation(){
		final AnimationState animState = player.getAnimationState();
		final Movement movement = animState.getMovement();
		animTime += Gdx.graphics.getDeltaTime();
		
		//Check if we change movement
//		if (movement != Movement.NONE && movement != Movement.WAIT && movement != null) {
//			System.out.println("Current movement is: " + movement.name());
//			if (movement != lastMovement) {
//				lastMovement = movement;
//				lastAnimation = arrayOfAnimation.get(lastMovement);
//				System.out.println("LastMovement turned in to: " +lastMovement);
//			}
//		} else if (player.getActiveAniRight() || player.getActiveAniLeft()
//				&& lastMovement == Movement.MOVE_LEFT || lastMovement == Movement.MOVE_RIGHT) {
//			
//			return lastAnimation.getKeyFrame(animTime, true);
//		}
//		
		if (!player.isGrabbingBlock() && movement == Movement.NONE) {
			if (player.isMoving()) {
				return getWalkingPic();
			} else {
				return getStillPic();
			} 
		} else {
			return getAnimations(movement);
		}
	}

	private TextureRegion getAnimations(Movement movement) {
//		System.out.println("Movement: " +movement.name());
		if (movement == Movement.PUSH_RIGHT || movement == Movement.PUSH_LEFT) {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime, true);

		} else if (player.isGrabbingBlock() && movement != Movement.PULL_LEFT && movement != Movement.PULL_RIGHT) {
			LOG.log(Level.INFO, "Grabbing");
			return player.getDirection() == Direction.LEFT ?
					arrayOfAnimation.get(Movement.GRAB_RIGHT).getKeyFrame(animTime):
						arrayOfAnimation.get(Movement.GRAB_LEFT).getKeyFrame(animTime);

		} else if (movement == Movement.MOVE_LEFT || movement == Movement.MOVE_RIGHT) {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime, true);

		} else if (movement == Movement.PLAYER_LIFT_LEFT || movement == Movement.PLAYER_LIFT_RIGHT
				|| movement == Movement.PLAYER_PUT_LEFT || movement == Movement.PLAYER_PUT_RIGHT) {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime);
			
		} else if (movement == Movement.PULL_LEFT || movement == Movement.PULL_RIGHT) {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime, false);
			
		} else {
			return standRight;
		}
	}

	private TextureRegion getWalkingPic() {
		return walkAnimations.get(player.getDirection()).getKeyFrame(animTime, true);
	}

	private TextureRegion getStillPic() {
		if (player.isLiftingBlock()) {
			return player.getDirection() == Direction.LEFT ? 
					arrayOfAnimation.get(Movement.MOVE_LEFT).getKeyFrame(animTime) :
						arrayOfAnimation.get(Movement.MOVE_RIGHT).getKeyFrame(animTime);
		}
		switch (player.getDirection()) {

		case LEFT:	return standLeft; 
		case RIGHT:	return standRight;
		default:	return standRight;

		}

	}

	public TextureRegion getCurrentAnimation(AnimationState anim, Float time){
		return arrayOfAnimation.get(anim.getMovement()).getKeyFrame(time, true);
	}

	public final void setSize(float width, float height) {
		sprite.setSize(width, height);
		player.setWidth(width);
		player.setHeight(width);
	}

	public Player getPlayer() {
		return player;
	}

	public float getX() {
		return player.getX();
	}

	public float getY() {
		return player.getY();
	}

}

package edu.chalmers.blockster.gdx.view;

import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.Player;
import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class PlayerView {

	private final Player player;
	private final Sprite sprite;
	private final TextureRegion standLeft;
	private final TextureRegion standRight;
	private final Map<Movement, Animation> arrayOfAnimation;
	private final Map<Direction, Animation> walkAnimations;
	private float animTime;
	private Movement lastMovement;

	public PlayerView(Player player, Map<Movement, Animation> arrayOfAnimation,
			Map<Direction, Animation> walkAnimations) {
		this.player = player;
		this.arrayOfAnimation = arrayOfAnimation;
		this.walkAnimations = walkAnimations;

		final String prefix = player.getWorld() == World.DAY ? "" : "night_";
		final Texture standPic = new Texture ("Animations/" + prefix + "stand.png");
		final TextureRegion[][] standPics = TextureRegion.split(standPic, standPic.getWidth(), standPic.getHeight()/2);

		standLeft = standPics[1][0];
		standRight = standPics[0][0];

		sprite = new Sprite();
	}

	public void draw(SpriteBatch batch, boolean isActive){
		final TextureRegion region = chooseAnimation();
		final int width = region.getRegionWidth();
		final int height = region.getRegionHeight();

		sprite.setRegion(region);
		setSize(width, height);

		if (isActive) {
			batch.draw(sprite,  getX(),  getY());
		} else {
			final Color color = batch.getColor();
			final float a = color.a;
			color.a = a * 0.3f;
			batch.setColor(color);
			batch.draw(sprite, getX(), getY(), getX(), getY(), width, height, 1f, 1f, 0);
			color.a = a;
			batch.setColor(color);
		}
	}


	public TextureRegion chooseAnimation(){
		final Movement movement = player.getAnimationState().getMovement();
		animTime += Gdx.graphics.getDeltaTime();

		if (!player.isGrabbingBlock() && movement == Movement.NONE ||
				player.isLiftingOrPlacing()) {
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
		if (movement == Movement.PULL_LEFT || movement == Movement.PULL_RIGHT ||
				movement == Movement.PUSH_LEFT || movement == Movement.PUSH_RIGHT) {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime, true);
		} else if ( movement == Movement.FALL_DOWN) {
			return getFallAnim();
		} else { 
			return getMovingAnim(movement);  		
		}
	}

	private TextureRegion getMovingAnim(Movement movement) {
		if (player.isGrabbingBlock()) { 
			return arrayOfAnimation.get(lastMovement == Movement.GRAB_LEFT ? Movement.GRAB_LEFT :Movement.GRAB_RIGHT).getKeyFrame(animTime);
		} else {
			return arrayOfAnimation.get(movement).getKeyFrame(animTime, true);
		}
	}

	private TextureRegion getFallAnim() {
		Movement move = player.getDirection() == Direction.LEFT ? Movement.MOVE_LEFT : Movement.MOVE_RIGHT;
		return arrayOfAnimation.get(move).getKeyFrame(animTime, true);
	}

	private TextureRegion getWalkingPic() {
		lastMovement = player.getDirection() == Direction.LEFT ? Movement.GRAB_LEFT : Movement.GRAB_RIGHT;
		return walkAnimations.get(player.getDirection()).getKeyFrame(animTime, true);
	}

	private TextureRegion getStillPic() {
		if (player.isLiftingBlock()) {
			Movement move = Movement.getMoveMovement(player.getDirection());
			return arrayOfAnimation.get(move).getKeyFrame(animTime);
		} else {
			return player.getDirection() == Direction.LEFT ? standLeft : standRight;
		}
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

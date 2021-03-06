package edu.chalmers.blockster.core.objects;

import javax.vecmath.Vector2f;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;

public abstract class AbstractBlocksterObject extends ScaledObject {

	private float totalTime;
	private final Vector2f velocity;
	protected Vector2f defaultVelocity;
	private Direction dir = Direction.NONE;
	protected AnimationState anim;
	protected BlockMap blockMap;
	protected boolean directionChanged;

	public AbstractBlocksterObject(float startX, float startY, BlockMap blockMap,
			float scaleX, float scaleY) {
		super(startX, startY, scaleX, scaleY);
		this.blockMap = blockMap;
		anim = AnimationState.NONE;
		velocity = new Vector2f(0, 0);
	}

	/**
	 * This method is used when pulling a block and checks if the player can
	 * continue to pull it or if there is something blocking the way (this is
	 * usually taken care of by the collision avoidance, but when moving a block
	 * then this isn't available).
	 * 
	 * @param movement
	 * @return true if nothing is blocking the way behind player, else false.
	 */
	public abstract boolean canMove(Direction dir);

	public AnimationState getAnimationState() {
		return anim;
	}

	public BlockMap getBlockMap() {
		return blockMap;
	}

	public Direction getDirection() {
		return dir;
	}

	public float getOriginX() {
		return super.getX();
	}

	public float getOriginY() {
		return super.getY();
	}

	public Vector2f getVelocity() {
		return velocity;
	}

	public float getX() {
		// If there is an animation currently running then
		// we want to return the relative position
		if (anim != AnimationState.NONE) {
			return getOriginX() + anim.getRelativePosition().x * getScaleX();
		}
		return getOriginX();
	}

	public float getY() {
		if (anim != AnimationState.NONE) {
			return getOriginY() + anim.getRelativePosition().y * getScaleY();
		}
		return getOriginY();
	}

	public void increaseGravity(float deltaTime) {
		totalTime += deltaTime;
		setVelocityY(-9.82F * totalTime * getBlockMap().getBlockHeight());
	}

	public void moveToNextPosition() {
		final Direction direction = anim.getMovement().getDirection();
		final float scaleY = getScaleY();
		setX(getOriginX() + direction.getDeltaX() * getScaleX());
		setY(Math.round(getOriginY() / scaleY) * scaleY +
										direction.getDeltaY() * scaleY);
	}

	public void resetGravity() {
		totalTime = 0;
	}

	public void setAnimationState(AnimationState anim) {
		if (this.anim.isDone()) {
			this.anim = anim;
		}
	}

	public void setDefaultVelocity(Direction dir) {
		final Vector2f vel = getVelocity();
		setVelocityX(vel.x + dir.getDeltaX() * defaultVelocity.x);
		setVelocityY(vel.y + dir.getDeltaY() * defaultVelocity.y);
	}

	public void setDirection(Direction dir) {
		directionChanged |= this.dir != dir;
		this.dir = dir;
	}

	public void setVelocityX(float velocityX) {
		if (Math.abs(velocityX) > defaultVelocity.x) {
			velocity.x = Math.signum(velocityX) * defaultVelocity.x;
		} else {
			velocity.x = velocityX;
		}
	}

	public void setVelocityY(float velocityY) {
		if (Math.abs(velocityY) > defaultVelocity.y) {
			velocity.y = Math.signum(velocityY) * defaultVelocity.y;
		} else {
			velocity.y = velocityY;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": ("
				+ Math.round(getX() * 10) / (10 * getScaleX()) + ", "
				+ Math.round(getY() * 10) / (10 * getScaleY()) + ")";
	}

}
package edu.chalmers.Blockster.core.gdx.view;

import static java.lang.Math.*;

import javax.vecmath.Vector2f;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.util.Direction;

public class GdxPlayer extends Actor implements Player {
	
	private float maximumMovementSpeed = 700;
	private Vector2f velocity = new Vector2f();
	private Vector2f gravity = new Vector2f();
	private float gravityTime = 0;
	private float x;
	private float y;
	private TextureRegion region;
	
	public GdxPlayer (TextureRegion region) {
		this.region = region;
		
		float x = 0;
		float y = 0;
		float tw = region.getRegionWidth();
		float th = region.getRegionHeight();
		setWidth(tw);
		setHeight(th);
		setBounds(0, 0, tw, th);
	}
	
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
		batch.draw(region, getX(), getY(), getOriginX(), getOriginY(),
				region.getRegionWidth(), region.getRegionHeight(),
				getScaleX(), getScaleY(), getRotation());
	}
	
	
	@Override
	public Vector2f getGravity() {
		return gravity;
	}
	@Override
	public float getMaximumMovementSpeed() {
		return maximumMovementSpeed;
	}
	
	@Override
	public Vector2f getVelocity(){
		return velocity;
	}
	
	@Override
	public void increaseGravity(float deltaTime) {
		gravityTime += deltaTime;
		gravity.y = 9.82F * gravityTime;
	}
	
	@Override
	public void move(Direction dir, float distance) {
		setVelocityX(getVelocity().x + dir.deltaX * distance);
		setVelocityY(getVelocity().y + dir.deltaY * distance);
	}
	
	@Override
	public void resetGravity() {
		gravity.y = 0;
		gravityTime = 0;
	}
	
	@Override
	public void setVelocityX(float velX){
		velocity.x = min(velX, maximumMovementSpeed);
	}
	
	/**
	 * sets the velocity of the player
	 * @param velY
	 */
	@Override
	public void setVelocityY(float velY){
		velocity.y = min(velY, maximumMovementSpeed);
	}


	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return region.getRegionHeight();
	}


	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return region.getRegionWidth();
	}


	@Override
	public float getX() {
		// TODO Auto-generated method stub
		return x;
	}


	@Override
	public float getY() {
		// TODO Auto-generated method stub
		return y;
	}


	@Override
	public void setX(float x) {
		this.x = x;
	}


	@Override
	public void setY(float y) {
		this.y = y;
	}
}

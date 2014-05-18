package edu.chalmers.blockster.gdx.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;

public class AnimationFactory {

	private final Map<AnimationState, Animation> arrayOfAnimations;
	private final Map<Direction, Animation> walkAnimations;
	private final Animation bluePortalAnimations, yellowPortalAnimations;
	
	private final float walkAnimTime = 0.12f;
	private final int nbrWalkPicWidth = 7, nbrWalkPicHeight = 2;
	
	private final float portalAnimTime = 0.1f;
	private final int nbrPortalPicWidth = 4, nbrPortalPicHeigth = 2;
	
	private final float grabTime = 0.2f;
	private final int nbrGrabPicWidth = 3, nbrGrabPicHeight = 2;
	
	public AnimationFactory() {
		
		walkAnimations = new HashMap<Direction, Animation>();
		arrayOfAnimations = new HashMap<AnimationState, Animation>();
	
		Texture playerWalk = new Texture(Gdx.files.internal("Animations/walk_animation1.png"));
		Texture bluePortal = new Texture(Gdx.files.internal("Animations/blue_portal.png"));
		Texture yellowPortal = new Texture(Gdx.files.internal("Animations/yellow_portal.png"));
		Texture grab = new Texture(Gdx.files.internal("Animations/grab_animation.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] walkPics = TextureRegion.split(playerWalk, 
				playerWalk.getWidth()/nbrWalkPicWidth, playerWalk.getHeight()/nbrWalkPicHeight);
		
		TextureRegion[][] bluePortalPics = TextureRegion.split(bluePortal, bluePortal.getWidth()/nbrPortalPicWidth,
				bluePortal.getHeight()/nbrPortalPicHeigth);
		
		TextureRegion[][] yellowPortalPics = TextureRegion.split(yellowPortal, yellowPortal.getWidth()/nbrPortalPicWidth,
				yellowPortal.getHeight()/nbrPortalPicHeigth);
		
		TextureRegion[][] grabPics = TextureRegion.split(grab, grab.getWidth()/nbrGrabPicWidth, grab.getHeight()/nbrGrabPicHeight);
		
			
		TextureRegion[] tempWalkLeft = new TextureRegion[nbrWalkPicWidth];
		TextureRegion[] tempWalkRight = new TextureRegion[nbrWalkPicWidth];
		TextureRegion[] tempBluePortal = new TextureRegion[nbrPortalPicWidth*nbrPortalPicHeigth];
		TextureRegion[] tempYellowPortal = new TextureRegion[nbrPortalPicWidth*nbrPortalPicHeigth];
		TextureRegion[] tempGrabLeft = new TextureRegion[nbrGrabPicWidth];
		TextureRegion[] tempGrabRight = new TextureRegion[nbrGrabPicWidth];
		
		
		// Get walk animations
		for (int i = 0; i < nbrWalkPicWidth; i++) {
			tempWalkRight[i] = walkPics[0][i];
			tempWalkLeft[i] = walkPics[1][i];
		}
		
		int index = 0;
		
		// Get portal Animations
		for (int i = 0; i < nbrPortalPicHeigth; i++) {
			for (int j = 0; j < nbrPortalPicWidth; j++) {
				tempBluePortal[index] = bluePortalPics[i][j];
				tempYellowPortal[index] = yellowPortalPics[i][j];
				index += 1;
			}
		}
		//Get grab Animations
		for (int i = 0; i < nbrGrabPicWidth; i++) {
				tempGrabLeft[i] = grabPics[0][i];
				tempGrabRight[i] = grabPics[1][i];
		}
		
		// Set Portal Animations
		bluePortalAnimations = new Animation(portalAnimTime, tempBluePortal);
		yellowPortalAnimations = new Animation(portalAnimTime, tempYellowPortal);
		
		// Set arrayOfAnimation
		Animation ani = new Animation(walkAnimTime, tempWalkLeft);
		ani.setPlayMode(Animation.LOOP_REVERSED);
		
		Animation grabLeft = new Animation(grabTime, tempGrabLeft);
		Animation grabRight = new Animation(grabTime, tempGrabRight);
		grabRight.setPlayMode(Animation.REVERSED);
		
		/**
		 * link the the AnimationState to the correct animation
		 */
		arrayOfAnimations.put(AnimationState.GRAB_LEFT, grabLeft);
		arrayOfAnimations.put(AnimationState.GRAB_RIGHT, grabRight);
		
		
		walkAnimations.put(Direction.LEFT, ani);
		walkAnimations.put(Direction.RIGHT, new Animation(walkAnimTime, tempWalkRight));
	}
	
	public Map<Direction, Animation> getWalkAnimations(){
		return walkAnimations;
	}
	
	public Map<AnimationState, Animation> getArrayOfAnimations(){
		return arrayOfAnimations;
	}

	public Animation getPortalAnimation(int color) {
		return color == 0 ? bluePortalAnimations : yellowPortalAnimations;
	}
	
}

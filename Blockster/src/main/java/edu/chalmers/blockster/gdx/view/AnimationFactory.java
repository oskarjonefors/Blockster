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
	
	private static final float WALK_ANIM_TIME = 0.12f;
	private static final int NBR_WALK_PIC_WIDTH = 7, NBR_WALK_PIC_HEIGHT = 2;
	private static final float PORTAL_ANIM_TIME = 0.1f;
	private static final int NBR_PORTAL_PIC_WIDTH = 4, NBR_PORTAL_PIC_HEIGHT = 2;
	private static final float GRAB_TIME = 0.2f;
	private static final int NBR_GRAB_PIC_WIDTH = 3, NBR_GRAB_PIC_HEIGHT = 2;
	
	public AnimationFactory() {
		
		walkAnimations = new HashMap<Direction, Animation>();
		arrayOfAnimations = new HashMap<AnimationState, Animation>();
	
		Texture playerWalk = new Texture(Gdx.files.internal("Animations/walk_animation1.png"));
		Texture bluePortal = new Texture(Gdx.files.internal("Animations/blue_portal.png"));
		Texture yellowPortal = new Texture(Gdx.files.internal("Animations/yellow_portal.png"));
		Texture grab = new Texture(Gdx.files.internal("Animations/grab_animation.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] walkPics = TextureRegion.split(playerWalk, 
				playerWalk.getWidth()/NBR_WALK_PIC_WIDTH, playerWalk.getHeight()/NBR_WALK_PIC_HEIGHT);
		
		TextureRegion[][] bluePortalPics = TextureRegion.split(bluePortal, bluePortal.getWidth()/NBR_PORTAL_PIC_WIDTH,
				bluePortal.getHeight()/NBR_PORTAL_PIC_HEIGHT);
		
		TextureRegion[][] yellowPortalPics = TextureRegion.split(yellowPortal, yellowPortal.getWidth()/NBR_PORTAL_PIC_WIDTH,
				yellowPortal.getHeight()/NBR_PORTAL_PIC_HEIGHT);
		
		TextureRegion[][] grabPics = TextureRegion.split(grab, grab.getWidth()/NBR_GRAB_PIC_WIDTH, grab.getHeight()/NBR_GRAB_PIC_HEIGHT);
		
			
		TextureRegion[] tempWalkLeft = new TextureRegion[NBR_WALK_PIC_WIDTH];
		TextureRegion[] tempWalkRight = new TextureRegion[NBR_WALK_PIC_WIDTH];
		TextureRegion[] tempBluePortal = new TextureRegion[NBR_PORTAL_PIC_WIDTH*NBR_PORTAL_PIC_HEIGHT];
		TextureRegion[] tempYellowPortal = new TextureRegion[NBR_PORTAL_PIC_WIDTH*NBR_PORTAL_PIC_HEIGHT];
		TextureRegion[] tempGrabLeft = new TextureRegion[NBR_GRAB_PIC_WIDTH];
		TextureRegion[] tempGrabRight = new TextureRegion[NBR_GRAB_PIC_WIDTH];
		
		
		// Get walk animations
		for (int i = 0; i < NBR_WALK_PIC_WIDTH; i++) {
			tempWalkRight[i] = walkPics[0][i];
			tempWalkLeft[i] = walkPics[1][i];
		}
		
		int index = 0;
		
		// Get portal Animations
		for (int i = 0; i < NBR_PORTAL_PIC_HEIGHT; i++) {
			for (int j = 0; j < NBR_PORTAL_PIC_WIDTH; j++) {
				tempBluePortal[index] = bluePortalPics[i][j];
				tempYellowPortal[index] = yellowPortalPics[i][j];
				index += 1;
			}
		}
		//Get grab Animations
		for (int i = 0; i < NBR_GRAB_PIC_WIDTH; i++) {
				tempGrabLeft[i] = grabPics[0][i];
				tempGrabRight[i] = grabPics[1][i];
		}
		
		// Set Portal Animations
		bluePortalAnimations = new Animation(PORTAL_ANIM_TIME, tempBluePortal);
		yellowPortalAnimations = new Animation(PORTAL_ANIM_TIME, tempYellowPortal);
		
		// Set arrayOfAnimation
		Animation ani = new Animation(WALK_ANIM_TIME, tempWalkLeft);
		ani.setPlayMode(Animation.LOOP_REVERSED);
		
		Animation grabLeft = new Animation(GRAB_TIME, tempGrabLeft);
		Animation grabRight = new Animation(GRAB_TIME, tempGrabRight);
		grabRight.setPlayMode(Animation.REVERSED);
		
		/**
		 * link the the AnimationState to the correct animation
		 */
		arrayOfAnimations.put(AnimationState.GRAB_LEFT, grabLeft);
		arrayOfAnimations.put(AnimationState.GRAB_RIGHT, grabRight);
		
		
		walkAnimations.put(Direction.LEFT, ani);
		walkAnimations.put(Direction.RIGHT, new Animation(WALK_ANIM_TIME, tempWalkRight));
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

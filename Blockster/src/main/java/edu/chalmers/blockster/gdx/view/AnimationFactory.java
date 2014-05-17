package edu.chalmers.blockster.gdx.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class AnimationFactory {

	private final Map<Movement, Animation> arrayOfAnimations;
	private final Map<Direction, Animation> walkAnimations;
	private final Animation bluePortalAnimations, yellowPortalAnimations;
	
	private float walkAnimTime = 0.12f;
	private int nbrWalkPicWidth = 7, nbrWalkPicHeight = 2;
	
	private float portalAnimTime = 0.2f;
	private int nbrPortalPicWidth = 4, nbrPortalPicHeigth = 2;
	
	
	
	
	public AnimationFactory(){
		
		walkAnimations = new HashMap<Direction, Animation>();
		arrayOfAnimations = new HashMap<Movement, Animation>();
	
		Texture playerWalk = new Texture(Gdx.files.internal("Animations/walk_animation1.png"));
		Texture bluePortal = new Texture(Gdx.files.internal("Animations/blue_portal.png"));
		Texture yellowPortal = new Texture(Gdx.files.internal("Animations/yellow_portal.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] walkPics = TextureRegion.split(playerWalk, 
				playerWalk.getWidth()/nbrWalkPicWidth, playerWalk.getHeight()/nbrWalkPicHeight);
		
		TextureRegion[][] bluePortalPics = TextureRegion.split(bluePortal, bluePortal.getWidth()/nbrPortalPicWidth,
				bluePortal.getWidth()/nbrPortalPicHeigth);
		
		TextureRegion[][] yellowPortalPics = TextureRegion.split(yellowPortal, yellowPortal.getWidth()/nbrPortalPicWidth,
				yellowPortal.getHeight()/nbrPortalPicHeigth);
		
			
		TextureRegion[] tempWalkLeft = new TextureRegion[nbrWalkPicWidth];
		TextureRegion[] tempWalkRight = new TextureRegion[nbrWalkPicWidth];
		TextureRegion[] tempBluePortal = new TextureRegion[nbrPortalPicWidth*nbrPortalPicHeigth];
		TextureRegion[] tempYellowPortal = new TextureRegion[nbrPortalPicWidth*nbrPortalPicHeigth];
		
		
		//Walk animations
		for (int i = 0; i < nbrWalkPicWidth; i++) {
			tempWalkRight[i] = walkPics[0][i];
			tempWalkLeft[i] = walkPics[1][i];
		}
		int index = 0;
		// Portal Animations
		for(int i = 0; i < nbrPortalPicHeigth - 1; i++) {
			for(int j = 0; j < nbrPortalPicWidth - 1; j++) {
				tempBluePortal[index++] = bluePortalPics[i][j];
				tempYellowPortal[index++] = yellowPortalPics[i][j];
			}
		}
		
		bluePortalAnimations = new Animation(portalAnimTime, tempBluePortal);
		yellowPortalAnimations = new Animation(portalAnimTime, tempYellowPortal);
		
		/**
		 * link the the Movement to the correct animation
		 */
		Animation ani = new Animation(walkAnimTime, tempWalkLeft);
		ani.setPlayMode(Animation.LOOP_REVERSED);
		walkAnimations.put(Direction.LEFT, ani);
		walkAnimations.put(Direction.RIGHT, new Animation(walkAnimTime, tempWalkRight));
		
	}
	
	public Map<Direction, Animation> getWalkAnimations(){
		return walkAnimations;
	}
	
	public Map<Movement, Animation> getArrayOfAnimations(){
		return arrayOfAnimations;
	}

	public Animation getPortalAnimation(int color) {
		return color == 0 ? bluePortalAnimations : yellowPortalAnimations;
	}
	
}

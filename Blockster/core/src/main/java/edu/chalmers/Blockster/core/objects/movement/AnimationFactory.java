package edu.chalmers.Blockster.core.objects.movement;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationFactory {

	private HashMap<Movement, Animation> arrayOfAnimations;
	private HashMap<Direction, Animation> walkAnimations;
	
	private float picHeight = 50f;
	private float picWidth = 32f;
	private float animTime = 0.25f;
	private int nbrPicWidth = 4;
	private int nbrPicHeight = 3;
	private int nbrsOfAnimations = 10;
	
	
	
	public AnimationFactory(){
		
		walkAnimations = new HashMap<Direction, Animation>();
		arrayOfAnimations = new HashMap<Movement, Animation>();
	
		Texture playerWalk = new Texture(Gdx.files.internal("Animations/playerWalk.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] allPics = TextureRegion.split(playerWalk, 
				playerWalk.getWidth()/nbrPicWidth, playerWalk.getHeight()/nbrPicHeight);
		
			
		TextureRegion[] walkLeft = new TextureRegion[nbrPicWidth];
		TextureRegion[] walkRight = new TextureRegion[nbrPicWidth];
		TextureRegion[] liftLeft = new TextureRegion[nbrPicWidth];
		TextureRegion[] liftRight = new TextureRegion[nbrPicWidth];
		TextureRegion[] climbLeft = new TextureRegion[nbrPicWidth];
		TextureRegion[] climbRight = new TextureRegion[nbrPicWidth];
		TextureRegion[] pullLeft = new TextureRegion[nbrPicWidth];
		TextureRegion[] pullRight = new TextureRegion[nbrPicWidth];
		TextureRegion[] pushLeft = new TextureRegion[nbrPicWidth];
		TextureRegion[] pushRight = new TextureRegion[nbrPicWidth];
		
			
		for (int i = 0; i < nbrPicWidth; i++){
			walkLeft[i] = allPics[0][i];
			walkRight[i] = allPics[1][i];
			/**
			liftLeft[i] = allPics[2][i];
			liftRight[i] = allPics[3][i];
			climbLeft[i] = allPics[4][i];
			climbRight[i] = allPics[5][i];
			pullLeft[i] = allPics[6][i];
			pullRight[i] = allPics[7][i];
			pushLeft[i] = allPics[8][i];
			pushRight[i] = allPics[9][i];
			*/
		}
		
		
		/**
		 * link the the Movement to the correct animation
		 */
		walkAnimations.put(Direction.LEFT, new Animation(animTime, walkLeft));
		walkAnimations.put(Direction.RIGHT, new Animation(animTime, walkRight));
		
		arrayOfAnimations.put(Movement.LIFT_LEFT, new Animation(animTime, liftLeft));
		arrayOfAnimations.put(Movement.LIFT_RIGHT, new Animation(animTime, liftRight));
		arrayOfAnimations.put(Movement.PULL_LEFT, new Animation(animTime, liftRight));
		
	}
	
	public HashMap<Direction, Animation> getWalkAnimations(){
		return walkAnimations;
	}
	
	public HashMap<Movement, Animation> getArrayOfAnimations(){
		return arrayOfAnimations;
	}
	
}

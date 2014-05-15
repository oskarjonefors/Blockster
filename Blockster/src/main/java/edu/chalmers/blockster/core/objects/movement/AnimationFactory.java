package edu.chalmers.blockster.core.objects.movement;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationFactory {

	private final Map<Movement, Animation> arrayOfAnimations;
	private final Map<Direction, Animation> walkAnimations;
	
	private float animTime = 0.12f;
	private int nbrPicWidth = 7;
	private int nbrPicHeight = 2;
	private int nbrsOfAnimations = 10;
	
	
	
	public AnimationFactory(){
		
		walkAnimations = new HashMap<Direction, Animation>();
		arrayOfAnimations = new HashMap<Movement, Animation>();
	
		Texture playerWalk = new Texture(Gdx.files.internal("Animations/walk_animation1.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] walkPics = TextureRegion.split(playerWalk, 
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
			walkRight[i] = walkPics[0][i];
			walkLeft[i] = walkPics[1][i];
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
		Animation ani = new Animation(animTime, walkLeft);
		ani.setPlayMode(Animation.LOOP_REVERSED);
		walkAnimations.put(Direction.LEFT, ani);
		walkAnimations.put(Direction.RIGHT, new Animation(animTime, walkRight));
		
	}
	
	public Map<Direction, Animation> getWalkAnimations(){
		return walkAnimations;
	}
	
	public Map<Movement, Animation> getArrayOfAnimations(){
		return arrayOfAnimations;
	}
	
}

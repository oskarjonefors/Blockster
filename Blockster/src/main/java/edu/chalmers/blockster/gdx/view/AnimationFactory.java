package edu.chalmers.blockster.gdx.view;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import edu.chalmers.blockster.core.objects.World;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.objects.movement.Movement;

public class AnimationFactory {

	private final Map<Movement, Animation> dayArrayOfAnimations;
	private final Map<Movement, Animation> nightArrayOfAnimations;
	private final Map<Direction, Animation> dayWalkAnimations;
	private final Map<Direction, Animation> nightWalkAnimations;
	private final Animation bluePortalAnimations, yellowPortalAnimations;

	private static final float ZERO_ONE_TIME = 0.1f;
	private static final float ZERO_THREE_TIME = 0.3f;
	
	private static final int NBR_PIC_HEIGHT = 2;
	private static final int NBR_PORTAL_PIC_WIDTH = 4;
	private static final int NBR_WALK_PIC_WIDTH = 7;
	
	private static final int TWO_PIC_WIDTH = 2;
	private static final int THREE_PIC_WIDTH = 3;
	private static final int SIX_PIC_WIDTH = 6;
	
	
	public AnimationFactory() {

		dayWalkAnimations = generateWalkMap(World.DAY);
		nightWalkAnimations = generateWalkMap(World.NIGHT);
		dayArrayOfAnimations = generateAnimationMap(World.DAY);
		nightArrayOfAnimations = generateAnimationMap(World.NIGHT);

		Texture bluePortal = new Texture(Gdx.files.internal("Animations/blue_portal.png"));
		Texture yellowPortal = new Texture(Gdx.files.internal("Animations/yellow_portal.png"));
		
		//split all the images into the TextureRegion matrix
		TextureRegion[][] bluePortalPics = TextureRegion.split(bluePortal, bluePortal.getWidth()/NBR_PORTAL_PIC_WIDTH,
				bluePortal.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[][] yellowPortalPics = TextureRegion.split(yellowPortal, yellowPortal.getWidth()/NBR_PORTAL_PIC_WIDTH,
				yellowPortal.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[] tempBluePortal = new TextureRegion[NBR_PORTAL_PIC_WIDTH*NBR_PIC_HEIGHT];
		TextureRegion[] tempYellowPortal = new TextureRegion[NBR_PORTAL_PIC_WIDTH*NBR_PIC_HEIGHT];




		int index = 0;
		// Get portal Animations
		for (int i = 0; i < NBR_PIC_HEIGHT; i++) {
			for (int j = 0; j < NBR_PORTAL_PIC_WIDTH; j++) {
				tempBluePortal[index] = bluePortalPics[i][j];
				tempYellowPortal[index] = yellowPortalPics[i][j];
				index += 1;
			}
		}

		// Set Portal Animations
		bluePortalAnimations = new Animation(ZERO_ONE_TIME, tempBluePortal);
		yellowPortalAnimations = new Animation(ZERO_ONE_TIME, tempYellowPortal);
	}

	private Map<Movement, Animation> generateAnimationMap(World world) {

		final Map<Movement, Animation> animationMap = new HashMap<Movement, Animation>();
		final String prefix = world == World.DAY ? "Animations/" : "Animations/night_";

		Texture grab = new Texture(Gdx.files.internal(prefix + "grab_animation.png"));
		Texture push = new Texture(Gdx.files.internal(prefix + "push_animation.png"));
		Texture pull = new Texture(Gdx.files.internal(prefix + "pull_animation.png"));
		Texture carry = new Texture(Gdx.files.internal(prefix + "lift_animation.png"));
		Texture jump = new Texture(Gdx.files.internal(prefix + "jump.png"));
		Texture liftJump = new Texture(Gdx.files.internal(prefix + "jump_with_block.png"));
		

		TextureRegion[][] grabPics = TextureRegion.split(grab, grab.getWidth()/THREE_PIC_WIDTH, grab.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[][] pushPics = TextureRegion.split(push, push.getWidth()/SIX_PIC_WIDTH, push.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[][] carryPics = TextureRegion.split(carry, carry.getWidth()/SIX_PIC_WIDTH, carry.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[][] jumpPics = TextureRegion.split(jump, jump.getWidth()/TWO_PIC_WIDTH, jump.getHeight()/NBR_PIC_HEIGHT);
		
		TextureRegion[][] jumpWithBlockPics = TextureRegion.split(liftJump, liftJump.getWidth()/TWO_PIC_WIDTH, liftJump.getHeight()/NBR_PIC_HEIGHT);
		
		TextureRegion[][] pullPics = TextureRegion.split(pull, pull.getWidth()/SIX_PIC_WIDTH, pull.getHeight()/NBR_PIC_HEIGHT);

		TextureRegion[] tempGrabLeft = new TextureRegion[THREE_PIC_WIDTH];
		TextureRegion[] tempGrabRight = new TextureRegion[THREE_PIC_WIDTH];
		TextureRegion[] tempPushLeft = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempPushRight = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempCarryLeft = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempCarryRight = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempPullLeft = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempPullRight = new TextureRegion[SIX_PIC_WIDTH];
		TextureRegion[] tempJumpLeft = new TextureRegion[TWO_PIC_WIDTH];
		TextureRegion[] tempJumpRight = new TextureRegion[TWO_PIC_WIDTH];
		TextureRegion[] tempLiftJumpLeft = new TextureRegion[TWO_PIC_WIDTH];
		TextureRegion[] tempLiftJumpRight = new TextureRegion[TWO_PIC_WIDTH];
		
		//Get grab&Lift Animations
		for (int i = 0; i < THREE_PIC_WIDTH; i++) {
			tempGrabLeft[i] = grabPics[0][i];
			tempGrabRight[i] = grabPics[1][i];
		}

		// Get push and carry Animations
		for (int i = 0; i < SIX_PIC_WIDTH; i++) {
			tempPushRight[i] = pushPics[0][i];
			tempPushLeft[i] = pushPics[1][i];

			tempCarryRight[i] = carryPics[0][i];
			tempCarryLeft[i] = carryPics[1][i];
			
			tempPullLeft[i] = pullPics[0][i];
			tempPullRight[i] = pullPics[1][i];
					
		}
		
		for (int i = 0; i < TWO_PIC_WIDTH; i++) {
			tempJumpRight[i] = jumpPics[0][i];
			tempJumpLeft[i] = jumpPics[1][i];
			
			tempLiftJumpRight[i] = jumpWithBlockPics[0][i];  
			tempLiftJumpLeft[i] = jumpWithBlockPics[1][i];
		}

		// Set arrayOfAnimation
		Animation grabLeft = new Animation(ZERO_ONE_TIME, tempGrabLeft);
		Animation grabRight = new Animation(ZERO_ONE_TIME, tempGrabRight);
		grabRight.setPlayMode(Animation.REVERSED);

		Animation pushLeft = new Animation(ZERO_ONE_TIME, tempPushLeft);
		Animation pushRight = new Animation(ZERO_ONE_TIME, tempPushRight);
		
		Animation pullRight = new Animation(ZERO_ONE_TIME, tempPullRight);
		Animation pullLeft = new Animation(ZERO_ONE_TIME, tempPullLeft);

		Animation carryLeft = new Animation(ZERO_ONE_TIME, tempCarryLeft);
		Animation carryRight = new Animation(ZERO_ONE_TIME, tempCarryRight);
		carryLeft.setPlayMode(Animation.REVERSED);
		
		Animation jumpLeft = new Animation(ZERO_ONE_TIME, tempJumpLeft);
		Animation jumpRight = new Animation(ZERO_ONE_TIME, tempJumpRight);
		
		Animation liftJumpLeft = new Animation(ZERO_ONE_TIME, tempLiftJumpLeft);
		Animation liftJumpRight = new Animation(ZERO_ONE_TIME, tempLiftJumpRight);
		
		/**
		 * link the the AnimationState to the correct animation
		 */
		animationMap.put(Movement.GRAB_LEFT, grabLeft);
		animationMap.put(Movement.GRAB_RIGHT, grabRight);

		animationMap.put(Movement.PUSH_LEFT, pushLeft);
		animationMap.put(Movement.PUSH_RIGHT, pushRight);
		
		animationMap.put(Movement.PULL_RIGHT, pullRight);
		animationMap.put(Movement.PULL_LEFT, pullLeft);
		
		animationMap.put(Movement.MOVE_LEFT, carryLeft);
		animationMap.put(Movement.MOVE_RIGHT, carryRight);

		animationMap.put(Movement.CLIMB_LEFT, jumpLeft);
		animationMap.put(Movement.CLIMB_RIGHT, jumpRight);
		
		animationMap.put(Movement.LIFTING_CLIMB_RIGHT, liftJumpRight);
		animationMap.put(Movement.LIFTING_CLIMB_LEFT, liftJumpLeft);
		
		animationMap.put(Movement.CLIMB_DOWN_LEFT, liftJumpLeft);
		animationMap.put(Movement.CLIMB_DOWN_RIGHT, liftJumpRight);
		
		animationMap.put(Movement.PLAYER_PLACE_LEFT, carryLeft);
		animationMap.put(Movement.PLAYER_PLACE_RIGHT, carryRight);
		

		return animationMap;
	}

	private Map<Direction, Animation> generateWalkMap(World world) {
		final Map<Direction, Animation> walkMap = new HashMap<Direction, Animation>();
		final String prefix = world == World.DAY ? "" : "night_";

		Texture playerWalk = new Texture(Gdx.files.internal("Animations/" +
				prefix + "walk_animation1.png"));

		//split all the images into the TextureRegion matrix
		TextureRegion[][] walkPics = TextureRegion.split(playerWalk, 
				playerWalk.getWidth()/NBR_WALK_PIC_WIDTH, playerWalk.getHeight()/NBR_PIC_HEIGHT);
		TextureRegion[] tempWalkLeft = new TextureRegion[NBR_WALK_PIC_WIDTH];
		TextureRegion[] tempWalkRight = new TextureRegion[NBR_WALK_PIC_WIDTH];

		// Get walk animations
		for (int i = 0; i < NBR_WALK_PIC_WIDTH; i++) {
			tempWalkRight[i] = walkPics[0][i];
			tempWalkLeft[i] = walkPics[1][i];
		}

		Animation ani = new Animation(ZERO_ONE_TIME, tempWalkLeft);
		ani.setPlayMode(Animation.LOOP_REVERSED);

		walkMap.put(Direction.LEFT, ani);
		walkMap.put(Direction.RIGHT, new Animation(ZERO_ONE_TIME, tempWalkRight));
		
		return walkMap;
	}

	public Map<Direction, Animation> getWalkAnimations(World world){
		return world == World.DAY ? dayWalkAnimations : nightWalkAnimations;
	}

	public Map<Movement, Animation> getArrayOfAnimations(World world){
		return world == World.DAY ? dayArrayOfAnimations : nightArrayOfAnimations;
	}

	public Animation getPortalAnimation(int color) {
		return color == 0 ? bluePortalAnimations : yellowPortalAnimations;
	}

}

package edu.chalmers.Blockster.core;

import com.badlogic.gdx.maps.tiled.TiledMap;

import static edu.chalmers.Blockster.core.Block.Animation.*;


public class AnimationHandler {

	public static boolean handleBlockPlayerAnimation(Block block, Player player, TiledMap map) {
		edu.chalmers.Blockster.core.Block.Animation anim = block.getAnimation();
		if (anim != NONE) {
			switch(anim) {
			case PUSH_LEFT:
				//Player to the right of the block, direction Left
				break;
			case PUSH_RIGHT:
				//Player to the left of the block, direction Right
				break;
			case PULL_LEFT:
				//Player to the left of the block, direction Left
				break;
			case PULL_RIGHT:
				//Player to the right of the block, direction Right
				break;
			case LIFT:
				//Player to the side of the block, direction up and to the side
				break;
			default:
				//Destroy the block
				
			}
			return true;
		} else {
			return false;
		}
	}
	
}

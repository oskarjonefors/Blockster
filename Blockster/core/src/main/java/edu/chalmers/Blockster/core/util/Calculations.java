package edu.chalmers.Blockster.core.util;

import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.Player;

public class Calculations {
	

	public final static float STANDARD_MOVE_DURATION = 0.4f;
	
	@SuppressWarnings("unused")
	public static boolean collisionAbove(Player player, BlockLayer blockLayer) {
		try {
			return collisionUpperLeft(player, blockLayer) ||
					collisionUpperRight(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean collisionBelow(Player player, BlockLayer blockLayer) {
		try {
			return collisionLowerLeft(player, blockLayer) ||
					collisionLowerRight(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean collisionHorisontally(Player player, BlockLayer blockLayer) {
		if (player.getVelocity().x < 0) {
			return collisionLeft(player, blockLayer);
		} else if (player.getVelocity().x > 0) {
			return collisionRight(player, blockLayer);
		} else {
			return false;
		}
	}

	public static boolean collisionLeft(Player player, BlockLayer blockLayer) {
		try {
			return collisionUpperLeft(player, blockLayer) 
					|| collisionLowerLeft(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	public static boolean collisionLowerLeft(Player player, 
			BlockLayer blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}

	public static boolean collisionLowerRight(Player player, 
			BlockLayer blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeight));
	}

	public static boolean collisionRight(Player player, BlockLayer blockLayer) {
		try {
			return collisionUpperRight(player, blockLayer)
					|| collisionLowerRight(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}
	
	public static boolean collisionUpperLeft(Player player, 
			BlockLayer blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionUpperRight(Player player, 
			BlockLayer blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionVertically(Player player, BlockLayer blockLayer) {
		if (player.getVelocity().y < 0) {
			return collisionBelow(player, blockLayer);
		}

		//TODO: What if blocks are above the character??
		return false;
	}
	
	private static boolean isSolid(BlockLayer blockLayer, int x, int y) {
		return blockLayer.getBlock(x,y).isSolid();
	}
}

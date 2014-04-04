package edu.chalmers.Blockster.core.util;

import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.Player;

public class Calculations {

	public static boolean collisionUpperLeft(Player player, 
			BlockLayer collisionLayer) {
		float tileWidth = collisionLayer.getBlockWidth();
		float tileHeight = collisionLayer.getBlockHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerLeft(Player player, 
			BlockLayer collisionLayer) {
		float tileWidth = collisionLayer.getBlockWidth();
		float tileHeight = collisionLayer.getBlockHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	public static boolean collisionUpperRight(Player player, 
			BlockLayer collisionLayer) {
		float tileWidth = collisionLayer.getBlockWidth();
		float tileHeight = collisionLayer.getBlockHeight();
		
		return isSolid(collisionLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerRight(Player player, 
			BlockLayer collisionLayer) {
		float tileWidth = collisionLayer.getBlockWidth();
		float tileHeight = collisionLayer.getBlockHeight();
		
		return isSolid(collisionLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	private static boolean isSolid(BlockLayer collisionLayer, int x, int y) {
		return collisionLayer.getBlock(x,y).isSolid();
	}
}

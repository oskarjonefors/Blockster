package edu.chalmers.Blockster.core;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Calculations {

	public static boolean collisionUpperLeft(Player player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerLeft(Player player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	public static boolean collisionUpperRight(Player player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerRight(Player player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	private static boolean isSolid(TiledMapTileLayer collisionLayer, int x, int y) {
		return collisionLayer.getCell(x, y).getTile().getProperties().containsKey("Solid");
	}
}

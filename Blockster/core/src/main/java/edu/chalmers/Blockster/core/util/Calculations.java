package edu.chalmers.Blockster.core.util;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

import edu.chalmers.Blockster.core.gdx.view.GdxPlayer;

public class Calculations {

	public static boolean collisionUpperLeft(GdxPlayer player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerLeft(GdxPlayer player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	public static boolean collisionUpperRight(GdxPlayer player, 
			TiledMapTileLayer collisionLayer) {
		float tileWidth = collisionLayer.getTileWidth();
		float tileHeight = collisionLayer.getTileHeight();
		
		return isSolid(collisionLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionLowerRight(GdxPlayer player, 
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

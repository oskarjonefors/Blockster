package edu.chalmers.blockster.core.util;

public final class Calculations {
	
	public final static float STANDARD_MOVE_DURATION = 0.2f;
	public final static float MOVE_LIFTED_BLOCK_DURATION = 0.15f;
	public final static float BLOCK_FALL_DURATION = 0.05f;
	/*public final static int CHECK_UP_FLAG = 1 << 0;
	public final static int CHECK_UP_RIGHT_FLAG = 1 << 1;
	public final static int CHECK_RIGHT_FLAG = 1 << 2;
	public final static int CHECK_DOWN_RIGHT_FLAG = 1 << 3;
	public final static int CHECK_DOWN_FLAG = 1 << 4;
	public final static int CHECK_DOWN_LEFT_FLAG = 1 << 5;
	public final static int CHECK_LEFT_FLAG = 1 << 6;
	public final static int CHECK_UP_LEFT_FLAG = 1 << 7;*/
	
	private Calculations() {}
	
	public static boolean collisionEitherCorner(PhysicalObject player, GridMap blockLayer) {
		try {
			return collisionUpperLeft(player, blockLayer) ||
					collisionUpperRight(player, blockLayer) ||
					collisionLowerLeft(player, blockLayer) ||
					collisionLowerRight(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private static boolean collisionLowerLeft(PhysicalObject player, 
			GridMap blockLayer) {
		final float tileWidth = blockLayer.getBlockWidth();
		final float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}

	private static boolean collisionLowerRight(PhysicalObject player, 
			GridMap blockLayer) {
		final float tileWidth = blockLayer.getBlockWidth();
		final float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	private static boolean collisionUpperLeft(PhysicalObject player, 
			GridMap blockLayer) {
		final float tileWidth = blockLayer.getBlockWidth();
		final float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	private static boolean collisionUpperRight(PhysicalObject player, 
			GridMap blockLayer) {
		final float tileWidth = blockLayer.getBlockWidth();
		final float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	/*public static boolean collisionSurroundingBlocks(PhysicalObject pos,
			 GridMap blockLayer, int flags) {
		boolean collision = false;
		final float scaleX = pos.getScaleX();
		final float scaleY = pos.getScaleY();
		
		if ((flags & CHECK_UP_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX), 
					(int) (pos.getY() / scaleY) + 1);
		}
		
		if ((flags & CHECK_UP_RIGHT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) + 1, 
					(int) (pos.getY() / scaleY) + 1);
		}
		
		if ((flags & CHECK_RIGHT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) + 1, 
					(int) (pos.getY() / scaleY));
		}
		
		if ((flags & CHECK_DOWN_RIGHT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) + 1, 
					(int) (pos.getY() / scaleY) - 1);
		}
		
		if ((flags & CHECK_DOWN_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX), 
					(int) (pos.getY() / scaleY) - 1);
		}
		
		if ((flags & CHECK_DOWN_LEFT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) - 1, 
					(int) (pos.getY() / scaleY) - 1);
		}

		if ((flags & CHECK_LEFT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) - 1, 
					(int) (pos.getY() / scaleY));
		}

		if ((flags & CHECK_UP_LEFT_FLAG) != 0) {
			collision |= isSolid(blockLayer,
					(int) (pos.getX() / scaleX) - 1, 
					(int) (pos.getY() / scaleY) + 1);
		}
		
		return collision;
	}*/
	
	private static boolean isSolid(GridMap blockLayer, int x, int y) {
		return blockLayer.hasBlock(x, y) && blockLayer.getBlock(x,y).isSolid();
	}
}

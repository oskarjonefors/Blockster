package edu.chalmers.Blockster.core.util;

import edu.chalmers.Blockster.core.objects.Block;
import edu.chalmers.Blockster.core.objects.BlockMap;
import edu.chalmers.Blockster.core.objects.BlocksterObject;
import edu.chalmers.Blockster.core.objects.Player;

public class Calculations {
	

	public final static float STANDARD_MOVE_DURATION = 0.2f;
	public final static int CHECK_UP_FLAG = 1 << 0;
	public final static int CHECK_UP_RIGHT_FLAG = 1 << 1;
	public final static int CHECK_RIGHT_FLAG = 1 << 2;
	public final static int CHECK_DOWN_RIGHT_FLAG = 1 << 3;
	public final static int CHECK_DOWN_FLAG = 1 << 4;
	public final static int CHECK_DOWN_LEFT_FLAG = 1 << 5;
	public final static int CHECK_LEFT_FLAG = 1 << 6;
	public final static int CHECK_UP_LEFT_FLAG = 1 << 7;
	
	public static boolean collisionEitherCorner(BlocksterObject player, BlockMap blockLayer) {
		try {
			return collisionUpperLeft(player, blockLayer) ||
					collisionUpperRight(player, blockLayer) ||
					collisionLowerLeft(player, blockLayer) ||
					collisionLowerRight(player, blockLayer);
		} catch (NullPointerException e) {
			return false;
		}
	}

	private static boolean collisionLowerLeft(BlocksterObject player, 
			BlockMap blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) (player.getY() / tileHeight));
	}

	private static boolean collisionLowerRight(BlocksterObject player, 
			BlockMap blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) (player.getY() / tileHeight));
	}
	
	private static boolean collisionUpperLeft(BlocksterObject player, 
			BlockMap blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) (player.getX() / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	private static boolean collisionUpperRight(BlocksterObject player, 
			BlockMap blockLayer) {
		float tileWidth = blockLayer.getBlockWidth();
		float tileHeight = blockLayer.getBlockHeight();
		
		return isSolid(blockLayer, (int) ((player.getX() + player.getWidth()) / tileWidth),
				(int) ((player.getY() + player.getHeight()) / tileHeight));
	}
	
	public static boolean collisionSurroundingBlocks(Player player,
			BlockMap blockLayer, int flags) {
		return collisionSurroundingBlocks(player, blockLayer, 
				blockLayer.getBlockWidth(), blockLayer.getBlockHeight(),
				flags);
	}
	
	public static boolean collisionSurroundingBlocks(Block block,
			BlockMap blockLayer, int flags) {
		return collisionSurroundingBlocks(block, blockLayer, 1, 1, flags);
	}
	
	private static boolean collisionSurroundingBlocks(BlocksterObject pos,
			 BlockMap blockLayer, float scaleX, float scaleY, int flags) {
		boolean collision = false;
		
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
	}
	
	private static boolean isSolid(BlockMap blockLayer, int x, int y) {
		return blockLayer.hasBlock(x, y) && blockLayer.getBlock(x,y).isSolid();
	}
}

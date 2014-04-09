package edu.chalmers.Blockster.core.util;

import static edu.chalmers.Blockster.core.util.Direction.LEFT;
import static edu.chalmers.Blockster.core.util.Direction.RIGHT;
import edu.chalmers.Blockster.core.Animation;
import edu.chalmers.Blockster.core.Block;
import edu.chalmers.Blockster.core.BlockLayer;
import edu.chalmers.Blockster.core.Player;
import edu.chalmers.Blockster.core.Position;

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
	
	public static boolean collisionSurroundingBlocks(Player player,
			BlockLayer blockLayer, int flags) {
		return collisionSurroundingBlocks(player, blockLayer, 
				blockLayer.getBlockHeight(), blockLayer.getBlockHeight(),
				flags);
	}
	
	public static boolean collisionSurroundingBlocks(Block block,
			BlockLayer blockLayer, int flags) {
		return collisionSurroundingBlocks(block, blockLayer, 1, 1, flags);
	}
	
	private static boolean collisionSurroundingBlocks(Position pos,
			 BlockLayer blockLayer, float scaleX, float scaleY, int flags) {
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
	
	private static boolean isSolid(BlockLayer blockLayer, int x, int y) {
		return blockLayer.hasBlock(x, y) && blockLayer.getBlock(x,y).isSolid();
	}
	
	public static Block getAdjacentBlock(Direction dir, Player player
			, BlockLayer blockLayer) {
		float blockWidth = blockLayer.getBlockWidth();
		float blockHeight = blockLayer.getBlockHeight();
		Block block = null;

		try {
			if (dir == LEFT) {
				Block adjacentBlockLeft = blockLayer.getBlock(
						(int) (player.getX() / blockWidth) - 1,
						(int) ((2 * player.getY() + player.getHeight()) / 2 / blockHeight));
				block = adjacentBlockLeft;
			}

			if (dir == RIGHT) {
				Block adjacentBlockRight = blockLayer.getBlock(
						(int) ((player.getX() + player.getWidth()) / blockWidth) + 1,
						(int) ((2 * player.getY() + player.getHeight()) / 2 / blockHeight));

				block = adjacentBlockRight;
			}
		} catch (NullPointerException e) {
			block = null;
		}
		return block;
	}
}

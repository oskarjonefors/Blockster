package edu.chalmers.Blockster.core;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An interface to handle a block.
 * @author Oskar Jönefors
 * 
 */
public interface Block {
	
	/**
	 * Returns the color this block is represented by in level maps.
	 * @return	The RGB value of the color in the default sRGB ColorModel. 
	 */
	public abstract int getRGB();
	
	/**
	 * Return the texture of this block.
	 * @return	The texture of this block.
	 */
	public abstract TextureRegion getTextureRegion();
	
	/**
	 * Whether or not this block is affected by gravity.
	 * @return	True if block is affected by gravity, false otherwise.
	 */
	public abstract boolean hasWeight();

	/**
	 * Whether or not this block can be climbed like a ladder.
	 * @return	True if block can be climbed, false otherwise.
	 */
	public abstract boolean isClimbable();
	
	/**
	 * Whether or not this block can be destroyed.
	 * @return	True if block can be destroyed, false otherwise.
	 */
	public abstract boolean isDestructible();
	
	/**
	 * Whether or not block can be lifted.
	 * @return True if block can be lifted, false otherwise.
	 */
	public abstract boolean isLiftable();
	
	/**
	 * Whether or not this block is currently being lifted.
	 * @return True if character is holding block, false otherwise.
	 */
	public abstract boolean isLifted();
	
	/**
	 * Whether or not this block can be moved.
	 * @return	True if block can be moved, false otherwise.
	 */
	public abstract boolean isMovable();
	
	/**
	 * Whether or not this block is solid. If not, it can be walked through.
	 * @return	True if block is solid, false otherwise.
	 */
	public abstract boolean isSolid();
	
	/**
	 * Called when the user destroys this block.
	 */
	public abstract void destroy();
	
	/**
	 * Set this block as being lifted.
	 */
	public abstract void lift();
	
	/**
	 * Set this block as being put down.
	 */
	public abstract void putDown();
	
	/**
	 * Make this block climbable.
	 */
	public abstract void setClimbable();
	
	/**
	 * Make this block destructible.
	 */
	public abstract void setDestructible();
	
	/**
	 * Make this block liftable.
	 */
	public abstract void setLiftable();
	
	/**
	 * Make this block movable.
	 */
	public abstract void setMovable();
	
	/**
	 * Make this block solid.
	 */
	public abstract void setSolid();
	
	/**
	 * Make this block affected by gravity.
	 */
	public abstract void setWeight();
}

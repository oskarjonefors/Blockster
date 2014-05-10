package edu.chalmers.blockster.core.objects;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import edu.chalmers.blockster.core.objects.interactions.Interactable;
import edu.chalmers.blockster.core.objects.movement.AnimationState;
import edu.chalmers.blockster.core.objects.movement.Direction;
import edu.chalmers.blockster.core.util.GridObject;

public class Block extends BlocksterObject implements GridObject, Interactable {

	private final Set<String> properties;
	
	public Block(float startX, float startY, BlockMap blockLayer) {
		super(startX, startY, blockLayer, 1, 1);
		properties = new HashSet<String>();
	}
	
	public void setProperty(String property) {
		properties.add(property.toLowerCase(Locale.ENGLISH));
	}
	
	public boolean canMove(Direction dir) {
		final BlockMap bLayer = getBlockLayer();
		final float blockWidth = bLayer.getBlockWidth();
		final int checkX = (int) (getOriginX() / getScaleX());
		final int checkY = (int) (getOriginY() / getScaleY());
		
		return checkX >= 1 && checkX < blockWidth && !bLayer.
				hasBlock(checkX + dir.deltaX, checkY + dir.deltaY);
			
	}
	
	@Override
	public boolean isSolid() {
		return properties.contains("solid");
	}
	
	@Override
	public boolean isLiftable() {
		return properties.contains("liftable");
	}
	
	@Override
	public boolean isMovable() {
		return properties.contains("movable");
	}
	
	@Override
	public boolean hasWeight() {
		return properties.contains("weight");
	}
	
	@Override
	public void moveToNextPosition() {
		blockMap.setBlock((int) getOriginX(), (int) getOriginY(), null);
		System.out.println("Removing "+this);
		super.moveToNextPosition();
		
	}
	
	@Override
	public void setAnimationState(AnimationState anim) {
		super.setAnimationState(anim);
		if (anim != AnimationState.NONE) {
			blockMap.addActiveBlock(this);
		}
	}


	
}

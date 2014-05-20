package edu.chalmers.blockster.core.objects.movement;

import static org.junit.Assert.assertTrue;

import javax.vecmath.Vector2f;

import org.junit.Before;
import org.junit.Test;

public class AnimationStateTest {

	public AnimationState none = AnimationState.NONE;
	public AnimationState anim;
	
	@Before
	public void setUp() {
		anim = new AnimationState(Movement.PULL_LEFT);
	}
	
	@Test
	public void testGetRelativePosition() {
		Vector2f zeroVector = new Vector2f(0f,0f);
		AnimationState noAnimation = new AnimationState(Movement.NONE);
		System.out.println(noAnimation.getRelativePosition());
		System.out.println(zeroVector);
		assertTrue(noAnimation.getRelativePosition().equals(zeroVector));
	}
	
	@Test
	public void testGetElapsedTime() {
		float startTime = anim.getElapsedTime();
		float deltaTime = anim.getMovement().getDuration() / 2f;
		
		anim.updatePosition(deltaTime);
		
		assertTrue(anim.getElapsedTime() == startTime + deltaTime);
	}

	@Test
	public void testIsDone() {
		boolean animStartsNotDone = !anim.isDone();
		boolean noneStartsDone = none.isDone();
		
		anim.updatePosition(anim.getMovement().getDuration());
		
		boolean animEndsDone = anim.isDone();
		
		assertTrue(animStartsNotDone && noneStartsDone && animEndsDone);
	}

}

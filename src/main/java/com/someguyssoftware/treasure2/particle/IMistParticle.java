package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.spatial.ICoords;

public interface IMistParticle {

	/**
	 * Call before adding particle to world. Perform particle setup that uses overridden methods
	 */
	void init();

	float provideStartSize();
	
	float provideMaxSize();

	int provideMaxAge();

	void transitionIn();

	void transitionOut();

	/**
	 * provides a gravity value
	 * 
	 * @return
	 */
	float provideGravity();

	/**
	 * provides an alpha value.
	 * 
	 * @return
	 */
	float provideAlpha();

	ICoords getParentEmitterCoords();

	void setParentEmitterCoords(ICoords parentEmitterCoords);

	float getTransitionInSizeIncrement();

	void setTransitionInSizeIncrement(float transitionInScaleIncrement);

	float getTransitionOutSizeIncrement();

	void setTransitionOutSizeIncrement(float transitionOutScaleIncrement);

	/**
	 * retrieve the particle's gravity
	 * 
	 * @return
	 */
	float getGravity();

	/**
	 * retrieve the particle's scale
	 * 
	 * @return
	 */
	float getScale();

	/**
	 * retrieve the particle's alpha
	 * 
	 * @return
	 */
	float getAlpha();
}
package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.positional.ICoords;

public interface IMistParticle {

	/**
	 * Call before adding particle to world. Perform particle setup that uses overridden methods
	 */
	void init();

	float provideMaxScale();

	int provideMaxAge();

	void transitionIn(float initialScale, int stopAge, float scaleIncrement);

	void transitionOut(float finalScale, int startAge, float scaleIncrement);

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

	float getTransitionInScaleIncrement();

	void setTransitionInScaleIncrement(float transitionInScaleIncrement);

	float getTransitionOutScaleIncrement();

	void setTransitionOutScaleIncrement(float transitionOutScaleIncrement);

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
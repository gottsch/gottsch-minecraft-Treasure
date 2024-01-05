package mod.gottsch.forge.treasure2.core.particle;

public interface IMistParticle {

	/**
	 * Call before adding particle to world. Perform particle setup that uses overridden methods
	 */
	void init();

	float getStartSize();
	
	float getMaxSize();
	
	int getMaxAge();

	void transitionIn();

	void transitionOut();

	/**
	 * provides a gravity value
	 * 
	 * @return
	 */
	float getMistGravity();

	/**
	 * provides an alpha value.
	 * 
	 * @return
	 */
	float getMistAlpha();

//	ICoords getParentEmitterCoords();
//
//	void setParentEmitterCoords(ICoords parentEmitterCoords);

	float getTransitionInSizeIncrement();

	void setTransitionInSizeIncrement(float transitionInScaleIncrement);

	float getTransitionOutSizeIncrement();

	void setTransitionOutSizeIncrement(float transitionOutScaleIncrement);

	/**
	 * retrieve the particle's scale
	 * 
	 * @return
	 */
	float getScale();

	float getEndSize();

	/**
	 * 
	 * @return
	 */
	float getTransitionInStopAge();

	float getTransitionOutStartAge();

}
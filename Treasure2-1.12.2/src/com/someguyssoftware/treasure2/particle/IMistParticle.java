package com.someguyssoftware.treasure2.particle;

import com.someguyssoftware.gottschcore.positional.ICoords;

public interface IMistParticle {

	ICoords getParentEmitterCoords();

	void setParentEmitterCoords(ICoords parentEmitterCoords);

	float getMaxScale();

	float getMaxAlpha();

	int getMaxAge();

	float getGravity();

}
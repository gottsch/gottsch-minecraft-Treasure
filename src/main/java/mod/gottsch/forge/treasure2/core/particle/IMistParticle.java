/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.particle;

/**
 * 
 * @author Mark Gottschling
 *
 */
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
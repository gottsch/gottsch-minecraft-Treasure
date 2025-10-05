/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.cache;

import java.util.List;

import mod.gottsch.forge.gottschcore.spatial.ICoords;

/**
 * 
 * @author Mark Gottschling on May 19, 2023
 *
 * @param <T>
 */
public interface ISimpleDistanceCache<T> {

	boolean isCached(T object);

	/**
	 * 
	 */
	void cache(ICoords key, T object);

	/**
	 * 
	 * @param object
	 */
	void uncache(ICoords key);

	/**
	 * 
	 */
	void clear();

	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public boolean withinArea(ICoords start, ICoords end);
	
	/**
	 * This will not update parent collection.
	 * @return
	 */
	List<T> getValues();
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.config;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;

/**
 * @author Mark Gottschling on Jan 23, 2018
 *
 */
public interface IChestConfig {
	public void init();
	
	public boolean isEnableChest();
	public boolean isSurfaceAllowed();
	public boolean isSubterraneanAllowed();
	public int getWeight();
	@Deprecated
	public int getMinYSpawn();
	public double getMimicProbability();

	public List<String> getBiomeWhitelist();
	public List<String> getBiomeBlacklist();
	public List<BiomeTypeHolder> getBiomeTypeWhiteList();
	public List<BiomeTypeHolder> getBiomeTypeBlackList();

	public int getMinDepth();
	public int getMaxDepth();
}
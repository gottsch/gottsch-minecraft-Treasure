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
package com.someguyssoftware.treasure2.generator.pit;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.IGeneratorResult;

import net.minecraft.world.IServerLevel;

public interface IPitGenerator<RESULT extends IGeneratorResult<?>> {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param surfaceCoords
	 * @param spawnCoords
	 * @return
	 */
	public GeneratorResult<ChestGeneratorData> generate(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateBase(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public boolean generatePit(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords);
	
	public boolean generateEntrance(IServerLevel world, Random random, ICoords surfaceCoords, ICoords spawnCoords);

	public int getOffsetY();
	public void setOffsetY(int i);

	int getMinSurfaceToSpawnDistance();
}
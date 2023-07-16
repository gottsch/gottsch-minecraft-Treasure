/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.world.feature;

import java.util.Random;

import mod.gottsch.forge.gottschcore.world.WorldGenContext;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 
 * @author Mark Gottschling on Jun 3, 2023
 *
 */
public class FeatureGenContext extends WorldGenContext implements IFeatureGenContext {
	private IFeatureType featureType;

	/**
	 * 
	 * @param level
	 * @param chunkGenerator
	 * @param random
	 * @param featureType
	 */
	public FeatureGenContext(ServerLevelAccessor level, ChunkGenerator chunkGenerator, Random random, IFeatureType featureType) {
		super(level, chunkGenerator, random);
		this.featureType = featureType;
	}
	
	/**
	 * 
	 * @param context
	 * @param featureType
	 */
	public FeatureGenContext(FeaturePlaceContext<?> context, IFeatureType featureType) {
		this(context.level(), context.chunkGenerator(), context.random(), featureType);
	}
	
	@Override
	public IFeatureType getFeatureType() {
		return featureType;
	}

	@Override
	public void setFeatureType(IFeatureType featureType) {
		this.featureType = featureType;
	}
}

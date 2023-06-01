/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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

import java.util.Optional;

import com.mojang.serialization.Codec;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGeneratedContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * @author Mark Gottschling on Jan 27, 2021
 *
 */
public class AqueousChestFeature extends Feature<NoneFeatureConfiguration> implements IChestFeature {

	// NOTE aqueous chest feature does not use waitChunkCount, since even if you spawn on an island in an ocean
	// the closest chest will still probably not be immediately visible/accessible.

	/**
	 * 
	 * @param configuration
	 */
	public AqueousChestFeature(Codec<NoneFeatureConfiguration> configuration) {
		super(configuration);
	}

	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel genLevel = context.level();
		ResourceLocation dimension = WorldInfo.getDimension(genLevel.getLevel());

		// test the dimension
		if (!meetsDimensionCriteria(dimension)) { 
			return false;
		}
		
		// get the chest registry
		GeneratedCache<ChestGeneratedContext> registry = DimensionalGeneratedRegistry.getChestGeneratedCache(dimension, FeatureType.AQUATIC);
		if (registry == null) {
			Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & AQUATIC. This shouldn't be. Should be initialized.");
			return false;
		}
		
		// get the generator config
		ChestConfiguration config = Config.chestConfigMap.get(dimension);
		if (config == null) {
			Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
			return false;
		}
		
		Generator generatorConfig = config.getGenerator(FeatureType.AQUATIC.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FeatureType.AQUATIC.getName());
			return false;
		}
		
		ICoords spawnCoords = WorldInfo.getOceanFloorSurfaceCoords(genLevel, context.chunkGenerator(),
				new Coords(context.origin().offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == Coords.EMPTY) {
			return false;
		}
		
		// determine what type to generate
		IRarity rarity = (IRarity) RarityLevelWeightedChestGeneratorRegistry.getNextRarity(dimension, FeatureType.TERRESTRIAL);
		//		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity == Rarity.NONE) {
			Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}", FeatureType.TERRESTRIAL);
			return false;
		}
		Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
		if (!rarityConfig.isPresent()) {
			Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
			return false;
		}
		
		// TODO complete
		
		return false;
	}
}

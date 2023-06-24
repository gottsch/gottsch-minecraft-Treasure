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
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.ChestRarity;
import mod.gottsch.forge.treasure2.core.config.ChestFeaturesConfiguration.Generator;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.persistence.TreasureSavedData;
import mod.gottsch.forge.treasure2.core.registry.DimensionalGeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.FeatureGeneratorSelectorRegistry;
import mod.gottsch.forge.treasure2.core.registry.GeneratedCache;
import mod.gottsch.forge.treasure2.core.registry.RarityLevelWeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * NOTE: Feature is the equivalent to 1.12 WorldGenerator
 * @author Mark Gottschling on Jan 4, 2021
 *
 */
public class TerraneanChestFeature extends ChestFeature {
	private static final IFeatureType FEATURE_TYPE = FeatureType.TERRANEAN;
	
	/**
	 * 
	 * @param configuration
	 */
	public TerraneanChestFeature(Codec<NoneFeatureConfiguration> configuration) {
		super(configuration);
	}

	/*
	 * The minimum depth from surface for a chest spawn
	 */
	protected static int UNDERGROUND_OFFSET = 5;

	private int waitChunksCount = 0;
	
	/**
	 * NOTE equivalent to 1.12 generate()
	 * NOTE only use seedReader.setblockState() and that only allows you to access the 3x3 chunk area.
	 *  chest/pit spawn IS doable as long as you keep it within the 3x3 chunk area, else would have to use a Jigsaw Structures setup
	 */
	@Override
	public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
		WorldGenLevel genLevel = context.level();
		ResourceLocation dimension = WorldInfo.getDimension(genLevel.getLevel());
		//		Treasure.LOGGER.debug("dimension -> {}", dimension.toString());
		// test the dimension
		if (!meetsDimensionCriteria(dimension)) { 
			return false;
		}

		// get the chest registry
		GeneratedCache<GeneratedChestContext> chestCache = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FEATURE_TYPE);
		if (chestCache == null) {
			Treasure.LOGGER.debug("GeneratedRegistry is null for dimension & TERRANEAN. This shouldn't be. Should be initialized.");
			return false;
		}
		// get the generator config
		ChestFeaturesConfiguration config = Config.chestConfig; //Config.chestConfigMap.get(dimension);
		if (config == null) {
			Treasure.LOGGER.debug("ChestConfiguration is null. This shouldn't be.");
			return false;
		}

		Generator generatorConfig = config.getGenerator(FEATURE_TYPE.getName());
		if (generatorConfig == null) {
			Treasure.LOGGER.warn("unable to locate a config for feature type -> {}.", FEATURE_TYPE.getName());
			return false;
		}

		// test the world age
		if (!meetsWorldAgeCriteria(genLevel, chestCache, generatorConfig)) {
			return false;
		}

		// TODO add a check against a tag that lists all the build on materials (dirt, stone, cobblestone etc), or a blacklist (bricks, planks, wool, etc)

		// the get first surface y (could be leaves, trunk, water, etc)
		ICoords spawnCoords = WorldInfo.getDryLandSurfaceCoords(genLevel, context.chunkGenerator(), new Coords(context.origin().offset(WorldInfo.CHUNK_RADIUS - 1, 0, WorldInfo.CHUNK_RADIUS - 1)));
		if (spawnCoords == Coords.EMPTY) {
			return false;
		}

		// determine what type to generate
		IRarity rarity = (IRarity) RarityLevelWeightedChestGeneratorRegistry.getNextRarity(dimension, FEATURE_TYPE);
		//		Treasure.LOGGER.debug("rarity -> {}", rarity);
		if (rarity == Rarity.NONE) {
			Treasure.LOGGER.warn("unable to obtain the next rarity for generator - >{}", FEATURE_TYPE);
			return false;
		}
		Optional<ChestRarity> rarityConfig = generatorConfig.getRarity(rarity);
		if (!rarityConfig.isPresent()) {
			Treasure.LOGGER.warn("unable to locate rarity config for rarity - >{}", rarity);
			return false;
		}
		// test if the override (global) biome is allowed

		// TODO might have feature generator specific biome and proximity criteria checks. ie Wither
		if (!meetsBiomeCriteria(genLevel.getLevel(), spawnCoords, rarityConfig.get().getBiomeWhitelist(), rarityConfig.get().getBiomeBlacklist())) {
			return false;
		}

		// check against all registered chests
		if (!meetsProximityCriteria(genLevel, dimension, FEATURE_TYPE, spawnCoords, generatorConfig.getMinBlockDistance())) {
			return false;
		}			

		// check if meets the probability criteria. this is used as a randomizer so that chests aren't predictably placed.
		if (!meetsProbabilityCriteria(context.random(), generatorConfig)) {
			// place a placeholder chest in the registry
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}

		// select the feature generator
		Optional<IFeatureGeneratorSelector> generatorSelector = FeatureGeneratorSelectorRegistry.getSelector(FEATURE_TYPE, rarity);
		if (!generatorSelector.isPresent()) {
			Treasure.LOGGER.warn("unable to obtain a generator selector for rarity - >{}", rarity);
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}

		// select the generator
		IFeatureGenerator featureGenerator = generatorSelector.get().select();
		Treasure.LOGGER.debug("feature generator -> {}", featureGenerator.getClass().getSimpleName());
		// call generate
		Optional<GeneratorResult<ChestGeneratorData>> result = featureGenerator.generate(new FeatureGenContext(context, FEATURE_TYPE), spawnCoords, rarity, rarityConfig.get());

		if (result.isPresent()) {
			cacheGeneratedChest(context.level(), rarity, FEATURE_TYPE, chestCache, result.get());
			updateChestGeneratorRegistry(dimension, rarity, FEATURE_TYPE);
		} else {
			return failAndPlaceholdChest(genLevel, chestCache, rarity, spawnCoords, FEATURE_TYPE);
		}

		// save world data
		TreasureSavedData savedData = TreasureSavedData.get(genLevel.getLevel());
		if (savedData != null) {
			savedData.setDirty();
		}
		return true;
	}
	
	/**
	 * @param world
	 * @param registry
	 * @return
	 */
	protected boolean meetsWorldAgeCriteria(ServerLevelAccessor world, GeneratedCache<GeneratedChestContext> registry, Generator generatorConfig) {
		// wait count check		
		if (registry.getValues().isEmpty() && waitChunksCount < generatorConfig.getWaitChunks()) {
			Treasure.LOGGER.debug("world is too young");
			this.waitChunksCount++;
			return false;
		}
		return true;
	}
}
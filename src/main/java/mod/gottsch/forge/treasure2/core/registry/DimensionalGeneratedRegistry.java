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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.resources.ResourceLocation;



/**
 * This is a singleton Registry that contains the instance registries
 * for the generated chests in all the dimension/region combinations.
 * It also contains the mappings/registries for the special Wither Tree
 * Chests and Wells.
 * @author Mark Gottschling on Nov 30, 2022
 *
 */
public class DimensionalGeneratedRegistry {	

	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<ChestGenContext>>> CHEST_REGISTRY = new HashMap<>();
	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<GeneratedContext>>> WELL_REGISTRY = new HashMap<>();
	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<GeneratedContext>>> WITHER_REGISTRY = new HashMap<>();

	/**
	 * 
	 */
	private DimensionalGeneratedRegistry() {}
	
	/**
	 * 
	 */
	public static void initialize() {
		// for each allowable dimension for the mod
		for (String dimensionName : Config.SERVER.integration.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimensionName);
			ResourceLocation dimension = ModUtil.asLocation(dimensionName);
			
			/*
			 * setup chest registry
			 */
			// find the ChestConfiguration that contains the same dimension
			ChestConfiguration chestConfig = Config.chestConfigMap.get(ModUtil.asLocation(dimensionName));
			//			for (ChestConfiguration chestConfig : Config.chestConfigs) {
			if (chestConfig != null) {
				if (chestConfig.getDimensions().contains(dimension.toString())) {
					// for each generator
					chestConfig.getGenerators().forEach(generator -> {
						// match the generator to the enum
						Optional<IGeneratorType> type = TreasureApi.getGeneratorType(generator.getKey().toUpperCase());
						if (type.isPresent()) {
							// create a new map for generatorType->generatedChestRegistry
							Map<IGeneratorType, GeneratedRegistry<ChestGenContext>> chestRegistryMap = new HashMap<>();
							// create a new generated chest registry with size set from config
							chestRegistryMap.put(type.get(), new GeneratedRegistry<>(generator.getRegistrySize()));
							// add generator map to byDimension map
							CHEST_REGISTRY.put(dimension, chestRegistryMap);
						}
					});
				}
			}
			
			/*
			 * setup wither tree registry
			 */
			if (Config.SERVER.witherTree.enableWitherTree.get()) {
				Map<IGeneratorType, GeneratedRegistry<GeneratedContext>> witherRegistryMap = new HashMap<>();
				witherRegistryMap.put(GeneratorType.WITHER, new GeneratedRegistry<>(Config.SERVER.witherTree.registrySize.get()));
				WITHER_REGISTRY.put(dimension, witherRegistryMap);
			}
			
			/*
			 * setup well registry
			 */
			if (Config.SERVER.wells.enableWells.get()) {
				Map<IGeneratorType, GeneratedRegistry<GeneratedContext>> wellsRegistryMap = new HashMap<>();
				wellsRegistryMap.put(GeneratorType.WELL, new GeneratedRegistry<>(Config.SERVER.wells.registrySize.get()));
				WELL_REGISTRY.put(dimension, wellsRegistryMap);
			}
		}
	}
	
	/**
	 * 
	 * @param dimension
	 * @param genType
	 * @return
	 */
	public static GeneratedRegistry<ChestGenContext> getChestGeneratedRegistry(ResourceLocation dimension, IGeneratorType genType) {
		GeneratedRegistry<ChestGenContext> registry = null;
		Map<IGeneratorType, GeneratedRegistry<ChestGenContext>> registryMap = CHEST_REGISTRY.get(dimension);
		if (registryMap != null) {
			registry =  registryMap.get(genType);
		}
		return registry;
	}
}

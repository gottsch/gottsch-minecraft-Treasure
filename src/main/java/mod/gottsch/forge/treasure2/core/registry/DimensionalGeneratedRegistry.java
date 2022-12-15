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
import java.util.function.Supplier;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
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
	private static final String DIMENSION_NAME = "dimension";
	private static final String CHEST_REGISTRY_NAME = "chestRegistry";
	private static final String WELL_REGISTRIES_NAME = "wellRegistries";
	private static final String WITHER_TREE_REGISTRIES_NAME = "witherTreeRegistries";

	// legacy
	//	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";

	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>>> CHEST_REGISTRY = new HashMap<>();
	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>>> WELL_REGISTRY = new HashMap<>();
	public static final Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>>> WITHER_REGISTRY = new HashMap<>();

	/**
	 * 
	 */
	private DimensionalGeneratedRegistry() {}

	/**
	 * Initialize from Config file.
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
							Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>> chestRegistryMap = new HashMap<>();
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
				Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>> witherRegistryMap = new HashMap<>();
				witherRegistryMap.put(GeneratorType.WITHER, new GeneratedRegistry<>(Config.SERVER.witherTree.registrySize.get()));
				WITHER_REGISTRY.put(dimension, witherRegistryMap);
			}

			/*
			 * setup well registry
			 */
			if (Config.SERVER.wells.enableWells.get()) {
				Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>> wellsRegistryMap = new HashMap<>();
				wellsRegistryMap.put(GeneratorType.WELL, new GeneratedRegistry<>(Config.SERVER.wells.registrySize.get()));
				WELL_REGISTRY.put(dimension, wellsRegistryMap);
			}
		}
	}

	public static Tag save() {
		CompoundTag tag = new CompoundTag();

		//		ListTag dimensionalRegistries = new ListTag();
		//		CHEST_REGISTRY.forEach((dimension, map) -> {
		//			CompoundTag registryTag = new CompoundTag();
		//			registryTag.putString(DIMENSION_NAME, dimension.toString());
		//			ListTag keysTag = new ListTag();
		//			map.forEach((key, registry) -> {
		//				CompoundTag keyTag = new CompoundTag();
		//				keyTag.putString("name", key.getName());
		//				ListTag dataTag = new ListTag();
		//				registry.getValues().forEach(datum -> {
		//					CompoundTag datumTag = datum.save();
		//					// add entry to list
		//					dataTag.add(datumTag);	
		//				});
		//				keyTag.put(CHEST_REGISTRY_NAME, dataTag);
		//				keysTag.add(keyTag);
		//			});
		//			registryTag.put("keys", keysTag);
		//			dimensionalRegistries.add(registryTag);
		//		});

		Tag chestTag = saveRegistry(CHEST_REGISTRY);
		tag.put(CHEST_REGISTRY_NAME, chestTag);		
		// wells
		Tag wellsTag = saveRegistry(WELL_REGISTRY);
		tag.put(WELL_REGISTRIES_NAME, wellsTag);
		// wither		
		Tag witherTag = saveRegistry(WITHER_REGISTRY);
		tag.put(WITHER_TREE_REGISTRIES_NAME, witherTag);

		return tag;
	}

	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static Tag saveRegistry(Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>>> registry) {
		ListTag dimensionalRegistriesTag = new ListTag();
		registry.forEach((dimension, map) -> {
			CompoundTag dimensionRegistryTag = new CompoundTag();
			dimensionRegistryTag.putString(DIMENSION_NAME, dimension.toString());
			ListTag generatedRegistriesTag = new ListTag();
			map.forEach((generatorType, genRegistry) -> {
				CompoundTag generatedRegistryTag = new CompoundTag();
				generatedRegistryTag.putString("name", generatorType.getName());
				ListTag dataTag = new ListTag();
				genRegistry.getValues().forEach(datum -> {
					CompoundTag datumTag = datum.save();
					// add entry to list
					dataTag.add(datumTag);	
				});
				generatedRegistryTag.put("data", dataTag);
				generatedRegistriesTag.add(generatedRegistryTag);
			});
			dimensionRegistryTag.put("registries", generatedRegistriesTag);
			dimensionalRegistriesTag.add(dimensionRegistryTag);
		});
		return dimensionalRegistriesTag;
	}

	/**
	 * 
	 * @param tag
	 */
	public static void load(CompoundTag tag) {
		if (tag.contains(CHEST_REGISTRY_NAME)) {
			loadRegistry(tag.getList(CHEST_REGISTRY_NAME, Tag.TAG_COMPOUND), CHEST_REGISTRY, ChestGenContext::new);
		}
		if (tag.contains(WELL_REGISTRIES_NAME)) {
			loadRegistry(tag.getList(WELL_REGISTRIES_NAME,  Tag.TAG_COMPOUND), WELL_REGISTRY, GeneratedContext::new);
		}
		if (tag.contains(WITHER_TREE_REGISTRIES_NAME)) {
			loadRegistry(tag.getList(WITHER_TREE_REGISTRIES_NAME,  Tag.TAG_COMPOUND), WITHER_REGISTRY, GeneratedContext::new);
		}
	}

	/**
	 * 
	 * @param <T>
	 * @param dimensinoalRegistriesTag the tag to load from
	 * @param registry the registry to load into
	 * @param supplier a factor to create data objects
	 */
	@SuppressWarnings("unchecked")
	public static <T> void loadRegistry(ListTag dimensinoalRegistriesTag, 
			Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<?>>> registry, Supplier<? extends GeneratedContext> supplier) {
		if (dimensinoalRegistriesTag != null) {
			Treasure.LOGGER.debug("loading chest registries...");  	
			dimensinoalRegistriesTag.forEach(dimensionalRegistryTag -> {
				CompoundTag dimensionalRegistryCompound = (CompoundTag)dimensionalRegistryTag;
				if (dimensionalRegistryCompound.contains(DIMENSION_NAME)) {
					String dimensionName = dimensionalRegistryCompound.getString(DIMENSION_NAME);
					Treasure.LOGGER.debug("loading dimension -> {}", dimensionName);
					if (dimensionalRegistryCompound.contains("registries")) {
						ListTag registriesTag = dimensionalRegistryCompound.getList("registries", Tag.TAG_COMPOUND);
						registriesTag.forEach(registryTag -> {
							CompoundTag registryCompound = (CompoundTag)registryTag;
							if (registryCompound.contains("name")) {
								// extract the name
								String name = registryCompound.getString("name");
								Optional<IGeneratorType> generatorType = TreasureApi.getGeneratorType(name);
								if (!generatorType.isPresent()) {
									return;
								}
								if (registryCompound.contains("data")) {
									// extract the data
									GeneratedContext context = supplier.get();
									context.load(registryCompound.getCompound("data"));
									ResourceLocation dimension = ModUtil.asLocation(dimensionName);
									
									GeneratedRegistry<GeneratedContext> generatedRegistry = (GeneratedRegistry<GeneratedContext>) getGeneratedRegistry(registry, dimension, generatorType.get());
									if (generatedRegistry != null) {
										generatedRegistry.register(context.getRarity(), context.getCoords(), context);
									}
								}
							}
						});
					}            		
				}   
			});
		}
	}

	/**
	 * 
	 * @param dimension
	 * @param genType
	 * @return
	 */
	public static GeneratedRegistry<?> getChestGeneratedRegistry(ResourceLocation dimension, IGeneratorType genType) {
		return getGeneratedRegistry(CHEST_REGISTRY, dimension, genType);
	}
	
	/**
	 * 
	 * @param registry
	 * @param dimension
	 * @param genType
	 * @return
	 */
	public static GeneratedRegistry<? extends GeneratedContext> getGeneratedRegistry(Map<ResourceLocation, Map<IGeneratorType, GeneratedRegistry<?>>> registry, ResourceLocation dimension, IGeneratorType genType) {
		Map<IGeneratorType, GeneratedRegistry<? extends GeneratedContext>> registryMap = registry.get(dimension);
		GeneratedRegistry<? extends GeneratedContext> genRegistry = null;
		if (registryMap != null) {
			genRegistry =  (GeneratedRegistry<? extends GeneratedContext>) registryMap.get(genType);
		}
		return genRegistry;
	}
}

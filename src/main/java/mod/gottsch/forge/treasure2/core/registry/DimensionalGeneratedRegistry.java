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
import mod.gottsch.forge.treasure2.core.registry.support.ChestGenContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedContext;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;


// TODO this is actually a Cache - rename.
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
//	private static final String WELL_REGISTRIES_NAME = "wellRegistries";
	//	@Deprecated
	//	private static final String WITHER_TREE_REGISTRIES_NAME = "witherTreeRegistries";

	// legacy
	//	private static final String DIMENSION_ID_TAG_NAME = "dimensionID";

	public static final Map<ResourceLocation, Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>>> CHEST_REGISTRY = new HashMap<>();

	// TODO rework these - dont' need to be map of maps - actually all three of these could be rolled into 1 map of map
	// TODO don't need a GeneratedRegistry at all for Wells because there isn't a related rarity.
//	public static final Map<ResourceLocation, GeneratedRegistry<GeneratedContext>> WELL_REGISTRY = new HashMap<>();
	// TODO don't need wither at all - rolled into Chest Registry
	@Deprecated
	public static final Map<ResourceLocation, Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>>> WITHER_REGISTRY = new HashMap<>();

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
					// create a new map for generatorType->generatedChestRegistry
					Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>> chestRegistryMap = new HashMap<>();
					// add generator map to byDimension map
					CHEST_REGISTRY.put(dimension, chestRegistryMap);

					// for each generator
					chestConfig.getGenerators().forEach(generator -> {
						// match the generator to the enum
						Optional<IFeatureType> type = TreasureApi.getFeatureType(generator.getKey().toUpperCase());
						if (type.isPresent()) {
							// create a new generated chest registry with size set from config
							chestRegistryMap.put(type.get(), new GeneratedRegistry<>(generator.getRegistrySize()));
						}
					});
				}
			}

			/*
			 * setup wither tree registry
			 */
			//			if (Config.SERVER.witherTree.enableWitherTree.get()) {
			//				Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>> witherRegistryMap = new HashMap<>();
			//				witherRegistryMap.put(FeatureType.WITHER, new GeneratedRegistry<>(Config.SERVER.witherTree.registrySize.get()));
			//				WITHER_REGISTRY.put(dimension, witherRegistryMap);
			//			}

			/*
			 * setup well registry
			 */
//			if (Config.SERVER.wells.enableWells.get()) {
//				GeneratedRegistry<GeneratedContext> wellsRegistry = new GeneratedRegistry<>(Config.SERVER.wells.registrySize.get());
//				WELL_REGISTRY.put(dimension, wellsRegistry);
//			}
		}
	}

	public static Tag save() {
		CompoundTag tag = new CompoundTag();

		Tag chestTag = saveRegistry(CHEST_REGISTRY);
		tag.put(CHEST_REGISTRY_NAME, chestTag);		
		// wells
//		Tag wellTag = saveWellRegistry(WELL_REGISTRY);
//		tag.put(WELL_REGISTRIES_NAME, wellTag);
		// wither		
		//		Tag witherTag = saveRegistry(WITHER_REGISTRY);
		//		tag.put(WITHER_TREE_REGISTRIES_NAME, witherTag);

		return tag;
	}

	/**
	 * 
	 * @param registry
	 * @return
	 */
	public static Tag saveRegistry(Map<ResourceLocation, Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>>> registry) {
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
	 * @param registry
	 * @return
	 */
//	@Deprecated
//	public static Tag saveWellRegistry(Map<ResourceLocation, GeneratedRegistry<GeneratedContext>> registry) {
//		ListTag dimensionalRegistriesTag = new ListTag();
//		registry.forEach((dimension, genRegistry) -> {
//			CompoundTag dimensionRegistryTag = new CompoundTag();
//			dimensionRegistryTag.putString(DIMENSION_NAME, dimension.toString());
//			ListTag dataTag = new ListTag();
//			genRegistry.getValues().forEach(datum -> {
//				CompoundTag datumTag = datum.save();
//				// add entry to list
//				dataTag.add(datumTag);	
//			});			
//			dimensionRegistryTag.put("data", dataTag);
//			dimensionalRegistriesTag.add(dimensionRegistryTag);
//		});
//		return dimensionalRegistriesTag;
//	}

	/**
	 * 
	 * @param tag
	 */
	public static void load(CompoundTag tag) {
		if (tag.contains(CHEST_REGISTRY_NAME)) {
			loadRegistry(tag.getList(CHEST_REGISTRY_NAME, Tag.TAG_COMPOUND), CHEST_REGISTRY, ChestGenContext::new);
		}
//		if (tag.contains(WELL_REGISTRIES_NAME)) {
//			loadWellRegistry(tag.getList(WELL_REGISTRIES_NAME,  Tag.TAG_COMPOUND), WELL_REGISTRY, GeneratedContext::new);
//		}
		//		if (tag.contains(WITHER_TREE_REGISTRIES_NAME)) {
		//			loadRegistry(tag.getList(WITHER_TREE_REGISTRIES_NAME,  Tag.TAG_COMPOUND), WITHER_REGISTRY, GeneratedContext::new);
		//		}
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
			Map<ResourceLocation, Map<IFeatureType, GeneratedRegistry<?>>> registry, Supplier<? extends GeneratedContext> supplier) {
		
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
								Optional<IFeatureType> generatorType = TreasureApi.getFeatureType(name);
								if (!generatorType.isPresent()) {
									return;
								}
								if (registryCompound.contains("data")) {
									ResourceLocation dimension = ModUtil.asLocation(dimensionName);										
									// clear the registry
									GeneratedRegistry<GeneratedContext> generatedRegistry = (GeneratedRegistry<GeneratedContext>) getGeneratedRegistry(registry, dimension, generatorType.get());
									generatedRegistry.clear();
									
									// get the data list
									ListTag dataTag = registryCompound.getList("data", Tag.TAG_COMPOUND);
									dataTag.forEach(datum -> {
										/////
										GeneratedContext context = supplier.get();
										// extract the data
										context.load((CompoundTag)datum);
										Treasure.LOGGER.debug("context -> {}", context);
										if (context.getRarity() != null && context.getCoords() != null) {										
											if (generatedRegistry != null) {
												generatedRegistry.register(context.getRarity(), context.getCoords(), context);
											}
										}										
										//////
									});
								}
							}
						});
					}            		
				}   
			});
		}
	}

//	@Deprecated
//	@SuppressWarnings("unchecked")
//	public static <T> void loadWellRegistry(ListTag dimensinoalRegistriesTag, 
//			Map<ResourceLocation, GeneratedRegistry<GeneratedContext>> registry, Supplier<GeneratedContext> supplier) {
//
//		if (dimensinoalRegistriesTag != null) {
//			Treasure.LOGGER.debug("loading well registries...");  	
//			dimensinoalRegistriesTag.forEach(dimensionalRegistryTag -> {
//				CompoundTag dimensionalRegistryCompound = (CompoundTag)dimensionalRegistryTag;
//				if (dimensionalRegistryCompound.contains(DIMENSION_NAME)) {
//					String dimensionName = dimensionalRegistryCompound.getString(DIMENSION_NAME);
//					Treasure.LOGGER.debug("loading dimension -> {}", dimensionName);
//					// load the data
//					if (dimensionalRegistryCompound.contains("data")) {
//						ResourceLocation dimension = ModUtil.asLocation(dimensionName);					
//						// clear the registry first
//						WELL_REGISTRY.get(dimension).clear();
//						
//						ListTag dataTag = dimensionalRegistryCompound.getList("data", Tag.TAG_COMPOUND);
//						dataTag.forEach(datum -> {
//							GeneratedContext context = supplier.get();
//							// extract the data
//							context.load((CompoundTag)datum);
//							Treasure.LOGGER.debug("context -> {}", context);
//							if (context.getRarity() != null && context.getCoords() != null) {									
//								WELL_REGISTRY.get(dimension).register(context.getRarity(), context.getCoords(), context);
//							}
//						});
//					}
//				}
//			});
//		}            		
//	}


	/**
	 * 
	 * @param dimension
	 * @param genType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static GeneratedRegistry<ChestGenContext> getChestGeneratedRegistry(ResourceLocation dimension, IFeatureType genType) {
		return (GeneratedRegistry<ChestGenContext>) getGeneratedRegistry(CHEST_REGISTRY, dimension, genType);
	}

	/**
	 * 
	 * @param registry
	 * @param dimension
	 * @param genType
	 * @return
	 */
	public static GeneratedRegistry<? extends GeneratedContext> getGeneratedRegistry(Map<ResourceLocation, Map<IFeatureType, GeneratedRegistry<?>>> registry, ResourceLocation dimension, IFeatureType genType) {
		Map<IFeatureType, GeneratedRegistry<? extends GeneratedContext>> registryMap = registry.get(dimension);
		GeneratedRegistry<? extends GeneratedContext> genRegistry = null;
		if (registryMap != null) {
			genRegistry =  (GeneratedRegistry<? extends GeneratedContext>) registryMap.get(genType);
		}
		return genRegistry;
	}
}

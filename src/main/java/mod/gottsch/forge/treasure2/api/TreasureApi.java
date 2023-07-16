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
package mod.gottsch.forge.treasure2.api;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IEnum;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.IMarkerType;
import mod.gottsch.forge.treasure2.core.enums.IPitType;
import mod.gottsch.forge.treasure2.core.enums.MarkerType;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.IMarkerGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.ruin.IRuinGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.IWellGenerator;
import mod.gottsch.forge.treasure2.core.item.IKeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.registry.*;
import mod.gottsch.forge.treasure2.core.structure.IStructureCategory;
import mod.gottsch.forge.treasure2.core.structure.IStructureType;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.wishable.IWishableHandler;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.selector.IFeatureGeneratorSelector;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
public class TreasureApi {
	public static final String RARITY = "rarity";
	public static final String KEY_LOCK_CATEGORY = "keyLockCategory";
	public static final String GENERATOR_TYPE = "generatorType";
	public static final String CHEST_GENERATOR_TYPE = "chestGeneratorType";
	public static final String REGION_PLACEMENT = "regionPlacement";
	
	public static final String LOOT_TABLE_TYPE = "lootTableType";
	public static final String FEATURE_TYPE = "featureType";
	public static final String STRUCTURE_CATEGORY = "structureCategory";
	public static final String STRUCTURE_TYPE = "structureType";
	public static final String PIT_TYPE = "pitType";
	public static final String MARKER_TYPE = "markerType";
	
	
	/**
	 * 
	 * @param rarity
	 */
	public static void registerRarity(IRarity rarity) {
		EnumRegistry.register(RARITY, rarity);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Optional<IRarity> getRarity(String key) {
		IEnum ienum = EnumRegistry.get(RARITY, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IRarity) ienum);
		}
	}
	
	public static List<IRarity> getRarities() {
		List<IEnum> enums = EnumRegistry.getValues(RARITY);
		return enums.stream().map(e -> (IRarity)e).collect(Collectors.toList());
	}
	
	/**
	 * 
	 * @param category
	 */
	public static void registerKeyLockCategory(IKeyLockCategory category) {
		EnumRegistry.register(KEY_LOCK_CATEGORY, category);
		// TODO remove
//		KeyLockRegistry.registerCategory(category);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Optional<IKeyLockCategory> getKeyLockCategory(String key) {
		IEnum ienum = EnumRegistry.get(KEY_LOCK_CATEGORY, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IKeyLockCategory) ienum);
		}
	}
	
	public static void registerLootTableType(ILootTableType type) {
		EnumRegistry.register(LOOT_TABLE_TYPE, type);		
	}
	
	public static Optional<ILootTableType> getLootTableType(String key) {
		IEnum ienum = EnumRegistry.get(LOOT_TABLE_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((ILootTableType) ienum);
		}
	}	
	

	public static void registerFeatureType(IFeatureType type) {
		EnumRegistry.register(FEATURE_TYPE, type);
	}
	
	public static Optional<IFeatureType> getFeatureType(String key) {
		IEnum ienum = EnumRegistry.get(FEATURE_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IFeatureType) ienum);
		}
	}
	
	public static void registerStructureCategory(IStructureCategory category) {
		EnumRegistry.register(STRUCTURE_CATEGORY, category);
	}
	
	public static Optional<IStructureCategory> getStructureCategory(String key) {
		IEnum ienum = EnumRegistry.get(STRUCTURE_CATEGORY, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IStructureCategory) ienum);
		}
	}
	
	public static List<IStructureCategory> getStructureCategories() {
		List<IEnum> enums = EnumRegistry.getValues(STRUCTURE_CATEGORY);
		return enums.stream().map(e -> (IStructureCategory)e).collect(Collectors.toList());		
	}
	
	public static void registerStructureType(IStructureType type) {
		EnumRegistry.register(STRUCTURE_TYPE, type);
	}
	
	public static Optional<IStructureType> getStructureType(String key) {
		IEnum ienum = EnumRegistry.get(STRUCTURE_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IStructureType) ienum);
		}
	}
	
	public static void registerPitType(IPitType type) {
		EnumRegistry.register(PIT_TYPE, type);
	}
	
	public static Optional<IPitType> getPitType(String key) {
		IEnum ienum = EnumRegistry.get(PIT_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IPitType) ienum);
		}
	}
	
	public static void registerMarkerType(IMarkerType type) {
		EnumRegistry.register(MARKER_TYPE, type);
	}
	
	public static Optional<IMarkerType> getMarkerType(String key) {
		IEnum ienum = EnumRegistry.get(MARKER_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IMarkerType) ienum);
		}
	}
	
	/**
	 * 
	 * @param rarity
	 */
	public static void registerRarityTags(IRarity rarity) {
		// check if the rarity is registered
		if (!EnumRegistry.isRegistered(RARITY, rarity)) {
			Treasure.LOGGER.warn("rarity {} is not registered. unable to complete tag registration.", rarity);
			return;
		}
		
		// create tags
		TagKey<Item> keyTag = TreasureTags.Items.mod(Treasure.MODID, "keys/" + rarity.getValue());
		TagKey<Item> lockTag = TreasureTags.Items.mod(Treasure.MODID, "locks/" + rarity.getValue());
		TagKey<Block> chestTag = TreasureTags.Blocks.mod(Treasure.MODID, "chests/" + rarity.getValue());
		
		TagRegistry.registerKeyLockChests(rarity, keyTag, lockTag, chestTag);
	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 */
	public static void registerRarityTags(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag) {
		// check if the rarity is registered
		if (!EnumRegistry.isRegistered(RARITY, rarity)) {
			Treasure.LOGGER.warn("rarity {} is not registered. unable to complete tag registration.", rarity);
			return;
		}
		
		TagRegistry.registerKeyLocks(rarity, keyTag, lockTag);
	}
	
	/**
	 * 
	 * @param rarity
	 * @param keyTag
	 * @param lockTag
	 * @param chestTag
	 */
	public static void registerRarityTags(IRarity rarity, TagKey<Item> keyTag, TagKey<Item> lockTag, TagKey<Block> chestTag) {
		// check if the rarity is registered
		if (!EnumRegistry.isRegistered(RARITY, rarity)) {
			Treasure.LOGGER.warn("rarity {} is not registered. unable to complete tag registration.", rarity);
			return;
		}
		TagRegistry.registerKeyLockChests(rarity, keyTag, lockTag, chestTag);
	}
	

	public static void registerRarityTags(IRarity rarity, TagKey<Block>chestTag) {
		// check if the rarity is registered
		if (!EnumRegistry.isRegistered(RARITY, rarity)) {
			Treasure.LOGGER.warn("rarity {} is not registered. unable to complete tag registration.", rarity);
			return;
		}
		TagRegistry.registerChests(rarity, chestTag);
	}
	
	public static void registerWishableTag(IRarity rarity, TagKey<Item> tag) {
		TagRegistry.registerWishable(rarity, tag);
	}
	
	// TODO does the api need to expose this?
	public static TagKey<Item> getKeyTag(IRarity rarity) {
		return TagRegistry.getKeyTag(rarity);
	}
	
	// these registerXXX() methods simply register the object by its resource name
	public static void registerKey(RegistryObject<KeyItem> key) {
		KeyLockRegistry.registerKey(key);
	}

	public static void registerLock(RegistryObject<LockItem> lock) {
		KeyLockRegistry.registerLock(lock);		
	}	

	// TODO add method for Locks accepting keys

	public static void registerChest(RegistryObject<Block> chest) {
		ChestRegistry.register(chest);
	}
	
	public static void registerWishableHandler(Item item, IWishableHandler handler) {
		WishableRegistry.registerHandler(item, handler);
	}
	
	public static void registerLootTables(String modID) {
		TreasureLootTableRegistry.register(modID);
	}
	
	public static void registerTemplates(String modID) {
		TreasureTemplateRegistry.register(modID);
	}

	/*
	 * Registers the chest generator object.
	 */
//	@Deprecated
//	public static void registerChestGenerator(IChestGeneratorType type, IChestGenerator generator) {
//		ChestGeneratorRegistry.registerGeneator(type, generator);
//	}
	
	/**
	 * Registers the chest generator by rarity and generatorType.
	 * @param rarity
	 * @param featureType
	 * @param chestGeneratorType
	 */
	public static void registerChestFeatureGenerator(IRarity rarity, IFeatureType featureType) {
		RarityLevelWeightedChestGeneratorRegistry.registerGenerator(rarity, featureType);		
	}
	
	/*
	 * Registers the chest generator object.
	 */
	public static void registerChestGenerator(IRarity rarity, IChestGenerator generator) {
		ChestGeneratorRegistry.registerGeneator(rarity, generator);
	}
	
	public static void registerPitGenerator(IPitType type, IPitGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		PitGeneratorRegistry.register(type, generator);
	}
	
	public static void registerMarkerGenerator(MarkerType type, IMarkerGenerator<GeneratorResult<GeneratorData>> generator) {
		MarkerGeneratorRegistry.register(type, generator);
	}
	
	public static void registerRuinGenerator(IStructureCategory category, IRuinGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		RuinGeneratorRegistry.register(category, generator);
	}
	
	public static void registerWellGenerator(IStructureCategory category, IWellGenerator<GeneratorResult<GeneratorData>> generator) {
		WellGeneratorRegistry.register(category, generator);		
	}
	
	/*
	 * Maps the chest generator object by rarity and type.
	 */
//	@Deprecated
//	public static void registerWeightedChestGenerator(IRarity rarity, IGeneratorType type, IChestGenerator generator, Number weight) {
//		WeightedChestGeneratorRegistry.registerGenerator(rarity, type, generator, weight);
//	}
//	public static void registerWeightedChestGenerator(IRarity rarity, IGeneratorType type, IChestGeneratorType chestGenType, Number weight) {
//		WeightedChestGeneratorRegistry.registerGenerator(rarity, type, chestGenType, weight);
//	}
	
	public List<LockItem> getLocks(IRarity rarity) {
		return KeyLockRegistry.getLocks(rarity).stream().map(lock -> lock.get()).collect(Collectors.toList());
	}

	public static void registerFeatureGeneator(IFeatureType type, IFeatureGenerator featureGenerator) {
		FeatureGeneratorRegistry.registerGenerator(type, featureGenerator);
	}

	public static void registerFeatureGeneatorSelector(FeatureType type, IRarity rarity, IFeatureGeneratorSelector selector) {
		FeatureGeneratorSelectorRegistry.registerSelector(type, rarity, selector);
	}
	
	public static void registerMimic(ResourceLocation chestName, ResourceLocation mimicName) {
		MimicRegistry.register(chestName, mimicName);
	}
}

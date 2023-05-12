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
import mod.gottsch.forge.treasure2.core.enums.IPitType;
import mod.gottsch.forge.treasure2.core.enums.IRegionPlacement;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.IGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.item.IKeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.loot.ISpecialLootTables;
import mod.gottsch.forge.treasure2.core.registry.*;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
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
	public static final String SPECIAL_LOOT_TABLE = "specialLootTable";
	
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
	
	/**
	 * 
	 * @param type
	 */
	public static void registerGeneratorType(IGeneratorType type) {
		EnumRegistry.register(GENERATOR_TYPE, type);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static Optional<IGeneratorType> getGeneratorType(String key) {
		IEnum ienum = EnumRegistry.get(GENERATOR_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IGeneratorType) ienum);
		}
	}
	
	public static void registerChestGeneratorType(IChestGeneratorType type) {
		EnumRegistry.register(CHEST_GENERATOR_TYPE, type);
	}
	
	public static Optional<IChestGeneratorType> getChestGeneratorType(String key) {
		IEnum ienum = EnumRegistry.get(CHEST_GENERATOR_TYPE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IChestGeneratorType) ienum);
		}
	}	

	public static void registerRegionPlacement(IRegionPlacement placement) {
		EnumRegistry.register(REGION_PLACEMENT, placement);
	}
	
	public static Optional<IRegionPlacement> getRegionPlacement(String key) {
		IEnum ienum = EnumRegistry.get(REGION_PLACEMENT, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((IRegionPlacement) ienum);
		}
	}
	
	@Deprecated
	public static void registerSpecialLootTable(ISpecialLootTables table) {
		EnumRegistry.register(SPECIAL_LOOT_TABLE, table);
	}
	
	@Deprecated
	public static Optional<ISpecialLootTables> getSpecialLootTable(String key) {
		IEnum ienum = EnumRegistry.get(SPECIAL_LOOT_TABLE, key);
		if (ienum == null) {
			return Optional.empty();
		}
		else {
			return Optional.of((ISpecialLootTables) ienum);
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
	
//	public static void registerRarity(IRarity rarity, TagKey<Block> tag) {
//		RarityRegistry.register(rarity, tag);
//	}
	
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
	

	public static void registerRarityTags(Rarity rarity, TagKey<Block>chestTag) {
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
	
	public static void registerWishable(RegistryObject<Item> wishable) {
		WishableRegistry.register(wishable);
	}
	
	public static void registerLootTables(String modID) {
		TreasureLootTableRegistry.register(modID);
	}
	
	public static void registerMeta(String modID) {
		TreasureMetaRegistry.register(modID);
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
	 * @param generatorType
	 * @param chestGeneratorType
	 */
	public static void registerTypeChestGenerator(IRarity rarity, IGeneratorType generatorType,	IChestGeneratorType chestGeneratorType) {
		RarityLevelWeightedChestGeneratorRegistry.registerGenerator(rarity, generatorType, chestGeneratorType);		
	}
	
	/*
	 * Registers the chest generator object.
	 */
	public static void registerChestGenerator(IChestGenerator generator) {
		ChestGeneratorRegistry.registerGeneator(generator);
	}
	
	public static void registerPitGenerator(IPitType type, IPitGenerator<GeneratorResult<ChestGeneratorData>> generator) {
		PitGeneratorRegistry.register(type, generator);
	}
	
	/*
	 * Maps the chest generator object by rarity and type.
	 */
	@Deprecated
	public static void registerWeightedChestGenerator(IRarity rarity, IGeneratorType type, IChestGenerator generator, Number weight) {
		WeightedChestGeneratorRegistry.registerGenerator(rarity, type, generator, weight);
	}
	public static void registerWeightedChestGenerator(IRarity rarity, IGeneratorType type, IChestGeneratorType chestGenType, Number weight) {
		WeightedChestGeneratorRegistry.registerGenerator(rarity, type, chestGenType, weight);
	}
	
	public List<LockItem> getLocks(IRarity rarity) {
		return KeyLockRegistry.getLocks(rarity).stream().map(lock -> lock.get()).collect(Collectors.toList());
	}

}

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
package mod.gottsch.forge.treasure2.core.setup;

import static mod.gottsch.forge.treasure2.core.generator.GeneratorType.AQUATIC;
import static mod.gottsch.forge.treasure2.core.generator.GeneratorType.TERRESTRIAL;
import static mod.gottsch.forge.treasure2.core.generator.GeneratorType.WELL;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.*;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.*;
import mod.gottsch.forge.treasure2.core.generator.pit.*;
import mod.gottsch.forge.treasure2.core.item.KeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.SpecialLootTables;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.registry.WeightedChestGeneratorRegistry;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * 
 * @author Mark Gottschling on Nov 10, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonSetup {

	/**
	 * 
	 * @param event
	 */
	public static void init(final FMLCommonSetupEvent event) {
		Config.instance.addRollingFileAppender(Treasure.MODID);

		// register rarities
		TreasureApi.registerRarity(Rarity.COMMON);
		TreasureApi.registerRarity(Rarity.UNCOMMON);
		TreasureApi.registerRarity(Rarity.SCARCE);
		TreasureApi.registerRarity(Rarity.RARE);
		TreasureApi.registerRarity(Rarity.EPIC);
		TreasureApi.registerRarity(Rarity.LEGENDARY);
		TreasureApi.registerRarity(Rarity.MYTHICAL);
		TreasureApi.registerRarity(Rarity.SKULL);

		// register the key/lock categories
		TreasureApi.registerKeyLockCategory(KeyLockCategory.ELEMENTAL);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.METALS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.GEMS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.MOB);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.WITHER);

		// register the generator types
		TreasureApi.registerGeneratorType(TERRESTRIAL);
		TreasureApi.registerGeneratorType(AQUATIC);
		TreasureApi.registerGeneratorType(WELL);
		TreasureApi.registerGeneratorType(GeneratorType.WITHER);

		// register the chest generator types
		TreasureApi.registerChestGeneratorType(COMMON);
		TreasureApi.registerChestGeneratorType(UNCOMMON);
		TreasureApi.registerChestGeneratorType(SCARCE);
		TreasureApi.registerChestGeneratorType(RARE);
		TreasureApi.registerChestGeneratorType(EPIC);
		TreasureApi.registerChestGeneratorType(LEGENDARY);
		TreasureApi.registerChestGeneratorType(MYTHICAL);
		TreasureApi.registerChestGeneratorType(ChestGeneratorType.WITHER);
		TreasureApi.registerChestGeneratorType(SKULL);
		TreasureApi.registerChestGeneratorType(GOLD_SKULL);
		TreasureApi.registerChestGeneratorType(CRYSTAL_SKULL);
		TreasureApi.registerChestGeneratorType(CAULDRON);
		
		// register the region placements
		TreasureApi.registerRegionPlacement(RegionPlacement.SUBMERGED);

		TreasureApi.registerSpecialLootTable(SpecialLootTables.BLACK_PEARL_WELL);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.CAULDRON_CHEST);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.CRYSTAL_SKULL_CHEST);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.GOLD_SKULL_CHEST);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.GOLD_WELL);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.SILVER_WELL);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.SKULL_CHEST);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.WHITE_PEARL_WELL);
		TreasureApi.registerSpecialLootTable(SpecialLootTables.WITHER_CHEST);

		// register rarity tags.
		TreasureApi.registerRarityTags(Rarity.COMMON, 
				TreasureTags.Items.COMMON_KEY, 
				TreasureTags.Items.COMMON_LOCKS,
				TreasureTags.Blocks.COMMON_CHESTS);
		TreasureApi.registerRarityTags(Rarity.UNCOMMON, 
				TreasureTags.Items.UNCOMMON_KEY, 
				TreasureTags.Items.UNCOMMON_LOCKS,
				TreasureTags.Blocks.UNCOMMON_CHESTS);
		TreasureApi.registerRarityTags(Rarity.SCARCE, 
				TreasureTags.Items.SCARCE_KEY, 
				TreasureTags.Items.SCARCE_LOCKS,
				TreasureTags.Blocks.SCARCE_CHESTS);
		TreasureApi.registerRarityTags(Rarity.RARE, 
				TreasureTags.Items.RARE_KEY, 
				TreasureTags.Items.RARE_LOCKS,
				TreasureTags.Blocks.RARE_CHESTS);
		TreasureApi.registerRarityTags(Rarity.EPIC, 
				TreasureTags.Items.EPIC_KEY, 
				TreasureTags.Items.EPIC_LOCKS,
				TreasureTags.Blocks.EPIC_CHESTS);
		TreasureApi.registerRarityTags(Rarity.LEGENDARY, 
				TreasureTags.Items.LEGENDARY_KEYS, 
				TreasureTags.Items.LEGENDARY_LOCKS,
				TreasureTags.Blocks.LEGENDARY_CHESTS);
		TreasureApi.registerRarityTags(Rarity.MYTHICAL, 
				TreasureTags.Items.MYTHICAL_KEY, 
				TreasureTags.Items.MYTHICAL_LOCKS,
				TreasureTags.Blocks.MYTHICAL_CHESTS);

		TreasureApi.registerRarityTags(Rarity.SKULL, TreasureTags.Blocks.SKULL_CHESTS);
		
		// regsiter wishable tags
		TreasureApi.registerWishableTag(Rarity.COMMON, TreasureTags.Items.COMMON_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.UNCOMMON, TreasureTags.Items.UNCOMMON_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.SCARCE, TreasureTags.Items.SCARCE_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.RARE, TreasureTags.Items.RARE_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.EPIC, TreasureTags.Items.EPIC_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.LEGENDARY, TreasureTags.Items.LEGENDARY_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.MYTHICAL, TreasureTags.Items.MYTHICAL_WISHABLE);

		// register all the keys
		TreasureApi.registerKey(TreasureItems.WOOD_KEY);
		TreasureApi.registerKey(TreasureItems.STONE_KEY);
		TreasureApi.registerKey(TreasureItems.LEAF_KEY);
		TreasureApi.registerKey(TreasureItems.EMBER_KEY);
		TreasureApi.registerKey(TreasureItems.LIGHTNING_KEY);

		TreasureApi.registerKey(TreasureItems.IRON_KEY);
		TreasureApi.registerKey(TreasureItems.GOLD_KEY);
		TreasureApi.registerKey(TreasureItems.METALLURGISTS_KEY);

		TreasureApi.registerKey(TreasureItems.DIAMOND_KEY);
		TreasureApi.registerKey(TreasureItems.EMBER_KEY);
		TreasureApi.registerKey(TreasureItems.RUBY_KEY);
		TreasureApi.registerKey(TreasureItems.SAPPHIRE_KEY);
		TreasureApi.registerKey(TreasureItems.JEWELLED_KEY);

		TreasureApi.registerKey(TreasureItems.SPIDER_KEY);
		TreasureApi.registerKey(TreasureItems.WITHER_KEY);

		TreasureApi.registerKey(TreasureItems.SKELETON_KEY);

		TreasureApi.registerKey(TreasureItems.PILFERERS_LOCK_PICK);
		TreasureApi.registerKey(TreasureItems.THIEFS_LOCK_PICK);

		TreasureApi.registerKey(TreasureItems.ONE_KEY);
		// register all the locks
		TreasureApi.registerLock(TreasureItems.WOOD_LOCK);
		TreasureApi.registerLock(TreasureItems.STONE_LOCK);
		TreasureApi.registerLock(TreasureItems.LEAF_LOCK);
		TreasureApi.registerLock(TreasureItems.EMBER_LOCK);
		TreasureApi.registerLock(TreasureItems.IRON_LOCK);
		TreasureApi.registerLock(TreasureItems.GOLD_LOCK);

		TreasureApi.registerLock(TreasureItems.DIAMOND_LOCK);
		TreasureApi.registerLock(TreasureItems.EMERALD_LOCK);
		TreasureApi.registerLock(TreasureItems.RUBY_LOCK);
		TreasureApi.registerLock(TreasureItems.SAPPHIRE_LOCK);

		TreasureApi.registerLock(TreasureItems.SPIDER_LOCK);
		TreasureApi.registerLock(TreasureItems.WITHER_LOCK);

		// register all the chests
		TreasureApi.registerChest(TreasureBlocks.WOOD_CHEST);
		TreasureApi.registerChest(TreasureBlocks.CRATE_CHEST);
		TreasureApi.registerChest(TreasureBlocks.MOLDY_CRATE_CHEST);
		TreasureApi.registerChest(TreasureBlocks.IRONBOUND_CHEST);
		TreasureApi.registerChest(TreasureBlocks.PIRATE_CHEST);
		TreasureApi.registerChest(TreasureBlocks.SAFE);
		TreasureApi.registerChest(TreasureBlocks.IRON_STRONGBOX);
		TreasureApi.registerChest(TreasureBlocks.GOLD_STRONGBOX);
		TreasureApi.registerChest(TreasureBlocks.DREAD_PIRATE_CHEST);
		TreasureApi.registerChest(TreasureBlocks.COMPRESSOR_CHEST);
		TreasureApi.registerChest(TreasureBlocks.SKULL_CHEST);
		TreasureApi.registerChest(TreasureBlocks.GOLD_SKULL_CHEST);
		TreasureApi.registerChest(TreasureBlocks.CRYSTAL_SKULL_CHEST);
		TreasureApi.registerChest(TreasureBlocks.CAULDRON_CHEST);
		TreasureApi.registerChest(TreasureBlocks.SPIDER_CHEST);
		TreasureApi.registerChest(TreasureBlocks.VIKING_CHEST);
		TreasureApi.registerChest(TreasureBlocks.CARDBOARD_BOX);
		TreasureApi.registerChest(TreasureBlocks.MILK_CRATE);
		TreasureApi.registerChest(TreasureBlocks.WITHER_CHEST);

		// register all the wishable items
		TreasureApi.registerWishable(TreasureItems.COPPER_COIN);
		TreasureApi.registerWishable(TreasureItems.SILVER_COIN);
		TreasureApi.registerWishable(TreasureItems.GOLD_COIN);
		TreasureApi.registerWishable(TreasureItems.TOPAZ);
		TreasureApi.registerWishable(TreasureItems.ONYX);
		TreasureApi.registerWishable(TreasureItems.RUBY);
		TreasureApi.registerWishable(TreasureItems.SAPPHIRE);
		TreasureApi.registerWishable(TreasureItems.WHITE_PEARL);
		TreasureApi.registerWishable(TreasureItems.BLACK_PEARL);

		// register loot tables
		TreasureApi.registerLootTables(Treasure.MODID);

		/*
		 *  in order for chest context to know what generator to use, we need a registry (map)
		 *  of generator type to generator object.
		 */
		// register chest generators that can be used within the mod
		TreasureApi.registerChestGenerator(new CommonChestGenerator(COMMON));
		TreasureApi.registerChestGenerator(new UncommonChestGenerator(UNCOMMON));
		TreasureApi.registerChestGenerator(new ScarceChestGenerator(SCARCE));
		TreasureApi.registerChestGenerator(new RareChestGenerator(RARE));
		TreasureApi.registerChestGenerator(new EpicChestGenerator(EPIC));
		TreasureApi.registerChestGenerator(new LegendaryChestGenerator(LEGENDARY));
		TreasureApi.registerChestGenerator(new MythicalChestGenerator(MYTHICAL));
		TreasureApi.registerChestGenerator(new SkullChestGenerator(SKULL));
		TreasureApi.registerChestGenerator(new GoldSkullChestGenerator(GOLD_SKULL));
		TreasureApi.registerChestGenerator(new WitherChestGenerator(ChestGeneratorType.WITHER));
		TreasureApi.registerChestGenerator(new CauldronChestGenerator(CAULDRON));
		TreasureApi.registerChestGenerator(new CrystalSkullChestGenerator(CRYSTAL_SKULL));
		
		// register pit generators
		TreasureApi.registerPitGenerator(PitType.STANDARD, new SimplePitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new AirPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new BigBottomMobTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new CollapsingTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new LavaSideTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new LavaTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new MobTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new TntTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STANDARD, new VolcanoPitGenerator());
		
		// TEMP use normal generators for structures pits
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new SimplePitGenerator());
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new AirPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new LavaSideTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new LavaTrapPitGenerator());
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new TntTrapPitGenerator());
		
		// TODO using a nonstandard map, removes the need for a weighted chest generator. just need Rarity+GenType -> Generator
		// TreasureApi.registerNonStandardChestGenerator(TERRESTRIAL, TreasureBlocks.SKULL_CHEST, SKULL);
		
		// TODO/NOTE can we make this customizable via tags?
		// register/map chest generators by rarity and type
		// TODO if still using this weighted list, then just pass in the ChestGeneratorType instead of a new object
		// TODO what is the point of dimensions here if you can't register a different generator based on dimesion.
		// NOTE until the chest/gen rarity issue is refactored from ground up, this is the way to go without making things
		// crazy complicated.
		// link the rarity, generator type and chest generator type
		/*
		TreasureApi.registerWeightedChestGenerator(Rarity.COMMON, TERRESTRIAL, COMMON, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.UNCOMMON, TERRESTRIAL, UNCOMMON, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.SCARCE, TERRESTRIAL, SCARCE, 85);
		TreasureApi.registerWeightedChestGenerator(Rarity.SCARCE, TERRESTRIAL, SKULL, 15);
		TreasureApi.registerWeightedChestGenerator(Rarity.RARE, TERRESTRIAL, RARE, 85);
		TreasureApi.registerWeightedChestGenerator(Rarity.RARE, TERRESTRIAL, GOLD_SKULL, 15);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, TERRESTRIAL, EPIC, 240);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, TERRESTRIAL, CRYSTAL_SKULL, 30);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, TERRESTRIAL, CAULDRON, 30);
		TreasureApi.registerWeightedChestGenerator(Rarity.LEGENDARY, TERRESTRIAL, LEGENDARY, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.MYTHICAL, TERRESTRIAL, MYTHICAL, 1);
		
		TreasureApi.registerWeightedChestGenerator(Rarity.COMMON, AQUATIC, COMMON, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.UNCOMMON, AQUATIC, UNCOMMON, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.SCARCE, AQUATIC, SCARCE, 85);
		TreasureApi.registerWeightedChestGenerator(Rarity.SCARCE, AQUATIC, SKULL, 15);
		TreasureApi.registerWeightedChestGenerator(Rarity.RARE, AQUATIC, RARE, 85);
		TreasureApi.registerWeightedChestGenerator(Rarity.RARE, AQUATIC, GOLD_SKULL, 15);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, AQUATIC, EPIC, 240);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, AQUATIC, CRYSTAL_SKULL, 30);
		TreasureApi.registerWeightedChestGenerator(Rarity.EPIC, AQUATIC, CAULDRON, 30);
		TreasureApi.registerWeightedChestGenerator(Rarity.LEGENDARY, AQUATIC, LEGENDARY, 1);
		TreasureApi.registerWeightedChestGenerator(Rarity.MYTHICAL, AQUATIC, MYTHICAL, 1);
		*/
		
		// TODO register chest generator by RARITY and TYPE
		// chest context probably should take in the GeneratorType (Terrestrial / Aquatic) as well and use this registry
		// TOOD can this and ChestGenerator be merged into one registry ???
		// a 1-1 registry of rarity+type -> chest generator
		TreasureApi.registerTypeChestGenerator(Rarity.COMMON, TERRESTRIAL, COMMON);
		TreasureApi.registerTypeChestGenerator(Rarity.UNCOMMON, TERRESTRIAL, UNCOMMON);
		TreasureApi.registerTypeChestGenerator(Rarity.SCARCE, TERRESTRIAL, SCARCE);
		TreasureApi.registerTypeChestGenerator(Rarity.SKULL, TERRESTRIAL, SKULL);
		TreasureApi.registerTypeChestGenerator(Rarity.RARE, TERRESTRIAL, RARE);
//		TreasureApi.registerTypeChestGenerator(Rarity.RARE, TERRESTRIAL, GOLD_SKULL);
		TreasureApi.registerTypeChestGenerator(Rarity.EPIC, TERRESTRIAL, EPIC);
//		TreasureApi.registerTypeChestGenerator(Rarity.EPIC, TERRESTRIAL, CRYSTAL_SKULL);
//		TreasureApi.registerTypeChestGenerator(Rarity.EPIC, TERRESTRIAL, CAULDRON);
		TreasureApi.registerTypeChestGenerator(Rarity.LEGENDARY, TERRESTRIAL, LEGENDARY);
		TreasureApi.registerTypeChestGenerator(Rarity.MYTHICAL, TERRESTRIAL, MYTHICAL);
		
		TreasureApi.registerTypeChestGenerator(Rarity.COMMON, AQUATIC, COMMON);
		TreasureApi.registerTypeChestGenerator(Rarity.UNCOMMON, AQUATIC, UNCOMMON);
		TreasureApi.registerTypeChestGenerator(Rarity.SCARCE, AQUATIC, SCARCE);
		TreasureApi.registerTypeChestGenerator(Rarity.SKULL, AQUATIC, SKULL);
		TreasureApi.registerTypeChestGenerator(Rarity.RARE, AQUATIC, RARE);
		TreasureApi.registerTypeChestGenerator(Rarity.EPIC, AQUATIC, EPIC);
		TreasureApi.registerTypeChestGenerator(Rarity.LEGENDARY, AQUATIC, LEGENDARY);
		TreasureApi.registerTypeChestGenerator(Rarity.MYTHICAL, AQUATIC, MYTHICAL);
		// register network
		TreasureNetworking.register();
	}

	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), BoundSoul.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerEntitySpawn(RegistryEvent.Register<EntityType<?>> event) {
		SpawnPlacements.register(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
	}
}

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
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.CAULDRON;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.COMMON;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.CRYSTAL_SKULL;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.EPIC;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.GOLD_SKULL;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.LEGENDARY;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.MYTHICAL;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.RARE;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.SCARCE;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.SKULL;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.UNCOMMON;
import static mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType.WITHER;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.cache.FeatureCaches;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.MarkerType;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.enums.WishableExtraRarity;
import mod.gottsch.forge.treasure2.core.generator.GeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.CauldronChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.generator.chest.CommonChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.CrystalSkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.EpicChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.GoldSkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.LegendaryChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.MythicalChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.RareChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.ScarceChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.SkullChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.UncommonChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.WitherChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.GravestoneMarkerGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.StructureMarkerGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.AirPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.BigBottomMobTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.CollapsingTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.LavaSideTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.LavaTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.MobTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.SimplePitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.StructurePitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.TntTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.VolcanoPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.ruin.SurfaceRuinGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.WellGenerator;
import mod.gottsch.forge.treasure2.core.item.KeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Items;
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
		Treasure.LOGGER.debug("file appender created");
		/**
		 * Most resources in Treasure2 are associated with a Rarity. Register rarites
		 * to enable them in other features. The registry in conjunction with
		 * the IRarity interface allows enxtensibility with addon mods.
		 * Always perform a check agaisnt the registry to determine if
		 * the rarity is allowed.
		 */
		// register rarities
		TreasureApi.registerRarity(Rarity.COMMON);
		TreasureApi.registerRarity(Rarity.UNCOMMON);
		TreasureApi.registerRarity(Rarity.SCARCE);
		TreasureApi.registerRarity(Rarity.RARE);
		TreasureApi.registerRarity(Rarity.EPIC);
		TreasureApi.registerRarity(Rarity.LEGENDARY);
		TreasureApi.registerRarity(Rarity.MYTHICAL);
		TreasureApi.registerRarity(Rarity.SKULL);
		TreasureApi.registerRarity(SpecialRarity.GOLD_SKULL);
		TreasureApi.registerRarity(SpecialRarity.CRYSTAL_SKULL);
		TreasureApi.registerRarity(SpecialRarity.CAULDRON);
		TreasureApi.registerRarity(SpecialRarity.WITHER);
		TreasureApi.registerRarity(WishableExtraRarity.WHITE_PEARL);
		TreasureApi.registerRarity(WishableExtraRarity.BLACK_PEARL);
		
		// register the key/lock categories
		TreasureApi.registerKeyLockCategory(KeyLockCategory.ELEMENTAL);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.METALS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.GEMS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.MOB);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.WITHER);



		// DEPRECATED - can use IRarity instead
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
		
		// register the loot table types
		TreasureApi.registerLootTableType(LootTableType.CHESTS);
		TreasureApi.registerLootTableType(LootTableType.WISHABLES);
		TreasureApi.registerLootTableType(LootTableType.INJECTS);
		
		// TODO rename to FeatureGeneratorType - figure out if/how these play together with FeatureType
		// register the generator types
		TreasureApi.registerGeneratorType(TERRESTRIAL);
		TreasureApi.registerGeneratorType(AQUATIC);
		TreasureApi.registerGeneratorType(WELL);
		// deprecated
		TreasureApi.registerGeneratorType(GeneratorType.WITHER);
		
		/* 
		 * register the feature types.
		 * this is used so that modders can register additional
		 * feature generators to a feature.
		 */
		TreasureApi.registerFeatureType(FeatureType.TERRESTRIAL);
		TreasureApi.registerFeatureType(FeatureType.AQUATIC);
		TreasureApi.registerFeatureType(FeatureType.WELL);
		
		/*
		 *  register the feature generators.
		 *  a feature generator is a bridge or proxy between the feature object
		 *  and the generators that add changes to the world.  this allows
		 *  modders to insert addtional feature generators.
		 */
		// TODO does Config exist at this point? it should as the first line is the config setting up the file appender.
		// TODO make these weighted values in the config with a default.
//		StringUtils.defaultIfBlank(Config.getValue(), 10);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRESTRIAL,TreasureFeatureGenerators.SIMPLE_SURFACE_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRESTRIAL, TreasureFeatureGenerators.PIT_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRESTRIAL, TreasureFeatureGenerators.SURFACE_STRUCTURE_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRESTRIAL, TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR);
		
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.COMMON, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.UNCOMMON, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.SCARCE, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.RARE, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.EPIC, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.LEGENDARY, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, Rarity.MYTHICAL, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);

		// ...
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRESTRIAL, SpecialRarity.WITHER, TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR_SELECTOR);

		// TODO aquatic feature generators
		
		// register structure categories
		TreasureApi.registerStructureCategory(StructureCategory.SUBAQUEOUS);
		TreasureApi.registerStructureCategory(StructureCategory.SUBTERRANEAN);
		TreasureApi.registerStructureCategory(StructureCategory.TERRANEAN);
		
		// register structure types
		TreasureApi.registerStructureType(StructureType.MARKER);
		TreasureApi.registerStructureType(StructureType.ROOM);
		TreasureApi.registerStructureType(StructureType.RUIN);
		TreasureApi.registerStructureType(StructureType.WELL);
				
		// register pit types
		TreasureApi.registerPitType(PitType.STANDARD);
		TreasureApi.registerPitType(PitType.STRUCTURE);
		
		// register marker types
		TreasureApi.registerMarkerType(MarkerType.STANDARD);
		TreasureApi.registerMarkerType(MarkerType.STRUCTURE);
		
		// register the region placements
		TreasureApi.registerRegionPlacement(RegionPlacement.SURFACE);
		TreasureApi.registerRegionPlacement(RegionPlacement.SUBMERGED);

		// TODO these can be removed when LootTableManager is refactored
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.BLACK_PEARL_WELL);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.CAULDRON_CHEST);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.CRYSTAL_SKULL_CHEST);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.GOLD_SKULL_CHEST);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.GOLD_WELL);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.SILVER_WELL);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.SKULL_CHEST);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.WHITE_PEARL_WELL);
//		TreasureApi.registerSpecialLootTable(SpecialLootTables.WITHER_CHEST);

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
		// TODO add WHITE, BLACK_PEARL

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

		// TODO are these still valid since using Tags
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

		// register wishable handlers
		// TEMP test
		TreasureApi.registerWishableHandler(Items.DIAMOND, TreasureWishableHandlers.DEFAULT_WISHABLE_HANDLER);
		
		// register loot tables
		TreasureApi.registerLootTables(Treasure.MODID);
		
		// regiser templates
		TreasureApi.registerTemplates(Treasure.MODID);

		// TODO replace ChestGeneratorType with IRarity
		// TODO this is probably moot - mergable with TypeChestGenerator
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
		
		// TODO this type of registration creates multiple instances of every generator used. refactor.
		// ie. create TreasurePitGenerators and register, then reference here.
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
		
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new SimplePitGenerator()));
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new AirPitGenerator()));
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new LavaSideTrapPitGenerator()));
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new TntTrapPitGenerator()));
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new MobTrapPitGenerator()));
		TreasureApi.registerPitGenerator(PitType.STRUCTURE, new StructurePitGenerator(new MobTrapPitGenerator()));
		
		TreasureApi.registerRuinGenerator(StructureCategory.TERRANEAN, new SurfaceRuinGenerator());
//		TreasureApi.registerRuinGenerator(StructureCategory.SUBAQUEOUS, new SubterraneanRuinGenerator());
		
		TreasureApi.registerWellGenerator(StructureCategory.TERRANEAN, new WellGenerator());
		
		// TODO may need to add a placement enum, unless MarkerType can handle all situations. ie on Water or in Sky.
		TreasureApi.registerMarkerGenerator(MarkerType.STANDARD, new GravestoneMarkerGenerator());
		TreasureApi.registerMarkerGenerator(MarkerType.STRUCTURE, new StructureMarkerGenerator());
		
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
		// TODO merge with ChestGeneratorRegistry
		// TODO in the registry make a new class Key<IGeneratorType, IRarity> that is used as the key
		TreasureApi.registerChestFeatureGenerator(Rarity.COMMON, FeatureType.TERRESTRIAL, COMMON);
		TreasureApi.registerChestFeatureGenerator(Rarity.UNCOMMON, FeatureType.TERRESTRIAL, UNCOMMON);
		TreasureApi.registerChestFeatureGenerator(Rarity.SCARCE, FeatureType.TERRESTRIAL, SCARCE);
		TreasureApi.registerChestFeatureGenerator(Rarity.SKULL, FeatureType.TERRESTRIAL, SKULL);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.GOLD_SKULL, FeatureType.TERRESTRIAL, GOLD_SKULL);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CRYSTAL_SKULL, FeatureType.TERRESTRIAL, CRYSTAL_SKULL);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.WITHER, FeatureType.TERRESTRIAL, WITHER);
		TreasureApi.registerChestFeatureGenerator(Rarity.RARE, FeatureType.TERRESTRIAL, RARE);
		TreasureApi.registerChestFeatureGenerator(Rarity.EPIC, FeatureType.TERRESTRIAL, EPIC);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CAULDRON, FeatureType.TERRESTRIAL, CAULDRON);
		TreasureApi.registerChestFeatureGenerator(Rarity.LEGENDARY, FeatureType.TERRESTRIAL, LEGENDARY);
		TreasureApi.registerChestFeatureGenerator(Rarity.MYTHICAL, FeatureType.TERRESTRIAL, MYTHICAL);
		
		TreasureApi.registerChestFeatureGenerator(Rarity.COMMON, FeatureType.AQUATIC, COMMON);
		TreasureApi.registerChestFeatureGenerator(Rarity.UNCOMMON, FeatureType.AQUATIC, UNCOMMON);
		TreasureApi.registerChestFeatureGenerator(Rarity.SCARCE, FeatureType.AQUATIC, SCARCE);
		TreasureApi.registerChestFeatureGenerator(Rarity.SKULL, FeatureType.AQUATIC, SKULL);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.GOLD_SKULL, FeatureType.AQUATIC, GOLD_SKULL);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CRYSTAL_SKULL, FeatureType.AQUATIC, CRYSTAL_SKULL);
		TreasureApi.registerChestFeatureGenerator(Rarity.RARE, FeatureType.AQUATIC, RARE);
		TreasureApi.registerChestFeatureGenerator(Rarity.EPIC, FeatureType.AQUATIC, EPIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.LEGENDARY, FeatureType.AQUATIC, LEGENDARY);
		TreasureApi.registerChestFeatureGenerator(Rarity.MYTHICAL, FeatureType.AQUATIC, MYTHICAL);
		
		// register network
		TreasureNetworking.register();
		
		FeatureCaches.initialize();
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

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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import mod.gottsch.forge.treasure2.core.entity.monster.CauldronChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.CrateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.MoldyCrateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.PirateChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.VikingChestMimic;
import mod.gottsch.forge.treasure2.core.entity.monster.WoodChestMimic;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.MarkerType;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.SpecialRarity;
import mod.gottsch.forge.treasure2.core.enums.WishableExtraRarity;
import mod.gottsch.forge.treasure2.core.generator.chest.CauldronChestGenerator;
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
import mod.gottsch.forge.treasure2.core.generator.ruin.SubaquaticRuinGenerator;
import mod.gottsch.forge.treasure2.core.generator.ruin.SurfaceRuinGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.WellGenerator;
import mod.gottsch.forge.treasure2.core.item.KeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.TreasureCreativeModeTabs;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.structure.StructureCategory;
import mod.gottsch.forge.treasure2.core.structure.StructureType;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
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
		// create a treasure2 specific log file
		Config.instance.addRollingFileAppender(Treasure.MODID);
		Treasure.LOGGER.debug("file appender created");
				
		/**
		 * Most resources in Treasure2 are associated with a Rarity. Register rarities
		 * to enable them in other features. The registry in conjunction with
		 * the IRarity interface allows extensibility with addon mods.
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
		TreasureApi.registerRarity(SpecialRarity.SKULL);
		TreasureApi.registerRarity(SpecialRarity.GOLD_SKULL);
		TreasureApi.registerRarity(SpecialRarity.CRYSTAL_SKULL);
		TreasureApi.registerRarity(SpecialRarity.CAULDRON);
		TreasureApi.registerRarity(SpecialRarity.WITHER);
		// special rarities for wishables
		TreasureApi.registerRarity(WishableExtraRarity.WHITE_PEARL);
		TreasureApi.registerRarity(WishableExtraRarity.BLACK_PEARL);
		
		// register the key/lock categories
		TreasureApi.registerKeyLockCategory(KeyLockCategory.ELEMENTAL);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.METALS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.GEMS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.MOB);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.WITHER);
		
		// register the loot table types
		TreasureApi.registerLootTableType(LootTableType.CHESTS);
		TreasureApi.registerLootTableType(LootTableType.WISHABLES);
		TreasureApi.registerLootTableType(LootTableType.INJECTS);
		
		/* 
		 * register the feature types.
		 * this is used so that modders can register additional
		 * feature generators to a feature.
		 */
		TreasureApi.registerFeatureType(FeatureType.TERRANEAN);
		TreasureApi.registerFeatureType(FeatureType.AQUATIC);
		TreasureApi.registerFeatureType(FeatureType.WELL);
		
		/*
		 *  register the feature generators.
		 *  a feature generator is a bridge or proxy between the feature object
		 *  and the generators that add changes to the world.  this allows
		 *  modders to insert addtional feature generators.
		 */
		// TODO make these weighted values in the config with a default.
//		StringUtils.defaultIfBlank(Config.getValue(), 10);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRANEAN,TreasureFeatureGenerators.SIMPLE_SURFACE_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRANEAN, TreasureFeatureGenerators.PIT_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRANEAN, TreasureFeatureGenerators.SURFACE_STRUCTURE_FEATURE_GENERATOR);
		TreasureApi.registerFeatureGeneator(FeatureType.TERRANEAN, TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR);
		
		/*
		 * register the feature generator selectors.
		 * different featureType + rarity could use different feature generator selectors.
		 * ex. standard_chest_feature_generator_selector will select a feature generator (pit, surface or surface structure)
		 * based on a weighted randomization, whereas a wither_feature_generator_selector is specific to 
		 * the wither_generator and will only select a wither generator (there is currently only one). 
		 */
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.COMMON, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.UNCOMMON, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.SCARCE, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.RARE, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.EPIC, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.LEGENDARY, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, Rarity.MYTHICAL, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, SpecialRarity.SKULL, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, SpecialRarity.GOLD_SKULL, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, SpecialRarity.CRYSTAL_SKULL, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, SpecialRarity.CAULDRON, TreasureFeatureGenerators.STANDARD_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.TERRANEAN, SpecialRarity.WITHER, TreasureFeatureGenerators.WITHER_FEATURE_GENERATOR_SELECTOR);

		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.COMMON, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.UNCOMMON, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.SCARCE, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.RARE, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.EPIC, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.LEGENDARY, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, Rarity.MYTHICAL, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, SpecialRarity.SKULL, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, SpecialRarity.GOLD_SKULL, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, SpecialRarity.CRYSTAL_SKULL, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);
		TreasureApi.registerFeatureGeneatorSelector(FeatureType.AQUATIC, SpecialRarity.CAULDRON, TreasureFeatureGenerators.AQUATIC_CHEST_FEATURE_GENERATOR_SELECTOR);

		// ... FINISH
		
		// register structure categories
		TreasureApi.registerStructureCategory(StructureCategory.SUBAQUATIC);
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

		/*
		 * 
		 */
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

		TreasureApi.registerRarityTags(SpecialRarity.SKULL, TreasureTags.Blocks.SKULL_CHESTS);
		TreasureApi.registerRarityTags(SpecialRarity.GOLD_SKULL, TreasureTags.Blocks.GOLD_SKULL_CHESTS);
		TreasureApi.registerRarityTags(SpecialRarity.CRYSTAL_SKULL, TreasureTags.Blocks.CRYSTAL_SKULL_CHESTS);
		TreasureApi.registerRarityTags(SpecialRarity.WITHER, TreasureTags.Blocks.WITHER_CHESTS);
		
		/*
		 *  regsiter and map wishable tags to their rarity.
		 *  these are the allowable rarity grouping of wishable items.
		 *  modders can add/remove items to these tags.
		 */
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
		TreasureApi.registerKey(TreasureItems.TOPAZ_KEY);
		TreasureApi.registerKey(TreasureItems.ONYX_KEY);
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
		TreasureApi.registerLock(TreasureItems.TOPAZ_LOCK);
		TreasureApi.registerLock(TreasureItems.ONYX_LOCK);
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

		// register mimics
		TreasureApi.registerMimic(TreasureBlocks.WOOD_CHEST.getId(), TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.PIRATE_CHEST.getId(), TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.VIKING_CHEST.getId(), TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.CAULDRON_CHEST.getId(), TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.CRATE_CHEST.getId(), TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.MOLDY_CRATE_CHEST.getId(), TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		
		/*
		 *  register wishable handlers
		 *  NOTE assigning an item to DEFAULT_WISHABLE_HANDLER is redundant an unnecassary
		 *   since the default will be assigned if a handler cannot be found. The item, however, must be
		 *   added to one of the wishable tags.
		 *   The below assignment/registration is an example.
		 */
		TreasureApi.registerWishableHandler(Items.DIAMOND, TreasureWishableHandlers.DEFAULT_WISHABLE_HANDLER);
		
		// register loot tables
		TreasureApi.registerLootTables(Treasure.MODID);
		
		// regiser templates
		TreasureApi.registerTemplates(Treasure.MODID);

		/*
		 *  in order for chest context to know what generator to use, we need a registry (map)
		 *  of generator type to generator object.
		 */
		// TODO map Rarity to ChestGeneratorSelector (like FeatureGeneratorSelector and FeatureGenerator)
		// the ChestGeneratorSelector in turn selects a ChestGenerator. Using this method we can have specific chest generators (ex Spider chest gen)
		// without having to create a specific rarity for Spider
		// ex. scarce -> ScaraceChestGeneratorSelector -> [ScarceChestGenerator, SpiderChestGenerator] could be weighted or unweighted
		// this would make the special rarities moot
		
		// register chest generators that can be used within the mod
		TreasureApi.registerChestGenerator(Rarity.COMMON, new CommonChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.UNCOMMON, new UncommonChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.SCARCE, new ScarceChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.RARE, new RareChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.EPIC, new EpicChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.LEGENDARY, new LegendaryChestGenerator());
		TreasureApi.registerChestGenerator(Rarity.MYTHICAL, new MythicalChestGenerator());
		TreasureApi.registerChestGenerator(SpecialRarity.SKULL, new SkullChestGenerator());
		TreasureApi.registerChestGenerator(SpecialRarity.GOLD_SKULL, new GoldSkullChestGenerator());
		TreasureApi.registerChestGenerator(SpecialRarity.WITHER, new WitherChestGenerator());
		TreasureApi.registerChestGenerator(SpecialRarity.CAULDRON, new CauldronChestGenerator());
		TreasureApi.registerChestGenerator(SpecialRarity.CRYSTAL_SKULL, new CrystalSkullChestGenerator());
		
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
		TreasureApi.registerRuinGenerator(StructureCategory.SUBAQUATIC, new SubaquaticRuinGenerator());
		
		TreasureApi.registerWellGenerator(StructureCategory.TERRANEAN, new WellGenerator());
		
		// TODO may need to add a placement enum, unless MarkerType can handle all situations. ie on Water or in Sky.
		TreasureApi.registerMarkerGenerator(MarkerType.STANDARD, new GravestoneMarkerGenerator());
		TreasureApi.registerMarkerGenerator(MarkerType.STRUCTURE, new StructureMarkerGenerator());

		TreasureApi.registerChestFeatureGenerator(Rarity.COMMON, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.UNCOMMON, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.SCARCE, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.SKULL, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.GOLD_SKULL, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CRYSTAL_SKULL, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.WITHER, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.RARE, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.EPIC, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CAULDRON, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.LEGENDARY, FeatureType.TERRANEAN);
		TreasureApi.registerChestFeatureGenerator(Rarity.MYTHICAL, FeatureType.TERRANEAN);
		
		TreasureApi.registerChestFeatureGenerator(Rarity.COMMON, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.UNCOMMON, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.SCARCE, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.SKULL, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.GOLD_SKULL, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(SpecialRarity.CRYSTAL_SKULL, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.RARE, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.EPIC, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.LEGENDARY, FeatureType.AQUATIC);
		TreasureApi.registerChestFeatureGenerator(Rarity.MYTHICAL, FeatureType.AQUATIC);
		
		TreasureFeatureGenerators.initialize();
		
		// register network
		TreasureNetworking.register();
	}

	// ONLY NEED this event if adding items to an existing tab.
//	@SubscribeEvent
//	public static void registemItemsToTab(BuildCreativeModeTabContentsEvent event) {
//		if (event.getTab() == TreasureCreativeModeTabs.MOD_TAB.get()) {
//			// add all items
//			Registration.ITEMS.getEntries().forEach(item -> {
//				if (!item.equals(TreasureItems.LOGO)) {
//					event.accept(item.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
//				}
//			});
//		}
//	}
	
	@SubscribeEvent
	public static void onAttributeCreate(EntityAttributeCreationEvent event) {
		event.put(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), BoundSoul.createAttributes().build());
		event.put(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), WoodChestMimic.createAttributes().build());
		event.put(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), PirateChestMimic.createAttributes().build());
		event.put(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), VikingChestMimic.createAttributes().build());
		event.put(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), CauldronChestMimic.createAttributes().build());
		event.put(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), CrateChestMimic.createAttributes().build());
		event.put(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), MoldyCrateChestMimic.createAttributes().build());

	}

	@SubscribeEvent
	public static void registerEntitySpawn(SpawnPlacementRegisterEvent event) {
		// these registers don't actual spawn anything. these are the rules if & when a mob is spawned.
		// to actually enable the spawning of a mob, the entity has to be registered to a biome(s).
		event.register(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

	}
}

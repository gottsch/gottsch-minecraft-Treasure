/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.setup;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.entity.monster.*;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.wishable.TreasureWishableHandlers;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.levelgen.Heightmap;
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

		/* 
		 * register the feature types.
		 */
		TreasureApi.registerFeatureType(FeatureType.TERRANEAN);
		TreasureApi.registerFeatureType(FeatureType.AQUATIC);
		TreasureApi.registerFeatureType(FeatureType.WELL);

		// register mimics
		TreasureApi.registerMimic(TreasureBlocks.WOOD_CHEST.getId(), TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.PIRATE_CHEST.getId(), TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.VIKING_CHEST.getId(), TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.CAULDRON_CHEST.getId(), TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.CRATE_CHEST.getId(), TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.MOLDY_CRATE_CHEST.getId(), TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.CARDBOARD_BOX.getId(), TreasureEntities.CARDBOARD_BOX_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.MILK_CRATE.getId(), TreasureEntities.MILK_CRATE_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.BARREL_CHEST.getId(), TreasureEntities.BARREL_MIMIC_ENTITY_TYPE.getId());
		TreasureApi.registerMimic(TreasureBlocks.VANILLA_CHEST.getId(), TreasureEntities.VANILLA_CHEST_MIMIC_ENTITY_TYPE.getId());

		/*
		 *  register wishable handlers
		 *  NOTE assigning an item to DEFAULT_WISHABLE_HANDLER is redundant and unnecessary
		 *   since the default will be assigned if a handler cannot be found. The item, however, must be
		 *   added to one of the wishable tags.
		 *   The below assignment/registration is an example.
		 */
		TreasureApi.registerWishableHandler(Items.DIAMOND, TreasureWishableHandlers.DEFAULT_WISHABLE_HANDLER);

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
		event.put(TreasureEntities.WITHERWOOD_GOLEM_ENTITY_TYPE.get(), WitherwoodGolem.createAttributes().build());
		event.put(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), WoodChestMimic.createAttributes().build());
		event.put(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), PirateChestMimic.createAttributes().build());
		event.put(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), VikingChestMimic.createAttributes().build());
		event.put(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), CauldronChestMimic.createAttributes().build());
		event.put(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), CrateChestMimic.createAttributes().build());
		event.put(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), MoldyCrateChestMimic.createAttributes().build());
		event.put(TreasureEntities.CARDBOARD_BOX_MIMIC_ENTITY_TYPE.get(), CardboardBoxMimic.createAttributes().build());
		event.put(TreasureEntities.MILK_CRATE_MIMIC_ENTITY_TYPE.get(), MilkCrateMimic.createAttributes().build());
		event.put(TreasureEntities.BARREL_MIMIC_ENTITY_TYPE.get(), BarrelMimic.createAttributes().build());
		event.put(TreasureEntities.VANILLA_CHEST_MIMIC_ENTITY_TYPE.get(), VanillaChestMimic.createAttributes().build());
	}

	@SubscribeEvent
	public static void registerEntitySpawn(
			SpawnPlacementRegisterEvent event) {
		// these registers don't actual spawn anything. these are the rules if & when a mob is spawned.
		// to actually enable the spawning of a mob, the entity has to be registered to a biome(s).
		event.register(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.WITHERWOOD_GOLEM_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);

		event.register(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.CARDBOARD_BOX_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.MILK_CRATE_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.BARREL_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
		event.register(TreasureEntities.VANILLA_CHEST_MIMIC_ENTITY_TYPE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
	}
}

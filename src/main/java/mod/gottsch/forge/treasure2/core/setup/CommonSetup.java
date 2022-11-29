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
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.KeyLockCategory;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
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
		
		// register each rarity and rarity tags.
		TreasureApi.registerRarity(Rarity.COMMON, 
				TreasureTags.Items.COMMON_KEY, 
				TreasureTags.Items.COMMON_LOCKS,
				TreasureTags.Blocks.COMMON_CHESTS);
		TreasureApi.registerRarity(Rarity.UNCOMMON, 
				TreasureTags.Items.UNCOMMON_KEY, 
				TreasureTags.Items.UNCOMMON_LOCKS,
				TreasureTags.Blocks.UNCOMMON_CHESTS);
		TreasureApi.registerRarity(Rarity.SCARCE, 
				TreasureTags.Items.SCARCE_KEY, 
				TreasureTags.Items.SCARCE_LOCKS,
				TreasureTags.Blocks.SCARCE_CHESTS);
		TreasureApi.registerRarity(Rarity.RARE, 
				TreasureTags.Items.RARE_KEY, 
				TreasureTags.Items.RARE_LOCKS,
				TreasureTags.Blocks.RARE_CHESTS);
		TreasureApi.registerRarity(Rarity.EPIC, 
				TreasureTags.Items.EPIC_KEY, 
				TreasureTags.Items.EPIC_LOCKS,
				TreasureTags.Blocks.EPIC_CHESTS);
		TreasureApi.registerRarity(Rarity.LEGENDARY, 
				TreasureTags.Items.LEGENDARY_KEYS, 
				TreasureTags.Items.LEGENDARY_LOCKS,
				TreasureTags.Blocks.LEGENDARY_CHESTS);
		TreasureApi.registerRarity(Rarity.MYTHICAL, 
				TreasureTags.Items.MYTHICAL_KEY, 
				TreasureTags.Items.MYTHICAL_LOCKS,
				TreasureTags.Blocks.MYTHICAL_CHESTS);
		
		// regsiter wishable tags
		TreasureApi.registerWishableTag(Rarity.COMMON, TreasureTags.Items.COMMON_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.UNCOMMON, TreasureTags.Items.UNCOMMON_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.SCARCE, TreasureTags.Items.SCARCE_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.RARE, TreasureTags.Items.RARE_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.EPIC, TreasureTags.Items.EPIC_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.LEGENDARY, TreasureTags.Items.LEGENDARY_WISHABLE);
		TreasureApi.registerWishableTag(Rarity.MYTHICAL, TreasureTags.Items.MYTHICAL_WISHABLE);
		
		// register the key/lock categories
		TreasureApi.registerKeyLockCategory(KeyLockCategory.ELEMENTAL);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.METALS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.GEMS);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.MOB);
		TreasureApi.registerKeyLockCategory(KeyLockCategory.WITHER);
		
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

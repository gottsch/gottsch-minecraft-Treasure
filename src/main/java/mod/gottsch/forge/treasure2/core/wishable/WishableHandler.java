/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.wishable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootPoolShell;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootGenerators;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.WishableRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.registries.RegistryObject;

/**
 * TODO should this be a instance object(s) and assigned to different wishable items ??
 * Currently all Wishables are associated with this WishableHandler
 * @author Mark Gottschling May 26, 2023
 *
 */
public class WishableHandler implements IWishableHandler {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param itemStack
	 * @param coords
	 * @return
	 */
	public Optional<ItemStack> generateLoot(Level world, Random random, ItemEntity itemEntity, ICoords coords) {
		ItemStack outputStack = null;

		ItemStack itemStack = itemEntity.getItem();
		
		// determine rarity of item
		IRarity rarity = WishableRegistry.getRarity(itemStack.getItem()).orElse(Rarity.COMMON);	
		List<LootTableShell> lootTables = getLootTables(rarity);

//		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
		if (lootTables.isEmpty()) {
			outputStack = getDefaultLootKey(random, rarity);
		}
		else {
			// attempt to get the player who dropped the coin
//			ItemStack wealthItem = itemStack;
//			CompoundTag tag = wealthItem.getTag();
//			Treasure.LOGGER.debug("item as a tag");
			Player player = null;
//			if (tag != null && tag.contains(DROPPED_BY)) {
			if (itemEntity.getThrower() != null) {
				try {
//					UUID playerUuid = tag.getUUID(DROPPED_BY);
					UUID playerUuid = itemEntity.getThrower();
					player = world.getPlayerByUUID(playerUuid);
				}
				catch(Exception e) {
					// catch silently
				}

				if (player != null && Treasure.LOGGER.isDebugEnabled()) {
					Treasure.LOGGER.debug("coin dropped by player -> {}", player.getName());
				}
				else {
					Treasure.LOGGER.debug("can't find player!");
				}
			}
//			Treasure.LOGGER.debug("player -> {}", player.getName().getString());

			// select a table shell
			LootTableShell tableShell = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
			if (tableShell.getResourceLocation() == null) {
				return Optional.empty();
			}

			// get the vanilla table from shell
			LootTable table = world.getServer().getLootTables().get(tableShell.getResourceLocation());
			// get a list of loot pools
			List<LootPoolShell> lootPoolShells = tableShell.getPools();
			
			// generate a context
			LootContext lootContext = getLootContext(world, player, coords);

			List<ItemStack> itemStacks = new ArrayList<>();
			for (LootPoolShell pool : lootPoolShells) {
				Treasure.LOGGER.debug("processing pool -> {}", pool.getName());
				// go get the vanilla managed pool
				LootPool lootPool = table.getPool(pool.getName());				
				// geneate loot from pools
				lootPool.addRandomItems(itemStacks::add, lootContext);
			}
	
			// get all injected loot tables
			injectLoot(world, random, itemStacks, rarity, lootContext);
			
			for (ItemStack stack : itemStacks) {
				Treasure.LOGGER.debug("possible loot item -> {}", stack.getItem().getRegistryName().toString());
			}
			
			// select one item randomly
			outputStack = itemStacks.get(RandomHelper.randomInt(0, itemStacks.size()-1));
			Treasure.LOGGER.debug("loot item output stack -> {}", outputStack.getItem().getRegistryName().toString());
		}				
		return Optional.of(outputStack);
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> getLootTables(IRarity rarity) {
		return TreasureLootTableRegistry.getLootTableByRarity(LootTableType.WISHABLES, rarity);
	}
	
	/**
	 * 
	 * @param random
	 * @param itemEntity
	 * @return
	 */
	public ItemStack getDefaultLootKey (Random random, ItemEntity itemEntity) {
		IRarity rarity = WishableRegistry.getRarity(itemEntity.getItem().getItem().getRegistryName()).orElse(Rarity.COMMON);
		return getDefaultLootKey(random, rarity);
	}

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	public ItemStack getDefaultLootKey(Random random, IRarity rarity) {
		List<RegistryObject<KeyItem>> keys = KeyLockRegistry.getKeys(rarity);
		List<KeyItem> keyItems = keys.stream().map(k -> k.get()).toList();
		if (keyItems.isEmpty()) {
			return new ItemStack(TreasureItems.THIEFS_LOCK_PICK.get());
		}
		return new ItemStack(keyItems.get(random.nextInt(keyItems.size())));
	}
	
	/**
	 * 
	 * @param world
	 * @param player
	 * @return
	 */
	public LootContext getLootContext(Level world, Player player, ICoords coords) {
		return new LootContext.Builder((ServerLevel) world)
				.withLuck((player != null) ? player.getLuck() : 0)
				.withOptionalParameter(LootContextParams.THIS_ENTITY, player)
				.withParameter(LootContextParams.ORIGIN, coords.toVec3())
				.create(LootContextParamSets.CHEST);
	}
	
	/**
	 *
	 * @param world
	 * @param random
	 * @param itemStacks
	 * @param category
	 * @param rarity
	 * @param lootContext
	 */
	public void injectLoot(Level world, Random random, List<ItemStack> itemStacks, IRarity rarity, LootContext lootContext) {
		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, rarity);
		
		if (!injectLootTableShells.isEmpty()) {
			Treasure.LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.size());
			itemStacks.addAll(getInjectedLootItems(world, random, injectLootTableShells, lootContext, p -> {
				return !p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL);
			}));
		}
	}
	
	public List<LootTableShell> buildLootTableList(ILootTableType key, IRarity rarity) {
		List<LootTableShell> injectLootTableShells =  TreasureLootTableRegistry.getLootTableByRarity(key, rarity);
		injectLootTableShells = injectLootTableShells
				.stream()
				.filter(s -> s.getResourceLocation().getPath().contains(LootTableType.WISHABLES.getValue()))
				.toList();
		
		return injectLootTableShells;
	}
	
	public List<ItemStack> getInjectedLootItems(Level world, Random random, List<LootTableShell> lootTableShells,
			LootContext lootContext, Predicate<LootPoolShell> predicate) {

		List<ItemStack> itemStacks = new ArrayList<>();		

		for (LootTableShell injectLootTableShell : lootTableShells) {			
			Treasure.LOGGER.debug("injectable resource -> {}", injectLootTableShell.getResourceLocation());

			// get the vanilla managed loot table
			LootTable injectLootTable = world.getServer().getLootTables().get(injectLootTableShell.getResourceLocation());

			if (injectLootTable != null) {
				// filter the pool
				List<LootPoolShell> lootPoolShells = injectLootTableShell.getPools().stream()
						.filter(pool -> predicate.test(pool) )
						.collect(Collectors.toList());

				lootPoolShells.forEach(poolShell -> {
					// get the vanilla managed loot pool
					LootPool lootPool = injectLootTable.getPool(poolShell.getName());					
					if (lootPool != null) {
						// add loot from tables to itemStacks
						lootPool.addRandomItems(itemStacks::add, lootContext);
					}
				});
				Treasure.LOGGER.debug("size of item stacks after inject -> {}", itemStacks.size());
			}
		}
		return itemStacks;
	}
}

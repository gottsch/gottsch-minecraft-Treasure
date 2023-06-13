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
package mod.gottsch.forge.treasure2.core.loot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootPoolShell;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;
import net.minecraft.server.level.ServerLevel;
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
 * TODO use this as the base class for Wishables, Chests, and Loot Modifiers
 * @author Mark Gottschling Jun 12, 2023
 *
 */
public abstract class LootGenerator implements ILootGenerator {

	/**
	 * 
	 * @param world
	 * @param random
	 * @param rarity
	 * @return
	 */
	@Override
	public Pair<List<ItemStack>, List<ItemStack>> generateLoot(Level world, Random random, ILootTableType type, IRarity rarity, Player player, ICoords coords) {
		List<ItemStack> treasureStacks = new ArrayList<>();
		List<ItemStack> itemStacks = new ArrayList<>();

		Optional<LootTableShell> tableShell = getLootTableShell(world, random, type, rarity);
		if (tableShell.isEmpty()) {
			treasureStacks.add(getDefaultLootItem(random, rarity));
			return Pair.of(treasureStacks, itemStacks);	
		}

		// get the vanilla table from shell
		LootTable table = world.getServer().getLootTables().get(tableShell.get().getResourceLocation());
		// get a list of loot pools
		List<LootPoolShell> lootPoolShells = tableShell.get().getPools();

		// generate a context
		LootContext lootContext = getLootContext(world, player, coords);

		// TODO this portion may need its own method (TEMPLATE PATTERN) so chests can separate out into different lists

		for (LootPoolShell pool : lootPoolShells) {
			Treasure.LOGGER.debug("processing pool -> {}", pool.getName());
			// go get the vanilla managed pool
			LootPool lootPool = table.getPool(pool.getName());
			
			/*
			 * geneate loot from pools
			 */
			// separate into two item stack buckets - treasure and other
			if (pool.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL)) {
				Treasure.LOGGER.debug("generating loot from treasure/charm pool -> {}", pool.getName());
				lootPool.addRandomItems(treasureStacks::add, lootContext);
			} else {
				lootPool.addRandomItems(itemStacks::add, lootContext);
			}
		}

		// populate a pair
		Pair<List<ItemStack>, List<ItemStack>> lootStacks = Pair.of(treasureStacks, itemStacks);
		
		// inject injectables into the loot stacks
		injectLoot(world, random, lootStacks, type, rarity, lootContext);

		return lootStacks;
	}

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	@Override
	public ItemStack getDefaultLootItem(Random random, IRarity rarity) {
		List<RegistryObject<KeyItem>> keys = KeyLockRegistry.getKeys(rarity);
		List<KeyItem> keyItems = keys.stream().map(k -> k.get()).toList();
		if (keyItems.isEmpty()) {
			return new ItemStack(TreasureItems.THIEFS_LOCK_PICK.get());
		}
		return new ItemStack(keyItems.get(random.nextInt(keyItems.size())));
	}
	
	/**
	 * 
	 * @param level
	 * @param random
	 * @param rarity
	 * @return
	 */
	public Optional<LootTableShell> getLootTableShell(Level level, Random random, ILootTableType type, IRarity rarity) {
		List<LootTableShell> lootTables = getLootTables(type, rarity);

		//		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
		if (lootTables.isEmpty()) {
			return Optional.empty();
		}

		// select a table shell
		LootTableShell tableShell = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
		if (tableShell.getResourceLocation() == null) {
			return Optional.empty();
		}
		
		return Optional.of(tableShell);
	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> getLootTables(ILootTableType type, IRarity rarity) {
		return TreasureLootTableRegistry.getLootTableByRarity(type, rarity);
	}

	/**
	 * 
	 * @param world
	 * @param player
	 * @param coords
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
	 * @param key
	 * @param rarity
	 * @return
	 */
	public List<LootTableShell> buildLootTableList(ILootTableType key, IRarity rarity, ILootTableType secondaryType) {
		List<LootTableShell> injectLootTableShells =  TreasureLootTableRegistry.getLootTableByRarity(key, rarity);
		if (secondaryType != null) {
			injectLootTableShells = injectLootTableShells
					.stream()
					.filter(s -> s.getResourceLocation().getPath().contains(secondaryType.getValue()))
					.toList();
		}
		return injectLootTableShells;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param itemStacks
	 * @param rarity
	 * @param lootContext
	 */
	@Deprecated
	// can't be called using the implemented generateLoot()
	public void injectLoot(Level level, Random random, List<ItemStack> itemStacks, ILootTableType type, IRarity rarity, LootContext lootContext) {
		// get a list of "inject" loot tables
		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, rarity, type);

		if (!injectLootTableShells.isEmpty()) {
			Treasure.LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.size());
			itemStacks.addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> true));
		}
	}
	
	/**
	 * Injects loot into separate lists based on the pool - treasure pool and other
	 * @param level
	 * @param random
	 * @param lootStacks
	 * @param type
	 * @param rarity
	 * @param lootContext
	 */
	public void injectLoot(Level level, Random random, Pair<List<ItemStack>, List<ItemStack>> lootStacks,
			ILootTableType type, IRarity rarity, LootContext lootContext) {
		
		// get a list of "inject" loot tables
		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, rarity, type);

		// TODO this is only removing Treasure Pools instead of separating the pools
		if (!injectLootTableShells.isEmpty()) {
			// add predicate
			lootStacks.getLeft().addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
				return p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL);
			}));
			lootStacks.getRight().addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
				return !p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL) ;
			}));
		}
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param lootTableShells
	 * @param lootContext
	 * @param predicate
	 * @return
	 */
	public List<ItemStack> getInjectedLootItems(Level world, Random random, List<LootTableShell> lootTableShells,
			LootContext lootContext, Predicate<LootPoolShell> predicate) {

		List<ItemStack> itemStacks = new ArrayList<>();		

		for (LootTableShell injectLootTableShell : lootTableShells) {			
			Treasure.LOGGER.debug("injectable resource -> {}", injectLootTableShell.getResourceLocation());

			// get the vanilla managed loot table
			LootTable injectLootTable = world.getServer().getLootTables().get(injectLootTableShell.getResourceLocation());

			if (injectLootTable != null) {
				// TODO why do i want this filter!! can't inject into treasure or charms pool??!!
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

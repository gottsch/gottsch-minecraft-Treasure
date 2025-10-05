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
package mod.gottsch.forge.treasure2.core.loot;

import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.generator.chest.ChestGenerationHelper;
import mod.gottsch.forge.treasure2.core.item.KeyItem;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.rarity.IRarity;
import mod.gottsch.forge.treasure2.core.registry.LootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.RarityTagAssociationRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * similar but not the same as ChestGenerationHelper.
 * this class generates a pair of lists of treasure loot table
 * items and returns them to the caller.
 * @author Mark Gottschling Jun 12, 2023
 *
 */
public abstract class LootGenerator implements ILootGenerator {

	/**
	 * 
	 * @param level
	 * @param random
	 * @param rarity
	 * @return
	 */
	@Override
	public Pair<List<ItemStack>, List<ItemStack>> generateLoot(Level level, RandomSource random, ILootTableTypes lootTableType, IRarity rarity, Player player, ICoords coords) {
		List<ItemStack> treasureStacks = new ArrayList<>();
		List<ItemStack> itemStacks = new ArrayList<>();

//		Optional<LootTableShell> tableShell = getLootTableShell(world, random, type, rarity);
//		if (tableShell.isEmpty()) {
//			treasureStacks.add(getDefaultLootItem(random, rarity));
//			return Pair.of(treasureStacks, itemStacks);
//		}

		// 1. select a loot table by the given rarity
		Optional<ResourceLocation> lootTableIdOpt = randomLootTable(random, lootTableType, rarity);
		if (lootTableIdOpt.isEmpty()) {
			Treasure.LOGGER.warn("could not determine a loot table for rarity -> {}", rarity);
			treasureStacks.add(getDefaultLootItem(random, rarity));
			return Pair.of(treasureStacks, itemStacks);
		}
		ResourceLocation lootTableId = lootTableIdOpt.get();

		// 2. get the vanilla table from shell
		LootTable lootTable = LootTableRegistry.getLootTable((ServerLevel) level, lootTableId);
		if (lootTable == LootTable.EMPTY) {
			Treasure.LOGGER.warn("unable to load lootTable -> {}", lootTableId);
			return Pair.of(treasureStacks, itemStacks);
		}
		Treasure.LOGGER.debug("selected loot table -> {} for rarity -> {}", lootTableId, rarity);

		// 3. prepare for loot generation
		LootContext lootContext = ChestGenerationHelper.createLootContext((ServerLevel)level, coords.toPos(), player, lootTableId);

		// 4. generate loot from the primary table
		ChestGenerationHelper.generateLootFromPools(lootTable, lootContext, treasureStacks, itemStacks);

		// 5. inject loot from other registered tables
		ChestGenerationHelper.addInjectedLoot((ServerLevel)level, rarity, lootContext, treasureStacks, itemStacks);

		// get a list of loot pools
//		List<LootPoolShell> lootPoolShells = tableShell.get().getPools();

		// generate a context
//		LootContext lootContext = getLootContext(world, player, coords);

		// TODO this portion may need its own method (TEMPLATE PATTERN) so chests can separate out into different lists

//		for (LootPoolShell pool : lootPoolShells) {
//			Treasure.LOGGER.debug("processing pool -> {}", pool.getName());
//			// go get the vanilla managed pool
//			LootPool lootPool = table.getPool(pool.getName());
//
//			/*
//			 * geneate loot from pools
//			 */
//			// separate into two item stack buckets - treasure and other
//			if (pool.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL)) {
//				Treasure.LOGGER.debug("generating loot from treasure/charm pool -> {}", pool.getName());
//				lootPool.addRandomItems(treasureStacks::add, lootContext);
//			} else {
//				lootPool.addRandomItems(itemStacks::add, lootContext);
//			}
//		}

		// 6. populate a pair
//		Pair<List<ItemStack>, List<ItemStack>> lootStacks = Pair.of(treasureStacks, itemStacks);
		
		// inject injectables into the loot stacks
//		injectLoot(level, random, lootStacks, type, rarity, lootContext);

		return Pair.of(treasureStacks, itemStacks);
	}

	/**
	 * 
	 * @param random
	 * @param rarityEntry
	 * @return
	 */
	@Override
	public ItemStack getDefaultLootItem(RandomSource random, IRarity rarityEntry) {

		List<KeyItem> keyItems = RarityTagAssociationRegistry.getKeyItems(rarityEntry)
				.stream().map(k -> (KeyItem)k).toList();

		if (keyItems.isEmpty()) {
			return new ItemStack(TreasureItems.THIEFS_LOCK_PICK.get());
		}
		return new ItemStack(keyItems.get(random.nextInt(keyItems.size())));
	}

	/**
	 * selects a random loot table resource location from the registry based on the given
	 * loot table type and rarity.
	 *
	 * @param random the random source.
	 * @param rarity the rarity entry to filter loot tables by.
	 * @return an Optional containing the ResourceLocation, or empty if none are found.
	 */
	public static Optional<ResourceLocation> randomLootTable(RandomSource random, ILootTableTypes lootTableType, IRarity rarity) {
		List<ResourceLocation> lootTables = LootTableRegistry.getLootTableIds(lootTableType, rarity);
		if (lootTables.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(lootTables.get(random.nextInt(lootTables.size())));
	}

//	/**
//	 *
//	 * @param level
//	 * @param random
//	 * @param rarity
//	 * @return
//	 */
//	@Deprecated
//	public Optional<LootTableShell> getLootTableShell(Level level, RandomSource random, ILootTableType type, IRarity rarity) {
//		List<LootTableShell> lootTables = getLootTables(type, rarity);
//
//		//		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
//		if (lootTables.isEmpty()) {
//			return Optional.empty();
//		}
//
//		// select a table shell
//		LootTableShell tableShell = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
//		if (tableShell.getResourceLocation() == null) {
//			return Optional.empty();
//		}
//
//		return Optional.of(tableShell);
//	}
	
	/**
	 * 
	 * @param rarity
	 * @return
	 */
//	@Deprecated
//	public List<LootTableShell> getLootTables(ILootTableType type, IRarity rarity) {
//		return TreasureLootTableRegistry.getLootTableByRarity(type, rarity);
//	}

	/**
	 * 
	 * @param world
	 * @param player
	 * @param coords
	 * @return
	 */
//	@Deprecated
//	public LootContext getLootContext(Level world, Player player, ICoords coords) {
//		LootParams.Builder lootParamsBuilder = (new LootParams.Builder((ServerLevel)world))
//				.withParameter(LootContextParams.ORIGIN, coords.toVec3());
//
//        if (player != null) {
//           lootParamsBuilder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
//        }
//        LootParams params = lootParamsBuilder.create(LootContextParamSets.CHEST);
//        LootContext lootContext = new LootContext.Builder(params).create(null);
//        return lootContext;
//	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
//	@Deprecated
//	public List<LootTableShell> buildLootTableList(ILootTableType key, IRarity rarity, ILootTableType secondaryType) {
//		List<LootTableShell> injectLootTableShells =  TreasureLootTableRegistry.getLootTableByRarity(key, rarity);
//		if (secondaryType != null) {
//			injectLootTableShells = injectLootTableShells
//					.stream()
//					.filter(s -> s.getResourceLocation().getPath().contains(secondaryType.getValue()))
//					.toList();
//		}
//		return injectLootTableShells;
//	}
	
	/**
	 * 
	 * @param level
	 * @param random
	 * @param itemStacks
	 * @param rarity
	 * @param lootContext
	 */
//	@Deprecated
//	// can't be called using the implemented generateLoot()
//	public void injectLoot(Level level, RandomSource random, List<ItemStack> itemStacks, ILootTableType type, IRarity rarity, LootContext lootContext) {
//		// get a list of "inject" loot tables
//		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, rarity, type);
//
//		if (!injectLootTableShells.isEmpty()) {
//			Treasure.LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.size());
//			itemStacks.addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> true));
//		}
//	}
	
	/**
	 * Injects loot into separate lists based on the pool - treasure pool and other
	 * @param level
	 * @param random
	 * @param lootStacks
	 * @param type
	 * @param rarity
	 * @param lootContext
	 */
//	@Deprecated
//	public void injectLoot(Level level, RandomSource random, Pair<List<ItemStack>, List<ItemStack>> lootStacks,
//			ILootTableType type, IRarity rarity, LootContext lootContext) {
//
//		// get a list of "inject" loot tables
//		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, rarity, type);
//
//		// TODO this is only removing Treasure Pools instead of separating the pools
//		if (!injectLootTableShells.isEmpty()) {
//			// add predicate
//			lootStacks.getLeft().addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
//				return p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL);
//			}));
//			lootStacks.getRight().addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
//				return !p.getName().equalsIgnoreCase(TreasureLootGenerators.TREASURE_POOL) ;
//			}));
//		}
//	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param lootTableShells
	 * @param lootContext
	 * @param predicate
	 * @return
	 */
//	@Deprecated
//	public List<ItemStack> getInjectedLootItems(Level world, RandomSource random, List<LootTableShell> lootTableShells,
//			LootContext lootContext, Predicate<LootPoolShell> predicate) {
//
//		List<ItemStack> itemStacks = new ArrayList<>();
//
//		for (LootTableShell injectLootTableShell : lootTableShells) {
//			Treasure.LOGGER.debug("injectable resource -> {}", injectLootTableShell.getResourceLocation());
//
//			// get the vanilla managed loot table
//			LootTable injectLootTable = world.getServer().getLootData().getLootTable(injectLootTableShell.getResourceLocation());
//
//			if (injectLootTable != null) {
//				// TODO why do i want this filter!! can't inject into treasure or charms pool??!!
//				// filter the pool
//				List<LootPoolShell> lootPoolShells = injectLootTableShell.getPools().stream()
//						.filter(pool -> predicate.test(pool) )
//						.collect(Collectors.toList());
//
//				lootPoolShells.forEach(poolShell -> {
//					// get the vanilla managed loot pool
//					LootPool lootPool = injectLootTable.getPool(poolShell.getName());
//					if (lootPool != null) {
//						// add loot from tables to itemStacks
//						lootPool.addRandomItems(itemStacks::add, lootContext);
//					}
//				});
//				Treasure.LOGGER.debug("size of item stacks after inject -> {}", itemStacks.size());
//			}
//		}
//		return itemStacks;
//	}
}

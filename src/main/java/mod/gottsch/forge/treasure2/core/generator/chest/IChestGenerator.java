/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootPoolShell;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity.GenerationContext;
import mod.gottsch.forge.treasure2.core.block.entity.ITreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.config.ChestConfiguration;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.GeneratorUtil;
import mod.gottsch.forge.treasure2.core.generator.effects.IChestGeneratorEffects;
import mod.gottsch.forge.treasure2.core.generator.marker.GravestoneMarkerGenerator;
import mod.gottsch.forge.treasure2.core.generator.marker.StructureMarkerGenerator;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import mod.gottsch.forge.treasure2.core.lock.LockLayout;
import mod.gottsch.forge.treasure2.core.lock.LockState;
import mod.gottsch.forge.treasure2.core.registry.*;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext;
import mod.gottsch.forge.treasure2.core.registry.support.GeneratedChestContext.GeneratedType;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.RegistryObject;


/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator extends IChestGeneratorEffects {
	public static final String TREASURE_POOL = "treasure";
	public static final String CHARMS_POOL = "charms";
	
//	public IChestGeneratorType getChestGeneratorType();
	
	/**
	 * TODO should pass RandomSource and not use a new Random
	 * @param context
	 * @param coords
	 * @param rarity
	 * @param state
	 * @return
	 */
	default public GeneratorResult<ChestGeneratorData> generate(IFeatureGenContext context, ICoords coords,
			final IRarity rarity, BlockState state) {

		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setCoords(coords);
		result.getData().setSpawnCoords(coords);

		// select a loot table
		Optional<LootTableShell> lootTableShell = selectLootTable(context.random(), rarity);
		ResourceLocation lootTableResourceLocation = null;
		if (lootTableShell.isPresent()) {
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			Treasure.LOGGER.debug("unable to select a LootTable for rarity -> {}", rarity);
			return result.fail();
		}

		// select a chest from the rarity
		AbstractTreasureChestBlock chest = selectChest(context.random(), rarity);
		if (chest == null) {
			Treasure.LOGGER.warn("unable to select a chest for rarity -> {}.", rarity);
			return result.fail();
		}
		result.getData().setRegistryName(chest.getRegistryName());

		// update the state
		if (state == null) {
			state = chest.defaultBlockState();
		}

		// TODO need to get the FACING property from somewhere - should be covered in the placeInWorld - it inspects the replaced block

		
		// place the chest in the world
		BlockEntity blockEntity = null;
		if (state != null) {
			blockEntity = placeInWorld(context, coords, chest, state, false);
		}

		if (blockEntity == null) {
			Treasure.LOGGER.debug("Unable to locate block entity for chest -> {}", coords);
			return result.fail();
		}
		
		ITreasureChestBlockEntity chestEntity = (ITreasureChestBlockEntity)blockEntity;
		
		// add the loot table
		addLootTable(chestEntity, lootTableResourceLocation);

		// seal the chest
		addSeal(chestEntity);

		// TODO remove and update from Feature when all is complete.
		// update the backing block entity's generation contxt
		// NOTE only updates generation context with Rarity and ChestGeneratorType. The featureType at this point is unknown.
		addGenerationContext(context, chestEntity, rarity);

		// add locks
		addLocks(context.random(), chest, chestEntity, rarity);

		// add mimic if any
		addMimic(context, chest, chestEntity, rarity);
		
		// add effects
		addGenEffects(context.level(), state, coords.toPos(), rarity);
		
		// update result
		result.getData().setSpawnCoords(coords);
		result.getData().setState(state);

		return result.success();
	}

	@Override
	default void addGenEffects(ServerLevelAccessor level, BlockState state, BlockPos pos,
			IRarity rarity) {
		if (level.getBlockState(pos).getBlock() == TreasureBlocks.SPIDER_CHEST.get()) {
			if (level.getBlockState(pos.above()).isAir()) {
				level.setBlock(pos.above(), Blocks.COBWEB.defaultBlockState(), 3);
			}
			if (level.getBlockState(pos.north()).isAir()) {
				level.setBlock(pos.north(), Blocks.COBWEB.defaultBlockState(), 3);
			}
			if (level.getBlockState(pos.south()).isAir()) {
				level.setBlock(pos.south(), Blocks.COBWEB.defaultBlockState(), 3);
			}
			if (level.getBlockState(pos.east()).isAir()) {
				level.setBlock(pos.east(), Blocks.COBWEB.defaultBlockState(), 3);
			}
			if (level.getBlockState(pos.west()).isAir()) {
				level.setBlock(pos.west(), Blocks.COBWEB.defaultBlockState(), 3);
			}
		}
		else {
			IChestGeneratorEffects.super.addGenEffects(level, state, pos, rarity);
		}
	}
	
	/**
	 * 
	 * @param context
	 * @param chest
	 * @param blockEntity
	 * @param rarity
	 */
	default public void addMimic(IFeatureGenContext context, AbstractTreasureChestBlock chest, ITreasureChestBlockEntity blockEntity,
			IRarity rarity) {
		// check against config if mimic should be used
		if (Config.SERVER.mobs.enableMimics.get() && RandomHelper.checkProbability(context.random(), Config.SERVER.mobs.mimicProbability.get())) {
			Optional<ResourceLocation> mimicName = MimicRegistry.getMimic(chest.getRegistryName());
			if (mimicName.isPresent()) {
				// set the mimic name in the block entity
				blockEntity.setMimic(mimicName.get());
			}
		}
	}
	

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	default public Optional<LootTableShell> selectLootTable(Random random, final IRarity rarity) {
		LootTableShell lootTableShell = null;

		// select the loot table by rarity
		List<LootTableShell> tables = buildLootTableList(LootTableType.CHESTS, rarity);
		if (tables !=null) { 
			Treasure.LOGGER.debug("tables size -> {}", tables.size());
		}

		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			Treasure.LOGGER.debug("Selected loot table shell index --> {}, shell -> {}", index, lootTableShell.getCategories());
		}
		return Optional.ofNullable(lootTableShell);
	}

	/**
	 * 
	 * @param factory
	 * @param rarity
	 * @return
	 */
	default public Optional<LootTableShell> selectLootTable(Supplier<Random> factory, IRarity rarity) {
		LootTableShell lootTableShell = null;

		// select the loot table by rarity
		List<LootTableShell> tables = buildLootTableList(LootTableType.CHESTS, rarity);
		if (tables !=null) {
			Treasure.LOGGER.debug("tables size -> {}", tables.size());
		}

		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			if (tables.size() == 1) {
				lootTableShell = tables.get(0);
			} else {
				index = RandomHelper.randomInt(factory.get(), 0, tables.size() - 1);
				lootTableShell = tables.get(index);
			}
			Treasure.LOGGER.debug("Selected loot table shell index --> {}", index);
		}
		return Optional.ofNullable(lootTableShell);	
	}
	
	/**
	 * 
	 * @param key
	 * @param rarity
	 * @return
	 */
	default public List<LootTableShell> buildLootTableList(ILootTableType key, IRarity rarity) {
		return TreasureLootTableRegistry.getLootTableByRarity(LootTableType.CHESTS, rarity);
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	// TODO how to prevent special chests from ending up in the rarity tag lists?
	// TODO move to the ChestRegistry ?
	// why did I deprecate this?? - should use ChestRegistry
	// 5/1/2023 correct, because Skull chest is a scarce chest which is fine. it's chestGenerator provides the correct loot table
	default public AbstractTreasureChestBlock selectChest(final Random random, final IRarity rarity) {
		Treasure.LOGGER.debug("attempting to get chest list for rarity -> {}", rarity);
		List<RegistryObject<Block>> chestList = (List<RegistryObject<Block>>) ChestRegistry.getChest(rarity);
		Treasure.LOGGER.debug("size of chests lists -> {}", chestList.size());
		RegistryObject<Block> chest = null;
		if (!chestList.isEmpty()) {
			chest = chestList.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));
		}
		return chest == null ? null : (AbstractTreasureChestBlock) chest.get();
	}

	/**
	 * 
	 * @param level
	 * @param random
	 * @param blockEntity
	 * @param lootRarity
	 */
	default public void fillChest(final Level level, Random random, final BlockEntity blockEntity, final IRarity rarity, Player player) {
		Optional<LootTableShell> lootTableShell = null;
		ResourceLocation lootTableResourceLocation = ((ITreasureChestBlockEntity)blockEntity).getLootTable();
		Treasure.LOGGER.debug("chest has loot table property of -> {}", lootTableResourceLocation);

		if (!(blockEntity instanceof AbstractTreasureChestBlockEntity)) {
			return;
		}
		AbstractTreasureChestBlockEntity chestBlockEntity = (AbstractTreasureChestBlockEntity)blockEntity;

		// if a chest didn't have its loot table set then pick one randomly
		if (lootTableResourceLocation == null) {
			lootTableShell = selectLootTable(random, rarity);
		}
		else {
			lootTableShell = TreasureLootTableRegistry.getLootTableByResourceLocation(LootTableType.CHESTS, lootTableResourceLocation);
		}
		
		// is valid loot table shell
		if (lootTableShell.isPresent()) {
			Treasure.LOGGER.debug("using loot table shell -> {}, {}", lootTableShell.get().getCategory(), lootTableShell.get().getRarity());
			lootTableResourceLocation = lootTableShell.get().getResourceLocation();
		}
		else {
			Treasure.LOGGER.debug("Unable to select a LootTable for rarity -> {}", rarity);
			return;
		}
		Treasure.LOGGER.debug("loot table resource -> {}", lootTableResourceLocation); 

		LootTable lootTable = level.getServer().getLootTables().get(lootTableResourceLocation);
		if (lootTable == null) {
			Treasure.LOGGER.warn("Unable to select a lootTable.");
			return;
		}		
		Treasure.LOGGER.debug("selected loot table -> {} from resource -> {}", lootTable, lootTableResourceLocation);

		// TODO remove effectiveRarity
		// update rarity from lootTableShell		
//		IRarity effectiveRarity = TreasureLootTableRegistry.getEffectiveRarity(lootTableShell.get(), rarity);		
//		Treasure.LOGGER.debug("generating loot from loot table for effective rarity {}", effectiveRarity);
		IRarity effectiveRarity = rarity;
		
		// setup lists of items
		List<ItemStack> treasureStacks = new ArrayList<>();
		List<ItemStack> itemStacks = new ArrayList<>();

		/*
		 * Using per loot table file - category strategy (instead of per pool strategy)
		 */
		// get a list of loot pools
		List<LootPoolShell> lootPoolShells = lootTableShell.get().getPools();
		if (lootPoolShells != null && lootPoolShells.size() > 0) {
			Treasure.LOGGER.debug("# of pools -> {}", lootPoolShells.size());
		}

		// setup context
		LootContext lootContext = null;
		lootContext = new LootContext.Builder((ServerLevel) level)
				.withLuck(player.getLuck())
				.withParameter(LootContextParams.THIS_ENTITY, player)
				.withParameter(LootContextParams.ORIGIN, 
						new Vec3(blockEntity.getBlockPos().getX(), 
								blockEntity.getBlockPos().getY(), 
								blockEntity.getBlockPos().getZ()))
				.create(LootContextParamSets.CHEST);


		//		Treasure.LOGGER.debug("loot context -> {}", lootContext);

		for (LootPoolShell pool : lootPoolShells) {
			Treasure.LOGGER.debug("processing pool (from poolShell) -> {}", pool.getName());
			// go get the vanilla managed pool
			LootPool lootPool = lootTable.getPool(pool.getName());

			if (lootPool != null) {
				// geneate loot from pools
				if (pool.getName().equalsIgnoreCase(TREASURE_POOL) ||
						pool.getName().equalsIgnoreCase(CHARMS_POOL)) {
					Treasure.LOGGER.debug("generating loot from treasure/charm pool -> {}", pool.getName());
					lootPool.addRandomItems(treasureStacks::add, lootContext);
				}
				else {
					Treasure.LOGGER.debug("generating loot from loot pool -> {}", pool.getName());
					lootPool.addRandomItems(itemStacks::add, lootContext);
				}
			}
		}
		Treasure.LOGGER.debug("size of treasure stacks -> {}", treasureStacks.size());
		Treasure.LOGGER.debug("size of item stacks -> {}", itemStacks.size());

		// record original item size (max number of items to pull from final list)
		int treasureLootItemSize = treasureStacks.size();
		int lootItemSize = itemStacks.size();

		// TODO move to separate method
		// fetch all injected loot tables by rarity
		// NOTE removed the category. trying to keep it as straight forward as possible
		Treasure.LOGGER.debug("searching for injectable tables for category ->{}, rarity -> {}", LootTableType.INJECTS, effectiveRarity);
		List<LootTableShell> injectLootTableShells = buildLootTableList(LootTableType.INJECTS, effectiveRarity);
		// NOTE injects are special case because they have 2 top-levels ex inject/chests, inject/wishables, so the list has to be filtered
		injectLootTableShells = injectLootTableShells
				.stream()
				.filter(s -> s.getResourceLocation().getPath().contains(LootTableType.CHESTS.getValue()))
				.toList();
		
		if (!injectLootTableShells.isEmpty()) {
			Treasure.LOGGER.debug("found injectable tables for category ->{}, rarity -> {}", lootTableShell.get().getCategory(), effectiveRarity);
			Treasure.LOGGER.debug("size of injectable tables -> {}", injectLootTableShells.size());

			// add predicate
			treasureStacks.addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
				return p.getName().equalsIgnoreCase(TREASURE_POOL) || p.getName().equalsIgnoreCase(CHARMS_POOL);
			}));
			itemStacks.addAll(getInjectedLootItems(level, random, injectLootTableShells, lootContext, p -> {
				return !p.getName().equalsIgnoreCase(TREASURE_POOL) && !p.getName().equalsIgnoreCase(CHARMS_POOL);
			}));
			//			itemStacks.addAll(TreasureLootTableRegistry.getLootTableMaster().getInjectedLootItems(world, random, injectLootTableShells.get(), lootContext));
		}

		// check the inventory
		IItemHandler itemHandler = chestBlockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(null);
		if (itemHandler != null) {
			ItemStackHandler inventory = (ItemStackHandler)itemHandler;

			// add the treasure items to the chest
			Collections.shuffle(treasureStacks, random);
			fillInventory(inventory, random, treasureStacks.stream().limit(treasureLootItemSize).collect(Collectors.toList()));

			// add a treasure map if there is still space
			addTreasureMap(level, random, inventory, new Coords(blockEntity.getBlockPos()), rarity);

			// shuffle the items list
			Collections.shuffle(itemStacks, random);		
			// fill the chest with items
			fillInventory(inventory, random, itemStacks.stream().limit(lootItemSize).collect(Collectors.toList()));
		}
	}

	//////////////////
	// TODO add predicate to signature and use it instead of "treasure" filter
	default public List<ItemStack> getInjectedLootItems(Level world, Random random, List<LootTableShell> lootTableShells,
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
	/////////////////

	/**
	 * 
	 * @param world
	 * @param random
	 * @param inventory
	 * @param chestCoords
	 * @param rarity
	 */
	default public void addTreasureMap(Level world, Random random, ItemStackHandler inventory, ICoords chestCoords, IRarity rarity) {
		ResourceLocation dimension = WorldInfo.getDimension(world);
		ChestConfiguration config = Config.chestConfigMap.get(dimension);
		//check for open slots first
		List<Integer> emptySlots = getEmptySlotsRandomized(inventory, random);
		if (!emptySlots.isEmpty() && config != null && RandomHelper.checkProbability(random, config.getTreasureMapProbability())) { 
			// determine what level of rarity map to generate
			IRarity mapRarity = getBoostedRarity(rarity, getRarityBoostAmount());
			Treasure.LOGGER.debug("get rarity chests for dimension -> {}", dimension.toString());

//			GeneratedCache<GeneratedChestContext> generatedRegistry = DimensionalGeneratedCache.getChestGeneratedCache(dimension, FeatureType.TERRANEAN);
			List<GeneratedCache<GeneratedChestContext>> caches = DimensionalGeneratedCache.getChestGeneratedCaches(dimension);
//			Optional<List<GeneratedChestContext>> generatedChestContexts = Optional.empty();
			List<GeneratedChestContext> chestContexts = new ArrayList<>();
			if (caches != null && !caches.isEmpty()) {
				caches.forEach(cache -> {
					Optional<List<GeneratedChestContext>> generatedChestContexts = cache.getByIRarity(mapRarity);
					if (generatedChestContexts.isPresent()) {
						chestContexts.addAll(generatedChestContexts.get());
					}
				});
			}
//			if (generatedRegistry != null) {
//				generatedChestContexts = generatedRegistry.getByIRarity(mapRarity);
//			}

//			if (generatedChestContexts.isPresent()) {
			if (!chestContexts.isEmpty()) {
				Treasure.LOGGER.debug("got chestContexts by rarity -> {}", mapRarity);
				List<GeneratedChestContext> validChestContexts = chestContexts.stream()
						.filter(c -> c.getGeneratedType() == GeneratedType.CHEST && !c.isDiscovered() && !c.isCharted()).toList();
				
				if (!validChestContexts.isEmpty()) {
					Treasure.LOGGER.debug("got valid chestInfos; size -> {}", validChestContexts.size());
					GeneratedChestContext chestContext = validChestContexts.get(random.nextInt(validChestContexts.size()));
					Treasure.LOGGER.debug("using chestInfo -> {}", chestContext);
					// build a map
					ItemStack mapStack = createMap(world, chestContext.getCoords(), mapRarity, (byte)2);

					// add map to chest
					inventory.setStackInSlot(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), mapStack);
					// update the chest info in the registry that the map is referring to with this chest's coords
					chestContext.setChartedFrom(chestCoords);

					// update the current chest gen context
					GeneratedCache<GeneratedChestContext> generatedRegistry = DimensionalGeneratedCache.getChestGeneratedCache(dimension, chestContext.getFeatureType());
					Optional<GeneratedChestContext> currentChestContext = generatedRegistry.get(rarity, chestCoords.toShortString());
					if (currentChestContext.isPresent()) {
						currentChestContext.get().setDiscovered(true);
					}
				}
			}			
		}	
	}

	/**
	 * 
	 * @param world
	 * @param coords
	 * @param rarity
	 * @param zoom
	 * @return
	 */
	default public ItemStack createMap(Level world, ICoords coords, IRarity rarity, byte zoom) {
		ItemStack itemStack = MapItem.create(world, coords.getX(), coords.getZ(), zoom, true, true);
		MapItem.renderBiomePreviewMap((ServerLevel) world, itemStack);
		MapItemSavedData.addTargetDecoration(itemStack, coords.toPos(), "+", MapDecoration.Type.RED_X);
		itemStack.setHoverName(new TranslatableComponent(LangUtil.screen("treasure_map." + rarity.getValue())));
		return itemStack;
	}

	/**
	 * 
	 * @param rarity
	 * @param amount
	 * @return
	 */
	default public IRarity getBoostedRarity(IRarity rarity, int amount) {
		// TODO needs to change
		// TODO get all rarities from registry and sort by code
		// get the highest and the closest to the boosted amount
		return Rarity.getByCode(Math.min(rarity.getCode() + amount, Rarity.EPIC.getCode()));
	}

	/**
	 * 
	 * @return
	 */
	default public int getRarityBoostAmount() {
		int rarityBoost = 1;
		double mapProbability = RandomHelper.randomDouble(0, 100);
		if (mapProbability < 5.0) {
			rarityBoost = 3;
		}
		else if (mapProbability < 15.0) {
			rarityBoost = 2;
		}
		else if (mapProbability < 25.0) {
			rarityBoost = 1;
		}
		return rarityBoost;
	}

	/**
	 * 
	 * @param inventory
	 * @param random
	 * @param context
	 */
	default public void fillInventory(ItemStackHandler inventory, Random random, List<ItemStack> list) {
		List<Integer> emptySlots = getEmptySlotsRandomized(inventory, random);
		Treasure.LOGGER.debug("empty slots size -> {}", emptySlots.size());
		this.shuffleItems(list, emptySlots.size(), random);

		for (ItemStack itemstack : list) {
			// if no more empty slots are available
			if (emptySlots.isEmpty()) {
				return;
			}

			if (itemstack.isEmpty()) {
				inventory.setStackInSlot(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), ItemStack.EMPTY);
			} 
			else {
				inventory.setStackInSlot(((Integer) emptySlots.remove(emptySlots.size() - 1)).intValue(), itemstack);
			}
		}
	}

	/**
	 * 
	 * @param blockEntity
	 * @param rarity
	 */
	default public void addGenerationContext(IFeatureGenContext context, ITreasureChestBlockEntity blockEntity, IRarity rarity) {
		GenerationContext generationContext = 
				((AbstractTreasureChestBlockEntity)blockEntity).new GenerationContext(rarity, context.getFeatureType());
		blockEntity.setGenerationContext(generationContext);
	}

	/**
	 * 
	 * @param blockEntity
	 * @param location
	 */
	default public void addLootTable(ITreasureChestBlockEntity blockEntity, ResourceLocation location) {
		blockEntity.setLootTable(location);
	}

	/**
	 * 
	 * @param blockEntity
	 */
	default public void addSeal(ITreasureChestBlockEntity blockEntity) {
		blockEntity.setSealed(true);
	}

	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * 
	 * @param chest
	 */
	default public void addLocks(Random random, AbstractTreasureChestBlock chest, 
			ITreasureChestBlockEntity blockEntity, IRarity rarity) {

		Treasure.LOGGER.debug("finding locks for rarity -> {}", rarity);
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(KeyLockRegistry.getLocks(rarity).stream().map(lock -> lock.get()).collect(Collectors.toList()));
		Treasure.LOGGER.debug("locks for rarity -> {}", locks);
		addLocks(random, chest, blockEntity, locks);
		locks.clear();
	}

	/**
	 * 
	 * @param random
	 * @param chest
	 * @param blockEntity
	 * @param locks
	 */
	default public void addLocks(Random random, AbstractTreasureChestBlock chest, 
			ITreasureChestBlockEntity blockEntity, List<LockItem> locks) {
		Treasure.LOGGER.debug("locks to select from -> {}", locks);
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getLockLayout());

		// get the lock states
		List<LockState> lockStates = blockEntity.getLockStates();

		// TODO add error handling if locks.size == 0
		for (int i = 0; i < numLocks; i++) {
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size() - 1));
			Treasure.LOGGER.debug("adding lock: {}", lock);
			// add the lock to the chest
			lockStates.get(i).setLock(lock);
		}
	}

	/**
	 * 
	 * @param random
	 * @param lockLayout
	 * @return
	 */
	default public int randomizedNumberOfLocksByChestType(Random random, LockLayout lockLayout) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 0, lockLayout.getMaxLocks());
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		return numLocks;
	}


	/**
	 * 
	 * @param inventory
	 * @param rand
	 * @return
	 */
	default public List<Integer> getEmptySlotsRandomized(ItemStackHandler inventory, Random rand) {
		List<Integer> list = Lists.<Integer>newArrayList();

		for (int i = 0; i < inventory.getSlots(); ++i) {
			if (inventory.getStackInSlot(i).isEmpty()) {
				list.add(Integer.valueOf(i));
			}
		}

		Collections.shuffle(list, rand);
		return list;
	}

	/**
	 * shuffles items by changing their order (no stack splitting)
	 */
	default public void shuffleItems(List<ItemStack> stacks, int emptySlotsSize, Random rand) {
		Collections.shuffle(stacks, rand);
	}

	/**
	 * TODO refactor out into it's own selectable generators
	 * Wrapper method so that is can be overridden (as used in the Template Pattern)
	 * 
	 * @param world
	 * @param random
	 * @param coods
	 */
	@Deprecated
	default public void addMarkers(IWorldGenContext context, ICoords coords, final boolean isSurfaceChest) {
		if (!isSurfaceChest && Config.SERVER.markers.enableMarkerStructures.get() 
				&& RandomHelper.checkProbability(context.random(), Config.SERVER.markers.structureProbability.get())) {
			Treasure.LOGGER.debug("generating a random structure marker -> {}", coords.toShortString());
						new StructureMarkerGenerator().generate(context, coords);
		} else {
			new GravestoneMarkerGenerator().generate(context, coords);
		}
	}

	/**
	 * 
	 * @param level
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	@Deprecated
	default public BlockEntity placeInWorld(IWorldGenContext context, AbstractTreasureChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GeneratorUtil.replaceBlockWithChest(context, chest, chestCoords, false);

		// get the backing block entity of the chest
		BlockEntity blockEntity = (BlockEntity) context.level().getBlockEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(context.level().getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractTreasureChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			// if a block entity exists, then this is on a server level
			if (blockEntity != null && (blockEntity instanceof AbstractTreasureChestBlockEntity)) {
				((ServerLevel)context.level()).removeBlockEntity(chestCoords.toPos());
			}
			return null;
		}

		// if block entity failed to create, remove the chest
		if (blockEntity == null || !(blockEntity instanceof AbstractTreasureChestBlockEntity)) {
			// remove chest
			context.level().setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create BlockEntityChest, removing BlockChest");
			return null;
		}
		return blockEntity;
	}

	/**
	 * 
	 * @param level
	 * @param random
	 * @param chestCoords
	 * @param chest
	 * @param state
	 * @return
	 */
	default public BlockEntity placeInWorld(IWorldGenContext context, ICoords chestCoords,
			AbstractTreasureChestBlock chest, BlockState state, boolean discovered) {
		
		// replace block @ coords
		boolean isPlaced = GeneratorUtil.replaceBlockWithChest(context, chestCoords, chest, state, discovered);
		Treasure.LOGGER.debug("isPlaced -> {}", isPlaced);
		// get the backing block entity of the chest
		BlockEntity blockEntity = (BlockEntity) context.level().getBlockEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(context.level().getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractTreasureChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)
			if (blockEntity != null && (blockEntity instanceof AbstractTreasureChestBlockEntity)) {
				((ServerLevel)context.level()).removeBlockEntity(chestCoords.toPos());
			}
			return null;
		}

		// if block entity failed to create, remove the chest
		if (blockEntity == null || !(blockEntity instanceof AbstractTreasureChestBlockEntity)) {
			// remove chest
			context.level().setBlock(chestCoords.toPos(), Blocks.AIR.defaultBlockState(), 3);
			Treasure.LOGGER.debug("Unable to create BlockEntityChest, removing BlockChest");
			return null;
		}
		return blockEntity;
	}
}
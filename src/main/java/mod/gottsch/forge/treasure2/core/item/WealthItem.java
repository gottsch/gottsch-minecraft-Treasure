/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.loot.LootPoolShell;
import mod.gottsch.forge.gottschcore.loot.LootTableShell;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.IWishingWellBlock;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.enums.ILootTableType;
import mod.gottsch.forge.treasure2.core.enums.LootTableType;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.registry.KeyLockRegistry;
import mod.gottsch.forge.treasure2.core.registry.TreasureLootTableRegistry;
import mod.gottsch.forge.treasure2.core.registry.WishableRegistry;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.registries.RegistryObject;


/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class WealthItem extends Item { //implements IWishable {
	public static final String DROPPED_BY = "droppedBy";
	
	/**
	 * 
	 * @param properties
	 */
	public WealthItem(Properties properties) {
        super(properties.stacksTo(Config.SERVER.wealth.wealthMaxStackSize.get()));
	}
	
	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
		// standard coin info
		tooltip.add(new TranslatableComponent(LangUtil.tooltip("wishable")).withStyle(ChatFormatting.GOLD, ChatFormatting.ITALIC));
	}

	/**
	 * 
	 */
	@Override
	public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entityItem) {
		// get the item stack or number of items.
		ItemStack entityItemStack = entityItem.getItem();
		
		Level level = entityItem.level;
		if (WorldInfo.isClientSide(level)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}
		
		// check if registered
//		if (!WishableRegistry.isRegistered(stack.getItem())) {
		if (!stack.is(TreasureTags.Items.WISHABLES)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}
		
		///// TODO possibly move this next section to WishableHandler
		ICoords coords = new Coords(entityItem.blockPosition());
//		BlockContext blockContext = new BlockContext(level, coords);
		int count = 0;
		// check if in water
		if (level.getBlockState(entityItem.blockPosition()).is(Blocks.WATER)) {
			// get the position
//		if (blockContext.equalsBlock(Blocks.WATER)) {
			// check if the water block is adjacent to 2 wishing well blocks
			ICoords checkCoords = coords.add(-1, 0, -1);
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					BlockContext checkBlock = new BlockContext(level, checkCoords);
					if (checkBlock.toBlock() instanceof IWishingWellBlock) {
						count++;
					}					
					if (count >= 2) {
						break;
					}
				}
			}

			if (count >=2) {
				Random random = new Random();
				for (int itemIndex = 0; itemIndex < entityItemStack.getCount(); itemIndex++) {
					// generate an item for each item in the stack
					Optional<ItemStack> lootStack = generateLoot(level, random, entityItem.getItem(), coords);
					if (lootStack.isPresent()) {
						// spawn the item 
						Containers.dropItemStack(level, (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), lootStack.get());
					}
				}
				// remove the item entity
				entityItem.remove(RemovalReason.DISCARDED);
				return true;
			}
		}
		return super.onEntityItemUpdate(stack, entityItem);
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param itemStack
	 * @param coords
	 */
//	@Override
	public Optional<ItemStack> generateLoot(Level world, Random random, ItemStack itemStack, ICoords coords) {
		IRarity rarity = WishableRegistry.getRarity(this);	
		
		List<LootTableShell> lootTables = getLootTables(rarity);
//		
//		// TODO most of this seems repeated from IChestGenerator.  Make a common class/methods
//		
		ItemStack outputStack = null;
//		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
		if (lootTables.isEmpty()) {
			outputStack = getDefaultLootKey(random, rarity);
		}
		else {
			// attempt to get the player who dropped the coin
			ItemStack wealthItem = itemStack;
			CompoundTag tag = wealthItem.getTag();
			Treasure.LOGGER.debug("item as a tag");
			Player player = null;
			if (tag != null && tag.contains(DROPPED_BY)) {
				UUID playerUuid = tag.getUUID(DROPPED_BY);
				player = world.getPlayerByUUID(playerUuid);
//				for (Player p : world.players()) {
//					if (p.getName().getString().equalsIgnoreCase(tag.getString(DROPPED_BY))) {
//						player = p;
//					}
//				}
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
//			TODO FINISH
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
//
//			// get effective rarity
//			Rarity effectiveRarity = TreasureLootTableRegistry.getLootTableMaster().getEffectiveRarity(tableShell, getDefaultEffectiveRarity(random));	
//			Treasure.LOGGER.debug("using effective rarity -> {}", effectiveRarity);
//			
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
	
	// ///////////////////////////  ///
		
	/**
	 * 
	 * @return
	 */
	public List<LootTableShell> getLootTables() {
		// determine the rarity tag the item belongs to
		
		// TODO should getRarity return a list? that would change the entire registry.
		IRarity rarity = WishableRegistry.getRarity(this);		
		
		return getLootTables(rarity);
	}
	
	public List<LootTableShell> getLootTables(IRarity rarity) {
		return TreasureLootTableRegistry.getLootTableByRarity(LootTableType.WISHABLES, rarity);
	}
	
	public ItemStack getDefaultLootKey (Random random) {
//
//		List<KeyItem> keys = new ArrayList<>(TreasureItems.keys.get(Rarity.COMMON));
//		return new ItemStack(keys.get(random.nextInt(keys.size())));
		IRarity rarity = WishableRegistry.getRarity(this);
		return getDefaultLootKey(random, rarity);
	}

	// TODO should this be static or create an object for vanilla items that are
	// in Wishables tag ?
	public ItemStack getDefaultLootKey(Random random, IRarity rarity) {
		List<RegistryObject<KeyItem>> keys = KeyLockRegistry.getKeys(rarity);
		List<KeyItem> keyItems = keys.stream().map(k -> k.get()).toList();
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
				return !p.getName().equalsIgnoreCase(IChestGenerator.TREASURE_POOL) && !p.getName().equalsIgnoreCase(IChestGenerator.CHARMS_POOL);
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
	
	// TODO exact same of IChestGenerator... need common class
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

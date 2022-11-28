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

import mod.gottsch.forge.gottschcore.block.BlockContext;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.IWishingWellBlock;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;


/**
 * @author Mark Gottschling on Aug 17, 2021
 *
 */
public class WealthItem extends Item { //implements IWishable {
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
		
		Level world = entityItem.level;
		if (WorldInfo.isClientSide(world)) {
			return super.onEntityItemUpdate(stack, entityItem);
		}
		
		// TODO check if member or wishing tag
		
		// get the position
		ICoords coords = new Coords(entityItem.blockPosition());
		BlockContext blockContext = new BlockContext(world, coords);
		int numWishingWellBlocks = 0;
		// check if in water
		if (blockContext.equalsBlock(Blocks.WATER)) {
			// check if the water block is adjacent to 2 wishing well blocks
			ICoords checkCoords = coords.add(-1, 0, -1);
			for (int z = 0; z < 3; z++) {
				for (int x = 0; x < 3; x++) {
					BlockContext checkCube = new BlockContext(world, checkCoords);
					if (checkCube.toBlock() instanceof IWishingWellBlock) {
						numWishingWellBlocks++;
					}					
					if (numWishingWellBlocks >= 2) {
						break;
					}
				}
			}
			
//			if (numWishingWellBlocks >=2) {
//				Random random = new Random();
//				for (int itemIndex = 0; itemIndex < entityItemStack.getCount(); itemIndex++) {
//					// generate an item for each item in the stack
//					Optional<ItemStack> lootStack = generateLoot(world, random, entityItem.getItem(), coords);
//					if (lootStack.isPresent()) {
//						// spawn the item 
//						InventoryHelper.dropItemStack(world, (double)coords.getX(), (double)coords.getY()+1, (double)coords.getZ(), lootStack.get());
//					}
//				}
//				// remove the item entity
//				entityItem.remove(RemovalReason.DISCARDED);
//				return true;
//			}
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
//	public Optional<ItemStack> generateLoot(Level world, Random random, ItemStack itemStack, ICoords coords) {
//		List<LootTableShell> lootTables = getLootTables();
//		
//		// TODO most of this seems repeated from IChestGenerator.  Make a common class/methods
//		
//		ItemStack outputStack = null;
//		// handle if loot tables is null or size = 0. return an item (apple) to ensure continuing functionality
//		if (lootTables == null || lootTables.size() == 0) {
//			outputStack = getDefaultLootKey(random);
//		}
//		else {
//			// attempt to get the player who dropped the coin
//			ItemStack wealthItem = itemStack;
//			CompoundNBT nbt = wealthItem.getTag();
//			Treasure.LOGGER.debug("item as a tag");
//			PlayerEntity player = null;
//			if (nbt != null && nbt.contains(DROPPED_BY_KEY)) {
//				// TODO change to check by UUID
//				for (PlayerEntity p : world.players()) {
//					if (p.getName().getString().equalsIgnoreCase(nbt.getString(DROPPED_BY_KEY))) {
//						player = p;
//					}
//				}
//				if (player != null && Treasure.LOGGER.isDebugEnabled()) {
//					Treasure.LOGGER.debug("coin dropped by player -> {}", player.getName());
//				}
//				else {
//					Treasure.LOGGER.debug("can't find player!");
//				}
//			}
////			Treasure.LOGGER.debug("player -> {}", player.getName().getString());
//
//			// select a table shell
//			LootTableShell tableShell = lootTables.get(RandomHelper.randomInt(random, 0, lootTables.size()-1));
//			if (tableShell.getResourceLocation() == null) {
//				return Optional.empty();
//			}
//			
//			// get the vanilla table from shell
//			LootTable table = world.getServer().getLootTables().get(tableShell.getResourceLocation());
//			// get a list of loot pools
//			List<LootPoolShell> lootPoolShells = tableShell.getPools();
//			
//			// generate a context
//			LootContext lootContext = getLootContext(world, player, coords);
//
//			List<ItemStack> itemStacks = new ArrayList<>();
//			for (LootPoolShell pool : lootPoolShells) {
//				Treasure.LOGGER.debug("processing pool -> {}", pool.getName());
//				// go get the vanilla managed pool
//				LootPool lootPool = table.getPool(pool.getName());
//				
//				// geneate loot from pools
//				lootPool.addRandomItems(itemStacks::add, lootContext);
//			}
//
//			// get effective rarity
//			Rarity effectiveRarity = TreasureLootTableRegistry.getLootTableMaster().getEffectiveRarity(tableShell, getDefaultEffectiveRarity(random));	
//			Treasure.LOGGER.debug("using effective rarity -> {}", effectiveRarity);
//			
//			// get all injected loot tables
//			injectLoot(world, random, itemStacks, tableShell.getCategory(), effectiveRarity, lootContext);
//			
//			for (ItemStack stack : itemStacks) {
//				Treasure.LOGGER.debug("possible loot item -> {}", stack.getItem().getRegistryName().toString());
//			}
//			
//			// select one item randomly
//			outputStack = itemStacks.get(RandomHelper.randomInt(0, itemStacks.size()-1));
//			Treasure.LOGGER.debug("loot item output stack -> {}", outputStack.getItem().getRegistryName().toString());
//		}				
//		return Optional.of(outputStack);
//	}
}

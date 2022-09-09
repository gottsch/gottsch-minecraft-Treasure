/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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
package com.someguyssoftware.treasure2.inventory;

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.POUCH_CAPABILITY;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class PouchInventory extends Inventory {
	public static int INVENTORY_SIZE = 9;

	/*
	 * Reference to the owning ItemStack
	 */
	private ItemStack itemStack;

	/**
	 * 
	 * @param stack
	 */
	public PouchInventory(ItemStack stack) {
		super(INVENTORY_SIZE);
		// save a ref to the item stack
		this.itemStack = stack;		
		IItemHandler itemHandler = stack.getCapability(POUCH_CAPABILITY, null).orElseThrow(IllegalStateException::new);
		readInventoryFromHandler(itemHandler);
	}

	/**
	 * 
	 * @param handler
	 */
	public void readInventoryFromHandler(IItemHandler handler) {
		try {
			// read the inventory
			for (int i = 0; i < INVENTORY_SIZE; i++) {
				setItem(i, handler.getStackInSlot(i));
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error reading items from IItemHandler:",  e);
		}
	}

	/**
	 * 
	 * @param handler
	 */
	public void writeInventoryToHandler(IItemHandler handler) {
		try {
			for (int i = 0; i < INVENTORY_SIZE; i++) {
				handler.insertItem(i, getItem(i), false);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Error writing Inventory to IItemHandler:",  e);
		}
	}

	///////////// IInventory Method

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getSizeInventory()
	 */
	@Override
	public int getContainerSize() {
		return INVENTORY_SIZE;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#getInventoryStackLimit()
	 * 
	 * If using custom Slots, this value must equal Slot.getItemStackLimit()
	 */
	@Override
	public int getMaxStackSize() {
		return TreasureConfig.BOOTY.wealthMaxStackSize.get();
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#closeInventory(net.minecraft.entity.player.PlayerEntity)
	 */
	@Override
	public void stopOpen(PlayerEntity player) {
		/*
		 *  write the locked state to the nbt
		 */
		IItemHandler itemHandler = getItemStack().getCapability(POUCH_CAPABILITY, null).orElseThrow(IllegalStateException::new);
		writeInventoryToHandler(itemHandler);
	}

	/**
	 * @return the itemStack
	 */
	public ItemStack getItemStack() {
		return itemStack;
	}

	/**
	 * @param itemStack the itemStack to set
	 */
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	/////////// End of IInventory methods

}
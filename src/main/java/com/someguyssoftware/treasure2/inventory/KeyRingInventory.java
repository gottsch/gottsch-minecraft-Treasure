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

import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.KEY_RING_CAPABILITY;
import static com.someguyssoftware.treasure2.capability.TreasureCapabilities.KEY_RING_INVENTORY_CAPABILITY;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.IKeyRingCapability;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingInventory extends Inventory {
	public static int INVENTORY_SIZE = 14;

	/*
	 * Reference to the owning ItemStack
	 */
	private ItemStack itemStack;

	/**
	 * 
	 * @param stack
	 */
	public KeyRingInventory(ItemStack stack) {
		super(INVENTORY_SIZE);
		// save a ref to the item stack
		this.itemStack = stack;		
		IItemHandler itemHandler = stack.getCapability(KEY_RING_INVENTORY_CAPABILITY, null).orElseThrow(IllegalStateException::new);
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
			/* 
			 * NOTE must clear the ItemStackHandler first because it retains it's inventory, the
			 * when insertItem is called, it actually appends, not replaces, items into it's inventory
			 * causing doubling of items.
			 */
			// clear the item handler capability			
			((ItemStackHandler)handler).setSize(INVENTORY_SIZE);
			// copy all the items over
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
		return 1;
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#openInventory(net.minecraft.entity.player.PlayerEntity)
	 */
	@Override
	public void startOpen(PlayerEntity player) {
		/*
		 *  clear the items. prevents duplicating keys.
		 *  this is to prevent the player from taking the keys from the key ring inventory gui, then dropping the key ring
		 *  while the inventory is open, then picking up the key ring again with all its items intact.
		 *  so now, if the player does dropping the key ring, it will not have any items in it's inventory and the player will lose any
		 *  keys that are left in the gui when closed.
		 */
		IKeyRingCapability cap = getItemStack().getCapability(KEY_RING_CAPABILITY, null).orElseThrow(IllegalStateException::new);
		cap.setOpen(true);
	}

	/* (non-Javadoc)
	 * @see net.minecraft.inventory.IInventory#closeInventory(net.minecraft.entity.player.PlayerEntity)
	 */
	@Override
	public void stopOpen(PlayerEntity player) {
		/*
		 *  write the locked state to the nbt
		 */
		IItemHandler itemHandler = getItemStack().getCapability(KEY_RING_INVENTORY_CAPABILITY, null).orElseThrow(IllegalStateException::new);
		writeInventoryToHandler(itemHandler);

		IKeyRingCapability cap = getItemStack().getCapability(KEY_RING_CAPABILITY, null).orElseThrow(IllegalStateException::new);
		cap.setOpen(false);
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
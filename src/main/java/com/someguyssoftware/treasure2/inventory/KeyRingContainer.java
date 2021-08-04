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

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

/**
 * This is a special container for the Key Ring
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingContainer extends AbstractChestContainer {
	class Point {
		public int x;
		public int  y;
		public Point(int x, int y) { this.x = x; this.y = y;}
	}
	
	public static KeyRingContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		// TODO redo .. don't need to pass the size as there is only 1 key ring (nor the type for that matter)
		return new KeyRingContainer(windowID, TreasureContainers.KEY_RING_CONTAINER_TYPE, playerInventory, KeyRingInventory.INVENTORY_SIZE);
	}
	
	/**
	 * Client-side constructor
	 * @param windowId
	 * @param containerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private KeyRingContainer(int windowId, ContainerType<?> containerType, PlayerInventory playerInventory, int slotCount) {
		this(windowId, containerType, playerInventory, new Inventory(slotCount));
	}

	/**
	 * Server-side constructor
	 * @param playerInventory
	 * @param inventory
	 */
	public KeyRingContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowID, containerType, playerInventory, inventory);
		// set the dimensions
		setContainerInventoryXPos(64);
		
		// build the container
		buildContainer(playerInventory, inventory);
	}
	
	/**
	 * KeyRing prevents the held slot (which contains the key ring itself) from being moved.
	 */
	@Override
	public void buildHotbar(PlayerInventory player) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			if (slotNumber == player.selected) {
				addSlot(new NoSlot(player, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
			else {
				addSlot(new Slot(player, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void buildContainerInventory() {
		Point[][] points = {
				{
					new Point(28, 38),
					new Point(28 + getSlotXSpacing(),  38),
					new Point(118,  38),
					new Point(118 + getSlotXSpacing(),  38)
				}, 
				{
					new Point(28, 56),
					new Point(28 + getSlotXSpacing(),  56),
					new Point(118,  56),
					new Point(118 + getSlotXSpacing(),  56)					
				}
		};
		
		// add top row         
		for (int x = 0; x < 3; x++) {
			int slotNumber = x;
			int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
			int ypos = 18;
			addSlot(new KeyRingSlot(contents, slotNumber, xpos, ypos));
		}
		
		// middle rows
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				int slotNumber = (y * 4 + x) + 3;
				addSlot(new KeyRingSlot(contents, slotNumber, points[y][x].x, points[y][x].y));
			}
		}		
		
		// add bottom row
		for (int x = 0; x < 3; x++) {
			int slotNumber = x + 11;
			int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
			int ypos = 76;
			addSlot(new KeyRingSlot(contents, slotNumber, xpos, ypos));
		}
	}
	
	@Override
	public int getHotbarYPos() {
		return 158;
	}
	
	@Override
	public int getPlayerInventoryYPos() {
		return 100;
	}
	
	@Override
	public int getContainerInventorySlotCount() {
		return 14;
	}
}

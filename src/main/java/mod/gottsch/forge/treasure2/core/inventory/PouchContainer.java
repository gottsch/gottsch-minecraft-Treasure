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
package mod.gottsch.forge.treasure2.core.inventory;

import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;

/**
 * 
 *  @author Mark Gottschling on Jan 16, 2018
 *
 */
public class PouchContainer extends AbstractChestContainer {
	public static PouchContainer create(int windowID, PlayerInventory playerInventory, PacketBuffer extraData) {
		return new PouchContainer(windowID, TreasureContainers.POUCH_CONTAINER_TYPE, playerInventory, PouchInventory.INVENTORY_SIZE);
	}
	
	/**
	 * Client-side constructor
	 * @param windowID
	 * @param skullChestContainerType
	 * @param playerInventory
	 * @param slotCount
	 */
	private PouchContainer(int windowID, ContainerType<?> containerType,	PlayerInventory playerInventory, int slotCount) {
		this(windowID, containerType, playerInventory, new Inventory(slotCount));

	}
	
	/**
	 * Server-side constructor
	 * @param windowID
	 * @param playerInventory
	 * @param inventory
	 */
	public PouchContainer(int windowID, ContainerType<?> containerType, PlayerInventory playerInventory, IInventory inventory) {
		super(windowID, containerType, playerInventory, inventory);

		// set the dimensions
        setHotbarYPos(176);
        setPlayerInventoryYPos(118);
		
		// set the dimensions
		setContainerInventoryColumnCount(3);
        setContainerInventoryRowCount(3);
		// skull has 13 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() * 3);
		setContainerInventoryYPos(52);
		
		// build the container
		buildContainer(playerInventory, inventory);
	}

	/**
	 * Pouch prevents the held slot (which contains the key ring itself) from being moved.
	 */
	@Override
	public void buildHotbar(PlayerInventory player) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			if (slotNumber == player.selected && player.offhand.get(0).getItem() != TreasureItems.POUCH.get()) {
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
		for (int y = 0; y < getContainerInventoryRowCount(); y++) {
			for (int x = 0; x < getContainerInventoryColumnCount(); x++) {
				int slotNumber = (y * getContainerInventoryColumnCount()) + x;
				int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getContainerInventoryYPos() + y * getSlotYSpacing();
				// pouches use PouchSlot
				addSlot(new PouchSlot(this.contents, slotNumber, xpos, ypos));
			}
		}
	}
	
	@Override
	public int getContainerInventorySlotCount() {
		return 9;
	}
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 *  @author Mark Gottschling on Jan 16, 2018
 *
 */
public class PouchContainerMenu extends AbstractTreasureContainerMenu implements ITreasureContainer {

	/**
	 * Server-side constructor
	 * @param windowID
	 * @param playerInventory
	 * @param inventory
	 */
	public PouchContainerMenu(int windowId, Inventory playerInventory, IItemHandler itemHandler) {
		super(windowId, TreasureContainers.POUCH_CONTAINER.get(), playerInventory, itemHandler);

		// set the dimensions
        setHotbarYPos(176);
        setPlayerInventoryYPos(118);
		
		// set the dimensions
		setMenuInventoryColumnCount(3);
        setMenuInventoryRowCount(3);
		setMenuInventoryXPos(8 + getSlotXSpacing() * 3);
		setMenuInventoryYPos(52);
		
		// build the container
		buildContainer();
	}

	/**
	 * Pouch prevents the held slot (which contains the pouch itself) from being moved.
	 */
	@Override
	public void buildHotbar(IItemHandler inventory) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			if (slotNumber == playerInventory.selected && playerInventory.offhand.get(0).getItem() != TreasureItems.POUCH.get()) {
				addSlot(new NoSlotItemHandler(inventory, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
			else {
				addSlot(new SlotItemHandler(inventory, slotNumber, getHotbarXPos() + getSlotXSpacing() * x, getHotbarYPos()));
			}
		}
	}
	
	/**
	 * 
	 */
	@Override
	public void buildContainerInventory() {
		for (int y = 0; y < getMenuInventoryRowCount(); y++) {
			for (int x = 0; x < getMenuInventoryColumnCount(); x++) {
				int slotNumber = (y * getMenuInventoryColumnCount()) + x;
				int xpos = getMenuInventoryXPos() + x * getSlotXSpacing();
				int ypos = getMenuInventoryYPos() + y * getSlotYSpacing();
				// pouches use PouchSlot
				addSlot(new PouchSlotItemHandler(this.itemHandler, slotNumber, xpos, ypos));
			}
		}
	}
	
	@Override
	public int getMenuInventorySlotCount() {
		return 9;
	}
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
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
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * This is a special container for the Key Ring
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingContainerMenu extends AbstractTreasureContainerMenu implements ITreasureContainer {
	class Point {
		public int x;
		public int  y;
		public Point(int x, int y) { this.x = x; this.y = y;}
	}

	/**
	 * Server side
	 * @param windowId
	 * @param playerInventory
	 * @param itemHandler
	 */
	public KeyRingContainerMenu(int windowId, Inventory playerInventory, IItemHandler itemHandler) {
		super(windowId, TreasureContainers.KEY_RING_CONTAINER.get(), playerInventory, itemHandler);
		
		// set the dimensions
		setMenuInventoryXPos(64);
		
		// build the container
		buildContainer();
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
		
	/**
	 * KeyRing prevents the held slot (which contains the key ring itself) from being moved.
	 */
	@Override
	public void buildHotbar(IItemHandler inventory) {
		for (int x = 0; x < HOTBAR_SLOT_COUNT; x++) {
			int slotNumber = x;
			// determine if the item is in left hand			
			if (slotNumber == playerInventory.selected && playerInventory.offhand.get(0).getItem() != TreasureItems.KEY_RING.get()) {
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
			int xpos = getMenuInventoryXPos() + x * getSlotXSpacing();
			int ypos = 18;
			addSlot(new KeyRingSlotItemHandler(this.itemHandler, slotNumber, xpos, ypos));
		}
		
		// middle rows
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				int slotNumber = (y * 4 + x) + 3;
				addSlot(new KeyRingSlotItemHandler(itemHandler, slotNumber, points[y][x].x, points[y][x].y));
			}
		}		
		
		// add bottom row
		for (int x = 0; x < 3; x++) {
			int slotNumber = x + 11;
			int xpos = getMenuInventoryXPos() + x * getSlotXSpacing();
			int ypos = 76;
			addSlot(new KeyRingSlotItemHandler(itemHandler, slotNumber, xpos, ypos));
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
	public int getMenuInventorySlotCount() {
		return 14;
	}
}

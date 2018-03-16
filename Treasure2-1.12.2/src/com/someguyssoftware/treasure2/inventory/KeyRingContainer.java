package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

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
	
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public KeyRingContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
		// set the dimensions
		setContainerInventoryXPos(64);
		
		// build the container
		buildContainer(invPlayer, inventory);
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
			addSlotToContainer(new KeyRingSlot(inventory, slotNumber, xpos, ypos));
		}
		
		// middle rows
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 4; x++) {
				int slotNumber = (y * 4 + x) + 3;
				addSlotToContainer(new KeyRingSlot(inventory, slotNumber, points[y][x].x, points[y][x].y));
			}
		}		
		
		// add bottom row
		for (int x = 0; x < 3; x++) {
			int slotNumber = x + 11;
			int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
			int ypos = 76;
			addSlotToContainer(new KeyRingSlot(inventory, slotNumber, xpos, ypos));
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

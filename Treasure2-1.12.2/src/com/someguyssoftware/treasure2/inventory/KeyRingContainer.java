package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Mar 9, 2018
 *
 */
public class KeyRingContainer extends AbstractChestContainer {
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public KeyRingContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
        
		// TODO column count and xpos doen't work for key ring where each slot is determined from a pre-made array
		// set the dimensions
		setContainerInventoryColumnCount(5);
		//key ring only has 15 slots, arranged in a circle
		setContainerInventoryXPos(8 + getSlotXSpacing() + getSlotXSpacing());
		
		// build the container
		buildContainer(invPlayer, inventory);
	}
	
	/**
	 * TODO set this up from an array
	 */
	@Override
	public void buildContainerInventory() {		
		/*
		 *  Add the tile inventory container to the gui
		 */
		for (int y = 0; y < getContainerInventoryRowCount(); y++) {
			for (int x = 0; x < getContainerInventoryColumnCount(); x++) {
				int slotNumber = y * getContainerInventoryColumnCount() + x;
				int xpos = getContainerInventoryXPos() + x * getSlotXSpacing();
				int ypos = getContainerInventoryYPos() + y * getSlotYSpacing();
				addSlotToContainer(new Slot(inventory, slotNumber, xpos, ypos));
			}
		}
	}
}

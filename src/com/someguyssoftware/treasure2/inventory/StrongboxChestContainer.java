package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class StrongboxChestContainer extends AbstractChestContainer {
	// Stores a reference to the tile entity instance for later use
	private IInventory inventory;

	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public StrongboxChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
        
		// set the dimensions
		setContainerInventoryColumnCount(5);
		// strongbox has 4 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() + getSlotXSpacing());
		
		// build the container
		buildContainer(invPlayer, inventory);
	}
}

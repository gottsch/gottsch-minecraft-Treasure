package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class SkullChestContainer extends AbstractChestContainer {
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public SkullChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
        
		// set the dimensions
		setContainerInventoryColumnCount(3);
        setContainerInventoryRowCount(3);
		// skull has 13 less columns - to center move the xpos over by xspacing*2
		setContainerInventoryXPos(8 + getSlotXSpacing() * 3);
		// TODO might lower the slots
		
		// build the container
		buildContainer(invPlayer, inventory);
	}
}

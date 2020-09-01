package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class WitherChestContainer extends AbstractChestContainer {
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public WitherChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
        
		// set the dimensions
		setHotbarYPos(198);
		setPlayerInventoryYPos(139);
		setContainerInventoryColumnCount(7);
        setContainerInventoryRowCount(6);
		// wither has 2 less columns - to center move the xpos over by 8+xspacing
		setContainerInventoryXPos(8 + getSlotXSpacing());
		// build the container
		buildContainer(invPlayer, inventory);
	}
}

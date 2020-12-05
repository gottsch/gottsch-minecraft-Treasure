package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * 
 * @author Mark Gottschling on Aug 27, 2019
 *
 */
public class MolluscChestContainer extends AbstractChestContainer {
	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public MolluscChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
        
		// set the dimensions
		setContainerInventoryColumnCount(1);
        setContainerInventoryRowCount(1);
		// skull has 15 less columns - to center move the xpos over by xspacing*4
		setContainerInventoryXPos(8 + getSlotXSpacing() * 4);
		setContainerInventoryYPos(getSlotYSpacing() * 2);
		
		// build the container
		buildContainer(invPlayer, inventory);
	}
}

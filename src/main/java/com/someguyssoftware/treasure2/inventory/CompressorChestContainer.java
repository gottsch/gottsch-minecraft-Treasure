package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class CompressorChestContainer extends AbstractChestContainer {
	// Stores a reference to the tile entity instance for later use
	private IInventory inventory;

	/**
	 * 
	 * @param invPlayer
	 * @param inventory
	 */
	public CompressorChestContainer(InventoryPlayer invPlayer, IInventory inventory) {
		super(invPlayer, inventory);
		// set the dimensions
		setHotbarXPos(44);
		setHotbarYPos(161);
		setPlayerInventoryXPos(44);
		setPlayerInventoryYPos(103);
        setContainerInventoryRowCount(4);
		setContainerInventoryColumnCount(13);

		// build the container
		buildContainer(invPlayer, inventory);
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on Dec 6, 2018
 *
 */
public class NoSlot extends Slot {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public NoSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    /**
     * Disable slot from movement.
     */
	@Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }
	
	@Override
	public boolean canTakeStack(EntityPlayer playerIn) {
		return false;
	}
}

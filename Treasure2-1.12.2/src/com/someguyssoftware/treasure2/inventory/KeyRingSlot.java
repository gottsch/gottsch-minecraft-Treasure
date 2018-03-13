/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.item.KeyItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityFurnace;

/**
 * @author Mark Gottschling on Mar 12, 2018
 *
 */
public class KeyRingSlot extends Slot {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public KeyRingSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    /**
     * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
     */
    public boolean isItemValid(ItemStack stack) {
        return (stack.getItem() instanceof KeyItem);
    }
    
    public int getItemStackLimit(ItemStack stack) {
        return 8;
    }
}

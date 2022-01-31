/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.WealthItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchSlot extends Slot {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public PouchSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    /**
     * Check if the stack is allowed to be placed in this slot.
     */
    public boolean isItemValid(ItemStack stack) {
    	if (stack.getItem() instanceof WealthItem || stack.getItem() instanceof Adornment) {
    		return true;
    	}
    	return false;
    }
    
    @Override
    public int getItemStackLimit(ItemStack stack) {
    	return 64; //TreasureConfig.BOOTY.wealthMaxStackSize.get();
    }
}

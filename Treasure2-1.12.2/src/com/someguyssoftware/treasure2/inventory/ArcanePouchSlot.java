/**
 * 
 */
package com.someguyssoftware.treasure2.inventory;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmCapabilityProvider;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author Mark Gottschling on May 18, 2020
 *
 */
public class ArcanePouchSlot extends PouchSlot {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public ArcanePouchSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

    /**
     * Check if the stack is allowed to be placed in this slot.
     */
	@Override
    public boolean isItemValid(ItemStack stack) {
		boolean result = super.isItemValid(stack);
		if (result) {
			if (!stack.hasCapability(CharmCapabilityProvider.CHARM_CAPABILITY, null)) {
				Treasure.logger.debug("item is not charmed, disallowed in arcane pouch slot");
				result = false;
			}
		}
		return result;    	
    }
	
    /**
     * 
     */
	@Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}

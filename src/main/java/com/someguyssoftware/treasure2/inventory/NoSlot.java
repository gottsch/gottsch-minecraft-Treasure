package com.someguyssoftware.treasure2.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.world.item.ItemStack;

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
    public boolean mayPlace(ItemStack stack) {
        return false;
    }


	@Override
	public boolean mayPickup(Player playerIn) {
		return false;
	}
}

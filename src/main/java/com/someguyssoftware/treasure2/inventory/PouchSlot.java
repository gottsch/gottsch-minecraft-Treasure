package com.someguyssoftware.treasure2.inventory;

import java.util.Set;
import java.util.stream.Collectors;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.CharmItem;
import com.someguyssoftware.treasure2.item.RunestoneItem;
import com.someguyssoftware.treasure2.item.WealthItem;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

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


	@Override
    public boolean mayPlace(ItemStack stack) {
		Set<String> tags = stack.getItem().getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
		if (stack.getItem() instanceof WealthItem
				|| stack.getItem() instanceof CharmItem
				|| stack.getItem() instanceof Adornment
				|| 	stack.getItem() instanceof RunestoneItem
				|| tags.contains("pouch")) {
    		return true;
    	}
    	return false;
    }
}

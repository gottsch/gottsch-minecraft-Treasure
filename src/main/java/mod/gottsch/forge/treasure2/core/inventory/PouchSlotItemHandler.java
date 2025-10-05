/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.inventory;

import mod.gottsch.forge.treasure2.core.item.WealthItem;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

/**
 * 
 * @author Mark Gottschling on May 14, 2020
 *
 */
public class PouchSlotItemHandler extends SlotItemHandler {

	/**
	 * 
	 * @param inventoryIn
	 * @param index
	 * @param xPosition
	 * @param yPosition
	 */
	public PouchSlotItemHandler(IItemHandler inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}


	@Override
    public boolean mayPlace(ItemStack stack) {
//		Set<String> tags = stack.getItem().getTags().stream().filter(tag -> tag.getNamespace().equals(Treasure.MODID)) .map(ResourceLocation::getPath).collect(Collectors.toSet());
		if (stack.getItem() instanceof WealthItem
//				|| stack.getItem() instanceof CharmItem
//				|| stack.getItem() instanceof Adornment
//				|| 	stack.getItem() instanceof RunestoneItem
				|| stack.is(TreasureTags.Items.POUCH)) {
    		return true;
    	}
    	return false;
    }
}

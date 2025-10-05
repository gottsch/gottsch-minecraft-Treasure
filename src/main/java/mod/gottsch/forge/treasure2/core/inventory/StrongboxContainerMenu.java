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

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * This is the base/standard container for chests that is similar configuration to that of a vanilla container.
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class StrongboxContainerMenu extends AbstractTreasureContainerMenu {

	public StrongboxContainerMenu(int windowId, BlockPos pos, Inventory playerInventory, Player player) {
		super(windowId, TreasureContainers.STRONGBOX_CONTAINER.get(), pos, playerInventory, player);

		// set the dimensions
		setMenuInventoryColumnCount(5);
		// strongbox has 4 less columns - to center move the xpos over by xspacing*2
		setMenuInventoryXPos(8 + getSlotXSpacing() + getSlotXSpacing());
		
		// build the container
		buildContainer();
	}
}

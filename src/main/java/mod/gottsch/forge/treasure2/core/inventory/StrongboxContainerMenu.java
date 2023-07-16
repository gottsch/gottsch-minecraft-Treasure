/*
 * This file is part of  Treasure2.
 * Copyright (c) 2018 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
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


	/**
	 * Server-side constructor
	 * @param playerInventory
	 * @param inventory
	 */
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

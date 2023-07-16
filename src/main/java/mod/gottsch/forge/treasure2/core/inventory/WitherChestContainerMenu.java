/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
 * @author Mark Gottschling on Aug 24, 2020
 *
 */
public class WitherChestContainerMenu extends AbstractTreasureContainerMenu {
	
	/**
	 * Server-side constructor
	 * @param windowID
	 * @param playerInventory
	 * @param inventory
	 */
	public WitherChestContainerMenu(int windowID, BlockPos pos, Inventory playerInventory, Player player) {
		super(windowID, TreasureContainers.WITHER_CHEST_CONTAINER.get(), pos, playerInventory, player);
        
		// set the dimensions
		setHotbarYPos(198);
		setPlayerInventoryYPos(139);
		setMenuInventoryColumnCount(7);
        setMenuInventoryRowCount(6);
		// wither has 2 less columns - to center move the xpos over by 8+xspacing
		setMenuInventoryXPos(8 + getSlotXSpacing());
		// build the container
		buildContainer();
	}

}

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
 * @author Mark Gottschling on Jan 16, 2018
 *
 */
public class CompressorChestContainerMenu extends AbstractTreasureContainerMenu {
	
	/**
	 * Server-side constructor
	 * @param playerInventory
	 * @param inventory
	 */
	public CompressorChestContainerMenu(int windowID, BlockPos pos, Inventory playerInventory, Player player) {
		super(windowID, TreasureContainers.COMPRESSOR_CHEST_CONTAINER.get(), pos, playerInventory, player);

		// set the dimensions
		setHotbarXPos(44);
		setHotbarYPos(161);
		setPlayerInventoryXPos(44);
		setPlayerInventoryYPos(103);
        setMenuInventoryRowCount(4);
		setMenuInventoryColumnCount(13);

		// build the container
		buildContainer();
	}
}

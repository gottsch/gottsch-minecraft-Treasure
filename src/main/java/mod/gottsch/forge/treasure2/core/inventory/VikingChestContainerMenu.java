/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.core.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

/**
 * 
 * @author Mark Gottschling May 25, 2023
 *
 */
public class VikingChestContainerMenu extends AbstractTreasureContainerMenu {

	/**
	 * 
	 * @param containerId
	 * @param pos
	 * @param playerInventory
	 * @param player
	 */
	public VikingChestContainerMenu(int containerId, BlockPos pos, Inventory playerInventory, Player player) {
		super(containerId, TreasureContainers.VIKING_CHEST_CONTAINER.get(), pos, playerInventory, player);
		
		if (Config.CLIENT.gui.enableCustomChestInventoryGui.get()) {
			setMenuInventoryYPos(19);
			setPlayerInventoryYPos(85);
			setHotbarYPos(143);
		}
		buildContainer();
	}
}

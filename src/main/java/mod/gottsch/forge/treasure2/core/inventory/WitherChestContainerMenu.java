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

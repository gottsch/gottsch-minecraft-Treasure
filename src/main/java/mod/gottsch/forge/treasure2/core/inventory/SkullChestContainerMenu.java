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

import mod.gottsch.forge.treasure2.core.block.entity.SkullChestBlockEntity;
import mod.gottsch.forge.treasure2.core.chest.ISkullChestType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 
 *  @author Mark Gottschling on Jan 16, 2018
 *
 */
public class SkullChestContainerMenu extends AbstractTreasureContainerMenu {
	private ISkullChestType skullType;
	
	public SkullChestContainerMenu(int containerId, BlockPos pos,
			Inventory playerInventory, Player player) {
		super(containerId,TreasureContainers.SKULL_CHEST_CONTAINER.get(), pos, playerInventory, player);

		// determine the chest by the pos
		BlockEntity blockEntity = player.level().getBlockEntity(pos);
		if (blockEntity instanceof SkullChestBlockEntity) {
			this.skullType = ((SkullChestBlockEntity)blockEntity).getSkullType();
		}
			
		// set the dimensions
		setMenuInventoryColumnCount(3);
        setMenuInventoryRowCount(3);
		// skull has 13 less columns - to center move the xpos over by xspacing*2
		setMenuInventoryXPos(8 + getSlotXSpacing() * 3);
		
		// build the container
		buildContainer();
	}

	public ISkullChestType getSkullType() {
		return skullType;
	}

}

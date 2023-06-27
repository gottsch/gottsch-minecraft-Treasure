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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.inventory.WitherChestContainerMenu;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * NOTE Wither ChestConfig does not have a "lid", but has doors, however, the use of the lidAngle from the super class
 * will still be used.  The rendered will use that value to determine how to render the doors.
 * 
 * @author Mark Gottschling on Jun 17, 2018
 *
 */
public class WitherChestBlockEntity extends AbstractTreasureChestBlockEntity {
    
	/**
	 * 
	 * @param texture
	 */
	public WitherChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.WITHER_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
    public AbstractContainerMenu createChestContainerMenu(int windowId, Inventory playerInventory, Player playerEntity) {
    	return new WitherChestContainerMenu(windowId, this.worldPosition, playerInventory, playerEntity);
    }
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("wither_chest.name"));
	}
    
	@Override
	public int getInventorySize() {
		return ChestInventorySize.WITHER.getSize();
	}
}

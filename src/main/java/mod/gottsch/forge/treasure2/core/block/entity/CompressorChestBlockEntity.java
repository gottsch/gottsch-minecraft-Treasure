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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.inventory.CompressorChestContainerMenu;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Mar 17, 2018
 *
 */
public class CompressorChestBlockEntity extends AbstractTreasureChestBlockEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public CompressorChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.COMPRESSOR_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
    public AbstractContainerMenu createChestContainerMenu(int windowId, Inventory playerInventory, Player playerEntity) {
    	return new CompressorChestContainerMenu(windowId, this.worldPosition, playerInventory, playerEntity);
    }
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("compressor_chest.name"));
	}
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getInventorySize() {
		return ChestInventorySize.COMPRESOR.getSize();
	}
}

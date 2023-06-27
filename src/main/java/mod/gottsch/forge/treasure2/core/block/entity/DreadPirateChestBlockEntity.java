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
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Mar 13, 2018
 *
 */
public class DreadPirateChestBlockEntity extends AbstractTreasureChestBlockEntity {
	
	/**
	 * 
	 * @param texture
	 */
	public DreadPirateChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.DREAD_PIRATE_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("dread_pirate_chest.name"));
	}
    
	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}
}

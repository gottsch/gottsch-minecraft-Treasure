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
import mod.gottsch.forge.treasure2.core.chest.ISkullChestType;
import mod.gottsch.forge.treasure2.core.chest.SkullChestType;
import mod.gottsch.forge.treasure2.core.inventory.SkullChestContainerMenu;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Mar 17, 2018
 *
 */
public class SkullChestBlockEntity extends AbstractTreasureChestBlockEntity {
	
	private ISkullChestType skullType;
	
	public SkullChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.SKULL_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
		skullType = SkullChestType.SKULL;
	}

	public SkullChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}
	
    @Override
    public AbstractContainerMenu createChestContainerMenu(int windowId, Inventory playerInventory, Player playerEntity) {
    	return new SkullChestContainerMenu(windowId, this.worldPosition, playerInventory, playerEntity);
    }

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putString("skullType", getSkullType().getName());
	}
	
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("skullType")) {
			// NOTE if wanted to make this extensible, you would need a registry for the enum
			setSkullType(SkullChestType.valueOf(tag.getString("skullType")));
		}
		else {
			setSkullType(SkullChestType.SKULL);
		}
	}
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("skull_chest.name"));
	}
	
	/**
	 * @return the numberOfSlots
	 */
	@Override
	public int getInventorySize() {
		return ChestInventorySize.SKULL.getSize();
	}

	public ISkullChestType getSkullType() {
		return skullType;
	}

	public void setSkullType(ISkullChestType skullType) {
		this.skullType = skullType;
	}
}

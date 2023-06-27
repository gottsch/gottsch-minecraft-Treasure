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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Jan 19, 2018
 *
 */
public class CrateChestBlockEntity extends AbstractTreasureChestBlockEntity {
	/** The current angle of the latch (between 0 and 1) */
	public float latchAngle;
	/** The angle of the latch last tick */
	public float prevLatchAngle;

	/**
	 * 
	 * @param texture
	 */
	public CrateChestBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.CRATE_CHEST_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
	public CrateChestBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("crate_chest.name"));
	}

	@Override
	public void tickClient() {
		this.prevLidAngle = this.lidAngle;

		if (this.openCount > 0 && this.lidAngle == 0.0F) {
//			this.playSound(SoundEvents.CHEST_OPEN);
			doChestOpenEffects(level, null, getBlockPos());
		}

		if (this.openCount == 0 && this.lidAngle > 0.0F || this.openCount > 0 && this.lidAngle < .125F) {
			float f2 = this.lidAngle;

			if (this.openCount > 0) {
				this.lidAngle += 0.0125F;
			} else {
				this.lidAngle -= 0.0125F;
			}

			if (this.lidAngle > 0.125F) {
				this.lidAngle = 0.125F;
			}

			if (this.lidAngle < 0.06F && f2 >= 0.06F) {
				doChestCloseEffects(level, null, getBlockPos());
//				this.playSound(SoundEvents.CHEST_CLOSE);
			}

			if (this.lidAngle < 0.0F) {
				this.lidAngle = 0.0F;
			}
		}
	}

	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}
}

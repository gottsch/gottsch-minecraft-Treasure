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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Feb 19, 2018
 *
 */
public class SafeBlockEntity extends AbstractTreasureChestBlockEntity {
	/** The angle of the latch last tick */
	public float prevLatchPos;

	public float handleAngle;
	public float prevHandleAngle;

	public boolean isHandleOpen = false;
	public boolean isHandleClosed = true;
	public boolean isLidOpen = false;
	public boolean isLidClosed = false;

	/**
	 * 
	 * @param texture
	 */
	public SafeBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.SAFE_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("safe.name"));
	}
    
	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}

	/**
	 * Like the old updateEntity(), except more generic.
	 */
	@Override
	public void tickClient() {

		// save the previous positions and angles of safe components
		this.prevLidAngle = this.lidAngle;
		this.prevHandleAngle = this.handleAngle;

		// opening ie. players
		if (this.openCount > 0) {
			// test the handle
			if (this.handleAngle > -1.0F) {
				isHandleOpen = false;
				this.handleAngle -= 0.1F;
				isHandleClosed = false;
				if (this.handleAngle <= -1.0F) {
					this.handleAngle = -1.0F;
					isHandleOpen = true;
				}
			} else {
				isHandleOpen = true;
			}

			if (isHandleOpen) {
				// play the opening chest sound the at the beginning of opening
				if (this.lidAngle == 0.0F) {
					this.playSound(SoundEvents.CHEST_OPEN);
				}

				// test the lid
				if (this.lidAngle < 1.0F) {
					isLidOpen = false;
					this.lidAngle += 0.1F;
					isLidClosed = false;
					if (this.lidAngle >= 1.0F) {
						this.lidAngle = 1.0F;
						isLidOpen = true;
					}
				} else {
					isLidOpen = true;
				}
			}
		}

		// closing ie no players
		else {
			float f2 = this.lidAngle;

			if (this.lidAngle > 0.0F) {
				isLidClosed = false;
				this.lidAngle -= 0.1F;
				isLidOpen = false;
				if (this.lidAngle <= 0.0F) {
					this.lidAngle = 0.0F;
					isLidClosed = true;
				}
			} else {
				isLidClosed = true;
			}

			// play the closing sound
			if (this.lidAngle < 0.06F && f2 >= 0.06F) {
				this.playSound(SoundEvents.CHEST_CLOSE);
			}

			if (isLidClosed) {
				if (this.handleAngle < 0.0F) {
					isHandleClosed = false;
					this.handleAngle += 0.1F;
					isHandleOpen = false;
					if (this.handleAngle >= 0.0F) {
						this.handleAngle = 0.0F;
						isHandleClosed = true;
					}
				} else {
					isHandleClosed = true;
				}
			}
		}
	}

	/**
	 * @return the prevLatchPos
	 */
	public float getPrevLatchPos() {
		return prevLatchPos;
	}

	/**
	 * @param prevLatchPos the prevLatchPos to set
	 */
	public void setPrevLatchPos(float prevLatchPos) {
		this.prevLatchPos = prevLatchPos;
	}

	/**
	 * @return the handleAngle
	 */
	public float getHandleAngle() {
		return handleAngle;
	}

	/**
	 * @param handleAngle the handleAngle to set
	 */
	public void setHandleAngle(float handleAngle) {
		this.handleAngle = handleAngle;
	}

	/**
	 * @return the prevHandleAngle
	 */
	public float getPrevHandleAngle() {
		return prevHandleAngle;
	}

	/**
	 * @param prevHandleAngle the prevHandleAngle to set
	 */
	public void setPrevHandleAngle(float prevHandleAngle) {
		this.prevHandleAngle = prevHandleAngle;
	}
}

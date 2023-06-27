/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
 * @author Mark Gottschling on Nov 29, 2020
 *
 */
public class CardboardBoxBlockEntity extends AbstractTreasureChestBlockEntity {
	/** The angle of the latch last tick */
	public float prevInnerLidPos;

	public float innerLidAngle;
	public float prevInnerLidAngle;

	public boolean isLidOpen = false;
	public boolean isLidClosed = true;
	public boolean isInnerLidOpen = false;
	public boolean isInnerLidClosed = true;

	/**
	 * 
	 * @param texture
	 */
	public CardboardBoxBlockEntity(BlockPos pos, BlockState state) {
		super(TreasureBlockEntities.CARDBOARD_BOX_BLOCK_ENTITY_TYPE.get(), pos, state);
	}
	
    @Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("cardboard_box.name"));
	}

	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}
	@Override
	public void tickClient() {

		// save the previous positions and angles of box components
		this.prevLidAngle = this.lidAngle;
		this.prevInnerLidAngle = this.innerLidAngle;

		// opening ie. players
		if (this.openCount > 0) {
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

			if (isLidOpen) {
				// play the opening chest sound the at the beginning of opening
				if (isInnerLidClosed) {
					this.playSound(SoundEvents.CHEST_OPEN);
				}

				// test the inner lid
				if (this.innerLidAngle < 1.0F) {
					isInnerLidOpen = false;
					this.innerLidAngle += 0.1F;
					isInnerLidClosed = false;
					if (this.innerLidAngle >= 1.0F) {
						this.innerLidAngle = 1.0F;
						isInnerLidOpen = true;
					}
				} else {
					isInnerLidOpen = true;
				}
			}
		}

		// closing
		else {
			// test the inner lid
			if (this.innerLidAngle > 0.0F) {
				isInnerLidClosed = false;
				this.innerLidAngle -= 0.1F;
				isInnerLidOpen = false;
				if (this.innerLidAngle <= 0.0F) {
					this.innerLidAngle = 0.0F;
					isInnerLidClosed = true;
				}
			} else {
				isInnerLidClosed = true;
			}

			// play the closing soud
			if (isInnerLidClosed ) {
				if (isLidOpen) {
					this.playSound(SoundEvents.CHEST_CLOSE);
				}
				// test the outer lid
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
			}	
		}		
	}

	public float getPrevInnerLidPos() {
		return prevInnerLidPos;
	}

	public void setPrevInnerLidPos(float innerLidPos) {
		this.prevInnerLidPos = innerLidPos;
	}

	public float getInnerLidAngle() {
		return innerLidAngle;
	}

	public void setInnerLidAngle(float innerLidAngle) {
		this.innerLidAngle = innerLidAngle;
	}

	public float getPrevInnerLidAngle() {
		return prevInnerLidAngle;
	}

	public void setPrevInnerLidAngle(float innerLidAngle) {
		this.prevInnerLidAngle = innerLidAngle;
	}
}

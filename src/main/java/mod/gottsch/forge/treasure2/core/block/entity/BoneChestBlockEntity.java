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

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.chest.ChestInventorySize;
import mod.gottsch.forge.treasure2.core.util.LangUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

/**
 * 
 * @author Mark Gottschling on Sep 29, 2022
 *
 */
public class BoneChestBlockEntity extends AbstractTreasureChestBlockEntity {
	private static final double PROXIMITY_SQUARED = 36;

	// persistent state property
	private boolean locked;

	// display state properties
	public float skullYPosition;
	public float prevSkullYPosition;
	
	public float lockAngle;
	public float prevLockAngle;

	public boolean isSkullOpen = false;
	public boolean isSkullClosed = true;
	public boolean isLockOpen = true;
	public boolean isLockClosed = false;
	public boolean isLidOpen = false;
	public boolean isLidClosed = false;

	public BoneChestBlockEntity(BlockPos pos, net.minecraft.world.level.block.state.BlockState state) {
		super(TreasureBlockEntities.BONE_CHEST.get(), pos, state);
		setLocked(false);
	}

	@Override
	public Component getDefaultName() {
		return Component.translatable(LangUtil.screen("bone_chest.name"));
	}

	@Override
	public int getInventorySize() {
		return ChestInventorySize.STANDARD.getSize();
	}


	@Override
	public void tickClient() {
		// save the previous positions and angles of safe components
		this.prevLidAngle = this.lidAngle;
		this.prevSkullYPosition = this.skullYPosition;
		this.prevLockAngle = this.lockAngle;		

		// check if player is within range
		boolean isProximityMet = false;
		for (Player player : getLevel().players()) {
			// get the distance
			double distanceSq = player.distanceToSqr(this.getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ());
			if (distanceSq < PROXIMITY_SQUARED) {
				isProximityMet = true;
				break;
			}
		}

		if (isProximityMet) {
			// test the skull
			if (this.skullYPosition > -1.0F) { // TODO rename to skullYAmount and make a positive. it represents a percent of movement, not the actual position
				isSkullOpen = false;
				this.skullYPosition -= 0.1F;
				isSkullClosed = false;
				if (this.skullYPosition <= -1.0F) {
					this.skullYPosition = -1.0F;
					isSkullOpen = true;
				}
			} else {
				isSkullOpen = true;
			}
		} else {
			if (this.skullYPosition < 0.0F) {
				isSkullClosed = false;
				this.skullYPosition += 0.1F;
				isSkullOpen = false;
				if (this.skullYPosition >= 0.0F) {
					this.skullYPosition = 0.0F;
					isSkullClosed = true;
				}
			} else {
				isSkullClosed = true;
			}
		}

		// process lock rotation
		if (!isLocked() && !isLockOpen) {
			if (this.lockAngle > 0F) {
				this.lockAngle -= 0.1F;
				isLockClosed = false;
				if (this.lockAngle <= 0F) {
					this.lockAngle = 0F;
					isLockOpen = true;
				}
			}
			else {
				isLockOpen = true;
			}
		}
		else if (isLocked() && !isLockClosed) {
			if (this.lockAngle < 1.0F) {
				this.lockAngle += 0.1F;
				isLockOpen = false;
				if (this.lockAngle > 1.0F) {
					this.lockAngle = 1F;
					isLockClosed = true;
				}
			}
			else {
				isLockClosed = true;
			}
		}
		
		// opening ie. players
		if (this.openCount > 0) {
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
		}		

	}

	@Override
	public void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		try {
			tag.putBoolean("locked", isLocked());
			Treasure.LOGGER.debug("saving Bone chest locked -> {}", isLocked());
		} catch (Exception e) {
			Treasure.LOGGER.error("error writing Properties to NBT:", e);
		}
	}

	/**
	 * 
	 */
	@Override
	public void load(CompoundTag tag) {
		super.load(tag);

		try {
			if (tag.contains("locked")) {
				setLocked(tag.getBoolean("locked"));
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("error reading to NBT:", e);
		}
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public float getSkullYPosition() {
		return skullYPosition;
	}

	public void setSkullYPosition(float skullYPosition) {
		this.skullYPosition = skullYPosition;
	}

	public float getPrevSkullYPosition() {
		return prevSkullYPosition;
	}

	public void setPrevSkullYPosition(float prevSkullYPosition) {
		this.prevSkullYPosition = prevSkullYPosition;
	}

	public float getLockAngle() {
		return lockAngle;
	}

	public void setLockAngle(float lockAngle) {
		this.lockAngle = lockAngle;
	}

	public float getPrevLockAngle() {
		return prevLockAngle;
	}

	public void setPrevLockAngle(float prevLockAngle) {
		this.prevLockAngle = prevLockAngle;
	}
}

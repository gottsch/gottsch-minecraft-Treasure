/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.lock;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.item.LockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public class LockState {
	private ILockSlot slot;
	private LockItem lockItem;

	/**
	 * 
	 */
	public LockState() {

	}

	/**
	 * 
	 * @param nbt
	 * @return
	 */
	public CompoundTag save(CompoundTag nbt) {
		try {
			if (this.getSlot() != null) {
				CompoundTag slotNBT = new CompoundTag();
				slotNBT = this.getSlot().save(slotNBT);
				nbt.put("slot", slotNBT);
			}
			if (this.getLock() != null) {
				CompoundTag lockNBT = new CompoundTag();
				new ItemStack(this.getLock()).save(lockNBT);
				nbt.put("lockItem", lockNBT);
			}
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	/**
	 * This method reads only this lockstate's properties from an NBT tag
	 * 
	 * @param tag
	 */
	public static LockState load(CompoundTag tag) {
		ILockSlot slot = null;
		ItemStack lockStack = null;

		CompoundTag slotNBT = null;
		if (tag.contains("slot", Tag.TAG_COMPOUND)) {
			slotNBT = tag.getCompound("slot");
			slot = new LockSlot().load(slotNBT);
		}

		CompoundTag lockNBT = null;
		if (tag.contains("lockItem", Tag.TAG_COMPOUND)) {
			lockNBT = tag.getCompound("lockItem");
			if (lockNBT != null) {
				lockStack = ItemStack.of(lockNBT);
			}
		}

		LockState lockState = new LockState();
		// update the properties
		if (slot != null) {
			lockState.setSlot(slot);
		}
		if (lockStack != null) {
			lockState.setLock((LockItem) lockStack.getItem());
		}
		return lockState;
	}

	/**
	 * @return the slot
	 */
	public ILockSlot getSlot() {
		return slot;
	}

	/**
	 * @param slot the slot to set
	 */
	public void setSlot(ILockSlot slot) {
		this.slot = slot;
	}

	/**
	 * @return the lockItem
	 */
	public LockItem getLock() {
		return lockItem;
	}

	/**
	 * @param lockItem the lockItem to set
	 */
	public void setLock(LockItem lockItem) {
		this.lockItem = lockItem;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockState [slot=" + slot + ", lockItem=" + lockItem + "]";
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.lock;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.item.LockItem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;

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
	public CompoundNBT writeToNBT(CompoundNBT nbt) {
		try {
			if (this.getSlot() != null) {
				CompoundNBT slotNBT = new CompoundNBT();
				slotNBT = this.getSlot().writeToNBT(slotNBT);
				nbt.put("slot", slotNBT);
			}
			if (this.getLock() != null) {
				CompoundNBT lockNBT = new CompoundNBT();
				new ItemStack(this.getLock()).write(lockNBT);
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
	public static LockState readFromNBT(CompoundNBT tag) {
		ILockSlot slot = null;
		ItemStack lockStack = null;

		CompoundNBT slotNBT = null;
		if (tag.contains("slot", Constants.NBT.TAG_COMPOUND)) {
			slotNBT = tag.getCompound("slot");
			slot = ILockSlot.readFromNBT(slotNBT);
		}

		CompoundNBT lockNBT = null;
		if (tag.contains("lockItem", Constants.NBT.TAG_COMPOUND)) {
			lockNBT = tag.getCompound("lockItem");
			if (lockNBT != null) {
				lockStack = ItemStack.read(lockNBT);
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

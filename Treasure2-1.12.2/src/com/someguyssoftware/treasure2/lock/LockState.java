/**
 * 
 */
package com.someguyssoftware.treasure2.lock;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.ILockSlot;
import com.someguyssoftware.treasure2.item.LockItem;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

/**
 * @author Mark Gottschling onJan 10, 2018
 *
 */
public class LockState {
//	private int index;
	private ILockSlot slot;
//	private Item lockItem; // item or just LockItem ?
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
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		try {
			if (this.getSlot() != null) {
				NBTTagCompound slotNBT = new NBTTagCompound();
				slotNBT = this.getSlot().writeToNBT(slotNBT);
				nbt.setTag("slot", slotNBT);
			}
			if (this.getLock() != null) {
				NBTTagCompound lockNBT = new NBTTagCompound();
				new ItemStack(this.getLock()).writeToNBT(lockNBT);
				nbt.setTag("lockItem", lockNBT);
			}			
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}		
		return nbt;
	}
	
	/**
	 * This method reads only this lockstate's properties from an NBT tag
	 * @param tag
	 */
	public static LockState readFromNBT(NBTTagCompound tag) {
		ILockSlot slot = null;
		ItemStack lockStack = null;
		
	    NBTTagCompound slotNBT = null;
	    if (tag.hasKey("slot", Constants.NBT.TAG_COMPOUND)) {
	        slotNBT = tag.getCompoundTag("slot");
	        slot = ILockSlot.readFromNBT(slotNBT);
	    }
	
	    NBTTagCompound lockNBT = null;
	    if (tag.hasKey("lockItem", Constants.NBT.TAG_COMPOUND)) {
	    	lockNBT = tag.getCompoundTag("lockItem");	    	
	    	if (lockNBT != null) {
	    		lockStack = new ItemStack(lockNBT);
	    	}
	    }

	    LockState lockState = new LockState();	    
	    // update the properties
	    if (slot != null) {
	    	lockState.setSlot(slot);
	    }
	    if (lockStack != null) {
	    	lockState.setLock((LockItem)lockStack.getItem());
	    }
	    return lockState;
	}
	
	
//	/**
//	 * @return the index
//	 */
//	public int getIndex() {
//		return index;
//	}
//
//	/**
//	 * @param index the index to set
//	 */
//	public void setIndex(int index) {
//		this.index = index;
//	}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LockState [slot=" + slot + ", lockItem=" + lockItem + "]";
	}
}

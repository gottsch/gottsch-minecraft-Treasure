/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

/**
 * NOTE should this contain all sort of data of a chest or is this class simply about locks? If about just locks it should change it's name.
 * @author Mark Gottschling on Jan 9, 2018
 *
 */
public class TreasureChestType {
	private int maxLocks;
	private LockSlot[] slots;
	
	/**
	 * 
	 */
	public TreasureChestType(int maxLocks) {
		setMaxLocks(maxLocks);
		setSlots(new LockSlot[maxLocks]);
	}

	/**
	 * @return the maxLocks
	 */
	public int getMaxLocks() {
		return maxLocks;
	}

	/**
	 * @param maxLocks the maxLocks to set
	 */
	private TreasureChestType setMaxLocks(int maxLocks) {
		this.maxLocks = maxLocks;
		return this;
	}

	/**
	 * @return the slots
	 */
	public LockSlot[] getSlots() {
		return slots;
	}

	/**
	 * 
	 * @param lockSlots
	 */
	public TreasureChestType addSlots(LockSlot...lockSlots ) {
		for (int i = 0; i < lockSlots.length; i++) {
			if (i < this.getMaxLocks()) {
				this.getSlots()[i] = lockSlots[i];
			}
		}
		return this;
	}
	
	/**
	 * @param slots the slots to set
	 */
	private TreasureChestType setSlots(LockSlot[] slots) {
		this.slots = slots;
		return this;
	}
}

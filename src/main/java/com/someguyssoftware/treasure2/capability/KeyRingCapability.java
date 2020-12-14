/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

/**
 * @author Mark Gottschling on May 11, 2020
 *
 */
public class KeyRingCapability implements IKeyRingCapability  {
	private boolean isOpen;
	private boolean usedOnChest;
	
	/**
	 * 
	 */
	public KeyRingCapability() {
		
	}
	
	@Override
	public boolean isOpen() {
		return isOpen;
	}
	@Override
	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
	@Override
	public boolean isUsedOnChest() {
		return usedOnChest;
	}
	@Override
	public void setUsedOnChest(boolean usedOnChest) {
		this.usedOnChest = usedOnChest;
	}
}

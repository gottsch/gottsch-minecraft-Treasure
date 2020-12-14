package com.someguyssoftware.treasure2.capability;

/**
 * 
 * @author Mark Gottschling on May 12, 2020
 *
 */
public interface IKeyRingCapability {

	public boolean isOpen();

	public void setOpen(boolean isOpen);

	public boolean isUsedOnChest();

	public void setUsedOnChest(boolean usedOnChest);

}
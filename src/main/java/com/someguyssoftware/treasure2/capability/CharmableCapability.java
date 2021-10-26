/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

@Deprecated
public class CharmableCapability implements ICharmableCapability {
    private int slots;
    private String customName;
    
	/**
	 * 
	 */
	public CharmableCapability() {
	}

	@Override
	public String getCustomName() {
		return customName;
	}

	@Override
	public void setCustomName(String customName) {
		this.customName = customName;
	}

	@Override
	public int getSlots() {
		return slots;
	}

	@Override
	public void setSlots(int slots) {
		this.slots = slots;
	}

}

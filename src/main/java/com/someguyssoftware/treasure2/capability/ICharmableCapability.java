/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

/**
 * @author Mark Gottschling on Dec 20, 2020
 *
 */
public interface ICharmableCapability {

	void setCustomName(String customName);

	String getCustomName();

	int getSlots();

	void setSlots(int slots);


}

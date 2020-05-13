/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.someguyssoftware.treasure2.item.charm.ICharmState;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public interface ICharmCapability {
	public List<ICharmState> getCharmStates();
	public void setCharmStates(List<ICharmState> states);
	double getCharmValueModifier();
	void setCharmValueModifier(double valueModifier);
	double getCharmPercentModifier();
	void setCharmPercentModifier(double charmPercentModifier);
}

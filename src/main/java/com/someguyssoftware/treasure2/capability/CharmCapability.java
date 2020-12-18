/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.treasure2.item.charm.ICharmState;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
public class CharmCapability implements ICharmCapability {
	private List<ICharmState> charmStates = new ArrayList<>(3);
//	private double charmValueModifier = 1.0;
//	private double charmPercentModifier = 1.0;
	
	/**
	 * 
	 */
	public CharmCapability() {
	}
	
	@Override
	public List<ICharmState> getCharmStates() {
		if (charmStates == null) {
			this.charmStates = new ArrayList<>(3);
		}
		return charmStates;
	}

	@Override
	public void setCharmStates(List<ICharmState> states) {
		this.charmStates = states;

	}

//	@Override
//	public double getCharmValueModifier() {
//		return charmValueModifier;
//	}
//
//	@Override
//	public void setCharmValueModifier(double charmMaxModifier) {
//		this.charmValueModifier = charmMaxModifier;
//	}
//
//	@Override
//	public double getCharmPercentModifier() {
//		return charmPercentModifier;
//	}
//
//	@Override
//	public void setCharmPercentModifier(double charmPercentModifier) {
//		this.charmPercentModifier = charmPercentModifier;
//	}

}

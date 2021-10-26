/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.treasure2.item.charm.ICharmInstance;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
@Deprecated
public class CharmCapability implements ICharmCapability {
    private List<ICharmInstance> charmInstances = new ArrayList<>(3);
    
	/**
	 * 
	 */
	public CharmCapability() {
	}
	
	@Override
	public List<ICharmInstance> getCharmInstances() {
		if (charmInstances == null) {
			this.charmInstances = new ArrayList<>(3);
		}
		return charmInstances;
	}

	@Override
	public void setCharmInstances(List<ICharmInstance> instances) {
		this.charmInstances = instances;

	}
}

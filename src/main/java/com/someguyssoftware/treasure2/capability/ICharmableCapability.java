/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.someguyssoftware.treasure2.item.charm.ICharmInstance;

/**
 * Revamped Charm Capability
 * @author Mark Gottschling on Jan 16, 2021
 *
 */
public interface ICharmableCapability {
	public List<ICharmInstance> getCharmInstances();
	public void setCharmInstances(List<ICharmInstance> instances);
}
/**
 * 
 */
package com.someguyssoftware.treasure2.capability;

import java.util.List;

import com.someguyssoftware.treasure2.item.charm.ICharmInstance;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
@Deprecated
public interface ICharmCapability {
	public List<ICharmInstance> getCharmInstances();
	public void setCharmInstances(List<ICharmInstance> instances);
}

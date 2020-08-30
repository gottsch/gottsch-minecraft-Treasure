/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.spatial.ICoords;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface IGeneratorData {
	public ICoords getSpawnCoords();
	public void setSpawnCoords(ICoords coords);
}

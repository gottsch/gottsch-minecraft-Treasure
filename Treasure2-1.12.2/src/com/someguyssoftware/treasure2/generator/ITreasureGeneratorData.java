/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface ITreasureGeneratorData {
	public ICoords getSpawnCoords();
	public void setSpawnCoords(ICoords coords);
}

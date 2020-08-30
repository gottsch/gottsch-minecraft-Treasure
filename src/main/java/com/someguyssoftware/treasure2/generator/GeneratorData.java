/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.someguyssoftware.gottschcore.spatial.ICoords;


/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GeneratorData implements IGeneratorData {
	private ICoords spawnCoords;

	public GeneratorData() {}
	
	public GeneratorData(ICoords spawnCoords) {
		super();
		this.spawnCoords = spawnCoords;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public ICoords getSpawnCoords() {
		return spawnCoords;
	}

	public void setSpawnCoords(ICoords spawnCoords) {
		this.spawnCoords = spawnCoords;
	}
		
}

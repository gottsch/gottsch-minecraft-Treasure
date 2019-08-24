/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter 
public class TreasureGeneratorData implements ITreasureGeneratorData {
	private ICoords spawnCoords;

	@Override
	public String toString() {
		return "TreasureGeneratorData [spawnCoords=" + spawnCoords + "]";
	}
	
	// lombok'ed
//	@Override
//	public ICoords getSpawnCoords() {
//		return spawnCoords;
//	}
//
//	@Override
//	public void setSpawnCoords(ICoords coords) {
//		this.spawnCoords = coords;
//	}
	
}

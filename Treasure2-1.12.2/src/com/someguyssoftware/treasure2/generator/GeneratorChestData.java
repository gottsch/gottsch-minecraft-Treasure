/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Mark Gottschling on Aug 21, 2019
 *
 */
@NoArgsConstructor
@Getter
@Setter
public class GeneratorChestData extends TreasureGeneratorData {
	private ICoords chestCoords;
}

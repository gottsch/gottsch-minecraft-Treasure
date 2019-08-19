/**
 * 
 */
package com.someguyssoftware.treasure2.generator.structure;

import com.someguyssoftware.treasure2.generator.GenericGeneratorData;
import com.someguyssoftware.treasure2.generator.GenericGeneratorResult;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public class GenericStructureGenerator implements INewTreasureStructureGenerator<GenericGeneratorResult<GenericGeneratorData>>{

	@Override
	public GenericGeneratorResult<GenericGeneratorData> generate() {
		GenericGeneratorResult<GenericGeneratorData> result = new GenericGeneratorResult<>(true);
		return result;
	}

	
}

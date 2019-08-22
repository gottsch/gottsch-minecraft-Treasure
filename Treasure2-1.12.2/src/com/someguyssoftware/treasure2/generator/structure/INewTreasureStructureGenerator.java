/**
 * 
 */
package com.someguyssoftware.treasure2.generator.structure;

import com.someguyssoftware.treasure2.generator.ITreasureGeneratorResult;

/**
 * @author Mark Gottschling on Aug 15, 2019
 *
 */
public interface INewTreasureStructureGenerator<RESULT extends ITreasureGeneratorResult<?>> {

	public RESULT generate();
}

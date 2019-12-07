/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import com.someguyssoftware.gottschcore.positional.ICoords;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.block.state.IBlockState;

/**
 * @author Mark Gottschling on Aug 21, 2019
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ChestGeneratorData extends GeneratorData {
	private ICoords chestCoords;
	private IBlockState chestState;
	
	@Override
	public String toString() {
		return "ChestGeneratorData [chestCoords=" + chestCoords + ", chestState=" + chestState + "]";
	}
}

/**
 * 
 */
package mod.gottsch.forge.treasure2.core.block;

import com.someguyssoftware.gottschcore.block.ModBlock;

/**
 * Pass-through block that implements the tag interface ITreasureBlock
 * for those blocks that don't need any special implementation but need to be tagged.
 * @author Mark Gottschling on Jan 24, 2021
 *
 */
public class TreasureBlock extends ModBlock implements ITreasureBlock {

	public TreasureBlock(String modID, String name, Properties properties) {
		super(modID, name, properties);
	}

}

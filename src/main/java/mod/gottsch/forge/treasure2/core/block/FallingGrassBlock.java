/**
 * 
 */
package mod.gottsch.forge.treasure2.core.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Feb 22, 2020
 *
 */
public class FallingGrassBlock extends AbstractTreasureFallingBlock implements ITreasureBlock {

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public FallingGrassBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, BlockState p_176502_3_, BlockState p_176502_4_) {
		worldIn.setBlockAndUpdate(pos, Blocks.GRASS_BLOCK.defaultBlockState());
	}
}
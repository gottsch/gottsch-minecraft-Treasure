/**
 * 
 */
package mod.gottsch.forge.treasure2.core.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.ModFallingBlock;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Mark Gottschling on Feb 9, 2021
 *
 */
public abstract class AbstractTreasureFallingBlock extends ModFallingBlock implements ITreasureBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

	protected static final VoxelShape BOUNDING_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	protected static final VoxelShape COLLISION_SHAPE = Block.box(0, 0, 0, 16, 16, 16);
	
	public AbstractTreasureFallingBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(ACTIVATED, Boolean.valueOf(false)));
	}
	

	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return BOUNDING_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return COLLISION_SHAPE;
	}

	/**
	 * Called When an Entity Collided with the Block.
	 */
	@Override
	public void stepOn(World world, BlockPos pos, Entity entityIn) {
//		Treasure.LOGGER.debug("stepped on block...");
		// only on server
		if (!WorldInfo.isClientSide(world)) {
			if (!(entityIn instanceof PlayerEntity)) {
				return;
			}
			// set to activated
			world.setBlock(pos, defaultBlockState().setValue(ACTIVATED, Boolean.valueOf(true)), 3);
			// initiate fall
			fall(world.getBlockState(pos), (ServerWorld)world, pos, null);
		}
	}

	/**
	 * 
	 * @param state
	 * @param worldIn
	 * @param pos
	 * @param rand
	 */
	public void fall(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
		// ensure the block is activated
		if (!worldIn.getBlockState(pos).getValue(ACTIVATED)) {
			return;
		}
		super.fall(state, worldIn, pos, null);
	}
	
	/**
	 * 
	 */
	public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		// do nothing
	}
}

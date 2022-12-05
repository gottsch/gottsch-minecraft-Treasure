/**
 * 
 */
package mod.gottsch.forge.treasure2.core.block;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * Does NOT appear in any creative tab.
 * @author Mark Gottschling on Jun 26, 2018
 *
 */
public class WitherChestTopBlock extends ModBlock implements ITreasureChestProxy, ITreasureBlock {
	protected static final VoxelShape AABB =  Block.box(1, 0, 1, 15, 10, 15);
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties 
	 */
	public WitherChestTopBlock(String modID, String name, Properties properties) {
		super(modID, name, properties);
	}

	/**
	 * Return the position of the chest.
	 */
	@Override
	public BlockPos getChestPos(BlockPos pos) {
		return pos.below();
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}
	
// DONT NEED
//	@Override
//	public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
//		// TODO Auto-generated method stub
//		Treasure.LOGGER.info("wither chest TOP clicked");		
//		onBlockActivated(state, worldIn, pos, player, Hand.OFF_HAND, null);
//	}

	/**
	 * 
	 */
	@Override
	public ActionResultType use(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		Treasure.LOGGER.info("wither chest TOP activated");
		// get the block at pos.down()
		BlockState bottomState = world.getBlockState(pos.below());
		return bottomState.getBlock().use(bottomState, world, pos.below(), player, hand, hit);
	}
	
	/**
	 * 
	 */
	@Override
	public void destroy(IWorld world, BlockPos pos, BlockState state) {
		Treasure.LOGGER.debug("Breaking Wither Chest Top block....!");
		BlockPos downPos = pos.below();
		// destory placeholder above
		Block downBlock = world.getBlockState(downPos).getBlock();
		if (downBlock == TreasureBlocks.WITHER_CHEST) {
			Block.updateOrDestroy(state, Blocks.AIR.defaultBlockState(), world, downPos, 3);
		}
	}
	
	/**
	 * 
	 */
//	@Override
//	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
//		super.onBlockDestroyedByPlayer(worldIn, pos, state);
//		BlockPos downPos = pos.down();
//		// destory placeholder above
//		Block downBlock = worldIn.getBlockState(downPos).getBlock();
//		if (downBlock == TreasureBlocks.WITHER_CHEST) {
//			downBlock.onBlockDestroyedByPlayer(worldIn, downPos, state);
//			worldIn.setBlockToAir(downPos);
//		}
//	}
	
	/**
	 * 
	 */
//	@Override
//	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
//		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
//		BlockPos downPos = pos.down();
//		// destory placeholder above
//		Block downBlock = worldIn.getBlockState(downPos).getBlock();
//		if (downBlock == TreasureBlocks.WITHER_CHEST) {
//			downBlock.onBlockDestroyedByExplosion(worldIn, downPos, explosionIn);
//			worldIn.setBlockToAir(downPos);
//		}
//	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		// if this is called somehow, return and empty list for the drops
        return Collections.emptyList();
	}
	
	   /**
	    * Spawns the block's drops in the world. By the time this is called the Block has possibly been set to air via
	    * Block.removedByPlayer
	    */
	   public void harvestBlock(World worldIn, PlayerEntity player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		   // do nothing - prevents spawnDrops from being called.
	   }
	
	/**
	 * 
	 */
//    @Override
//    public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
//    	return false;
//    }

	/**
	 * Render using a TESR.
	 */
	@Override
	public BlockRenderType getRenderShape(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}
}

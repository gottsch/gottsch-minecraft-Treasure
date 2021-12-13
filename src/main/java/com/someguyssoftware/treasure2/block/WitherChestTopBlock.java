/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.world.level.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;

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
	public VoxelShape getShape(BlockState state, LevelAccessor worldIn, BlockPos pos, CollisionContext context) {
		return AABB;
	}
	
// DONT NEED
//	@Override
//	public void onBlockClicked(BlockState state, Level worldIn, BlockPos pos, Player player) {
//		// TODO Auto-generated method stub
//		Treasure.LOGGER.info("wither chest TOP clicked");		
//		onBlockActivated(state, worldIn, pos, player, InteractionHand.OFF_HAND, null);
//	}

	/**
	 * 
	 */
	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockRayTraceResult hit) {
		Treasure.LOGGER.info("wither chest TOP activated");
		// get the block at pos.down()
		BlockState bottomState = world.getBlockState(pos.below());
		return bottomState.getBlock().use(bottomState, world, pos.below(), player, hand, hit);
	}
	
	/**
	 * 
	 */
	@Override
	public void destroy(ILevel world, BlockPos pos, BlockState state) {
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
//	public void onBlockDestroyedByPlayer(Level worldIn, BlockPos pos, IBlockState state) {
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
//	public void onBlockDestroyedByExplosion(Level worldIn, BlockPos pos, Explosion explosionIn) {
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
	   public void harvestBlock(Level worldIn, Player player, BlockPos pos, BlockState state, @Nullable TileEntity te, ItemStack stack) {
		   // do nothing - prevents spawnDrops from being called.
	   }
	
	/**
	 * 
	 */
//    @Override
//    public boolean isNormalCube(BlockState state, LevelAccessor world, BlockPos pos) {
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

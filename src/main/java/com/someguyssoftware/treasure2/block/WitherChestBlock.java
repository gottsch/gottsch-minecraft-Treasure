/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jun 19, 2018
 *
 */
public class WitherChestBlock extends StandardChestBlock {

	/**
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 * @param rarity
	 * @param properties 
	 */
	public WitherChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity, Properties properties) {
		super(modID, name, te, type, rarity, properties);
	}

	@Override
	public void onBlockAdded(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onBlockAdded(state, worldIn, pos, oldState, isMoving);

		// add the placeholder block above
		worldIn.setBlockState(pos.up(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState(), 3);
	}
	
	/**
	 * 
	 */
	 @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		// add the placeholder block above
		worldIn.setBlockState(pos.up(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState(), 3);
	}
	 
	/**
	 * Check if the wither chest (double high) can be placed at location.
	 */
	 @Override
	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
		// TODO Auto-generated method stub
		return super.isReplaceable(state, useContext);
	}
	 

	 @Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		 // TODO hopefully this prevents block from being placed if it can't
		BlockState blockState = context.getWorld().getBlockState(context.getPos().up());
		// TODO wrap these check in BlockContext
		if (blockState.isAir() && blockState.getMaterial().isReplaceable()) {
			return super.getStateForPlacement(context);
		}
		return null;
	}
	
	@Override
//	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
	public void onReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {

		// destory placeholder above
		BlockPos upPos = pos.up();
		Block topBlock = world.getBlockState(upPos).getBlock();
		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP) {
//			topBlock.onBlockDestroyedByPlayer(world, upPos, state);
//			world.setBlockToAir(upPos);
			Block.replaceBlock(world.getBlockState(upPos), Blocks.AIR.getDefaultState(), world, upPos, 3);
		}
		// break as normal
//		super.breakBlock(world, pos, state);
		super.onReplaced(state, world, pos, newState, isMoving);
	}

}

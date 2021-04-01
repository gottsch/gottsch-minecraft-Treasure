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
import net.minecraft.world.IWorld;
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
	public void onPlace(BlockState state, World worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
		super.onPlace(state, worldIn, pos, oldState, isMoving);

		// add the placeholder block above
		worldIn.setBlock(pos.above(), TreasureBlocks.WITHER_CHEST_TOP.defaultBlockState(), 3);
	}
	
	/**
	 * 
	 */
	 @Override
	public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(worldIn, pos, state, placer, stack);
		
		// TODO Check if the wither chest (double high) can be placed at location.
		// need to go above and check if == air
		// however if fails will not return boolean... therefor the check must take place in the Item
		
		// add the placeholder block above
		worldIn.setBlock(pos.above(), TreasureBlocks.WITHER_CHEST_TOP.defaultBlockState(), 3);
	}
	 
	/**
	 * Check if the wither chest (double high) can be placed at location.
	 */
//	 @Override
//	public boolean isReplaceable(BlockState state, BlockItemUseContext useContext) {
//		// TODO Auto-generated method stub
//		return super.isReplaceable(state, useContext);
//	}
	 

	 @Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {

		 // TODO hopefully this prevents block from being placed if it can't
		BlockState blockState = context.getLevel().getBlockState(context.getClickedPos().above());
		// TODO wrap these check in BlockContext
		if (blockState.isAir() && blockState.getMaterial().isReplaceable()) {
			return super.getStateForPlacement(context);
		}
		return null;
	}
	
	@Override
	public void destroy(IWorld world, BlockPos pos, BlockState state) {
		// destory placeholder above
		BlockPos upPos = pos.above();
		Block topBlock = world.getBlockState(upPos).getBlock();
		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP) {
			Block.updateOrDestroy(world.getBlockState(upPos), Blocks.AIR.defaultBlockState(), world, upPos, 3);
		}
		// break as normal
		super.destroy(world, pos, state);
	}

}

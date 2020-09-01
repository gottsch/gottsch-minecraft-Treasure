/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jun 19, 2018
 *
 */
public class WitherChestBlock extends TreasureChestBlock {

	/**
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 * @param rarity
	 */
	public WitherChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, te, type, rarity);
	}

	/**
	 * @param modID
	 * @param name
	 * @param material
	 * @param te
	 * @param type
	 * @param rarity
	 */
	public WitherChestBlock(String modID, String name, Material material, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, material, te, type, rarity);
	}
    
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		super.onBlockAdded(worldIn, pos, state);
		// add the placeholder block above
		worldIn.setBlockState(pos.up(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState(), 3);
	}
	
	/**
	 * 
	 */
	 @Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		// add the placeholder block above
		worldIn.setBlockState(pos.up(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState(), 3);
	}
	 
	/**
	 * Check if the wither chest (double high) can be placed at location.
	 */
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		boolean canPlaceAt = super.canPlaceBlockAt(worldIn, pos);
		
		if (canPlaceAt) {
			Cube cube = new Cube(worldIn, new Coords(pos.up()));
			
			// check if the position above the chest is air or replacable
			if (!cube.isAir() && !cube.isReplaceable()) {
				canPlaceAt = false;
			}
		}		
		return canPlaceAt;
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// destory placeholder above
		BlockPos upPos = pos.up();
		Block topBlock = worldIn.getBlockState(upPos).getBlock();
		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP) {
			topBlock.onBlockDestroyedByPlayer(worldIn, upPos, state);
			worldIn.setBlockToAir(upPos);
		}
		// break as normal
		super.breakBlock(worldIn, pos, state);
	}
	
	/**
	 * 
	 */
//	@Override
//	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
//		super.onBlockDestroyedByPlayer(worldIn, pos, state);
//		// destory placeholder above
//		BlockPos upPos = pos.up();
//		Block topBlock = worldIn.getBlockState(upPos).getBlock();
//		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP) {
//			topBlock.onBlockDestroyedByPlayer(worldIn, upPos, state);
//			worldIn.setBlockToAir(upPos);
//		}
//	}
	
	/**
	 * 
	 */
//	@Override
//	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
//		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
//		// destory placeholder above
//		BlockPos upPos = pos.up();
//		Block topBlock = worldIn.getBlockState(upPos).getBlock();
//		if (topBlock == TreasureBlocks.WITHER_CHEST_TOP) {
//			topBlock.onBlockDestroyedByExplosion(worldIn, upPos, explosionIn);
//			worldIn.setBlockToAir(upPos);
//		}
//	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Does NOT appear in any creative tab.
 * @author Mark Gottschling on Jun 26, 2018
 *
 */
public class WitherChestTopBlock extends ModBlock implements ITreasureBlock {
	protected static final AxisAlignedBB AABB =  new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.9375D, 0.9375D);
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public WitherChestTopBlock(String modID, String name) {
		this(modID, name, Material.WOOD);
	}
	
	/**
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherChestTopBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}

	/**
	 * @param modID
	 * @param name
	 * @param material
	 * @param mapColor
	 */
	public WitherChestTopBlock(String modID, String name, Material material, MapColor mapColor) {
		super(modID, name, material, mapColor);
	}

	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {

		// get the block at pos.down()
		Block witherChest = worldIn.getBlockState(pos.down()).getBlock();
		return witherChest.onBlockActivated(worldIn, pos.down(), state, playerIn, hand, facing, hitX, hitY, hitZ);
	}
	
	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		super.onBlockDestroyedByPlayer(worldIn, pos, state);
	}
	
	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		// TODO Auto-generated method stub
		super.onBlockDestroyedByExplosion(worldIn, pos, explosionIn);
	}
	
	
	/**
	 * 
	 */
    @java.lang.Override
    public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
    	return false;
    }

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	/**
	 * Render using a TESR.
	 */
	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.INVISIBLE;
	}
}

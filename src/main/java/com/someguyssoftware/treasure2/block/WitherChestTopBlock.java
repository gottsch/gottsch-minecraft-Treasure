/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
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
public class WitherChestTopBlock extends ModBlock implements ITreasureChestProxy, ITreasureBlock {
	protected static final AxisAlignedBB AABB =  new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.6563D, 0.9375D);
	
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
	 * Return the position of the chest.
	 */
	@Override
	public BlockPos getChestPos(BlockPos pos) {
		return pos.down();
	}
	
	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
//		return NULL_AABB;
	}

    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
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
	
	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		BlockPos downPos = pos.down();
		// destory placeholder above
		Block downBlock = worldIn.getBlockState(downPos).getBlock();
		if (downBlock == TreasureBlocks.WITHER_CHEST) {
			downBlock.breakBlock(worldIn, downPos, worldIn.getBlockState(downPos));
			worldIn.setBlockToAir(downPos);
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
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

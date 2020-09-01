/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.tileentity.AbstractProximityTileEntity;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * An invisible non-collision block (like AIR) that creates an IProximityTileEntity.
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class ProximityBlock extends ModBlock implements ITileEntityProvider {
	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<? extends AbstractProximityTileEntity> tileEntityClass;
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public ProximityBlock(String modID, String name, Class<? extends AbstractProximityTileEntity> te) {
		this(modID, name, Material.AIR);
		setTileEntityClass(te);
	}
	
	/**
	 * @param modID
	 * @param name
	 * @param material
	 */
	private ProximityBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}

	/**
	 * @param modID
	 * @param name
	 * @param material
	 * @param mapColor
	 */
	private ProximityBlock(String modID, String name, Material material, MapColor mapColor) {
		super(modID, name, material, mapColor);
	}

	/**
	 * Create the proximity tile entity.
	 */
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		AbstractProximityTileEntity proximityTileEntity = null;
		try {
			proximityTileEntity = (AbstractProximityTileEntity) getTileEntityClass().newInstance();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return (TileEntity) proximityTileEntity;
	}

	/**
	 * 
	 */
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.INVISIBLE;
    }
    
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return NULL_AABB;
    }
    
    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    /**
     * 
     */
    public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
        return false;
    }
    
    /**
     * Spawns this Block's drops into the World as EntityItems.
     */
    public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }

    /**
     * 
     */
    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
        return BlockFaceShape.UNDEFINED;
    }
    
    /**
     * 
     */
    public boolean isFullCube(IBlockState state) {
        return false;
    }
    
	/**
	 * @return the tileEntityClass
	 */
	public Class<? extends AbstractProximityTileEntity> getTileEntityClass() {
		return tileEntityClass;
	}

	/**
	 * @param tileEntityClass the tileEntityClass to set
	 */
	public void setTileEntityClass(Class<? extends AbstractProximityTileEntity> tileEntityClass) {
		this.tileEntityClass = tileEntityClass;
	}

}
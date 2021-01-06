/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.gottschcore.tileentity.AbstractProximityTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

/**
 * An invisible non-collision block (like AIR) that creates an IProximityTileEntity.
 * @author Mark Gottschling on Jan 17, 2019
 *
 */
public class ProximityBlock<E extends AbstractProximityTileEntity> extends ModBlock implements ITileEntityProvider {
	/*
	 *  the class of the tileEntityClass this BlockChest should use.
	 */
	private Class<? extends AbstractProximityTileEntity> tileEntityClass;

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public ProximityBlock(String modID, String name, Class<? extends AbstractProximityTileEntity> te, Block.Properties properties) {
		super(modID, name, properties);
		setTileEntityClass(te);
	}

	/**
	 * Create the proximity tile entity.
	 */
	@Override
	public TileEntity createNewTileEntity(IBlockReader worldIn) {
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
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.INVISIBLE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		return VoxelShapes.empty();
	}

	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	public boolean isAir(BlockState state) {
		return true;
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
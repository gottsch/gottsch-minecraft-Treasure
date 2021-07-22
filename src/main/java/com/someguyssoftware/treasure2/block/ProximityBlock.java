/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.tileentity.TreasureProximitySpawnerTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
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
public class ProximityBlock extends ModBlock {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param properties
	 */
	public ProximityBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
	}
	
	/**
	 * Create the proximity tile entity.
	 */
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader worldIn) {
		TreasureProximitySpawnerTileEntity proximityTileEntity = null;
//		Treasure.LOGGER.debug("creating proximity block tile entity...");
		try {
			proximityTileEntity = new TreasureProximitySpawnerTileEntity();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
//		Treasure.LOGGER.debug("created proximity te -> {}", proximityTileEntity.getClass().getSimpleName());
		return (TileEntity) proximityTileEntity;
	}
	
	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Override
   public BlockRenderType getRenderShape(BlockState state) {
	      return BlockRenderType.INVISIBLE;
	   }
	   
	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos,
			ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public boolean isAir(BlockState state) {
		return true;
	}
}
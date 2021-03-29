/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.FacingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * 
 * @author Mark Gottschling on Mar 26, 2018
 *
 */
public class WitherBranchBlock extends FacingBlock implements ITreasureBlock {

	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] shapes = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherBranchBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties.sound(SoundType.WOOD).strength(3.0F));
		setShapes(new VoxelShape[] {
				Block.box(4, 0, 0, 12, 8, 16),	// S
				Block.box(0, 0, 4, 16, 8, 12),	// W
				Block.box(4, 0, 0, 12, 8, 16),	// N
				Block.box(0, 0, 4, 16, 8, 12)	// E
		});
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.getValue(FACING)) {
		default:
		case NORTH:
			return shapes[0];
		case EAST:
			return shapes[1];
		case SOUTH:
			return shapes[2];
		case WEST:
			return shapes[3];
		}
	}
	
	/**
	 * 
	 */
//	@Override
//	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
//		return false;
//	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getShapes() {
		return shapes;
	}

	public WitherBranchBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}

}
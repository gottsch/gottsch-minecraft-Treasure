package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.FacingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

/**
 * 
 * @author Mark Gottschling on May 30, 2018
 *
 */
public class WitherBrokenLogBlock extends FacingBlock implements ITreasureBlock {
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	VoxelShape[] shapes = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public WitherBrokenLogBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties.strength(3.0F).sound(SoundType.WOOD));

		setShapes(
				new VoxelShape[] {
					Block.box(0, 0, 0, 1, 0.85, 1), 	// N
					Block.box(0, 0, 0, 1, 0.85, 1),  	// E
					Block.box(0, 0, 0, 1, 0.85, 1),  	// S
					Block.box(0, 0, 0, 1, 0.85, 1) 	// W)
				}
			);
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

	public WitherBrokenLogBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}
}

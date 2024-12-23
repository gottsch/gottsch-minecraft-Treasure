package mod.gottsch.forge.treasure2.core.block;

import mod.gottsch.forge.gottschcore.block.FacingBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * 
 * @author Mark Gottschling on May 30, 2018
 *
 */
public class WitherBrokenLogBlock extends FacingBlock implements ITreasureBlock {
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] shapes = new VoxelShape[4];
	
	/**
	 * 
	 */
	public WitherBrokenLogBlock(Block.Properties properties) {
		super(properties.strength(3.0F).sound(SoundType.WOOD));

		// TODO make better shape
		setShapes(
				new VoxelShape[] {
					Block.box(1, 0, 3, 15, 13, 15), 	// N
					Block.box(1, 0, 1, 15, 13, 15),  	// E
					Block.box(1, 0, 1, 16, 13, 13),  	// S
					Block.box(1, 0, 1, 15, 13, 15) 	// W)
				}
			);
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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

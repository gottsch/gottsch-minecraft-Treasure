package mod.gottsch.forge.treasure2.core.block;

import com.someguyssoftware.gottschcore.block.FacingBlock;

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
	 * @param modID
	 * @param name
	 */
	public WitherBrokenLogBlock(Block.Properties properties) {
		super(properties.strength(3.0F).sound(SoundType.WOOD));

		// TODO make better shape
		setShapes(
				new VoxelShape[] {
					Block.box(0, 0, 0, 16, 16, 16), 	// N
					Block.box(0, 0, 0, 16, 16, 16),  	// E
					Block.box(0, 0, 0, 16, 16, 16),  	// S
					Block.box(0, 0, 0, 16, 16, 16) 	// W)
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

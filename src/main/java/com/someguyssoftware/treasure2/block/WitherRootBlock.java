/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.FacingBlock;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.server.ServerWorld;

/**
 * 
 * @author Mark Gottschling on Mar 26, 2018
 *
 */
public class WitherRootBlock extends FacingBlock implements ITreasureBlock {
	public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");
	
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
	public WitherRootBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties.sound(SoundType.WOOD).strength(3.0F));
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, Boolean.valueOf(false)));
		setShapes(
			new VoxelShape[] {
					Block.box(3, 0, 0, 13, 4, 15),	// S
					Block.box(0, 0, 4, 15, 4, 12),	// W
					Block.box(3, 0, 0, 13, 4, 15),	// N
					Block.box(0, 0, 4, 15, 4, 12)	// E
			});
	}
	
	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED, FACING);
	}
	
	@Override
	public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
//		if (WorldInfo.isServerSide(world)) {
//			return;
//		}
//
//		if (!TreasureConfig.WORLD_GEN.getGeneralProperties().enablePoisonFog) {
//			return;
//		}
//
//		if (!state.getValue(ACTIVATED)) {
//			return;
//		}
//
//		int x = pos.getX();
//		int y = pos.getY();
//		int z = pos.getZ();
//
//		boolean isCreateParticle = checkTorchPrevention(world, random, x, y, z);
//		if (!isCreateParticle) {
//			return;
//		}
//
//		double xPos = (x + 0.5D) + (random.nextFloat() * 2.0D) - 1D;
//		double yPos = y + 0.125;
//		double zPos = (z + 0.5D) + (random.nextFloat() * 2.0D) - 1D;
//
//		// initial velocities
//		double velocityX = 0;
//		double velocityY = 0;
//		double velocityZ = 0;
//
//		AbstractMistParticle mistParticle = new WitherMistParticle(world, xPos, yPos, zPos, velocityX, velocityY,
//				velocityZ, new Coords(pos));
//
//		// remember to init!
//		mistParticle.init();
//		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
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

	public WitherRootBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}

}
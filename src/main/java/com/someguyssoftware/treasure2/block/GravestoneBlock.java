/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.MistParticleData;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Jan 29, 2018
 *
 */
public class GravestoneBlock extends FacingBlock implements ITreasureBlock, IMistSupport {

	/*
	 * An array of VoxelShape bounds for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		
		// set the default bounds/shape (full block)
		VoxelShape shape = Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
		setBounds(
				new VoxelShape[] {
						shape, 	// N
						shape,  	// E
						shape,  	// S
						shape	// W
				});
	}

	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		switch(state.get(FACING)) {
		default:
		case NORTH:
			return bounds[0];
		case EAST:
			return bounds[1];
		case SOUTH:
			return bounds[2];
		case WEST:
			return bounds[3];
		}
	}
	
	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context) {
		BlockState blockState = this.getDefaultState().with(FACING,
				context.getPlacementHorizontalFacing().getOpposite());
		return blockState;
	}
	
	/**
	 * 
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
	
	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

//		if (!TreasureConfig.GENERAL.enableFog) {
//			return;
//		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean isCreateParticle = checkTorchPrevention(world, random, x, y, z);
		if (!isCreateParticle) {
			return;
		}

		// initial positions - has a spread area of up to 1.5 blocks
		double xPos = (x + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
		double yPos = y;
		// + state.getBoundingBox(world, pos).maxY; // + 1.0;
		double zPos = (z + 0.5D) + (random.nextFloat() * 3.0) - 1.5D;
		// initial velocities
		double velocityX = 0;
		double velocityY = 0;
		double velocityZ = 0;
		
		final boolean IGNORE_RANGE_CHECK = false; // if true, always render particle regardless of how far away the player is
		
		// create particle
//		AbstractMistParticle mistParticle = null;

//		if (RandomHelper.checkProbability(random, 80)) {
//			mistParticle = new MistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ, new Coords(pos));
//		} else {
//			mistParticle = new BillowingMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
//					new Coords(pos));
//		}
		MistParticleData mistParticleData = new MistParticleData(new Coords(pos));
//		mistParticle.init();

//		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
		try {
			world.addParticle(mistParticleData, IGNORE_RANGE_CHECK, xPos, yPos, zPos, velocityX, velocityY, velocityZ);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}

	/*
	 * This is for animateTick()
	 */	
	@Override
	public int tickRate(IWorldReader worldIn) {
		// TODO Auto-generated method stub
		return 10;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withRotation(Rotation)} whenever possible. Implementing/overriding is
	 * fine.
	 */
	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 * @deprecated call via {@link IBlockState#withMirror(Mirror)} whenever possible. Implementing/overriding is fine.
	 */
	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn) {
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getBounds() {
		return bounds;
	}

	public GravestoneBlock setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}
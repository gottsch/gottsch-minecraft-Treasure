/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.particle.TreasureParticles;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.IParticleData;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.server.ServerLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Mar 26, 2018
 *
 */
public class WitherRootBlock extends FacingBlock implements ITreasureBlock, IMistSupport {
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
protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(ACTIVATED, FACING);
	}

	/**
	 * NOTE animateTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, Random random) {

		if (!TreasureConfig.FOG.enableFog.get()) {
			return;
		}

		if (!state.getValue(ACTIVATED)) {
			return;
		}

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean isCreatable = checkTorchPrevention(world, random, x, y, z);
		if (!isCreatable) {
			return;
		}

		double xPos = (x + 0.5D) + (random.nextFloat() * 2.0D) - 1D;
		double yPos = y + 0.125;
		double zPos = (z + 0.5D) + (random.nextFloat() * 2.0D) - 1D;

		// NOTE can override methods here as it is a factory that creates the particle
		IParticleData mistType = TreasureParticles.WITHER_MIST_PARTICLE_TYPE.get();
		
		try {
			world.addParticle(mistType, false, xPos, yPos, zPos, 0, 0, 0);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, LevelAccessor worldIn, BlockPos pos, CollisionContext context) {
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

	public WitherRootBlock setShapes(VoxelShape[] shapes) {
		this.shapes = shapes;
		return this;
	}

}
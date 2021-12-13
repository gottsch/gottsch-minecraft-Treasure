/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.particle.TreasureParticles;
import com.someguyssoftware.treasure2.particle.data.CollidingParticleType;
import com.someguyssoftware.treasure2.tileentity.MistEmitterBlockEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Jun 6, 2018
 *
 */
@SuppressWarnings("deprecation")
public class WitherSoulLog extends FacingBlock implements ITreasureBlock, IMistSupport, ITileEntityProvider {
	public static final EnumProperty<Appearance> APPEARANCE = EnumProperty.create("appearance", Appearance.class);
	/*
	 * An array of VoxelShape shapes for the bounding box
	 */
	private VoxelShape[] bounds = new VoxelShape[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherSoulLog(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
		registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(APPEARANCE, Appearance.NONE));
		
		// set the default shapes/shape (full block)
		VoxelShape shape = Block.box(0, 0, 0, 16, 16, 16);
		setBounds(
				new VoxelShape[] {
						shape, 	// N
						shape,  	// E
						shape,  	// S
						shape	// W
				});
	}

	@Override
	public TileEntity newBlockEntity(LevelAccessor world) {
		return new MistEmitterTileEntity(TreasureTileEntities.MIST_EMITTER_TILE_ENTITY_TYPE);		
	}

	/**
	 * 
	 */
	@Override
protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(APPEARANCE, FACING);
	}

	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, LevelAccessor worldIn, BlockPos pos, CollisionContext context) {
		switch(state.getValue(FACING)) {
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
		BlockState blockState = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
		return blockState;
	}

	   @OnlyIn(Dist.CLIENT)
	   public void animateTick(BlockState stateIn, Level world, BlockPos pos, Random random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

		if (!TreasureConfig.FOG.enableFog.get()) {
			return;
		}
		
		// get the appearance property
		Appearance appearance =stateIn.getValue(APPEARANCE);

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		boolean isCreatable = checkTorchPrevention(world, random, x, y, z);
		if (!isCreatable) {
			return;
		}

		// check appearance - if face block, then particles need to be generated on
		// ground and further away (since main tree is 2x2 appearance affects how
		// wide to spawn the mist (because the main tree is 2x2, and the soul log is
		// higher). (this is hard-coded knowledge - bad)
		double xPos = 0;
		double yPos = y;
		double zPos = 0;

		if (appearance == Appearance.FACE) {
			// initial positions - has a spread area of up to 2.5 blocks radius
			double xOffset = (random.nextFloat() * 5.0D) - 2.5D;
			double zOffset = (random.nextFloat() * 5.0D) - 2.5D;

			if (xOffset > 0D && (zOffset < 0.5D || zOffset > -1.5D)) {
				xOffset = Math.max(1.5D, xOffset);
			} else if (xOffset < 0D && (zOffset < 0.5D || zOffset > -1.5D)) {
				xOffset = Math.min(-0.5D, xOffset);
			}
			xPos = (x + 0.5D) + xOffset;
			zPos = (z + 0.5D) + zOffset;

			// y is 2 blocks down as the face it up the trunk
			yPos = y - 1.875D;
		} else {
			// initial positions - has a spread area of up to 2.5 blocks
			xPos = (x + 0.5D) + (random.nextFloat() * 5.0D) - 2.5D;
			zPos = (z + 0.5D) + (random.nextFloat() * 5.0D) - 2.5D;
		}

		CollidingParticleType mistType = null;
		if (appearance == Appearance.FACE) {
			mistType = TreasureParticles.WITHER_MIST_PARTICLE_TYPE.get();		
		} else {
			mistType = TreasureParticles.POISON_MIST_PARTICLE_TYPE.get();			
		}
		
		try {
			mistType.setSourceCoords(new Coords(x, y, z));
			world.addParticle(mistType, false, xPos, yPos, zPos, 0, 0, 0);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error with particle:", e);
		}
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Jun 7, 2018
	 *
	 */
	public static enum Appearance implements IStringSerializable {
		NONE("none", 0), FACE("face", 1);

		private final String name;
		private final int value;

		private Appearance(String name, int value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @return the value
		 */
		public int getValue() {
			return value;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public VoxelShape[] getBounds() {
		return bounds;
	}

	public WitherSoulLog setBounds(VoxelShape[] bounds) {
		this.bounds = bounds;
		return this;
	}
}

package mod.gottsch.forge.treasure2.core.block;

import javax.annotation.Nullable;

import mod.gottsch.forge.gottschcore.block.FacingBlock;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.entity.MistEmitterBlockEntity;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.particle.CollidingParticleType;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 
 * @author Mark Gottschling on Jun 6, 2018
 *
 */
@SuppressWarnings("deprecation")
public class WitherSoulLog extends FacingBlock implements ITreasureBlock, IMistSupport, EntityBlock {
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
	public WitherSoulLog(Block.Properties properties) {
		super(properties);
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
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MistEmitterBlockEntity(pos, state);		
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		if (level.isClientSide()) {
			return (lvl, pos, blockState, t) -> {
				if (t instanceof MistEmitterBlockEntity entity) { // test and cast
					entity.tickClient();
				}
			};
		}
		return null;
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
	public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
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
	public BlockState getStateForPlacement(BlockPlaceContext context) {		BlockState blockState = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
	return blockState;
	}

	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

		if (!Config.CLIENT.gui.enableFog.get()) {
			return;
		}

		// get the appearance property
		Appearance appearance =state.getValue(APPEARANCE);

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		if (!isMistAllowed(world, random, x, y, z)) {
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
			mistType = TreasureParticles.WITHER_MIST_PARTICLE.get();		
		} else {
			mistType = TreasureParticles.POISON_MIST_PARTICLE.get();			
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
	public static enum Appearance  implements StringRepresentable {
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
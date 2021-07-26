/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.FacingBlock;
import com.someguyssoftware.treasure2.tileentity.MistEmitterTileEntity;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

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
	public TileEntity newBlockEntity(IBlockReader world) {
		return new MistEmitterTileEntity(TreasureTileEntities.MIST_EMITTER_TILE_ENTITY_TYPE);		
	}

	/**
	 * 
	 */
	@Override
	protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
		builder.add(APPEARANCE, FACING);
	}

	/**
	 * 
	 */
	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
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
	
	/**
	 * 
	 */
//	@Override
//	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
//		return false;
//	}
	
	/**
	 * NOTE randomDisplayTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
//	   @OnlyIn(Dist.CLIENT)
//	   public void animateTick(BlockState stateIn, World world, BlockPos pos, Random rand) {
//		if (WorldInfo.isServerSide(world)) {
//			return;
//		}
//
//		if (!TreasureConfig.GENERAL.enablePoisonFog) {
//			return;
//		}
//		// get the appearance property
//		Appearance appearance = state.getValue(APPEARANCE);
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
//		// check appearance - if face block, then particles need to be generated on
//		// ground and further away (since main tree is 2x2 appearance affects how
//		// wide to spawn the mist (because the main tree is 2x2, and the soul log is
//		// higher). (this is hard-coded knowledge - bad)
//		double xPos = 0;
//		double yPos = y;
//		double zPos = 0;
//
//		if (appearance == Appearance.FACE) {
//			// initial positions - has a spread area of up to 2.5 blocks radius
//			double xOffset = (random.nextFloat() * 5.0D) - 2.5D;
//			double zOffset = (random.nextFloat() * 5.0D) - 2.5D;
//
//			if (xOffset > 0D && (zOffset < 0.5D || zOffset > -1.5D)) {
//				xOffset = Math.max(1.5D, xOffset);
//			} else if (xOffset < 0D && (zOffset < 0.5D || zOffset > -1.5D)) {
//				xOffset = Math.min(-0.5D, xOffset);
//			}
//			xPos = (x + 0.5D) + xOffset;
//			zPos = (z + 0.5D) + zOffset;
//
//			// y is 2 blocks down as the face it up the trunk
//			yPos = y - 1.875D;
//		} else {
//			// initial positions - has a spread area of up to 2.5 blocks
//			xPos = (x + 0.5D) + (random.nextFloat() * 5.0D) - 2.5D;
//			zPos = (z + 0.5D) + (random.nextFloat() * 5.0D) - 2.5D;
//		}
//
//		// initial velocities
//		double velocityX = 0;
//		double velocityY = 0;
//		double velocityZ = 0;
//
//		AbstractMistParticle mistParticle = null;
//		if (appearance == Appearance.FACE) {
//			mistParticle = new WitherMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
//					new Coords(pos));
//		} else {
//			mistParticle = new PoisonMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
//					new Coords(pos));
//		}
//		// remember to init!
//		mistParticle.init();
//		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
//	}
	
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
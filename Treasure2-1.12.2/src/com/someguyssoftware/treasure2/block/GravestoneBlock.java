/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.particle.AbstractMistParticle;
import com.someguyssoftware.treasure2.particle.BillowingMistParticle;
import com.someguyssoftware.treasure2.particle.MistParticle;
import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerTileEntity;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Jan 29, 2018
 *
 */
public class GravestoneBlock extends CardinalDirectionFacadeBlock
		implements ITreasureBlock, /* IFogSupport, */ IMistSupport, ITileEntityProvider {

	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneBlock(String modID, String name, Material material) {
		super(modID, name, material);
		setSoundType(SoundType.STONE);
		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);
//		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(HAS_ENTITY,
//				Boolean.valueOf(false)));

		setBoundingBox(new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), // S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F) // W
		);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		GravestoneProximitySpawnerTileEntity tileEntity = new GravestoneProximitySpawnerTileEntity();
		return (TileEntity) tileEntity;
	}

	/**
	 * 
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// check the 4x4x4 area and set all fog blocks to CHECK_DECAY = true
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// if all the blocks in the immediate area are loaded
		if (worldIn.isAreaLoaded(new BlockPos(x - 5, y - 5, z - 5), new BlockPos(x + 5, y + 5, z + 5))) {
			// use a MutatableBlockPos instead of Cube\Coords or BlockPos to say the
			// recreation of many objects
			BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();

			for (int x1 = -4; x1 <= 4; ++x1) {
				for (int y1 = -4; y1 <= 4; ++y1) {
					for (int z1 = -4; z1 <= 4; ++z1) {
						// that just checks a value.
						IBlockState inspectBlockState = worldIn.getBlockState(mbp.setPos(x + x1, y + y1, z + z1));
//						Block inspectBlock = inspectBlockState.getBlock();
						if (inspectBlockState.getMaterial() == TreasureItems.FOG) {
							worldIn.setBlockState(mbp, inspectBlockState.withProperty(FogBlock.CHECK_DECAY, true));
//							Treasure.logger.debug("Setting fog block @ {} to check decay = true", mbp);
						}
					}
				}
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	/**
	 * NOTE randomDisplayTick is on the client side only. The server is not keeping
	 * track of any particles NOTE cannot control the number of ticks per
	 * randomDisplayTick() call - it is not controlled by tickRate()
	 */
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
		if (WorldInfo.isServerSide(world)) {
			return;
		}

		if (!TreasureConfig.WORLD_GEN.getGeneralProperties().enableFog) {
			return;
		}

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

		// create particle
		AbstractMistParticle mistParticle = null;

		if (RandomHelper.checkProbability(random, 80)) {
			mistParticle = new MistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ, new Coords(pos));
		} else {
			mistParticle = new BillowingMistParticle(world, xPos, yPos, zPos, velocityX, velocityY, velocityZ,
					new Coords(pos));
		}
		mistParticle.init();

		Minecraft.getMinecraft().effectRenderer.addEffect(mistParticle);
	}

	/*
	 * This is for updateTick()
	 */
	@Override
	public int tickRate(World worldIn) {
		return 10;
	}

	/**
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		} else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		} else {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}

	/**
	 * Determines if this block can prevent leaves connected to it from decaying.
	 * 
	 * @param state The current state
	 * @param world The current world
	 * @param pos   Block position in world
	 * @return true if the presence this block can prevent leaves from decaying.
	 */
//	@Override
//	public boolean canSustainFog(IBlockState state, IBlockAccess world, BlockPos pos) {
////		return true;
//		return false;
//	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
//	@Override
//	public IBlockState getStateFromMeta(int meta) {
//		IBlockState state = super.getStateFromMeta(meta);
//		return state.withProperty(HAS_ENTITY, Boolean.valueOf((meta & 8) > 0));
//	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
//	@Override
//	public int getMetaFromState(IBlockState state) {
//		int meta = 0;
//		meta = state.getValue(FACING).getIndex();
//		if (state.getValue(HAS_ENTITY))
//			meta += 8;
//		return meta;
//	}

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 * @return
	 */
	public GravestoneBlock setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
		return this;
	}
}

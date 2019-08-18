/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModFallingBlock;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.FogHeight;
import com.someguyssoftware.treasure2.enums.FogType;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling on Feb 27, 2018
 *
 */
public class FogBlock extends ModFallingBlock {
	public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	public static final PropertyBool CHECK_DECAY =	PropertyBool.create("check_decay");
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	/** These bounding boxe are used to check for entities in a certain area. */
	protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB HIGH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.75D, 1.0D);
	protected static final AxisAlignedBB MED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.5D, 1.0D);
	protected static final AxisAlignedBB LOW_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.25D, 1.0D);

	public FogType fogType;
	public FogHeight fogHeight;
	
	public Map<FogHeight, FogBlock> fogMap;

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public FogBlock(String modID, String name, Material material, Map<FogHeight, FogBlock> map) {
		super(modID, name, material);
		this.setTickRandomly(true);
		this.setDefaultState(blockState.getBaseState()
				.withProperty(DECAYABLE, Boolean.valueOf(true))
				.withProperty(CHECK_DECAY, Boolean.valueOf(true)) // was false
				.withProperty(ACTIVATED, Boolean.valueOf(false)));
		setNormalCube(false);
		setSoundType(SoundType.SNOW);
		setCreativeTab(Treasure.TREASURE_TAB);
		setFogType(FogType.NORMAL);
		setFogHeight(FogHeight.FULL_FOG);
		setFogMap(map);
	}

	/**
	 *
	 */
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (getFogHeight()) {
		case FULL_FOG:
			return FULL_AABB;
		case HIGH_FOG:
			return HIGH_AABB;
		case MEDIUM_FOG:
			return MED_AABB;
		case LOW_FOG:
			return LOW_AABB;
		default:
			return FULL_AABB;
		}
	}

	/**
	 * 
	 */
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
		worldIn.setBlockState(pos, getDefaultState());
		IBlockState def = worldIn.getBlockState(pos);
		//		Treasure.logger.debug("FogBlock Placed @ {}. CD: {}, D: {}", pos.toString(), state.getValue(CHECK_DECAY), state.getValue(DECAYABLE));
		Treasure.logger.debug("Default State FogBlock Placed @ {}. CD: {}, D: {}", pos.toString(), def.getValue(CHECK_DECAY), def.getValue(DECAYABLE));

	}

	/**
	 * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	 * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	 * block, etc.
	 */
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		//        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
		// check if block should fall
		this.checkFallable(worldIn, pos);
	}

	/**
	 * How many world ticks before ticking
	 */
	@Override
	public int tickRate(World worldIn) {
		return 2;
	}

    /**
     * Determines if an entity can path through this block
     */
	@Override
    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return true;
    }
    
	/**
	 * 
	 * @param state
	 * @return
	 */	
	public static boolean canFallThrough(IBlockState state) {
		Material material = state.getMaterial();
		return material == Material.AIR;
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @param rand
	 */
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {

		//		Treasure.logger.debug("updateTick on FogBlock @ {}. CD: {}, D: {}", pos.toString(), state.getValue(CHECK_DECAY), state.getValue(DECAYABLE));
		if (WorldInfo.isServerSide(worldIn)) {
			if ((Boolean)state.getValue(CHECK_DECAY) && (Boolean)state.getValue(DECAYABLE)
					&& ((Boolean)state.getValue(ACTIVATED))) {
				//				Treasure.logger.debug("Fog @ {} has CHECK_DECAY = true", pos.toString());

				boolean isSupported = false;

				int x = pos.getX();
				int y = pos.getY();
				int z = pos.getZ();

				// if all the blocks in the immediate area are loaded
				if (worldIn.isAreaLoaded(new BlockPos(x - 5, y - 5, z - 5), new BlockPos(x + 5, y + 5, z + 5))) {
					// use a MutatableBlockPos instead of Cube\Coords or BlockPos to say the recreation of many objects
					BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();
					// process all the blocks in a 4x4x4 area
					inspectBlockLoop:
						for (int x1 = -4; x1 <= 4; ++x1) {
							for (int y1 = -4; y1 <= 4; ++y1) {
								for (int z1 = -4; z1 <= 4; ++z1) {								
									// that just checks a value.
									IBlockState inspectBlockState = worldIn	.getBlockState(mbp.setPos(x + x1, y + y1, z + z1));
									Block inspectBlock = inspectBlockState.getBlock();

									// if the block is a torch, then destroy the fog and return
									if (inspectBlock instanceof BlockTorch) {
										worldIn.setBlockToAir(pos);
										return;
									}
									// if the block implements IFogSupport AND can support fog
									if (inspectBlock instanceof IFogSupport && ((IFogSupport)inspectBlock).canSustainFog(inspectBlockState, worldIn, mbp.setPos(x + x1, y + y1, z + z1))) {
										// update the block @ pos with CHECK_DECAY = fasle and break
										//                                	Treasure.logger.debug("Found a fog supporting block @ {}", mbp.toString());
										worldIn.setBlockState(pos, state.withProperty(CHECK_DECAY, (Boolean)false), 3);
										//                                	Treasure.logger.debug("Changed to CHECK_DECAY=false for fog block @ {}", pos.toString());
										isSupported = true;
										break inspectBlockLoop;
									}
									
								}
							}
						}

					if (!isSupported) {
						int fogCount = 0;
						/*
						 *  if less than 4 fog neighbors, then swap for the next level fog block (or air if at end).
						 */
						if (worldIn.getBlockState(pos.up()).getMaterial() == TreasureItems.FOG) fogCount++;
						if (worldIn.getBlockState(pos.down()).getMaterial() == TreasureItems.FOG) fogCount++;
						if (worldIn.getBlockState(pos.north()).getMaterial() == TreasureItems.FOG) fogCount++;
						if (worldIn.getBlockState(pos.south()).getMaterial() == TreasureItems.FOG) fogCount++;
						if (worldIn.getBlockState(pos.east()).getMaterial() == TreasureItems.FOG) fogCount++;
						if (worldIn.getBlockState(pos.west()).getMaterial() == TreasureItems.FOG) fogCount++;

						//                		Treasure.logger.debug("Has {} fog neighbors", fogCount);

						// update the block
						if (fogCount < 4) {
//							                			Treasure.logger.debug("Block with {} neighbors= {}", fogCount, state.getBlock().getRegistryName());
//							if (state.getBlock() == TreasureBlocks.FOG_BLOCK) {
							if (((FogBlock)state.getBlock()).getFogHeight() == FogHeight.FULL_FOG) {
								worldIn.setBlockState(pos, getFogMap().get(FogHeight.HIGH_FOG).getDefaultState().withProperty(CHECK_DECAY, (Boolean)true));
							}
							else if (((FogBlock)state.getBlock()).getFogHeight() == FogHeight.HIGH_FOG) {
								worldIn.setBlockState(pos, getFogMap().get(FogHeight.MEDIUM_FOG).getDefaultState().withProperty(CHECK_DECAY, (Boolean)true));
							}
							else if (((FogBlock)state.getBlock()).getFogHeight() == FogHeight.MEDIUM_FOG) {
								worldIn.setBlockState(pos, getFogMap().get(FogHeight.LOW_FOG).getDefaultState().withProperty(CHECK_DECAY, (Boolean)true));
							}
							else if (((FogBlock)state.getBlock()).getFogHeight() == FogHeight.LOW_FOG) {
								worldIn.setBlockToAir(pos);
								return;
							}
						}
					}				
				}
			}

			/*
			 * All fog block's states are CHECK_DECAY = true by default. ACTIVATED = false by default. They only change size if ACTIVATED = true. At the end 
			 * of the update() ACTIVATED is set to true. This is to prevent a fog block from immediately reducing in size after a fall or placement.
			 * Change the getStateFromMeta to do bitwise checks. ie. get first bit to check DECAYABLE, 2nd bit CHECK_DECAY,
			 * 3rd bit ACTIVATED.
			 */
			if ((Boolean)state.getValue(DECAYABLE) && !(Boolean)state.getValue(ACTIVATED)) {
//				Treasure.logger.debug("Activating block @ {}", pos);
				worldIn.setBlockState(pos, state.withProperty(ACTIVATED, (Boolean)true), 3);
			}
			
			//			// check if block should fall
			//            this.checkFallable(worldIn, pos);
		}
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 */
	private void checkFallable(World worldIn, BlockPos pos) {
		if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
			int i = 32;

			if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (WorldInfo.isServerSide(worldIn)) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D,
							(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos).withProperty(CHECK_DECAY,(Boolean)true).withProperty(ACTIVATED, (Boolean)false));
					worldIn.spawnEntity(entityfallingblock);
				}
			}
		}
	}


	/**
	 * 
	 * @return
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {DECAYABLE, CHECK_DECAY, ACTIVATED});
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	/**
	 * 
	 */
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	/**
	 * Used to determine ambient occlusion and culling when rebuilding chunks for
	 * render
	 */
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	/**
	 * 
	 */
	public boolean canCollideCheck(IBlockState state, boolean hitIfLiquid) {
		return false;
	}

	/**
	 * Spawns this Block's drops into the World as EntityItems.
	 */
	public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune) {
	}

	/**
	 * Whether this Block can be replaced directly by other blocks (true for e.g.
	 * tall grass)
	 */
	public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is
	 * used to decide whether things like buttons are allowed to be placed on the
	 * face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED},
	 * which represents something that does not fit the other descriptions and will
	 * generally cause other things not to connect to the face.
	 * 
	 * @return an approximation of the form of the given face
	 */
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	/**
	 * 
	 * @param blockState
	 * @param blockAccess
	 * @param pos
	 * @param side
	 * @return
	 */
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
			EnumFacing side) {
		IBlockState iblockstate = blockAccess.getBlockState(pos.offset(side));
		Block block = iblockstate.getBlock();

		if (blockState != iblockstate) {
			return true;
		}

		if (block == this) {
			return false;
		}

		return false;
	}

	protected boolean canSilkHarvest() {
		return true;
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState()
				.withProperty(DECAYABLE, Boolean.valueOf((meta&1)>0))
				.withProperty(CHECK_DECAY, Boolean.valueOf((meta&2)>0))
				.withProperty(ACTIVATED, Boolean.valueOf((meta&4)>0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if (state.getValue(DECAYABLE)) meta = 1;
		if (state.getValue(CHECK_DECAY)) meta +=2;
		if (state.getValue(ACTIVATED)) meta +=4;
		return meta;
	}

	/**
	 * @return the fog
	 */
	public FogHeight getFogHeight() {
		return fogHeight;
	}

	/**
	 * @param fog the fog to set
	 */
	public FogBlock setFogHeight(FogHeight fog) {
		this.fogHeight = fog;
		return this;
	}

	/**
	 * @return the fogMap
	 */
	public Map<FogHeight, FogBlock> getFogMap() {
		return fogMap;
	}

	/**
	 * @param fogMap the fogMap to set
	 */
	public void setFogMap(Map<FogHeight, FogBlock> fogMap) {
		this.fogMap = fogMap;
	}

	/**
	 * @return the fogType
	 */
	public FogType getFogType() {
		return fogType;
	}

	/**
	 * @param fogType the fogType to set
	 */
	public FogBlock setFogType(FogType fogType) {
		this.fogType = fogType;
		return this;
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Mark Gottschling on Feb 27, 2018
 *
 */
// TODO needs to perform the same functionality as BlockFalling.
public class FogBlock extends ModBlock {
	 public static final PropertyBool DECAYABLE = PropertyBool.create("decayable");
	 public static final PropertyBool CHECK_DECAY =	PropertyBool.create("check_decay");

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public FogBlock(String modID, String name, Material material) {
		super(modID, name, material);
		this.setTickRandomly(true);
		 this.setDefaultState(blockState.getBaseState()
			 .withProperty(DECAYABLE, Boolean.valueOf(true))
			 .withProperty(CHECK_DECAY, Boolean.valueOf(false)));
		setNormalCube(false);
		setCreativeTab(Treasure.TREASURE_TAB);
	}

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        worldIn.scheduleUpdate(pos, this, this.tickRate(worldIn));
    }
    
    /**
     * How many world ticks before ticking
     */
    public int tickRate(World worldIn) {
        return 2;
    }
    
    /**
     * 
     * @param state
     * @return
     */
    public static boolean canFallThrough(IBlockState state) {
        Block block = state.getBlock();
        Material material = state.getMaterial();
        return material == Material.AIR && block != TreasureBlocks.FOG_BLOCK;
    }
    
	/**
	 * 
	 * @param worldIn
	 * @param pos
	 * @param state
	 * @param rand
	 */
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			// TODO test if check_decay = true
			
			// TODO check if any block is instance of IFogSupport 
			// 	TODO check if isFogSustainable is set
			//		TODO if so, set check_decay = false
			
			// TODO check how many neighbors are fog
			// TODO if < 4 change to next level of fog: fog -> med_fog; med_fog -> low_fog; low_fog -> air
			
			// check if block should fall
            this.checkFallable(worldIn, pos);
		}
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 */
	private void checkFallable(World worldIn, BlockPos pos) {
		Treasure.logger.debug("is air block below: {}", worldIn.isAirBlock(pos.down()));
		Treasure.logger.debug("can fall through: {}", canFallThrough(worldIn.getBlockState(pos.down())));
		if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
			int i = 32;

			Treasure.logger.debug("is area loaded: [}", worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32)));
			if (worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (!worldIn.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D,
							(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
					worldIn.spawnEntity(entityfallingblock);
				}
			}
			else {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockToAir(pos);
				BlockPos blockpos;

				for (blockpos = pos
						.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos)))
								&& blockpos.getY() > 0; blockpos = blockpos.down()) {
					;
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos.up(), state); // Forge: Fix loss of state information during world gen.
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
		 return new BlockStateContainer(this, new IProperty[] {DECAYABLE, CHECK_DECAY});
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
		 boolean decay = false;
		 boolean check = false;
		 if (meta == 1) decay = true;
		 if (meta == 2) check = true;
		 if (meta ==3) { decay = check = true;}
		 return this.getDefaultState().withProperty(DECAYABLE, Boolean.valueOf(decay)).withProperty(CHECK_DECAY, Boolean.valueOf(check));
	 }
	
	 /**
	 * Convert the BlockState into the correct metadata value
	 */
	 @Override
	 public int getMetaFromState(IBlockState state) {
		 int meta = 0;
		 if (state.getValue(DECAYABLE)) meta = 1;
		 if (state.getValue(CHECK_DECAY)) meta +=2;
		 return meta;
	 }
}

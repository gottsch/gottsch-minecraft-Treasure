/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import javax.annotation.Nullable;

import com.someguyssoftware.gottschcore.block.ModFallingBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockSand;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * TODO refactor the falling blocks to inherit from parent class
 * @author Mark Gottschling on Apr 17, 2020
 *
 */
public class FallingSandBlock extends ModFallingBlock {
	public static final PropertyBool ACTIVATED = PropertyBool.create("activated");

	protected static final AxisAlignedBB BOUNDING_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	protected static final AxisAlignedBB COLLISION_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.9975D, 1.0D);

	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public FallingSandBlock(String modID, String name) {
		super(modID, name, Material.GROUND);
		this.setDefaultState(blockState.getBaseState().withProperty(ACTIVATED, Boolean.valueOf(false)));
		setCreativeTab(Treasure.TREASURE_TAB);
		setSoundType(SoundType.PLANT);
		setHardness(0.5F);
	}

	/**
	 * 
	 * @return
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { ACTIVATED });
	}

	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BOUNDING_AABB;
	}

	@Nullable
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return COLLISION_AABB;
	}

	/**
	 * Called When an Entity Collided with the Block. Teleport and flag target.
	 */
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		// only on server
		if (!worldIn.isRemote) {
			if (!(entityIn instanceof EntityPlayer)) {
				return;
			}
			// set to activated
			worldIn.setBlockState(pos, getDefaultState().withProperty(ACTIVATED, Boolean.valueOf(true)), 3);

			// check if fallable and fall
			checkFallable(worldIn, pos);
		}
	}

	@Override
	public void onEndFalling(World worldIn, BlockPos pos, IBlockState p_176502_3_, IBlockState p_176502_4_) {
		worldIn.setBlockState(pos, Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND));
	}
	
	/**
	 * 
	 */
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		// do nothing
	}

	/**
	 * 
	 * @param worldIn
	 * @param pos
	 */
	private void checkFallable(World worldIn, BlockPos pos) {
		if (!worldIn.getBlockState(pos).getValue(ACTIVATED)) {
			return;
		}

		if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {

			if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (!worldIn.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D,
							(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
					this.onStartFalling(entityfallingblock);
					worldIn.spawnEntity(entityfallingblock);
				}
			} else {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockToAir(pos);
				BlockPos blockpos;

				for (blockpos = pos
						.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos)))
								&& blockpos.getY() > 0; blockpos = blockpos.down()) {
					;
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos.up(), state);
					// Forge: Fix loss of state information during world gen.
				}
			}
		}
	}

	/**
	 * 
	 */
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		// do nothing
	}

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.SAND.getItemDropped(Blocks.SAND.getDefaultState().withProperty(BlockSand.VARIANT, BlockSand.EnumType.SAND), rand, fortune);
    }
    
	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(ACTIVATED, Boolean.valueOf((meta & 1) > 0));
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;
		if (state.getValue(ACTIVATED))
			meta = 1;
		return meta;
	}
	
    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }
}

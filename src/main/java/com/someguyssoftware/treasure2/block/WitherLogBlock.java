/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Mar 19, 2018
 *
 */
public class WitherLogBlock extends ModLogBlock {
	public static final PropertyEnum<Bark> BARK = PropertyEnum.<Bark>create("bark", Bark.class);

	/**
	 * 
	 */
	public WitherLogBlock(String modID, String name) {
		super(modID, name);
		// add to creative tab
		this.setCreativeTab(Treasure.TREASURE_TAB);
		// set the hardness - a little more than regular wood
		this.setHardness(3.0F);
		// set the blast resistence - just under stone
		this.setResistance(9.0F);

		// set the harvest tool/material
		this.setHarvestLevel("axe", 2);

		this.setDefaultState(this.blockState
				.getBaseState()
				.withProperty(LOG_AXIS, BlockLog.EnumAxis.Y)
				.withProperty(BARK, Bark.NORMAL));
	}

	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { LOG_AXIS, BARK });
	}

	// @Override
	// public IBlockState onBlockPlaced(World worldIn, BlockPos pos,
	// EnumFacing facing, float hitX, float hitY, float hitZ, int meta,
	// EntityLivingBase placer) {
	// IBlockState blockState = super.onBlockPlaced(worldIn, pos, facing, hitX,
	// hitY, hitZ, meta, placer);
	// blockState = blockState.withProperty(LOG_AXIS,
	// BlockLog.EnumAxis.fromFacingAxis(facing.getAxis()));
	// Treasure.logger.debug("Placed: WitherLog Axis:" + LOG_AXIS.getName() + ": " +
	// blockState.getValue(LOG_AXIS));
	// return blockState;
	// }

	/**
	 * 	
	 */
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
		// don't do anything special
	}


	/**
	 * Returns the quantity of items to drop on block destruction.
	 */
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 1;
	}

	/**
	 * 
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	public IBlockState getStateFromMeta(int meta) {
		EnumAxis axis;
		switch (meta) {
		case 1:
			axis = EnumAxis.X;
			break;
		case 2:
			axis = EnumAxis.Y;
			break;
		case 3:
			axis = EnumAxis.Z;
			break;
		default:
			axis = EnumAxis.Y;
			break;
		}
		return this.getDefaultState().withProperty(LOG_AXIS, axis);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	public int getMetaFromState(IBlockState state) {
		int logBits = 0;
		switch ((EnumAxis) state.getValue(LOG_AXIS)) {
		case X:
			logBits = 0;
			break;
		case Y:
			logBits = 1;
			break;
		case Z:
			logBits = 2;
			break;
		default:
			break;
		}
		
		int barkBits = state.getValue(BARK).getValue();		
		int meta = barkBits << 2;
		meta += logBits;
		return meta;
	}

	/**
	 * 
	 * @author Mark Gottschling on Mar 20, 2018
	 *
	 */
	public static enum Bark implements IStringSerializable {
		NORMAL("normal", 0),
		FULL("full", 1),
		STRIPPED("stripped", 2);

	    private final String name;
	    private final int value;
	    
	    private Bark(String name, int value) {
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
	}
}

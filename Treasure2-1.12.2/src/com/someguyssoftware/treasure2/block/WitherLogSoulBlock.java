/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jun 6, 2018
 *
 */
public class WitherLogSoulBlock extends CardinalDirectionFacadeBlock implements IFogSupport, ITreasureBlock {
	
	public static final PropertyEnum<Appearance> APPEARANCE = PropertyEnum.create("appearance",  Appearance.class);
	
	/*
	 * An array of AxisAlignedBB bounds for the bounding box
	 */
	AxisAlignedBB[] bounds = new AxisAlignedBB[4];
	
	/**
	 * 
	 * @param modID
	 * @param name
	 */
	public WitherLogSoulBlock(String modID, String name) {
		super(modID, name, Material.WOOD);
		setSoundType(SoundType.WOOD);
		setCreativeTab(Treasure.TREASURE_TAB);
		setHardness(3.0F);
		this.setDefaultState(this.blockState
				.getBaseState()
				.withProperty(FACING, EnumFacing.NORTH)
				.withProperty(APPEARANCE, Appearance.NONE));
	}
	
	/**
	 * 
	 */
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, APPEARANCE});
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
			// use a MutatableBlockPos instead of Cube\Coords or BlockPos to say the recreation of many objects
			BlockPos.MutableBlockPos mbp = new BlockPos.MutableBlockPos();

			for (int x1 = -5; x1 <= 5; ++x1) {
				for (int y1 = -5; y1 <= 5; ++y1) {
					for (int z1 = -5; z1 <= 5; ++z1) {						
						// that just checks a value.
						IBlockState inspectBlockState = worldIn	.getBlockState(mbp.setPos(x + x1, y + y1, z + z1));
						if (inspectBlockState.getMaterial() == TreasureItems.FOG) {
							worldIn.setBlockState(mbp, inspectBlockState.withProperty(FogBlock.CHECK_DECAY, true));
						}
					}
				}
			}
		}		
		super.breakBlock(worldIn, pos, state);
	}
	
	/**
	 * 
	 */
	public boolean canSustainFog(IBlockState state, IBlockAccess world, BlockPos pos) {
		return true;
	}
	
    /**
     * Convert the given metadata into a BlockState for this Block
     */
	@Override
    public IBlockState getStateFromMeta(int meta) {
		IBlockState state = super.getStateFromMeta(meta & 0x07);
		state = state.withProperty(APPEARANCE, (meta & 0x08) == 0 ? Appearance.NONE : Appearance.FACE);
        return state;
    }
	
    /**
     * Convert the BlockState into the correct metadata value
     */
	@Override
    public int getMetaFromState(IBlockState state) {
		int facingBits = 0;
		facingBits =  state.getValue(FACING).getIndex();
		facingBits = (facingBits == -1) ? 2 : facingBits;

		int appearanceBits = state.getValue(APPEARANCE).getValue() == 0 ? 0 : 8;
		return facingBits + appearanceBits ;
    }
	
	
	/**
	 * Drops WitherRootItem or a stick.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random random, int fortune) {
		if (RandomHelper.checkProbability(random, TreasureConfig.WITHER_TREE.witherRootItemGenProbability)) {
			return TreasureItems.WITHER_ROOT_ITEM;
		}
		return Items.STICK;
    }

	/**
	 * @return the bounds
	 */
	public AxisAlignedBB[] getBounds() {
		return bounds;
	}

	/**
	 * @param bounds the bounds to set
	 */
	public void setBounds(AxisAlignedBB[] bounds) {
		this.bounds = bounds;
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Jun 7, 2018
	 *
	 */
	public static enum Appearance implements IStringSerializable {
		NONE("none", 0),
		FACE("face", 1);

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
	}
}

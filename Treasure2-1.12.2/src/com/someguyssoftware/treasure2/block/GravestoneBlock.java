/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.block.CardinalDirectionFacadeBlock;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.item.TreasureItems;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 29, 2018
 *
 */
public class GravestoneBlock extends CardinalDirectionFacadeBlock implements ITreasureBlock, IFogSupport {

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
		setBoundingBox(
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F), 	// N
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// E
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F),  	// S
				new AxisAlignedBB(0F, 0F, 0F, 1F, 1F, 1F)	// W
				);
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
        	
			for (int x1 = -4; x1 <= 4; ++x1) {
				for (int y1 = -4; y1 <= 4; ++y1) {
					for (int z1 = -4; z1 <= 4; ++z1) {								
						// that just checks a value.
						IBlockState inspectBlockState = worldIn	.getBlockState(mbp.setPos(x + x1, y + y1, z + z1));
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
	 * 
	 */
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(FACING) == EnumFacing.NORTH) {
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.SOUTH) {
			return bounds[EnumFacing.SOUTH.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.EAST) {
			return bounds[EnumFacing.EAST.getHorizontalIndex()];
		}
		else if (state.getValue(FACING) == EnumFacing.WEST) {
			return bounds[EnumFacing.WEST.getHorizontalIndex()];
		}
		else {		
			return bounds[EnumFacing.NORTH.getHorizontalIndex()];
		}
	}
	
	  /**
     * Determines if this block can prevent leaves connected to it from decaying.
     * @param state The current state
     * @param world The current world
     * @param pos Block position in world
     * @return true if the presence this block can prevent leaves from decaying.
     */
	@Override
    public boolean canSustainFog(IBlockState state, IBlockAccess world, BlockPos pos) {
        return true;
    }
    
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

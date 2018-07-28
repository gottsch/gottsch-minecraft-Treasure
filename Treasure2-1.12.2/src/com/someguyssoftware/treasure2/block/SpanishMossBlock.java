/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.gottschcore.cube.Cube;
import com.someguyssoftware.gottschcore.positional.Coords;

import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jul 25, 2018
 *
 */
public class SpanishMossBlock extends BlockBush {

	/**
	 * 
	 */
	public SpanishMossBlock(String modID, String name) {
		setRegistryName(modID, name);
		setUnlocalizedName(getRegistryName().toString());
	}
	
    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
//        Cube cube = new Cube(worldIn, new Coords(pos.up()));
//        return super.canPlaceBlockAt(worldIn, pos) && !cube.isAir() && !cube.isReplaceable();
    	return true;
    }
    
    /**
     * 
     */
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
//        if (state.getBlock() == this) {//Forge: This function is called during world gen and placement, before this block is set, so if we are not 'here' then assume it's the pre-check.
//            Cube cube = new Cube(worldIn, new Coords(pos.up()));
//            return super.canPlaceBlockAt(worldIn, pos) && !cube.isAir() && !cube.isReplaceable();
//        }
        return this.canSustainBush(worldIn.getBlockState(pos.down()));
    }
    
    /**
     * Return true if the block can sustain a Bush
     */
    public boolean canSustainBush(IBlockState state) {
        return true;
    }

}

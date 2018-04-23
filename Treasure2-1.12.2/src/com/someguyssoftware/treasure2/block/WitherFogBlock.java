/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Apr 18, 2018
 *
 */
public class WitherFogBlock extends FogBlock {

	// TODO move to FogBlock
    /** These bounding boxe are used to check for entities in a certain area. */
    protected static final AxisAlignedBB FULL_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB HIGH_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.75D, 1.0D);
    protected static final AxisAlignedBB MED_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.5D, 1.0D);
    protected static final AxisAlignedBB LOW_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.D, 1.0D, 0.25D, 1.0D);
    
	/**
	 * @param modID
	 * @param name
	 * @param material
	 */
	public WitherFogBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}

	/**
	 * TODO move to FogBlock
	 */
	@Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (getFog()) {
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
    @Override
    public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
    	// TODO inflict player with wither
    }
}

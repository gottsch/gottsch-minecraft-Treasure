/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.block.ModBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.LevelAccessor;

/**
 * @author Mark Gottschling on Sep 19, 2014
 *
 */
public class WishingWellBlock extends ModBlock implements IWishingWellBlock {
	// logger
	public static Logger logger = LogManager.getLogger(WishingWellBlock.class);
    
    VoxelShape shape = Block.box(0, 0, 0, 16, 16, 16);

	/**
	 * 
	 * @param material
	 */
	public WishingWellBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties);
	}

    /**
     * 
     * @param state
     * @param worldIn
     * @param pos
     * @param context
     * @return
     */
    @Override
	public VoxelShape getShape(BlockState state, LevelAccessor worldIn, BlockPos pos, CollisionContext context) {
		return shape;
    }
}
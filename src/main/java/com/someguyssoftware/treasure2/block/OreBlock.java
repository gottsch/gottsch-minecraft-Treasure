/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.block.ModBlock;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2018
 *
 */
public class OreBlock extends ModBlock {
	
	/**
	 * 
	 * @param material
	 */
	public OreBlock(String modID, String name, Block.Properties properties) {
		super(modID, name, properties.sound(SoundType.STONE));
	}
    
	/**
	 * 
	 */
	@Override
	public boolean isNormalCube(BlockState state, IBlockReader world, BlockPos pos) {
		return false;
	}
}
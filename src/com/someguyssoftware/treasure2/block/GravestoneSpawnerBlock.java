/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import java.util.Random;

import com.someguyssoftware.treasure2.tileentity.GravestoneProximitySpawnerTileEntity;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Sep 14, 2020
 *
 */
public class GravestoneSpawnerBlock extends GravestoneBlock	implements ITileEntityProvider {

	/**
	 * 
	 * @param modID
	 * @param name
	 * @param material
	 */
	public GravestoneSpawnerBlock(String modID, String name, Material material) {
		super(modID, name, material);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		GravestoneProximitySpawnerTileEntity tileEntity = new GravestoneProximitySpawnerTileEntity();
		return (TileEntity) tileEntity;
	}
	

	/**
	 * Drops standard gravestones.
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if (this == TreasureBlocks.GRAVESTONE1_SPAWNER_STONE) {
			return Item.getItemFromBlock(TreasureBlocks.GRAVESTONE1_STONE);
		}
		else if (this == TreasureBlocks.GRAVESTONE2_SPAWNER_COBBLESTONE) {
			return Item.getItemFromBlock(TreasureBlocks.GRAVESTONE2_COBBLESTONE);
		}
		else if (this == TreasureBlocks.GRAVESTONE3_SPAWNER_OBSIDIAN) {
			return Item.getItemFromBlock(TreasureBlocks.GRAVESTONE3_OBSIDIAN);
		}		
        return super.getItemDropped(state, rand, fortune);
    }
}

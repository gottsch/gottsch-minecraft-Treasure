/**
 * 
 */
package com.someguyssoftware.treasure2.block;

import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Jun 19, 2018
 *
 */
public class WitherChestBlock extends TreasureChestBlock {

	/**
	 * @param modID
	 * @param name
	 * @param te
	 * @param type
	 * @param rarity
	 */
	public WitherChestBlock(String modID, String name, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, te, type, rarity);
	}

	/**
	 * @param modID
	 * @param name
	 * @param material
	 * @param te
	 * @param type
	 * @param rarity
	 */
	public WitherChestBlock(String modID, String name, Material material, Class<? extends AbstractTreasureChestTileEntity> te, TreasureChestType type, Rarity rarity) {
		super(modID, name, material, te, type, rarity);
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		// TODO Add the placeholder block above
		// TODO placeholder on activated will have to call this block activated
		super.onBlockAdded(worldIn, pos, state);
	}
}

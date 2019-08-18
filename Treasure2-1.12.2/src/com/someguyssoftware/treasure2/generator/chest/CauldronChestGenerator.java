/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster.SpecialLootTables;

/**
 * 
 * @author Mark Gottschling on Jan 30, 2019
 *
 */
public class CauldronChestGenerator extends EpicChestGenerator {
	
	/**
	 * 
	 */
	public CauldronChestGenerator() {}
	
	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public LootTable selectLootTable(Random random, final Rarity chestRarity) {
		return Treasure.LOOT_TABLES.getSpecialLootTable(SpecialLootTables.CAULDRON_CHEST);
	}
	 
	/**
	 * Always select a wither chest.
	 */
	@Override
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.CAULDRON_CHEST;
		return chest;
	}
	
	/**
	 * Don't place any markers
	 */
//	@Override
//	public void addMarkers(World world, Random random, ICoords coords) {
//		return;
//	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
//	@Override
//	public TileEntity placeInWorld(World world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
//		// replace block @ coords
//		GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);
//		// ensure that chest is of type WITHER_CHEST
//		if (world.getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST) {
//			// add top placeholder
//			world.setBlockState(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState());
//		}
//		// get the backing tile entity of the chest 
//		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());
//
//		// if tile entity failed to create, remove the chest
//		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
//			// remove chest
//			world.setBlockToAir(chestCoords.toPos());
//			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
//			return null;
//		}
//		return te;
//	}
}
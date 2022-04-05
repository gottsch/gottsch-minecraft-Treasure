/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;

import com.someguyssoftware.gottschcore.loot.LootTableShell;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster2.SpecialLootTables;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public class WitherChestGenerator implements IChestGenerator {
	
	/**
	 * 
	 */
	public WitherChestGenerator() {}

	/**
	 * 
	 */
	@Override
	public void addGenerationContext(AbstractTreasureChestTileEntity tileEntity, Rarity rarity) {
		AbstractTreasureChestTileEntity.GenerationContext generationContext = tileEntity.new GenerationContext(rarity, ChestGeneratorType.WITHER);
		tileEntity.setGenerationContext(generationContext);
	}
	
	/*
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public Optional<LootTableShell> selectLootTable2(Random random, final Rarity chestRarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WITHER_CHEST));
	}

	@Override
	public Optional<LootTableShell> selectLootTable2(Supplier<Random> factory, final Rarity rarity) {
		return Optional.ofNullable(TreasureLootTableRegistry.getLootTableMaster().getSpecialLootTable(SpecialLootTables.WITHER_CHEST));
	}
	/**
	 * Always select a wither chest.
	 */
	@Override
	public TreasureChestBlock  selectChest(final Random random, final Rarity rarity) {
		TreasureChestBlock chest = (TreasureChestBlock) TreasureBlocks.WITHER_CHEST;
		return chest;
	}
	
	/**
	 * Wither will have at least one lock.
	 */
	public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());		
		Treasure.logger.debug("# of locks to use: {})", numLocks);
		
		return numLocks;
	}
	
	/**
	 * 
	 */
	@Override
	public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// get the chest type
		TreasureChestType type = chest.getChestType();
		// get the lock states
		List<LockState> lockStates = te.getLockStates();	
		// determine the number of locks to add (must have at least 1 lock)
		int numLocks = RandomHelper.randomInt(random, 1, type.getMaxLocks());
		for (int i = 0; i < numLocks; i++) {
			LockItem lock = TreasureItems.WITHER_LOCK;
			// add the lock to the chest
			lockStates.get(i).setLock(lock);
		}
	}
	
	/**
	 * Don't place any markers
	 */
	@Override
	public void addMarkers(World world, Random random, ICoords coords, boolean isSurfaceChest) {
		return;
	}
	
	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	@Override
	public TileEntity placeInWorld(World world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);
		// ensure that chest is of type WITHER_CHEST
		if (world.getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST) {
			// add top placeholder
			world.setBlockState(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState());
		}
		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
	
	@Override
	public TileEntity placeInWorld(World world, Random random, ICoords chestCoords, AbstractChestBlock chest, IBlockState state) {
		// replace block @ coords
		GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		
		// ensure that chest is of type WITHER_CHEST
		if (world.getBlockState(chestCoords.toPos()).getBlock() == TreasureBlocks.WITHER_CHEST) {
			// add top placeholder
			world.setBlockState(chestCoords.up(1).toPos(), TreasureBlocks.WITHER_CHEST_TOP.getDefaultState()); // TODO this needs to rotate to state as well.
		}
		// get the backing tile entity of the chest 
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator {

	default public GeneratorResult<ChestGeneratorData> generate(final World world, final Random random, ICoords coords,
			final Rarity rarity, BlockState state) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setSpawnCoords(coords);

		// select a loot table
		LootTable lootTable = selectLootTable(random, rarity);
		if (lootTable == null) {
			Treasure.LOGGER.warn("Unable to select a lootTable.");
			return result.fail();
		}

		// select a chest from the rarity
		AbstractChestBlock<?> chest = selectChest(random, rarity);
		if (chest == null) {
			Treasure.LOGGER.warn("Unable to select a chest for rarity {}.", rarity);
			return result.fail();
		}

		// place the chest in the world
		TileEntity te = null;
		if (state != null) {
			te = placeInWorld(world, random, coords, chest, state);
		} else {
			te = placeInWorld(world, random, chest, coords);
		}

		if (te == null) {
			Treasure.LOGGER.debug("Unable to locate tile entity for chest -> {}", coords);
			return result.fail();
		}
		
//	TODO 1.15.2
//		if (!(chest instanceof IMimicBlock)) {
//			Treasure.LOGGER.debug("Generating loot from loot table for rarity {}", rarity);
//			lootTable.fillInventory((IInventory) te, random, Treasure.LOOT_TABLES.getContext());
//		}

		// add locks
		addLocks(random, chest, (AbstractTreasureChestTileEntity) te, rarity);

		// update result
		result.getData().setChestContext(new BlockContext(coords, state));

		return result.success();
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public List<LootTable> buildLootTableList(Rarity rarity) {
//		TODO 1.15.2
//		return Treasure.LOOT_TABLES.getLootTableByRarity(rarity);
		return new ArrayList<LootTable>();
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public AbstractChestBlock selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureData.CHESTS_BY_RARITY.get(rarity); //TreasureBlocks.chests.get(rarity);
		AbstractChestBlock chest = (AbstractChestBlock) chestList
				.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));

		// TODO should have a map of available mimics mapped by chest. for now, since
		// only one mimic, just test for it determine if should be mimic get the config
//		TODO 1.15.2
//				IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
//		if (chest == TreasureBlocks.WOOD_CHEST) {
//			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
//				chest = (AbstractChestBlock) TreasureBlocks.WOOD_MIMIC;
//				Treasure.LOGGER.debug("Selecting a WOOD MIMIC chest!");
//			}
//		} else if (chest == TreasureBlocks.PIRATE_CHEST) {
//			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
//				chest = (AbstractChestBlock) TreasureBlocks.PIRATE_MIMIC;
//				Treasure.LOGGER.debug("Selecting a PIRATE MIMIC chest!");
//			}
//		}
		return chest;
	}

	/**
	 * 
	 * @param random
	 * @param rarity
	 * @return
	 */
	default public LootTable selectLootTable(Random random, final Rarity rarity) {
		LootTable table = null;

		// select the loot table by rarity
		List<LootTable> tables = buildLootTableList(rarity);

		// select a random table from the list
		if (tables != null && !tables.isEmpty()) {
			int index = 0;
			/*
			 * get a random container
			 */
			if (tables.size() == 1) {
				table = tables.get(0);
			} else {
				index = RandomHelper.randomInt(random, 0, tables.size() - 1);
				table = tables.get(index);
			}
			Treasure.LOGGER.debug("Selected loot table index --> {}", index);
		}
		return table;
	}

	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * 
	 * @param chest
	 */
	default public void addLocks(Random random, AbstractChestBlock<?> chest, AbstractTreasureChestTileEntity te,
			Rarity rarity) {
		List<LockItem> locks = new ArrayList<>();
		locks.addAll(TreasureItems.locks.get(rarity));
		addLocks(random, chest, te, locks);
		locks.clear();
	}

	/**
	 * 
	 * @param random
	 * @param chest
	 * @param te
	 * @param locks
	 */
	default public void addLocks(Random random, AbstractChestBlock<?> chest, AbstractTreasureChestTileEntity te,
			List<LockItem> locks) {
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getChestType());

		// get the lock states
		List<LockState> lockStates = te.getLockStates();

		for (int i = 0; i < numLocks; i++) {
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size() - 1));
			Treasure.LOGGER.debug("adding lock: {}", lock);
			// add the lock to the chest
			lockStates.get(i).setLock(lock);
		}
	}

	/**
	 * 
	 * @param random
	 * @param type
	 * @return
	 */
	default public int randomizedNumberOfLocksByChestType(Random random, TreasureChestType type) {
		// determine the number of locks to add
		int numLocks = RandomHelper.randomInt(random, 0, type.getMaxLocks());
		Treasure.LOGGER.debug("# of locks to use: {})", numLocks);
		return numLocks;
	}

	/**
	 * Wrapper method so that is can be overridden (as used in the Template Pattern)
	 * 
	 * @param world
	 * @param random
	 * @param coods
	 */
	default public void addMarkers(World world, Random random, ICoords coords, final boolean isSurfaceChest) {
//		TODO 1.15.2
//		if (!isSurfaceChest && TreasureConfig.WORLD_GEN.getMarkerProperties().isMarkerStructuresAllowed && RandomHelper
//				.checkProbability(random, TreasureConfig.WORLD_GEN.getMarkerProperties().markerStructureProbability)) {
//			Treasure.LOGGER.debug("generating a random structure marker -> {}", coords.toShortString());
//			new StructureMarkerGenerator().generate(world, random, coords);
//		} else {
//			new GravestoneMarkerGenerator().generate(world, random, coords);
//		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, AbstractChestBlock<?> chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockState(chestCoords.toPos(), Blocks.AIR.getDefaultState());
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chestCoords
	 * @param chest
	 * @param state
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, ICoords chestCoords, AbstractChestBlock<?> chest,
			BlockState state) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		Treasure.LOGGER.debug("isPlaced -> {}", isPlaced);
		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.LOGGER.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)
			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockState(chestCoords.toPos(), Blocks.AIR.getDefaultState());
			Treasure.LOGGER.debug("Unable to create TileEntityChest, removing BlockChest");
			return null;
		}
		return te;
	}
}
/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.loot.LootTable;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.gottschcore.world.gen.structure.BlockContext;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.AbstractChestBlock;
import com.someguyssoftware.treasure2.block.IMimicBlock;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.config.IChestConfig;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GenUtil;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.marker.StructureMarkerGenerator;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @author Mark Gottschling on Dec 4, 2019
 *
 */
public interface IChestGenerator {

	default public GeneratorResult<ChestGeneratorData> generate(final World world, final Random random, ICoords coords,
			final Rarity rarity, IBlockState state) {
		GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
		result.getData().setSpawnCoords(coords);

		// select a loot table
		LootTable lootTable = selectLootTable(random, rarity);
		if (lootTable == null) {
			Treasure.logger.warn("Unable to select a lootTable.");
			return result.fail();
		}

		// select a chest from the rarity
		AbstractChestBlock chest = selectChest(random, rarity);
		if (chest == null) {
			Treasure.logger.warn("Unable to select a chest for rarity {}.", rarity);
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
			Treasure.logger.debug("Unable to locate tile entity for chest -> {}", coords);
			return result.fail();
		}

		if (!(chest instanceof IMimicBlock)) {
			Treasure.logger.debug("Generating loot from loot table for rarity {}", rarity);
			lootTable.fillInventory((IInventory) te, random, Treasure.LOOT_TABLES.getContext());
		}

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
		return Treasure.LOOT_TABLES.getLootTableByRarity(rarity);
	}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	default public AbstractChestBlock selectChest(final Random random, final Rarity rarity) {
		List<Block> chestList = (List<Block>) TreasureBlocks.chests.get(rarity);
		AbstractChestBlock chest = (AbstractChestBlock) chestList
				.get(RandomHelper.randomInt(random, 0, chestList.size() - 1));

		// TODO should have a map of available mimics mapped by chest. for now, since
		// only one mimic, just test for it
		// determine if should be mimic
		// get the config
		IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity); // TODO how to change this from
																							// just being surfaceChests.
		if (chest == TreasureBlocks.WOOD_CHEST) {
			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
				chest = (AbstractChestBlock) TreasureBlocks.WOOD_MIMIC;
				Treasure.logger.debug("Selecting a WOOD MIMIC chest!");
			}
		} else if (chest == TreasureBlocks.PIRATE_CHEST) {
			if (RandomHelper.checkProbability(random, config.getMimicProbability())) {
				chest = (AbstractChestBlock) TreasureBlocks.PIRATE_MIMIC;
				Treasure.logger.debug("Selecting a PIRATE MIMIC chest!");
			}
		}
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
			Treasure.logger.debug("Selected loot table index --> {}", index);
		}
		return table;
	}

	/**
	 * Default implementation. Select locks only from with the same Rarity.
	 * 
	 * @param chest
	 */
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te,
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
	default public void addLocks(Random random, AbstractChestBlock chest, AbstractTreasureChestTileEntity te,
			List<LockItem> locks) {
		int numLocks = randomizedNumberOfLocksByChestType(random, chest.getChestType());

		// get the lock states
		List<LockState> lockStates = te.getLockStates();

		for (int i = 0; i < numLocks; i++) {
			LockItem lock = locks.get(RandomHelper.randomInt(random, 0, locks.size() - 1));
			Treasure.logger.debug("adding lock: {}", lock);
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
		Treasure.logger.debug("# of locks to use: {})", numLocks);
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
//		boolean isChestOnSurface = false;
		// don't place if the block underneath is of GenericBlock ChestConfig or
		// Container
//		Block block = world.getBlockState(coords/*.add(0, -1, 0)*/.toPos()).getBlock();
//		if(block instanceof BlockContainer || block instanceof AbstractModContainerBlock || block instanceof ITreasureBlock) {
//			isChestOnSurface = true;
//		}
		// GenUtil.placeMarkers(world, random, coords);
		if (!isSurfaceChest && TreasureConfig.WORLD_GEN.getMarkerProperties().isMarkerStructuresAllowed && RandomHelper
				.checkProbability(random, TreasureConfig.WORLD_GEN.getMarkerProperties().markerStructureProbability)) {
			Treasure.logger.debug("generating a random structure marker -> {}", coords.toShortString());
			new StructureMarkerGenerator().generate(world, random, coords);
		} else {
			new GravestoneMarkerGenerator().generate(world, random, coords);
		}
	}

	/**
	 * 
	 * @param world
	 * @param random
	 * @param chest
	 * @param chestCoords
	 * @return
	 */
	default public TileEntity placeInWorld(World world, Random random, AbstractChestBlock chest, ICoords chestCoords) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chest, chestCoords);

		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.logger.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

		// if tile entity failed to create, remove the chest
		if (te == null || !(te instanceof AbstractTreasureChestTileEntity)) {
			// remove chest
			world.setBlockToAir(chestCoords.toPos());
			Treasure.logger.debug("Unable to create TileEntityChest, removing BlockChest");
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
	default public TileEntity placeInWorld(World world, Random random, ICoords chestCoords, AbstractChestBlock chest,
			IBlockState state) {
		// replace block @ coords
		boolean isPlaced = GenUtil.replaceBlockWithChest(world, random, chestCoords, chest, state);
		Treasure.logger.debug("isPlaced -> {}", isPlaced);
		// get the backing tile entity of the chest
		TileEntity te = (TileEntity) world.getTileEntity(chestCoords.toPos());

		// check to ensure the chest has been generated
		if (!isPlaced || !(world.getBlockState(chestCoords.toPos()).getBlock() instanceof AbstractChestBlock)) {
			Treasure.logger.debug("Unable to place chest @ {}", chestCoords.toShortString());
			// remove the title entity (if exists)

			if (te != null && (te instanceof AbstractTreasureChestTileEntity)) {
				world.removeTileEntity(chestCoords.toPos());
			}
			return null;
		}

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

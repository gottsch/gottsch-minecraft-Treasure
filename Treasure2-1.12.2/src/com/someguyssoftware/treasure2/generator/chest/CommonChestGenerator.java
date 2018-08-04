/**
 * 
 */
package com.someguyssoftware.treasure2.generator.chest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureChestBlock;
import com.someguyssoftware.treasure2.chest.TreasureChestType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.item.LockItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.lock.LockState;
import com.someguyssoftware.treasure2.loot.TreasureLootTables;
import com.someguyssoftware.treasure2.tileentity.AbstractTreasureChestTileEntity;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class CommonChestGenerator extends AbstractChestGenerator {
	
	/**
	 * 
	 */
	public CommonChestGenerator() {}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
//	@Override
//	public LootContainer selectContainer(Random random, final Rarity rarity) {
//		LootContainer container = LootContainer.EMPTY_CONTAINER;
//		
//		// select the loot container by rarities
//		Rarity[] rarities = new Rarity[] {Rarity.COMMON, Rarity.UNCOMMON};
//		List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(Arrays.asList(rarities));
//		if (containers != null && !containers.isEmpty()) {
//			if (containers.size() == 1) {
//				container = containers.get(0);
//			}
//			else {
//				container = containers.get(RandomHelper.randomInt(random, 0, containers.size()-1));
//			}
//			Treasure.logger.info("Chosen chest container:" + container);
//		}
//		return container;
//	}

	/**
	 * 
	 * @param random
	 * @param chestRarity
	 * @return
	 */
	@Override
	public List<LootTable> buildLootTableList(final Rarity chestRarity) {
		LootTable table = null;
		// select the loot table by rarity
//		List<LootTable> tables = TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.COMMON);
//		tables.addAll(TreasureLootTables.CHEST_LOOT_TABLE_MAP.get(Rarity.UNCOMMON));
		
		// get all loot tables by column key
		List<LootTable> tables = new ArrayList<>();
		tables.addAll(TreasureLootTables.getLootTableByRarity(Rarity.COMMON));
		tables.addAll(TreasureLootTables.getLootTableByRarity(Rarity.UNCOMMON));
//		Map<String, List<LootTable>> mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.COMMON);
//		// convert to a single list
//		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
//			tables.addAll(n.getValue());
//		}
//		mapOfLootTables = TreasureLootTables.CHEST_LOOT_TABLES_TABLE.column(Rarity.UNCOMMON);
//		// convert to a single list
//		for(Entry<String, List<LootTable>> n : mapOfLootTables.entrySet()) {
//			tables.addAll(n.getValue());
//		}		
		return tables;
	}
	
	
	/**
	 * Select Locks from Common and Uncommon rarities.
	 * @param chest
	 */
	@Override
	public void addLocks(Random random, TreasureChestBlock chest, AbstractTreasureChestTileEntity te, Rarity rarity) {
		// select a rarity locks
		List<LockItem> locks = (List<LockItem>) TreasureItems.locks.get(Rarity.COMMON);
		locks.addAll(TreasureItems.locks.get(Rarity.UNCOMMON));
		addLocks(random, chest, te, locks);
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.random.RandomHelper;
import com.someguyssoftware.lootbuilder.db.DbManager;
import com.someguyssoftware.lootbuilder.model.LootContainer;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * 
 * @author Mark Gottschling on Jan 24, 2018
 *
 */
public class UncommonChestGenerator extends AbstractTreasureGenerator {
	
	/**
	 * 
	 */
	public UncommonChestGenerator() {}

	/**
	 * 
	 * @param rarity
	 * @return
	 */
	public LootContainer selectContainer(Random random, final Rarity rarity) {
		LootContainer container = LootContainer.EMPTY_CONTAINER;
		
		// select the loot container by rarities
		Rarity[] rarities = new Rarity[] {Rarity.COMMON, Rarity.UNCOMMON, Rarity.SCARCE};
		List<LootContainer> containers = DbManager.getInstance().getContainersByRarity(Arrays.asList(rarities));
		if (containers != null && !containers.isEmpty()) {
			if (containers.size() == 1) {
				container = containers.get(0);
			}
			else {
				container = containers.get(RandomHelper.randomInt(random, 0, containers.size()-1));
			}
			Treasure.logger.info("Chosen chest container:" + container);
		}
		return container;
	}
	
	// TODO override what pit generators can be used.
}

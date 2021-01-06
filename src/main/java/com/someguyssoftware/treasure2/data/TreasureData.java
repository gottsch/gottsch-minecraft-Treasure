/**
 * 
 */
package com.someguyssoftware.treasure2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.ChestEnvironment;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;

import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class TreasureData {
	// chest map by rarity and mapping flag - ** possible replacement for CHESTS_BY_RARITY **
	public static final Table<Rarity, ChestEnvironment, Block> CHESTS_BY_RARITY_FLAGS = HashBasedTable.create();
	
	// chest map by rarity
	public static final Multimap<Rarity, Block> CHESTS_BY_RARITY= ArrayListMultimap.create();
	
	// chest map by name
	public static final HashMap<String, Block> CHESTS_BY_NAME = new HashMap<>();
	
	// chest generators by rarity and environment
	public static final Table<Rarity, WorldGenerators, RandomWeightedCollection<IChestGenerator>> CHEST_GENS = HashBasedTable.create();
	
	// TODO setup as map by WorldGenerators
	public static final List<Rarity> RARITIES = new ArrayList<>();
	
	static {
		
	}
	
	public static void initialize() {
		// TODO finish later. but use meta data to populate the table map
		CHESTS_BY_RARITY_FLAGS.put(Rarity.COMMON, ChestEnvironment.SURFACE, TreasureBlocks.WOOD_CHEST);
		
		// setup chest collection generator maps
//		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(COMMON).isEnableChest()) {
//			RARITIES.add(COMMON);
			CHEST_GENS.put(Rarity.COMMON, WorldGenerators.SURFACE_CHEST, new RandomWeightedCollection<>());
			CHEST_GENS.get(Rarity.COMMON, WorldGenerators.SURFACE_CHEST).add(1, ChestGeneratorType.COMMON.getChestGenerator());
//		}
	}
}

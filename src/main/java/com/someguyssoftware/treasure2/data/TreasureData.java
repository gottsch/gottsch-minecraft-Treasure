/**
 * 
 */
package com.someguyssoftware.treasure2.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.chest.ChestEnvironment;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.CommonChestGenerator;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.pit.AirPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaSideTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.LavaTrapPitGenerator;
import com.someguyssoftware.treasure2.generator.pit.SimplePitGenerator;
import com.someguyssoftware.treasure2.generator.pit.TntTrapPitGenerator;

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
	
	// the pit chestGeneratorsMap
	public static Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGens =  HashBasedTable.create();
		
	// TODO setup as map by WorldGenerators
	public static final List<Rarity> RARITIES = new ArrayList<>();
	
	public static final Map<WorldGenerators, List<Rarity>> RARITIES_MAP = new HashMap<>();
	
	static {
		
	}
	
	public static void initialize() {
		// TODO finish later. but use meta data to populate the table map
		CHESTS_BY_RARITY_FLAGS.put(Rarity.COMMON, ChestEnvironment.SURFACE, TreasureBlocks.WOOD_CHEST);
		
		// setup chest collection generator maps
//		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(COMMON).isEnableChest()) {
//			RARITIES.add(COMMON);
			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.COMMON);
			CHEST_GENS.put(Rarity.COMMON, WorldGenerators.SURFACE_CHEST, new RandomWeightedCollection<>());
			CHEST_GENS.get(Rarity.COMMON, WorldGenerators.SURFACE_CHEST).add(1, ChestGeneratorType.COMMON.getChestGenerator());
//		}
			
		// setup pit generators
			pitGens.put(PitTypes.STANDARD, Pits.SIMPLE_PIT, new SimplePitGenerator());
//			pitGens.put(PitTypes.STRUCTURE, Pits.SIMPLE_PIT, new StructurePitGenerator(new SimplePitGenerator()));
			
			pitGens.put(PitTypes.STANDARD, Pits.AIR_PIT,  new AirPitGenerator());
//			pitGens.put(PitTypes.STRUCTURE, Pits.AIR_PIT, new StructurePitGenerator(new AirPitGenerator()));

            pitGens.put(PitTypes.STANDARD, Pits.LAVA_SIDE_TRAP_PIT,  new LavaSideTrapPitGenerator());
            pitGens.put(PitTypes.STANDARD, Pits.LAVA_TRAP_PIT,  new LavaTrapPitGenerator());
            pitGens.put(PitTypes.STANDARD, Pits.TNT_TRAP_PIT,  new TntTrapPitGenerator());
	}
	
	public static void addRarityToMap(WorldGenerators worldGen, Rarity rarity) {
		if (!RARITIES_MAP.containsKey(worldGen)) {
			RARITIES_MAP.put(worldGen, new ArrayList<>());
		}
		if (!RARITIES_MAP.get(worldGen).contains(rarity)) {
			RARITIES_MAP.get(worldGen).add(rarity);
		}
	}
}

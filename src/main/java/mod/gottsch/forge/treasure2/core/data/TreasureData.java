/**
 * 
 */
package mod.gottsch.forge.treasure2.core.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.someguyssoftware.gottschcore.random.RandomWeightedCollection;
import com.someguyssoftware.gottschcore.random.WeightedCollection;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.chest.ChestEnvironment;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig.Chests.ChestCollection;
import mod.gottsch.forge.treasure2.core.enums.ChestGeneratorType;
import mod.gottsch.forge.treasure2.core.enums.PitTypes;
import mod.gottsch.forge.treasure2.core.enums.Pits;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.WorldGenerators;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.chest.IChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.LegendaryChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.chest.MythicalChestGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.AirPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.BigBottomMobTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.CollapsingTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.LavaSideTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.LavaTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.MobTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.SimplePitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.StructurePitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.TntTrapPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.VolcanoPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.IWellGenerator;
import mod.gottsch.forge.treasure2.core.generator.well.WellGenerator;
import mod.gottsch.forge.treasure2.core.random.LevelWeightedCollection;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.RegistryType;
import mod.gottsch.forge.treasure2.core.registry.SimpleListRegistry;
import net.minecraft.block.Block;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
// TODO rename to TreasureGenerators and move to base .generator package
public class TreasureData {
	// chest map by rarity and mapping flag - ** possible replacement for CHESTS_BY_RARITY **
	//	public static final Table<Rarity, ChestEnvironment, Block> CHESTS_BY_RARITY_FLAGS = HashBasedTable.create();

	// chest map by rarity
	public static final Multimap<Rarity, Block> CHESTS_BY_RARITY= ArrayListMultimap.create();

	// chest map by name
	public static final HashMap<String, Block> CHESTS_BY_NAME = new HashMap<>();

	// chest generators by rarity and environment
	public static final Table<Rarity, WorldGenerators, WeightedCollection<Number, IChestGenerator>> CHEST_GENS = HashBasedTable.create();

	// the pit chestGeneratorsMap
	public static final Table<PitTypes, Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> PIT_GENS =  HashBasedTable.create();

	// well generator(s)
	public static final IWellGenerator<GeneratorResult<GeneratorData>> WELL_GEN = new WellGenerator();

	public static final List<Rarity> RARITIES = new ArrayList<>();

	public static final Map<WorldGenerators, LevelWeightedCollection<Rarity>> RARITIES_MAP = new HashMap<>();

	public static final Map<String, Map<RegistryType, ChestRegistry>> CHEST_REGISTRIES2 = new HashMap<>();

	// simple registries
	public static final Map<String, SimpleListRegistry<ICoords>> WELL_REGISTRIES = new HashMap<>();
	public static final Map<String, SimpleListRegistry<ICoords>> WITHER_TREE_REGISTRIES = new HashMap<>();

	public static void initialize() {
		// TODO finish later. but use meta data to populate the table map
		//		CHESTS_BY_RARITY_FLAGS.put(Rarity.COMMON, ChestEnvironment.SURFACE, TreasureBlocks.WOOD_CHEST);

		// setup chest collection generator maps
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.COMMON).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.COMMON);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.COMMON, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.COMMON));
			CHEST_GENS.put(Rarity.COMMON, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.COMMON, WorldGenerators.SURFACE_CHEST).add(1, ChestGeneratorType.COMMON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.UNCOMMON).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.UNCOMMON);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.UNCOMMON, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.UNCOMMON));
			CHEST_GENS.put(Rarity.UNCOMMON, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.UNCOMMON, WorldGenerators.SURFACE_CHEST).add(1, ChestGeneratorType.UNCOMMON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.SCARCE).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.SCARCE);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.SCARCE, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.SCARCE));
			CHEST_GENS.put(Rarity.SCARCE, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.SCARCE, WorldGenerators.SURFACE_CHEST).add(75, ChestGeneratorType.SCARCE.getChestGenerator());
			CHEST_GENS.get(Rarity.SCARCE, WorldGenerators.SURFACE_CHEST).add(25, ChestGeneratorType.SKULL.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.RARE).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.RARE);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.RARE, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.RARE));
			CHEST_GENS.put(Rarity.RARE, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.RARE, WorldGenerators.SURFACE_CHEST).add(85, ChestGeneratorType.RARE.getChestGenerator());
			CHEST_GENS.get(Rarity.RARE, WorldGenerators.SURFACE_CHEST).add(15, ChestGeneratorType.GOLD_SKULL.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.EPIC).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.EPIC);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.EPIC, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.EPIC));
			CHEST_GENS.put(Rarity.EPIC, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SURFACE_CHEST).add(240, ChestGeneratorType.EPIC.getChestGenerator());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SURFACE_CHEST).add(30, ChestGeneratorType.CRYSTAL_SKULL.getChestGenerator());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SURFACE_CHEST).add(30, ChestGeneratorType.CAULDRON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.LEGENDARY).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.LEGENDARY);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.LEGENDARY, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.LEGENDARY));
			CHEST_GENS.put(Rarity.LEGENDARY, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.LEGENDARY, WorldGenerators.SURFACE_CHEST).add(1, new LegendaryChestGenerator());
		}	
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.MYTHICAL).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SURFACE_CHEST, Rarity.MYTHICAL);
			addRarityToMap2(WorldGenerators.SURFACE_CHEST, Rarity.MYTHICAL, getWeight(TreasureConfig.CHESTS.surfaceChests, Rarity.MYTHICAL));
			CHEST_GENS.put(Rarity.MYTHICAL, WorldGenerators.SURFACE_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.MYTHICAL, WorldGenerators.SURFACE_CHEST).add(1, new MythicalChestGenerator());
		}	

		// submerged chests
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(Rarity.COMMON).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.COMMON);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.COMMON, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.COMMON));
			CHEST_GENS.put(Rarity.COMMON, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.COMMON, WorldGenerators.SUBMERGED_CHEST).add(1, ChestGeneratorType.COMMON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(Rarity.UNCOMMON).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.UNCOMMON);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.UNCOMMON, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.UNCOMMON));
			CHEST_GENS.put(Rarity.UNCOMMON, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.UNCOMMON, WorldGenerators.SUBMERGED_CHEST).add(1, ChestGeneratorType.UNCOMMON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(Rarity.SCARCE).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.SCARCE);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.SCARCE, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.SCARCE));
			CHEST_GENS.put(Rarity.SCARCE, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.SCARCE, WorldGenerators.SUBMERGED_CHEST).add(75, ChestGeneratorType.SCARCE.getChestGenerator());
			CHEST_GENS.get(Rarity.SCARCE, WorldGenerators.SUBMERGED_CHEST).add(25, ChestGeneratorType.SKULL.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(Rarity.RARE).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.RARE);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.RARE, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.RARE));
			CHEST_GENS.put(Rarity.RARE, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.RARE, WorldGenerators.SUBMERGED_CHEST).add(85, ChestGeneratorType.RARE.getChestGenerator());
			CHEST_GENS.get(Rarity.RARE, WorldGenerators.SUBMERGED_CHEST).add(15, ChestGeneratorType.GOLD_SKULL.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.submergedChests.configMap.get(Rarity.EPIC).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.EPIC);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.EPIC, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.EPIC));
			CHEST_GENS.put(Rarity.EPIC, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SUBMERGED_CHEST).add(240, ChestGeneratorType.EPIC.getChestGenerator());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SUBMERGED_CHEST).add(30, ChestGeneratorType.CRYSTAL_SKULL.getChestGenerator());
			CHEST_GENS.get(Rarity.EPIC, WorldGenerators.SUBMERGED_CHEST).add(30, ChestGeneratorType.CAULDRON.getChestGenerator());
		}
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.LEGENDARY).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.LEGENDARY);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.LEGENDARY, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.LEGENDARY));
			CHEST_GENS.put(Rarity.LEGENDARY, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.LEGENDARY, WorldGenerators.SUBMERGED_CHEST).add(1, new LegendaryChestGenerator());
		}	
		if (TreasureConfig.CHESTS.surfaceChests.configMap.get(Rarity.MYTHICAL).isEnableChest()) {
			//			addRarityToMap(WorldGenerators.SUBMERGED_CHEST, Rarity.MYTHICAL);
			addRarityToMap2(WorldGenerators.SUBMERGED_CHEST, Rarity.MYTHICAL, getWeight(TreasureConfig.CHESTS.submergedChests, Rarity.MYTHICAL));
			CHEST_GENS.put(Rarity.MYTHICAL, WorldGenerators.SUBMERGED_CHEST, new WeightedCollection<>());
			CHEST_GENS.get(Rarity.MYTHICAL, WorldGenerators.SUBMERGED_CHEST).add(1, new MythicalChestGenerator());
		}	

		// setup pit generators
		PIT_GENS.put(PitTypes.STANDARD, Pits.SIMPLE_PIT, new SimplePitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.SIMPLE_PIT, new StructurePitGenerator(new SimplePitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.AIR_PIT,  new AirPitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.AIR_PIT, new StructurePitGenerator(new AirPitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.LAVA_SIDE_TRAP_PIT,  new LavaSideTrapPitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.LAVA_SIDE_TRAP_PIT,  new StructurePitGenerator(new LavaSideTrapPitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.LAVA_TRAP_PIT,  new LavaTrapPitGenerator());

		PIT_GENS.put(PitTypes.STANDARD, Pits.TNT_TRAP_PIT,  new TntTrapPitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.TNT_TRAP_PIT,  new StructurePitGenerator(new TntTrapPitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.MOB_TRAP_PIT,  new MobTrapPitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.MOB_TRAP_PIT,  new StructurePitGenerator(new MobTrapPitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.BIG_BOTTOM_MOB_TRAP_PIT,  new BigBottomMobTrapPitGenerator());

		PIT_GENS.put(PitTypes.STANDARD, Pits.COLLAPSING_TRAP_PIT,  new CollapsingTrapPitGenerator());
		PIT_GENS.put(PitTypes.STRUCTURE, Pits.COLLAPSING_TRAP_PIT, new StructurePitGenerator(new CollapsingTrapPitGenerator()));

		PIT_GENS.put(PitTypes.STANDARD, Pits.VOLCANO_PIT,  new VolcanoPitGenerator());

		for (String dimension : TreasureConfig.GENERAL.dimensionsWhiteList.get()) {
			Treasure.LOGGER.debug("white list dimension -> {}", dimension);
			// old
			//			CHEST_REGISTRIES.put(dimension, new ChestRegistry());

			// new
			Map<RegistryType, ChestRegistry> chestRegistryMap = new HashMap<>();
			chestRegistryMap.put(RegistryType.SURFACE, new ChestRegistry(TreasureConfig.CHESTS.surfaceChestGen.registrySize.get()));
			chestRegistryMap.put(RegistryType.SUBMERGED, new ChestRegistry(TreasureConfig.CHESTS.submergedChestGen.registrySize.get()));
			CHEST_REGISTRIES2.put(dimension, chestRegistryMap);

			WELL_REGISTRIES.put(dimension, new SimpleListRegistry<>(TreasureConfig.WELLS.registrySize.get()));
			WITHER_TREE_REGISTRIES.put(dimension, new SimpleListRegistry<>(TreasureConfig.WITHER_TREE.registrySize.get()));
		}
	}

	private static Integer getWeight(ChestCollection chestCol, Rarity rarity) {
		return chestCol.configMap.get(rarity).getWeight();
	}

	//	public static void addRarityToMap(WorldGenerators worldGen, Rarity rarity) {
	//		if (!RARITIES_MAP.containsKey(worldGen)) {
	//			RARITIES_MAP.put(worldGen, new ArrayList<>());
	//		}
	//		if (!RARITIES_MAP.get(worldGen).contains(rarity)) {
	//			RARITIES_MAP.get(worldGen).add(rarity);
	//		}
	//	}

	// NEW
	public static void addRarityToMap2(WorldGenerators worldGen, Rarity rarity, Integer weight) {
		if (!RARITIES_MAP.containsKey(worldGen)) {
			RARITIES_MAP.put(worldGen, new LevelWeightedCollection<>());
		}
		RARITIES_MAP.get(worldGen).add(weight, rarity);
	}
}

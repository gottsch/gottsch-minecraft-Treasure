/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.config.ILoggerConfig;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresMcRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * About Categories / Sub-categories: See
 * https://mcforge.readthedocs.io/en/latest/config/annotations/#sub-categories Note that only the
 * 1st level sub-category from parent needs to be a static class referenced by a static field. Any
 * child n-level sub-categories need to be non-static field references. The inner classes themselves
 * can be static or non-static.
 * 
 * Implements IConfig and ILoggerConfig to be backwards compatible with ealier GottschCore versions.
 * 
 * @author Mark Gottschling on Sep 4, 2019
 *
 */
@Config(modid = Treasure.MODID, name = Treasure.MODID + "/" + Treasure.MODID + "-"
		+ TreasureConfig.CONFIG_VERSION, type = Type.INSTANCE)
public class TreasureConfig implements IConfig, ILoggerConfig {
	@Ignore
	public static final String CONFIG_VERSION = "c1.0";

	// @formatter:off
	/*
	 *  IDs
	 */
	// tab
	@Ignore public static final String TREASURE_TAB_ID = "treasure_tab";
	// chests
	@Ignore public static final String WOOD_CHEST_ID = "wood_chest";
	@Ignore public static final String CRATE_CHEST_ID = "crate_chest";
	@Ignore public static final String MOLDY_CRATE_CHEST_ID = "crate_chest_moldy";
	@Ignore public static final String IRONBOUND_CHEST_ID = "ironbound_chest";
	@Ignore public static final String PIRATE_CHEST_ID = "pirate_chest";
	@Ignore public static final String IRON_STRONGBOX_ID = "iron_strongbox";
	@Ignore public static final String GOLD_STRONGBOX_ID = "gold_strongbox";
	@Ignore public static final String SAFE_ID = "safe";
	@Ignore public static final String DREAD_PIRATE_CHEST_ID = "dread_pirate_chest";
	@Ignore public static final String COMPRESSOR_CHEST_ID = "compressor_chest";
	@Ignore public static final String WITHER_CHEST_ID = "wither_chest";
	@Ignore public static final String WITHER_CHEST_TOP_ID = "wither_chest_top";
	@Ignore public static final String SKULL_CHEST_ID = "skull_chest";
	@Ignore public static final String GOLD_SKULL_CHEST_ID = "gold_skull_chest";
	@Ignore public static final String CAULDRON_CHEST_ID = "cauldron_chest";
	@Ignore public static final String SPIDER_CHEST_ID = "spider_chest";
	@Ignore public static final String VIKING_CHEST_ID = "viking_chest";
	
	// mimics
	@Ignore public static final String WOOD_MIMIC_ID = "wood_mimic";
	@Ignore public static final String PIRATE_MIMIC_ID = "pirate_mimic";

	// locks
	@Ignore public static final String WOOD_LOCK_ID = "wood_lock";
	@Ignore public static final String STONE_LOCK_ID = "stone_lock";
	@Ignore public static final String IRON_LOCK_ID = "iron_lock";
	@Ignore public static final String GOLD_LOCK_ID = "gold_lock";
	@Ignore public static final String DIAMOND_LOCK_ID = "diamond_lock";
	@Ignore public static final String EMERALD_LOCK_ID = "emerald_lock";
	@Ignore public static final String RUBY_LOCK_ID = "ruby_lock";
	@Ignore public static final String SAPPHIRE_LOCK_ID = "sapphire_lock";
	@Ignore public static final String SPIDER_LOCK_ID = "spider_lock";
	@Ignore public static final String WITHER_LOCK_ID = "wither_lock";

	// keys
	@Ignore public static final String WOOD_KEY_ID = "wood_key";
	@Ignore public static final String IRON_KEY_ID = "iron_key";
	@Ignore public static final String GOLD_KEY_ID = "gold_key";
	@Ignore public static final String DIAMOND_KEY_ID = "diamond_key";

	@Ignore public static final String STONE_KEY_ID = "stone_key";
	@Ignore public static final String EMERALD_KEY_ID = "emerald_key";
	@Ignore public static final String RUBY_KEY_ID = "ruby_key";
	@Ignore public static final String SAPPHIRE_KEY_ID = "sapphire_key";
	@Ignore public static final String JEWELLED_KEY_ID = "jewelled_key";
	@Ignore public static final String METALLURGISTS_KEY_ID = "metallurgists_key";
	@Ignore public static final String SKELETON_KEY_ID = "skeleton_key";
	@Ignore public static final String WITHER_KEY_ID = "wither_key";
	@Ignore public static final String SPIDER_KEY_ID = "spider_key";

	@Ignore public static final String PILFERERS_LOCK_PICK_ID = "pilferers_lock_pick";
	@Ignore public static final String THIEFS_LOCK_PICK_ID = "thiefs_lock_pick";

	@Ignore public static final String KEY_RING_ID = "key_ring";

	@Ignore public static final String GOLD_COIN_ID = "gold_coin";
	@Ignore public static final String SILVER_COIN_ID = "silver_coin";

	@Ignore public static final String WHITE_PEARL_ID = "white_pearl";
	@Ignore public static final String BLACK_PEARL_ID = "black_pearl";

	// weapons / armor
	@Ignore public static final String SKULL_SWORD_ID = "skull_sword";
	@Ignore public static final String EYE_PATCH_ID = "eye_patch";

	// wither items
	@Ignore public static final String WITHER_ROOT_ITEM_ID = "wither_root_item";
	@Ignore public static final String WITHER_STICK_ITEM_ID = "wither_stick_item";

	// GRAVESTONES
	@Ignore public static final String GRAVESTONE1_STONE_ID = "gravestone1_stone";
	@Ignore public static final String GRAVESTONE1_COBBLESTONE_ID = "gravestone1_cobblestone";
	@Ignore public static final String GRAVESTONE1_MOSSY_COBBLESTONE_ID = "gravestone1_mossy_cobblestone";
	@Ignore public static final String GRAVESTONE1_POLISHED_GRANITE_ID = "gravestone1_polished_granite";
	@Ignore public static final String GRAVESTONE1_POLISHED_ANDESITE_ID = "gravestone1_polished_andesite";
	@Ignore public static final String GRAVESTONE1_POLISHED_DIORITE_ID = "gravestone1_polished_diorite";
	@Ignore public static final String GRAVESTONE1_OBSIDIAN_ID = "gravestone1_obsidian";

	@Ignore public static final String GRAVESTONE2_STONE_ID = "gravestone2_stone";
	@Ignore public static final String GRAVESTONE2_COBBLESTONE_ID = "gravestone2_cobblestone";
	@Ignore public static final String GRAVESTONE2_MOSSY_COBBLESTONE_ID = "gravestone2_mossy_cobblestone";
	@Ignore public static final String GRAVESTONE2_POLISHED_GRANITE_ID = "gravestone2_polished_granite";
	@Ignore public static final String GRAVESTONE2_POLISHED_ANDESITE_ID = "gravestone2_polished_andesite";
	@Ignore public static final String GRAVESTONE2_POLISHED_DIORITE_ID = "gravestone2_polished_diorite";
	@Ignore public static final String GRAVESTONE2_OBSIDIAN_ID = "gravestone2_obsidian";

	@Ignore public static final String GRAVESTONE3_STONE_ID = "gravestone3_stone";
	@Ignore public static final String GRAVESTONE3_COBBLESTONE_ID = "gravestone3_cobblestone";
	@Ignore public static final String GRAVESTONE3_MOSSY_COBBLESTONE_ID = "gravestone3_mossy_cobblestone";
	@Ignore public static final String GRAVESTONE3_POLISHED_GRANITE_ID = "gravestone3_polished_granite";
	@Ignore public static final String GRAVESTONE3_POLISHED_ANDESITE_ID = "gravestone3_polished_andesite";
	@Ignore public static final String GRAVESTONE3_POLISHED_DIORITE_ID = "gravestone3_polished_diorite";
	@Ignore public static final String GRAVESTONE3_OBSIDIAN_ID = "gravestone3_obsidian";

	@Ignore public static final String SKULL_CROSSBONES_ID = "skull_and_crossbones";
	@Ignore public static final String SKELETON_ID = "skeleton";

	@Ignore public static final String WISHING_WELL_BLOCK_ID = "wishing_well_block";
	@Ignore public static final String DESERT_WISHING_WELL_BLOCK_ID = "desert_wishing_well_block";
	@Ignore public static final String FOG_BLOCK_ID = "fog";
	@Ignore public static final String HIGH_FOG_BLOCK_ID = "high_fog";
	@Ignore public static final String MED_FOG_BLOCK_ID = "med_fog";
	@Ignore public static final String LOW_FOG_BLOCK_ID = "low_fog";
	@Ignore public static final String WITHER_FOG_ID = "wither_fog";
	@Ignore public static final String HIGH_WITHER_FOG_ID = "high_wither_fog";
	@Ignore public static final String MED_WITHER_FOG_ID = "med_wither_fog";
	@Ignore public static final String LOW_WITHER_FOG_ID = "low_wither_fog";
	@Ignore public static final String POISON_FOG_ID = "poison_fog";
	@Ignore public static final String HIGH_POISON_FOG_ID = "high_poison_fog";
	@Ignore public static final String MED_POISON_FOG_ID = "med_poison_fog";
	@Ignore public static final String LOW_POISON_FOG_ID = "low_poison_fog";

	@Ignore public static final String WITHER_LOG_ID = "wither_log";
	@Ignore public static final String WITHER_BRANCH_ID = "wither_branch";
	@Ignore public static final String WITHER_ROOT_ID = "wither_root";
	@Ignore public static final String WITHER_BROKEN_LOG_ID = "wither_broken_log";
	@Ignore public static final String WITHER_LOG_SOUL_ID = "wither_log_soul";

	// paintings
	@Ignore public static final String PAINTING_BLOCKS_BRICKS_ID = "painting_blocks_bricks";
	@Ignore public static final String PAINTING_BLOCKS_COBBLESTONE_ID = "painting_blocks_cobblestone";
	@Ignore public static final String PAINTING_BLOCKS_DIRT_ID = "painting_blocks_dirt";
	@Ignore public static final String PAINTING_BLOCKS_LAVA_ID = "painting_blocks_lava";
	@Ignore public static final String PAINTING_BLOCKS_SAND_ID = "painting_blocks_sand";
	@Ignore public static final String PAINTING_BLOCKS_WATER_ID = "painting_blocks_water";
	@Ignore public static final String PAINTING_BLOCKS_WOOD_ID = "painting_blocks_wood";

	@Ignore public static final String SPANISH_MOSS_BLOCK_ID = "spanish_moss";
	@Ignore public static final String SPANISH_MOSS_ITEM_ID = "spanish_moss";
	@Ignore public static final String FALLING_GRASS_ID = "falling_grass";
	@Ignore public static final String FALLING_SAND_ID = "falling_sand";
	@Ignore public static final String FALLING_RED_SAND_ID = "falling_red_sand";
	
	@Ignore public static final String WITHER_PLANKS_ID = "wither_planks";

	@Ignore public static final String SAPPHIRE_ORE_ID = "sapphire_ore";
	@Ignore public static final String SAPPHIRE_ID = "sapphire";
	@Ignore public static final String RUBY_ORE_ID = "ruby_ore";
	@Ignore public static final String RUBY_ID = "ruby";

	@Ignore public static final String PROXIMITY_SPAWNER_ID = "proximity_spawner";

	@Ignore public static final String TREASURE_TOOL_ITEM_ID = "treasure_tool";

	// TEs
	@Ignore public static final String WOOD_CHEST_TE_ID = "wood_chest_tile_entity";
	@Ignore public static final String CRATE_CHEST_TE_ID = "crate_chest_tile_entity";
	@Ignore public static final String MOLDY_CRATE_CHEST_TE_ID = "crate_chest_moldy_tile_entity";
	@Ignore public static final String IRONBOUND_CHEST_TE_ID = "ironbound_chest_tile_entity";
	@Ignore public static final String PIRATE_CHEST_TE_ID = "pirate_chest_tile_entity";
	@Ignore public static final String IRON_STRONGBOX_TE_ID = "iron_strongbox_tile_entity";
	@Ignore public static final String GOLD_STRONGBOX_TE_ID = "gold_strongbox_tile_entity";
	@Ignore public static final String SAFE_TE_ID = "safe_tile_entity";
	@Ignore public static final String DREAD_PIRATE_CHEST_TE_ID = "dread_pirate_chest_tile_entity";
	@Ignore public static final String WHALE_BONE_PIRATE_CHEST_TE_ID = "whale_bone_pirate_chest_tile_entity";
	@Ignore public static final String COMPRESSOR_CHEST_TE_ID = "compressor_chest_tile_entity";
	@Ignore public static final String WITHER_CHEST_TE_ID = "wither_chest_tile_entity";
	@Ignore public static final String SKULL_CHEST_TE_ID = "skull_chest_tile_entity";
	@Ignore public static final String GOLD_SKULL_CHEST_TE_ID = "gold_skull_chest_tile_entity";
	@Ignore public static final String CAULDRON_CHEST_TE_ID = "cauldron_chest_tile_entity";
	@Ignore public static final String OYSTER_CHEST_TE_ID = "oyster_chest_tile_entity";
	@Ignore public static final String CLAM_CHEST_TE_ID = "clam_chest_tile_entity";
	@Ignore public static final String SPIDER_CHEST_TE_ID = "spider_chest_tile_entity";
	@Ignore public static final String VIKING_CHEST_TE_ID = "viking_chest_tile_entity";
	@Ignore public static final String PROXIMITY_SPAWNER_TE_ID = "proximity_spawner_tile_entity";
	@Ignore public static final String GRAVESTONE_TE_ID = "gravestone_tile_entity";
	@Ignore public static final String GRAVESTONE_PROXIMITY_SPAWNER_TE_ID = "gravestone_proximity_spawner_tile_entity";
	///////////////////////////////////////////////////////////////////////////////////////////////////
	// @formatter:on
	@Name("01 mod")
	@Comment({ "General mod properties." })
	public static final ModConfig MOD = new ModConfig();

	@Name("02 logging")
	@Comment({ "Logging properties" })
	public static final LoggerConfig LOGGING = new LoggerConfig();

	@Name("03 chests")
	@Comment({ "Chest properties" })
	public static final Chests CHESTS = new Chests();

	//	@Name("04 actual chests")
	//	public static final ActualChests ACTUAL_CHESTS = new ActualChests();

	@Name("04 wells")
	@Comment({ "Well properties" })
	public static final Well WELL = new Well();

	@Name("05 wither tree")
	@Comment({ "Wither tree properties" })
	public static final WitherTree WITHER_TREE = new WitherTree();

	@Name("06 pits")
	@Comment({ "Pit properties" })
	public static final Pits PIT = new Pits();

	@Name("07 keys and locks")
	@Comment({ "Keys and Locks properties" })
	public static final KeysAndLocks KEYS_LOCKS = new KeysAndLocks();

	@Name("07b coins and valuables")
	@Comment({ "Coins and Valuables properties" })
	public static final Coins COINS = new Coins();

	@Name("08 gems and ores")
	@Comment({ "Gems and Ores properties" })
	public static final GemsAndOres GEMS_ORES = new GemsAndOres();

	@Name("09 world generation")
	@Comment("World generation properties")
	public static final WorldGen WORLD_GEN = new WorldGen();

	@Name("10 foreign mods")
	@Comment("Foreign mod properties")
	public static final ForeignModEnablements FOREIGN_MODS = new ForeignModEnablements();

	@Name("11 oasis")
	@Comment("Oasis properties")
	public static final Oases OASES = new Oases();

	@Ignore
	public static TreasureConfig instance = new TreasureConfig();

	/**
	 * 
	 */
	public TreasureConfig() {
	}

	/**
	 * 
	 */
	public static void init() {
		// load raw arrays into lists
		TreasureConfig.CHESTS.init();
		TreasureConfig.WELL.init();
		TreasureConfig.WITHER_TREE.init();
		TreasureConfig.WORLD_GEN.init();
		TreasureConfig.OASES.init();
	}

	/*
	 * 
	 */
	public static class ForeignModEnablements {
		@Comment({ "Add mod's MODID to this list to enable custom loot tables for a mod." })
		@Name("01. Foreign mod IDs for custom loot tables:")
		public String[] enableForeignModIDs = new String[] { "mocreatures", "sgs_metals" };
		@Comment({ "A list of mods that have prebuilt loot tables available.",
		"Note: used for informational purposes only." })
		@Name("02. Pre-build loot tables for foreign mod IDs:")
		public String[] availableForeignModLootTables = new String[] { "mocreatures", "sgs_metals" };
	}

	/*
	 * 
	 */
	public static class Chests {
		@Comment({ "Chests that generate on land.",
		"Note: There is a build-in check against ocean biomes for surface chests. Adding ocean biomes to the white lists will not change this functionality." })
		@Name("01 Surface Chests")
		public ChestCollection surfaceChests;
		@Comment({ "Chests that generate underwater (in ocean biomes).",
		"Note: There is a build-in check to only allow ocean biomes for submerged chests. Adding other biomes to the white lists will not change this functionality." })
		@Name("02 Submerged Chests")
		public ChestCollection submergedChests;

		@Comment({
			"The number of chests that are monitored. Most recent additions replace least recent when the registry is full.",
		"This is the set of chests used to measure distance between newly generated chests." })
		@Name("01. Max. size of chest registry:")
		@RangeInt(min = 5, max = 100)
		@RequiresMcRestart
		public int chestRegistrySize = 25;

		/*
		 * 
		 */
		public Chests() {
			// ocean names:
			// "ocean", "deep_ocean", "deep_frozen_ocean",
			// "cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean"

			// setup surface properties
			Map<Rarity, ChestConfig> configs = new HashMap<>();
			configs.put(Rarity.COMMON, new ChestConfig(true, 75, 85, 50, new String[] {}, new String[] {},
					new String[] {}, new String[] {}));
			configs.put(Rarity.UNCOMMON, new ChestConfig(true, 150, 75, 40, new String[] {}, new String[] {},
					new String[] {}, new String[] {}));
			configs.put(Rarity.SCARCE, new ChestConfig(true, 300, 50, 30, new String[] {}, new String[] {},
					new String[] {}, new String[] {}));
			configs.put(Rarity.RARE, new ChestConfig(true, 500, 25, 20, new String[] {},
					new String[] { "plains", "sunflower_plains" }, new String[] {}, new String[] { "plains" }));
			configs.put(Rarity.EPIC, new ChestConfig(true, 800, 15, 10, new String[] {},
					new String[] { "plains", "sunflower_plains" }, new String[] {}, new String[] { "plains" }));

			surfaceChests = new ChestCollection(configs);

			// setup submerged properties
			configs.clear();
			configs.put(Rarity.COMMON, new ChestConfig(false, 150, 85, 40, new String[] {}, new String[] {},
					new String[] {}, new String[] {}));
			configs.put(Rarity.UNCOMMON, new ChestConfig(false, 3000, 75, 30, new String[] {}, new String[] {},
					new String[] {}, new String[] {}));
			configs.put(Rarity.SCARCE,
					new ChestConfig(true, 400, 50, 20,
							new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean", "deep_cold_ocean",
									"lukewarm_ocean", "warm_ocean" },
							new String[] {}, new String[] { "ocean", "deep_ocean" }, new String[] {}));
			configs.put(Rarity.RARE,
					new ChestConfig(true, 600, 25, 5,
							new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean", "deep_cold_ocean",
									"lukewarm_ocean", "warm_ocean" },
							new String[] {}, new String[] { "ocean", "deep_ocean" }, new String[] {}));
			configs.put(Rarity.EPIC,
					new ChestConfig(true, 1000, 15, 5,
							new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean", "deep_cold_ocean",
									"lukewarm_ocean", "warm_ocean" },
							new String[] {}, new String[] { "ocean", "deep_ocean" }, new String[] {}));

			submergedChests = new ChestCollection(configs);

			// setup extra properties
			surfaceChests.commonChestProperties.mimicProbability = 20.0;
			submergedChests.commonChestProperties.mimicProbability = 0.0;
			surfaceChests.scarceChestProperties.mimicProbability = 15.0;
			submergedChests.scarceChestProperties.mimicProbability = 0.0;
		}

		public void init() {
			this.surfaceChests.init();
			this.submergedChests.init();
		}

		public class ChestCollection implements IChestCollection {
			/*
			 * Map of chest configs by rarity.
			 */
			@Ignore
			public Map<Rarity, IChestConfig> configMap = new HashMap<>();

			@Comment("The minimum number of chunks generated before another attempt to spawn a chest is made.")
			@Name("01. Min. chunks per chest spawn:")
			@RangeInt(min = 0, max = 32000)
			public int minChunksPerChest = 35;

			@Comment({ "The minimum distance, measured in chunks (16x16), that two chests can be in proximity.",
				"Note: Only chests in the chest registry are checked against this property.",
				"Used in conjunction with the chunks per chest and spawn probability.", "Ex. " })
			@Name("02. Min. distance per chest spawn:")
			@RangeInt(min = 0, max = 32000)
			public int minDistancePerChest = 75;

			@Comment({ "The probability chest will appear on the surface, instead of in a pit." })
			@Name("03. Probability of chest spawn on the surface:")
			@RangeInt(min = 0, max = 100)
			public int surfaceChestProbability = 15;

			@Name("01 Common chest")
			public ChestConfig commonChestProperties = new ChestConfig(
					true, 75, 85, 50, new String[] {}, new String[] { "ocean", "deep_ocean", "deep_frozen_ocean",
							"cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean" },
					new String[] {}, new String[] { "ocean", "deep_ocean" });

			@Name("02 Uncommon chest")
			public ChestConfig uncommonChestProperties = new ChestConfig(
					true, 150, 75, 40, new String[] {}, new String[] { "ocean", "deep_ocean", "deep_frozen_ocean",
							"cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean" },
					new String[] {}, new String[] { "ocean", "deep_ocean" });

			@Name("03 Scarce chest")
			public ChestConfig scarceChestProperties = new ChestConfig(true, 300, 50, 30, new String[] {},
					new String[] {}, new String[] {}, new String[] {});

			@Name("04 Rare chest")
			public ChestConfig rareChestProperties = new ChestConfig(true, 500, 25, 20, new String[] {},
					new String[] { "plains", "sunflower_plains" }, new String[] {}, new String[] { "plains" });

			@Name("05 Epic chest")
			public ChestConfig epicChestProperties = new ChestConfig(true, 800, 15, 10, new String[] {},
					new String[] { "plains", "sunflower_plains" }, new String[] {}, new String[] { "plains" });

			/**
			 * 
			 */
			public ChestCollection() {
				configMap.put(Rarity.COMMON, commonChestProperties);
				configMap.put(Rarity.UNCOMMON, uncommonChestProperties);
				configMap.put(Rarity.SCARCE, scarceChestProperties);
				configMap.put(Rarity.RARE, rareChestProperties);
				configMap.put(Rarity.EPIC, epicChestProperties);
			}

			public ChestCollection(Map<Rarity, ChestConfig> configs) {
				commonChestProperties = configs.get(Rarity.COMMON);
				uncommonChestProperties = configs.get(Rarity.UNCOMMON);
				scarceChestProperties = configs.get(Rarity.SCARCE);
				rareChestProperties = configs.get(Rarity.RARE);
				epicChestProperties = configs.get(Rarity.EPIC);

				// update the map
				configMap.put(Rarity.COMMON, commonChestProperties);
				configMap.put(Rarity.UNCOMMON, uncommonChestProperties);
				configMap.put(Rarity.SCARCE, scarceChestProperties);
				configMap.put(Rarity.RARE, rareChestProperties);
				configMap.put(Rarity.EPIC, epicChestProperties);
			}

			/**
			 * 
			 */
			@Override
			public void init() {
				this.commonChestProperties.init();
				this.uncommonChestProperties.init();
				this.scarceChestProperties.init();
				this.rareChestProperties.init();
				this.epicChestProperties.init();
			}
		}

		public interface IChestCollection {
			public void init();
		}
	}

	/*
	 * 
	 */
	public static class Pits {
		@Comment({ "The probability that a pit will contain a structure (treasure room(s), cavern etc.)" })
		@Name("01. Probability of pit structure spawn:")
		@RangeInt(min = 0, max = 100)
		public int pitStructureProbability = 25;
	}

	/*
	 * 
	 */
	public static class Well implements IWellConfig {
		@Comment({ "Toggle to allow/disallow the spawn of well." })
		@Name("01. Enabled wells:")
		public boolean wellAllowed = true;

		@Comment("The minimum number of chunks generated before another attempt to spawn a well is made.")
		@Name("02. Chunks per well spawn:")
		@RangeInt(min = 100, max = 32000)
		public int chunksPerWell = 400;

		@Comment({ "The probability that a well will generate." })
		@Name("03. Generation probability:")
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability = 80.0;

		@Name("biomes")
		@Comment({ "Biome white and black list properties." })
		public BiomesConfig biomes = new BiomesConfig(
				new String[] {}, new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean",
						"deep_cold_ocean", "lukewarm_ocean", "warm_ocean" },
				new String[] {}, new String[] { "ocean", "deep_ocean" });

		/**
		 * 
		 */
		@Override
		public void init() {
			this.biomes.init();
		}

		@Override
		public boolean isWellAllowed() {
			return wellAllowed;
		}

		@Override
		public int getChunksPerWell() {
			return chunksPerWell;
		}

		@Override
		public double getGenProbability() {
			return genProbability;
		}

		@Override
		public List<Biome> getBiomeWhiteList() {
			return biomes.getWhiteList();
		}

		@Override
		public List<Biome> getBiomeBlackList() {
			return biomes.getBlackList();
		}

		@Override
		public List<BiomeTypeHolder> getBiomeTypeWhiteList() {
			return biomes.getTypeWhiteList();
		}

		@Override
		public List<BiomeTypeHolder> getBiomeTypeBlackList() {
			return biomes.getTypeBlackList();
		}

	}

	/*
	 * 
	 */
	public static class WitherTree implements IWitherTreeConfig {
		@Comment({ "The number of chunks generated before a wither tree spawn is attempted." })
		@Name("01. Chunks per wither tree spawn:")
		@RangeInt(min = 200, max = 32000)
		public int chunksPerTree = 800;
		@Comment({ "The probability that a wither tree will spawn." })
		@Name("02. Probability of wither tree spawn:")
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability = 90.0;
		@Comment({ "The max. height a wither tree can reach.",
			"This is the high end of a calculated range. ex size is randomized between minTrunkSize and maxTrunkSize.",
		"(The min. is prefined.)" })
		@Name("03. Max. trunk height (in blocks):")
		@RangeInt(min = 7, max = 20)
		public int maxTrunkSize = 13;
		@Comment({ "The minimum number of supporting wither trees that surround the main tree in the grove." })
		@Name("04. Min. supporting trees:")
		@RangeInt(min = 0, max = 30)
		public int minSupportingTrees = 5;
		@Comment({ "The maximum number of supporting wither trees that surround the main tree in the grove." })
		@Name("05. Max. supporting trees:")
		@RangeInt(min = 0, max = 30)
		public int maxSupportingTrees = 15;

		@Comment("The probability that a wither branch item will drop when a wither branch is harvested.")
		@Name("06. Probability of branch item on harvest:")
		@RangeDouble(min = 0.0, max = 100.0)
		public double witherBranchItemGenProbability = 50.0;
		@Comment("The probability that a wither root item will drop when a wither root is harvested.")
		@Name("07. Probability of root item on harvest:")
		@RangeDouble(min = 0.0, max = 100.0)
		public double witherRootItemGenProbability = 50.0;

		// TODO add all other wither tree probabilities ie coarse dirt gen, etc.

		@Name("biomes")
		@Comment({ "Biome white and black list properties." })
		public BiomesConfig biomes = new BiomesConfig(new String[] {},
				new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean", "deep_cold_ocean",
						"lukewarm_ocean", "warm_ocean", "ice_spikes", "plains", "sunflower_plains", "the_void",
						"nether", "gravelly_mountanis", "modified_gravelly_mountains" },
				new String[] {}, new String[] {});

		/**
		 * 
		 */
		@Override
		public void init() {
			this.biomes.init();
		}

		@Override
		public int getChunksPerTree() {
			return chunksPerTree;
		}

		@Override
		public double getGenProbability() {
			return genProbability;
		}

		@Override
		public int getMaxTrunkSize() {
			return maxTrunkSize;
		}

		@Override
		public int getMinSupportingTrees() {
			return minSupportingTrees;
		}

		@Override
		public int getMaxSupportingTrees() {
			return maxSupportingTrees;
		}

		@Override
		public double getWitherBranchItemGenProbability() {
			return witherBranchItemGenProbability;
		}

		@Override
		public double getWitherRootItemGenProbability() {
			return witherRootItemGenProbability;
		}

		@Override
		public List<Biome> getBiomeWhiteList() {
			return biomes.getWhiteList();
		}

		@Override
		public List<Biome> getBiomeBlackList() {
			return biomes.getBlackList();
		}

		@Override
		public List<BiomeTypeHolder> getBiomeTypeWhiteList() {
			return biomes.getTypeWhiteList();
		}

		@Override
		public List<BiomeTypeHolder> getBiomeTypeBlackList() {
			return biomes.getTypeBlackList();
		}
	}

	/*
	 * 
	 */
	public static class Oases {
		@Comment({"The minimum number of chunks generated before another attempt to spawn any type of oasis is made.",
		"Ex. Both desert oasis and floating oasis have met their respective thresholds to spawn, but minChunksPerOasis was recently reset and hasn't met its threshold, therefor preventing a new oasis to spawn."})
		@Name("01. Min. chunks per oasis spawn:")
		@RangeInt(min = 0, max = 32000)
		public int minChunksPerOasis = 100;

		@Comment({ "The minimum distance, measured in blocks, that two oasis can be in proximity.",
			"Note: Only oases in the oasis registry are checked against this property.",
			"Used in conjunction with the chunks per oasis and spawn probability.", "Ex. " })
		@Name("02. Min. distance per oasis spawn:")
		@RangeInt(min = 0, max = 32000)
		public int minDistancePerOasis = 500;

		@Comment({
			"The number of oases that are monitored. Most recent additions replace least recent when the registry is full.",
		"This is the set of oases used to measure distance between newly generated oases." })
		@Name("03. Max. size of oasis registry:")
		@RangeInt(min = 5, max = 100)
		@RequiresMcRestart
		public int oasisRegistrySize = 25;

		@Name("01 Desert oasis")
		public OasisConfig desertOasisProperties = new OasisConfig(
				true, 1000, 80, 
				new String[] {"desert", "desert_hills", "desert_lakes", "badlands", 
						"badlands_plateau"}, 
				new String[] {},
				new String[] {"mesa", "dry"},
				new String[] { });

		/**
		 * 
		 */
		public void init() {
			this.desertOasisProperties.init();
		}
	}

	/*
	 * 
	 */
	public static class Coins {
		@Comment("The maximum size of a coin item stack.")
		@Name("01. Max Stack Size:")
		@RangeInt(min = 1, max = 64)
		@RequiresMcRestart
		public int maxStackSize = 8;
	}

	/*
	 * 
	 */
	public static class GemsAndOres {
		@Comment({ "Enable/Disable whether a gem ore will spawn." })
		@Name("00. Enable gem ore spawn:")
		public boolean enableGemOreSpawn = true;

		@Comment("The number of chunks generated before another attempt to generate a gem ore spawn is made.")
		@Name("01. Chunks per gem ore spawn:")
		@RangeInt(min = 1, max = 32000)
		public int chunksPerGemOre = 1;

		@Comment({ "The probability that a ruby ore will spawn." })
		@Name("02. Probability of ruby ore spawn.")
		@RangeDouble(min = 0.0, max = 100.0)
		public double rubyGenProbability = 65.0;

		@Comment({ "The max. y-value where a ruby ore can spawn." })
		@Name("03. Max. y-value for ruby ore spawn location:")
		@RangeInt(min = 1, max = 255)
		public int rubyOreMaxY = 14;

		@Comment({ "The min. y-value where a ruby ore can spawn." })
		@Name("04. Min. y-value for ruby ore spawn location:")
		@RangeInt(min = 1, max = 255)
		public int rubyOreMinY = 6;

		@Comment({ "The number of ruby ore blocks in a vein." })
		@Name("05. Ruby ore vein size:")
		@RangeInt(min = 1, max = 20)
		@RequiresMcRestart
		public int rubyOreVeinSize = 3;

		@Comment({ "The number of ruby ore veins in a chunk." })
		@Name("06. Ruby ore veins per chunk.")
		@RangeInt(min = 1, max = 20)
		public int rubyOreVeinsPerChunk = 1;

		@Comment({ "The probability that a sapphire ore will spawn." })
		@Name("07. Probability of sapphire ore spawn.")
		@RangeDouble(min = 0.0, max = 100.0)
		public double sapphireGenProbability = 65.0;

		@Comment({ "The max. y-value where a sapphire ore can spawn." })
		@Name("08. Max. y-value for sapphire ore spawn location:")
		@RangeInt(min = 1, max = 255)
		public int sapphireOreMaxY = 14;

		@Comment({ "The min. y-value where a sapphire ore can spawn." })
		@Name("09. Min. y-value for sapphire ore spawn location:")
		@RangeInt(min = 1, max = 255)
		public int sapphireOreMinY = 6;

		@Comment({ "The number of sapphire ore blocks in a vein." })
		@Name("10. Sapphire ore vein size:")
		@RangeInt(min = 1, max = 20)
		@RequiresMcRestart
		public int sapphireOreVeinSize = 3;

		@Comment({ "The number of sapphire ore veins in a chunk." })
		@Name("11. Sapphire ore veins per chunk.")
		@RangeInt(min = 1, max = 20)
		public int sapphireOreVeinsPerChunk = 1;
	}

	/*
	 * 
	 */
	public static class KeysAndLocks {
		@Comment({ "Enable/Disable whether a Key can break when attempting to unlock a Lock." })
		@Name("01. Enable key breaks:")
		public boolean enableKeyBreaks = true;

		@Comment({ "Enable/Disable whether a Lock item is dropped when unlocked by Key item." })
		@Name("02. Enable lock drops:")
		public boolean enableLockDrops = true;

		@Comment({ "The maximum uses for a given pilferers lock pick." })
		@Name("03. Pilferer's lockpick max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int pilferersLockPickMaxUses = 10;

		@Comment({ "The maximum uses for a given thiefs lock pick." })
		@Name("04. Thief's lockpick max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int thiefsLockPickMaxUses = 10;

		@Comment({ "The maximum uses for a given wooden key." })
		@Name("05. Wood key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int woodKeyMaxUses = 20;

		@Comment({ "The maximum uses for a given stone key." })
		@Name("06. Stone key max uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int stoneKeyMaxUses = 10;

		@Comment({ "The maximum uses for a given iron key." })
		@Name("07. Iron key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int ironKeyMaxUses = 10;

		@Comment({ "The maximum uses for a given gold key." })
		@Name("08. Gold key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int goldKeyMaxUses = 15;

		@Comment({ "The maximum uses for a given diamond key." })
		@Name("09. Diamond key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int diamondKeyMaxUses = 20;

		@Comment({ "The maximum uses for a given emerald key." })
		@Name("10. Emerald key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int emeraldKeyMaxUses = 10;

		@Comment({ "The maximum uses for a given ruby key." })
		@Name("11. Ruby key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int rubyKeyMaxUses = 5;

		@Comment({ "The maximum uses for a given sapphire key." })
		@Name("12. Sapphire key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int sapphireKeyMaxUses = 5;

		@Comment({ "The maximum uses for a given metallurgists key." })
		@Name("13. Metallurgists key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int metallurgistsKeyMaxUses = 25;

		@Comment({ "The maximum uses for a given skeleton key." })
		@Name("14. Skeleton key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int skeletonKeyMaxUses = 5;

		@Comment({ "The maximum uses for a given jewelled key." })
		@Name("15. Jewelled Key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int jewelledKeyMaxUses = 5;

		@Comment({ "The maximum uses for a given spider key." })
		@Name("16. Spider key max uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int spiderKeyMaxUses = 5;

		@Comment({ "The maximum uses for a given wither key." })
		@Name("17. Wither key max. uses:")
		@RangeInt(min = 1, max = 32000)
		@RequiresMcRestart
		public int witherKeyMaxUses = 5;
	}

	/*
	 * 
	 */
	public static class WorldGen {

		@Name("01 general")
		public GeneralProperties generalProperties = new GeneralProperties();

		@Name("02 markers")
		public MarkerProperties markerProperties = new MarkerProperties();

		public class GeneralProperties {
			@Comment({ "Enable/Disable whether a fog is generated (ex. around graves/tombstones and wither trees)" })
			@Name("01. Enable fog:")
			public boolean enableFog = true;
			@Comment({ "Enable/Disable whether a wither fog is generated (ex. around wither trees)" })
			@Name("02. Enable wither fog:")
			public boolean enableWitherFog = true;
			@Comment({ "Enable/Disable whether a poison fog is generated (ex. around wither trees)" })
			@Name("03. Enable poison fog:")
			public boolean enablePoisonFog = true;

			//			@Comment({"This is a temporary property.", "@since v1.5.0."})
			//			@Name("04. Probability of (under)water structure spawn:")
			//			@RangeDouble(min = 0.0, max = 100.0)
			//			public double waterStructureProbability = 50.0;

			@Comment({ "The probability that a surface structure will generate." })
			@Name("04. Probability of surface structure spawn:")
			@RangeInt(min = 0, max = 100)
			public int surfaceStructureProbability = 25;

			@Name("05. Dimension white list:")
			@Comment({
			"Allowed Dimensions for generation.\nTreasure2 was designed for \"normal\" overworld-type dimensions.\nThis setting does not use any wildcards (*). You must explicitly set the dimensions that are allowed." })
			public Integer[] rawDimensionsWhiteList = new Integer[] { 0 };
			@Ignore
			public List<Integer> dimensionsWhiteList = new ArrayList<>(3);

			/**
			 * 
			 */
			public void init() {
				TreasureConfig.WORLD_GEN.getGeneralProperties().dimensionsWhiteList = Arrays
						.asList(TreasureConfig.WORLD_GEN.getGeneralProperties().rawDimensionsWhiteList);
			}

			public List<Integer> getDimensionsWhiteList() {
				return dimensionsWhiteList;
			}

			public void setDimensionsWhiteList(List<Integer> dimensionsWhiteList) {
				this.dimensionsWhiteList = dimensionsWhiteList;
			}
		}

		public class MarkerProperties {
			@Comment("Enable/Disable whether grave markers (gravestones, bones)  are generated when generating pits.")
			@Name("01. Enable grave markers:")
			public boolean isGravestonesAllowed = true;

			@Comment("Enable/Disable whether structures (buildings and other non-grave) are generated when generating  pits.")
			@Name("02. Enable structure markers:")
			public boolean isMarkerStructuresAllowed = true;

			@Comment({ "The min. number of grave markers (gravestones, bones) per chest." })
			@Name("03. Min. grave markers per chest:")
			@RangeInt(min = 1, max = 5)
			public int minGravestonesPerChest = 4;

			@Comment({ "The max. number of grave markers (gravestones, bones) per chest." })
			@Name("04. Max. grave markers per chest: ")
			@RangeInt(min = 1, max = 10)
			public int maxGravestonesPerChest = 8;

			@Comment({ "@Deprecated. The probability that a gravestone will have fog." })
			@Name("05. Probability that grave marker has fog:")
			@RangeInt(min = 0, max = 100)
			@Deprecated
			public int gravestoneFogProbability = 50;

			@Comment({ "The probability that a marker will be a structure." })
			@Name("06. Probability that marker will be a structure:")
			@RangeInt(min = 0, max = 100)
			public int markerStructureProbability = 15;

			@Comment("Enable/Disable whether gravestones can spawn mobs (Bound Soul).")
			@Name("07. Enable gravestones to spawn mobs:")
			public boolean isGravestoneSpawnMobAllowed = true;

			@Comment({ "The probability that a gravestone will spawn a mob.",
			"Currently gravestones spawn Bound Souls." })
			@Name("08. Probability that grave marker will spawn a mob:")
			@RangeInt(min = 0, max = 100)
			public int gravestoneMobProbability = 80;
		}

		public MarkerProperties getMarkerProperties() {
			return markerProperties;
		}

		/**
		 * 
		 */
		public void init() {
			this.generalProperties.init();
		}

		public GeneralProperties getGeneralProperties() {
			return generalProperties;
		}

		public void setGeneralProperties(GeneralProperties generalProperties) {
			this.generalProperties = generalProperties;
		}

		public void setMarkerProperties(MarkerProperties markerProperties) {
			this.markerProperties = markerProperties;
		}
	}

	/**
	 * 
	 * @author Mark Gottschling on Nov 18, 2019
	 *
	 */
	@net.minecraftforge.fml.common.Mod.EventBusSubscriber
	public static class EventHandler {
		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(Treasure.MODID)) {
				ConfigManager.sync(Treasure.MODID, Config.Type.INSTANCE);
				TreasureConfig.init();
			}
		}
	}

	@Override
	public boolean isEnableVersionChecker() {
		return MOD.enableVersionChecker;
	}

	@Override
	public String getLatestVersion() {
		return MOD.latestVersion;
	}

	@Override
	public void setEnableVersionChecker(boolean enableVersionChecker) {
		MOD.enableVersionChecker = enableVersionChecker;
	}

	@Override
	public void setLatestVersion(String latestVersion) {
		MOD.latestVersion = latestVersion;

	}

	@Override
	public boolean isLatestVersionReminder() {
		return MOD.latestVersionReminder;
	}

	@Override
	public void setLatestVersionReminder(boolean latestVersionReminder) {
		MOD.latestVersionReminder = latestVersionReminder;
	}

	@Override
	public void setForgeConfiguration(Configuration forgeConfiguration) {
		// NOTE do nothing
	}

	@Override
	public Configuration getForgeConfiguration() {
		return null;
	}

	@Deprecated
	@Override
	public void setProperty(String category, String key, boolean value) {

	}

	@Deprecated
	@Override
	public void setProperty(String category, String key, String value) {

	}

	@Deprecated
	@Override
	public void setProperty(Property property, String value) {

	}

	@Override
	public boolean isModEnabled() {
		return MOD.enabled;
	}

	@Override
	public void setModEnabled(boolean modEnabled) {
		MOD.enabled = modEnabled;
	}

	@Override
	public String getModsFolder() {
		return MOD.folder;
	}

	@Override
	public void setModsFolder(String modsFolder) {
		MOD.folder = modsFolder;
	}

	/*
	 * ILoggerConfig inherited methods
	 */

	@Override
	public String getLoggerLevel() {
		return LOGGING.level;
	}

	@Override
	public String getLoggerFolder() {
		return LOGGING.folder;
	}

	@Override
	public String getLoggerSize() {
		return LOGGING.size;
	}

	@Override
	public String getLoggerFilename() {
		return LOGGING.filename;
	}

	@Override
	public String getConfigFolder() {
		return MOD.configFolder;
	}

	@Override
	public void setConfigFolder(String configFolder) {
		MOD.configFolder = configFolder;
	}

}

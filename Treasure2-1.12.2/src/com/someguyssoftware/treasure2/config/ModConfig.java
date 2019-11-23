/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.someguyssoftware.gottschcore.biome.BiomeHelper;
import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.config.ILoggerConfig;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.biome.TreasureBiomeHelper;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * About Categories / Sub-categories:
 * See https://mcforge.readthedocs.io/en/latest/config/annotations/#sub-categories
 * Note that only the 1st level sub-category from parent needs to be a static class referenced by a static field. Any
 * child n-level sub-categories need to be non-static field references. The inner classes themselves can be static or non-static.
 * 
 * @author Mark Gottschling on Sep 4, 2019
 *
 */
// TODO get version in the config name
@Config(modid = Treasure.MODID, name = Treasure.MODID + "/" + Treasure.MODID, type = Type.INSTANCE)
public class ModConfig implements IConfig, ILoggerConfig {
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

	// mimics
	@Ignore public static final String WOOD_MIMIC_ID = "wood_mimic";

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
	@Ignore public static final String PROXIMITY_SPAWNER_TE_ID = "proximity_spawner_tile_entity";

	///////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Name("01-mod")
	@Comment({"General mod properties."})
	public static final Mod MOD = new Mod();

	@Name("02-logging")
	@Comment({"Logging properties"})
	public static final Logging LOGGING = new Logging();

	@Name("03-chests")
	@Comment({"Chest properties"})
	public static final Chests CHESTS = new Chests();

	@Name("04-wells")
	@Comment({"Well properties"})
	public static final Well WELL = new Well();

	@Name("05-wither tree")
	@Comment({"Wither tree properties"})
	public static final WitherTree WITHER_TREE = new WitherTree();

	@Name("06-pits")
	@Comment({"Pit properties"})
	public static final Pits PIT = new Pits();

	@Name("07-keys and locks")
	@Comment({"Keys and Locks properties"})
	public static final KeysAndLocks KEYS_LOCKS = new KeysAndLocks();

	@Name("08-gems and ores")
	@Comment({"Gems and Ores properties"})
	public static final GemsAndOres GEMS_ORES = new GemsAndOres();

	@Name("09-world generation")
	@Comment("World generation properties")
	public static final WorldGen WORLD_GEN = new WorldGen();

	/*
	 * Map of chest configs by rarity.
	 */
	@Ignore
	public static Map<Rarity, IChestConfig> chestConfigs = new HashMap<>();

	@Ignore public static ModConfig instance = new ModConfig();
	
	static {
		chestConfigs.put(Rarity.COMMON, CHESTS.commonChestProperties);
		chestConfigs.put(Rarity.UNCOMMON, CHESTS.uncommonChestProperties);
		chestConfigs.put(Rarity.SCARCE, CHESTS.scarceChestProperties);
		chestConfigs.put(Rarity.RARE, CHESTS.rareChestProperties);
		chestConfigs.put(Rarity.EPIC, CHESTS.epicChestProperties);
	}

	public ModConfig() {
	}
	
	/*
	 * 
	 */
	public static class Mod {
		@Comment({"The relative path to the mods folder."})
		public String folder = "mods";
		@Comment({"Enables/Disables mod."})
		public boolean enabled = true;
		@Comment({"Enables/Disables version checking."})
		public boolean enableVersionChecker = true;
		@Comment({"The latest published version number.", "This is auto-updated by the version checker.", "This may be @deprecated."})
		public String latestVersion = "";
		@Comment({"Remind the user of the latest version (as indicated in latestVersion proeprty) update."})
		public boolean latestVersionReminder = true;

		@Comment({"The relative path to the default Treasure folder.", "This is where resource files will be located."})
		public String treasureFolder = folder + "/" + Treasure.MODID + "/";

		// this remains as general
		@Comment({"Enable/Disable a check to ensure the default loot tables exist on the file system.", "If enabled, then you will not be able to remove any default loot tables (but they can be edited).", "Only disable if you know what you're doing."})
		public boolean enableDefaultLootTablesCheck = true;
		@Comment({"Enable/Disable a check to ensure the default templates exist on the file system.", "If enabled, then you will not be able to remove any default templates.", "Only disable if you know what you're doing."})
		public boolean enableDefaultTemplatesCheck = true;

		@Name("01-foreign mods")
		public ForeignModEnablements foreignModProperties = new ForeignModEnablements();

		/*
		 * 
		 */
		public class ForeignModEnablements {
			@Comment({"Add mod's MODID to this list to enable custom loot tables for a mod."})
			public String[] enableForeignModIDs = new String[]{"mocreatures", "sgs_metals"};
			@Comment({"A list of mods that have prebuilt loot tables available.", "Note: used for informational purposes only."})
			public String[] availableForeignModLootTables = new String[]{"mocreatures", "sgs_metals"};
		}

		public ForeignModEnablements getForeignModProperties() {
			return foreignModProperties;
		}
	}
	
	/*
	 * 
	 */
	public static class Logging {
		@Comment({"The logging level. Set to 'off' to disable logging.", "Values = [trace|debug|info|warn|error|off]"})
		public String level = "debug";
		@Comment({"The directory where the logs should be stored.", "This is relative to the Minecraft install path."})
		public String folder = "mods/" + Treasure.MODID + "/logs/";
		@Comment({"The size a log file can be before rolling over to a new file."})
		public String size = "1000K";
		@Comment({"The base filename of the  log file."})
		public String filename = Treasure.MODID;
	}

	/*
	 * 
	 */
	public static class Chests {
		@Comment("The minimum number of chunks generated before another attempt to generate a chest is made.")
		@RangeInt(min = 0, max = 32000)
		public int minChunksPerChest = 35;

		@Comment({"The minimum distance, measured in chunks (16x16), that two chests can be in proximity.",
			"Note: Only chests in the chest registry are checked against this property.",
			"Used in conjunction with the chunks per chest and generation probability.",
		"Ex. "})
		@RangeInt(min = 0, max = 32000)
		public int minDistancePerChest = 75;

		@Comment({"The probability chest will appear on the surface."})
		@RangeInt(min = 0, max = 100)
		public int surfaceChestProbability = 15;

		@Comment({"The number of chests that are monitored. Most recent additions replace least recent when the registry is full.", "This is the set of chests used to measure distance between newly generated chests."})
		@RangeInt(min = 5, max = 100)
		@RequiresWorldRestart
		public int chestRegistrySize = 25;

		@Name("01-Common chest")
		public Chest commonChestProperties = new Chest(true, 75, 85, 50);

		@Name("02-Uncommon chest")
		public Chest uncommonChestProperties = new Chest(true, 150, 75, 40);

		@Name("03-Scarce chest")
		public Chest scarceChestProperties = new Chest(true, 300, 50, 30);

		@Name("04-Rare chest")
		public Chest rareChestProperties = new Chest(true, 500, 25, 20);

		@Name("05-Epic chest")
		public Chest epicChestProperties = new Chest(true, 800, 15, 10);

		/*
		 * 
		 */
		public Chests() {
			// setup extra properties
			commonChestProperties.mimicProbability = 20.0;
			commonChestProperties.rawBiomeWhiteList = new String[] {};
			commonChestProperties.rawBiomeBlackList = new String[] {"plains", "ocean", "deep_ocean"};
		}

		/*
		 * 
		 */
		public class Chest implements IChestConfig {
			@Ignore
			public boolean chestAllowed = true;

			@Comment({"The number of chunks generated before the chest generation is attempted."})
			@RangeInt(min = 50, max = 32000)
			public int chunksPerChest = 75;

			@Comment({"The probability that a chest will generate."})
			@RangeDouble(min = 0.0, max = 100.0)
			public double genProbability = 50.0;

			@Comment({"The minimum depth (y-axis) that a chest can generate at."})
			@RangeInt(min = 5, max = 250)
			public int minYSpawn = 25;

			// TODO most likely going to be removed with the use of meta files / archetype : type : biome categorizations
			public boolean surfaceAllowed = true;
			public boolean subterraneanAllowed = true;

			// TODO most likely going to be removed with the use of meta files
			@Comment({"Allowable Biome Types for general Chest generation. Must match the Type identifer(s)."})
			@Name("biomeWhiteList")
			public String[] rawBiomeWhiteList = new String[] {};
			@Comment({"Disallowable Biome Types for general Chest generation. Must match the Type identifer(s)."})
			@Name("biomeBlackList")
			public String[] rawBiomeBlackList = new String[] {"ocean"};

			@Comment({"The probability that a chest will be a mimic.", "NOTE: only common Wooden Chest have mimics avaiable."})
			@RangeDouble(min = 0.0, max = 100.0)
			public double mimicProbability = 0.0;

			@Ignore public List<BiomeTypeHolder> biomeWhiteList = new ArrayList<>();
			@Ignore public List<BiomeTypeHolder> biomeBlackList = new ArrayList<>();

			/*
			 * 
			 */
			public Chest()  {
				BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
				BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
			}

			/*
			 * 
			 */
			public Chest(boolean isAllowed, int chunksPer, double probability, int minYSpawn) {
				this.chestAllowed = isAllowed;
				this.chunksPerChest = chunksPer;
				this.genProbability = probability;
				this.minYSpawn = minYSpawn;
				BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
				BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
			}

			@Override
			public boolean isChestAllowed() {
				return chestAllowed;
			}

			@Override
			public int getChunksPerChest() {
				return chunksPerChest;
			}

			@Override
			public double getGenProbability() {
				return genProbability;
			}

			@Override
			public int getMinYSpawn() {
				return minYSpawn;
			}

			@Override
			public boolean isSurfaceAllowed() {
				return surfaceAllowed;
			}

			@Override
			public boolean isSubterraneanAllowed() {
				return subterraneanAllowed;
			}

			@Override
			public List<BiomeTypeHolder> getBiomeWhiteList() {
				return biomeWhiteList;
			}

			@Override
			public List<BiomeTypeHolder> getBiomeBlackList() {
				return biomeBlackList;
			}

			@Override
			public double getMimicProbability() {
				return mimicProbability;
			}
		}

		public Chest getCommonChestProperties() {
			return commonChestProperties;
		}

		public Chest getScarceChestProperties() {
			return scarceChestProperties;
		}

		public Chest getRareChestProperties() {
			return rareChestProperties;
		}

		public Chest getEpicChestProperties() {
			return epicChestProperties;
		}

		public Chest getUncommonChestProperties() {
			return uncommonChestProperties;
		}
	}

	/*
	 * 
	 */
	public static class Pits {
		@Comment({"The probability that a pit will contain a structure (treasure room(s), cavern etc.)"})
		@RangeInt(min = 0, max = 100)
		public int pitStructureProbability = 25;
	}

	/*
	 * 
	 */
	public static class Well implements IWellConfig {
		@Comment({"Toggle to allow/disallow the generation of well."})
		public boolean wellAllowed = true;
		@Comment("The minimum number of chunks generated before another attempt to generate a well is made.")
		@RangeInt(min = 100, max = 32000)
		public int chunksPerWell = 400;
		@Comment({"The probability that a well will generate."})
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability = 80.0;

		@Comment({"Allowable Biome Types for general Well generation. Must match the Type identifer(s)."})
		@Name("biomeWhiteList")
		public String[] rawBiomeWhiteList = new String[] {};
		
		@Comment({"Disallowable Biome Types for general Well generation. Must match the Type identifer(s)."})
		@Name("biomeBlackList")
		public String[] rawBiomeBlackList = new String[] {"ocean", "deep_ocean"} ;
		
		@Ignore public List<BiomeTypeHolder> biomeWhiteList = new ArrayList<>();
		@Ignore public List<BiomeTypeHolder> biomeBlackList = new ArrayList<>();
		
		@Ignore public List<Biome> biomesWhiteList = new ArrayList<>();
		@Ignore public List<Biome> biomesBlackList = new ArrayList<>();
		
		/*
		 * 
		 */
		public Well() {
			BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
			BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
			biomesWhiteList = TreasureBiomeHelper.loadBiomesList(rawBiomeWhiteList);
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
		public List<BiomeTypeHolder> getBiomeWhiteList() {
			return biomeWhiteList;
		}

		@Override
		public List<BiomeTypeHolder> getBiomeBlackList() {
			return biomeBlackList;
		}
	}

	/*
	 * 
	 */
	public static class WitherTree {
		@Comment({"The number of chunks generated before a wither tree generation is attempted."})
		@RangeInt(min = 200, max = 32000)
		public int chunksPerTree = 800;		
		@Comment({"The probability that a wither tree will generate."})
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability = 90.0;
		@Comment({})
		@RangeInt(min = 7, max = 20)
		public int maxTrunkSize;
		@Comment({})
		@RangeInt(min = 0, max = 30)
		public int minSupportingTrees = 5;
		@Comment({})
		@RangeInt(min = 0, max = 30)
		public int maxSupportingTrees = 15;

		@Comment("")
		@RangeDouble(min = 0.0, max = 100.0)	    		
		public double witherBranchItemGenProbability = 50.0;
		@Comment("")
		@RangeDouble(min = 0.0, max = 100.0)
		public double witherRootItemGenProbability=50.0;

		@Name("biomeWhiteList")
		@Comment({"Allowable Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
		public String[] rawBiomeWhiteList = new String[] {"forest", "magical", "lush", "spooky", "dead", "jungle", "coniferous", "savanna"};
		@Name("biomeBlackList")
		@Comment({"Disallowable Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
		public String[] rawBiomeBlackList = new String[] {};

		@Ignore public List<BiomeTypeHolder> biomeWhiteList = new ArrayList<>();
		@Ignore public List<BiomeTypeHolder> biomeBlackList = new ArrayList<>();

		public WitherTree() {
			BiomeHelper.loadBiomeList(rawBiomeWhiteList, biomeWhiteList);
			BiomeHelper.loadBiomeList(rawBiomeBlackList, biomeBlackList);
		}

		public int getChunksPerTree() {
			return chunksPerTree;
		}
		public double getGenProbability() {
			return genProbability;
		}
		public int getMaxTrunkSize() {
			return maxTrunkSize;
		}
		public int getMinSupportingTrees() {
			return minSupportingTrees;
		}
		public int getMaxSupportingTrees() {
			return maxSupportingTrees;
		}
		public double getWitherBranchItemGenProbability() {
			return witherBranchItemGenProbability;
		}
		public double getWitherRootItemGenProbability() {
			return witherRootItemGenProbability;
		}
		public List<BiomeTypeHolder> getBiomeWhiteList() {
			return biomeWhiteList;
		}
		public List<BiomeTypeHolder> getBiomeBlackList() {
			return biomeBlackList;
		}
	}

	/*
	 * 
	 */
	public static class GemsAndOres {
		@Comment("The number of chunks generated before another attempt to generate a gem ore spawn is made.")
		@RangeInt(min = 1, max = 32000)
		public int chunksPerGemOre = 1;

		@RangeDouble(min = 0.0, max = 100.0)
		public double rubyGenProbability = 65.0;

		@RangeInt(min = 1, max = 255)
		public int rubyOreMaxY = 14;

		@RangeInt(min = 1, max =  255)
		public int rubyOreMinY = 6;

		@RangeInt(min = 1, max = 20)
		public int rubyOreVeinSize = 3;

		@RangeInt(min = 1, max = 20)
		public int rubyOreVeinsPerChunk = 1;

		@RangeDouble(min=  0.0, max =  100.0)
		public double sapphireGenProbability = 65.0;

		@RangeInt(min = 1, max = 255)
		public int sapphireOreMaxY = 14;

		@RangeInt(min = 1, max = 255)
		public int sapphireOreMinY = 6;

		@RangeInt(min = 1, max = 20)
		public int sapphireOreVeinSize = 3;

		@RangeInt(min = 1, max = 20)
		public int sapphireOreVeinsPerChunk=1;
	}

	/*
	 * 
	 */
	public static class KeysAndLocks {
		@Comment({"Enable/Disable whether a Key can break when attempting to unlock a Lock."})
		public boolean enableKeyBreaks = true;

		@Comment({"Enable/Disable whether a Lock item is dropped when unlocked by Key item."})
		public boolean enableLockDrops = true;

		@Comment({"The maximum uses for a given diamond key."})
		@RangeInt(min = 1, max = 32000)
		public int diamondKeyMaxUses = 20;

		@Comment({"The maximum uses for a given emerald key."})
		@RangeInt(min = 1, max = 32000)
		public int emeraldKeyMaxUses = 10;

		@Comment({"The maximum uses for a given gold key."})
		@RangeInt(min = 1, max = 32000)
		public int goldKeyMaxUses = 15;

		@Comment({"The maximum uses for a given iron key."})
		@RangeInt(min = 1, max = 32000)
		public int ironKeyMaxUses = 10;

		@Comment({"The maximum uses for a given jewelled key."})
		@RangeInt(min = 1, max = 32000)
		public int jewelledKeyMaxUses = 5;

		@Comment({"The maximum uses for a given metallurgists key."})
		@RangeInt(min = 1, max = 32000)
		public int metallurgistsKeyMaxUses = 25;

		@Comment({"The maximum uses for a given pilferers lock pick."})
		@RangeInt(min = 1, max = 32000)
		public int pilferersLockPickMaxUses = 10;

		@Comment({"The maximum uses for a given ruby key."})
		@RangeInt(min = 1, max = 32000)
		public int rubyKeyMaxUses = 8;

		@Comment({"The maximum uses for a given sapphire key."})
		@RangeInt(min = 1, max = 32000)
		public int sapphireKeyMaxUses = 5;

		@Comment({"The maximum uses for a given skeleton key."})
		@RangeInt(min = 1, max =32000)
		public int skeletonKeyMaxUses = 5;

		@Comment({"The maximum uses for a given spider key."})
		@RangeInt(min = 1, max = 32000)
		public int spiderKeyMaxUses = 5;

		@Comment({"The maximum uses for a given stone key."})
		@RangeInt(min = 1, max = 32000)
		public int stoneKeyMaxUses = 10;

		@Comment({"The maximum uses for a given thiefs lock pick."})
		@RangeInt(min = 1, max = 32000)
		public int thiefsLockPickMaxUses = 10;

		@Comment({"The maximum uses for a given wither key."})
		@RangeInt(min = 1, max = 32000)
		public int witherKeyMaxUses = 5;

		@Comment({"The maximum uses for a given wooden key."})
		@RangeInt(min = 1, max = 32000)
		public int woodKeyMaxUses = 20;
	}

	/*
	 * 
	 */
	public static class WorldGen {

		@Name("01-general")
		public GeneralProperties generalProperties = new GeneralProperties();

		@Name("02-markers")
		public MarkerProperties markerProperties = new MarkerProperties();

		public class GeneralProperties {
			@Comment({"Disallowable Biome Types for general Chest generation.", "Must match the Type identifer(s)."})
			public String[] generalChestBiomeBlackList = new String[] {};
			@Comment({"Allowable Biome Types for general Chest generation.", "Must match the Type identifer(s)."})
			public String [] generalChestBiomeWhiteList = new String[] {};
			@Comment({"Enable/Disable whether a fog is generated (ex. around graves/tombstones and wither trees)"})
			public boolean enableFog = true;
			@Comment({"Enable/Disable whether a wither fog is generated (ex. around wither trees)"})
			public boolean enableWitherFog = true;
			@Comment({"Enable/Disable whether a poison fog is generated (ex. around wither trees)"})
			public boolean enablePoisonFog = true;
			@Comment({"This is a temporary property.", "@since v1.5.0."})
			@RangeDouble(min = 0.0, max = 100.0)
			public double waterStructureProbability = 50.0;
			@Comment({"The probability that a surface structure will generate."})
			@RangeInt(min = 0, max = 100)
			public int surfaceStructureProbability = 25;
		}

		public class MarkerProperties {
			@Comment("Enable/Disable whether gravestone are generated when generating Treasure pits.")
			public boolean isGravestonesAllowed = true;

			@Comment("Enable/Disable whether structures (building and other non-gravestones) are generated when generating Treasure pits.")
			public boolean isMarkerStructuresAllowed = true;

			@Comment({"The probability that a gravestone will have fog."})
			@RangeInt(min = 0,  max = 100)
			public int gravestoneFogProbability = 50;

			@Comment({"The minimun of Treasure chest markers (gravestones, bones)."})
			@RangeInt(min = 1, max = 5)
			public int minGravestonesPerChest = 4;

			@Comment({"The maximum of Treasure chest markers (gravestones, bones)."})
			@RangeInt(min = 1, max = 10)
			public int maxGravestonesPerChest = 8;

			@Comment({"The probability that a Treasure chest marker will be a structure."})
			@RangeInt(min = 0, max = 100)
			public int markerStructureProbability = 15;
		}

		public MarkerProperties getMarkerProperties() {
			return markerProperties;
		}

		public GeneralProperties getGeneralProperties() {
			return generalProperties;
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
}

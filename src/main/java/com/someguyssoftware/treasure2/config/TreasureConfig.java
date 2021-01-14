/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.someguyssoftware.gottschcore.config.AbstractConfig;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Reloading;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * 
 * @author Mark Gottschling on Aug 11, 2020
 *
 */
@EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TreasureConfig extends AbstractConfig {
	protected static final ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
	protected static final ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec COMMON_CONFIG;
	
	public static final String CHESTS_CATEGORY = "chests";
	public static final String WELLS_CATEGORY = "wells";
	public static final String PITS_CATEGORY = "pits";
	public static final String MARKERS_CATEGORY = "markers";
	public static final String KEYS_AND_LOCKS_CATEGORY = "keys and locks";
	public static final String COINS_CATEGORY = "coins";

	public static final General GENERAL;
	public static final Chests CHESTS;
    public static final Pits PITS;
    public static final Markers MARKERS;
    public static final Wells WELLS;
	public static final KeysAndLocks KEYS_LOCKS;
	public static final Coins COINS;

	public static final String CATEGORY_DIV = "##############################";
	public static final String UNDERLINE_DIV = "------------------------------";
	
	static {
		MOD = new Mod(COMMON_BUILDER);
		LOGGING = new Logging(COMMON_BUILDER);
		GENERAL = new General(COMMON_BUILDER);
		CHESTS = new Chests(COMMON_BUILDER);
        PITS = new Pits(COMMON_BUILDER);
        MARKERS = new Markers(COMMON_BUILDER);
        WELLS = new Wells(COMMON_BUILDER);
		KEYS_LOCKS = new KeysAndLocks(COMMON_BUILDER);
		COINS = new Coins(COMMON_BUILDER);
		COMMON_CONFIG = COMMON_BUILDER.build();

		// load raw arrays into lists
		TreasureConfig.CHESTS.init();
	}

	private static IMod mod;

	/**
	 * 
	 * @param mod
	 */
	public TreasureConfig(IMod mod) {
		TreasureConfig.mod = mod;
	}

	public static class ItemID {

		public static final String SILVER_COIN_ID = "silver_coin";
		public static final String GOLD_COIN_ID = "gold_coin";
		
	}
	
	public static class LockID {
		public static final String WOOD_LOCK_ID = "wood_lock";
		public static final String STONE_LOCK_ID = "stone_lock";
		public static final String EMBER_LOCK_ID = "ember_lock";
		public static final String LEAF_LOCK_ID = "leaf_lock";
		public static final String IRON_LOCK_ID = "iron_lock";
		public static final String GOLD_LOCK_ID = "gold_lock";
		public static final String DIAMOND_LOCK_ID = "diamond_lock";
		public static final String EMERALD_LOCK_ID = "emerald_lock";
		public static final String RUBY_LOCK_ID = "ruby_lock";
		public static final String SAPPHIRE_LOCK_ID = "sapphire_lock";
		public static final String SPIDER_LOCK_ID = "spider_lock";
		public static final String WITHER_LOCK_ID = "wither_lock";
	}

	public static class KeyID {
		public static final String WOOD_KEY_ID = "wood_key";
		public static final String STONE_KEY_ID = "stone_key";
		public static final String EMBER_KEY_ID = "ember_key";
		public static final String LEAF_KEY_ID = "leaf_key";
		public static final String LIGHTNING_KEY_ID = "lightning_key";
		public static final String IRON_KEY_ID = "iron_key";
		public static final String GOLD_KEY_ID = "gold_key";
		public static final String DIAMOND_KEY_ID = "diamond_key";
		public static final String EMERALD_KEY_ID = "emerald_key";
		public static final String RUBY_KEY_ID = "ruby_key";
		public static final String SAPPHIRE_KEY_ID = "sapphire_key";
		public static final String JEWELLED_KEY_ID = "jewelled_key";
		public static final String METALLURGISTS_KEY_ID = "metallurgists_key";
		public static final String SKELETON_KEY_ID = "skeleton_key";
		public static final String WITHER_KEY_ID = "wither_key";
		public static final String SPIDER_KEY_ID = "spider_key";

		public static final String PILFERERS_LOCK_PICK_ID = "pilferers_lock_pick";
		public static final String THIEFS_LOCK_PICK_ID = "thiefs_lock_pick";
	}

	public static class BlockID {
        public static final String WISHING_WELL_BLOCK_ID = "wishing_well_block";
        public static final String DESERT_WISHING_WELL_BLOCK_ID = "desert_wishing_well_block";
        public static final String BLACKSTONE_ID = "blackstone";	
		public static final String PROXIMITY_SPAWNER_ID = "proximity_spawner";
		
		public static final String GRAVESTONE1_STONE_ID = "gravestone1_stone";
		public static final String GRAVESTONE1_COBBLESTONE_ID = "gravestone1_cobblestone";
		public static final String GRAVESTONE1_MOSSY_COBBLESTONE_ID = "gravestone1_mossy_cobblestone";
		public static final String GRAVESTONE1_POLISHED_GRANITE_ID = "gravestone1_polished_granite";
		public static final String GRAVESTONE1_POLISHED_ANDESITE_ID = "gravestone1_polished_andesite";
		public static final String GRAVESTONE1_POLISHED_DIORITE_ID = "gravestone1_polished_diorite";
		public static final String GRAVESTONE1_OBSIDIAN_ID = "gravestone1_obsidian";
		public static final String GRAVESTONE1_SMOOTH_QUARTZ_ID = "gravestone1_smooth_quartz";	
		
		public static final String GRAVESTONE2_STONE_ID = "gravestone2_stone";
		public static final String GRAVESTONE2_COBBLESTONE_ID = "gravestone2_cobblestone";
		public static final String GRAVESTONE2_MOSSY_COBBLESTONE_ID = "gravestone2_mossy_cobblestone";
		public static final String GRAVESTONE2_POLISHED_GRANITE_ID = "gravestone2_polished_granite";
		public static final String GRAVESTONE2_POLISHED_ANDESITE_ID = "gravestone2_polished_andesite";
		public static final String GRAVESTONE2_POLISHED_DIORITE_ID = "gravestone2_polished_diorite";
		public static final String GRAVESTONE2_OBSIDIAN_ID = "gravestone2_obsidian";
		public static final String GRAVESTONE2_SMOOTH_QUARTZ_ID = "gravestone2_smooth_quartz";	
		
		public static final String GRAVESTONE3_STONE_ID = "gravestone3_stone";
		public static final String GRAVESTONE3_COBBLESTONE_ID = "gravestone3_cobblestone";
		public static final String GRAVESTONE3_MOSSY_COBBLESTONE_ID = "gravestone3_mossy_cobblestone";
		public static final String GRAVESTONE3_POLISHED_GRANITE_ID = "gravestone3_polished_granite";
		public static final String GRAVESTONE3_POLISHED_ANDESITE_ID = "gravestone3_polished_andesite";
		public static final String GRAVESTONE3_POLISHED_DIORITE_ID = "gravestone3_polished_diorite";
		public static final String GRAVESTONE3_OBSIDIAN_ID = "gravestone3_obsidian";
		public static final String GRAVESTONE3_SMOOTH_QUARTZ_ID = "gravestone3_smooth_quartz";
	}

	public static class ChestID {
		public static final String WOOD_CHEST_ID = "wood_chest";
		public static final String CRATE_CHEST_ID = "crate_chest";
		public static final String MOLDY_CRATE_CHEST_ID = "crate_chest_moldy";
		public static final String IRONBOUND_CHEST_ID = "ironbound_chest";
		public static final String PIRATE_CHEST_ID = "pirate_chest";
		public static final String IRON_STRONGBOX_ID = "iron_strongbox";
		public static final String GOLD_STRONGBOX_ID = "gold_strongbox";
		public static final String SAFE_ID = "safe";
		public static final String DREAD_PIRATE_CHEST_ID = "dread_pirate_chest";
		public static final String COMPRESSOR_CHEST_ID = "compressor_chest";
		public static final String WITHER_CHEST_ID = "wither_chest";
		public static final String WITHER_CHEST_TOP_ID = "wither_chest_top";
		public static final String SKULL_CHEST_ID = "skull_chest";
		public static final String GOLD_SKULL_CHEST_ID = "gold_skull_chest";
		public static final String CAULDRON_CHEST_ID = "cauldron_chest";
		public static final String SPIDER_CHEST_ID = "spider_chest";
		public static final String VIKING_CHEST_ID = "viking_chest";
    }

	public static class TileEntityID {
		public static final String WOOD_CHEST_TE_ID = "wood_chest_tile_entity_type";
		public static final String CRATE_CHEST_TE_ID = "crate_chest_tile_entity_type";
		public static final String MOLDY_CRATE_CHEST_TE_ID = "crate_chest_moldy_tile_entity_type";
		public static final String IRONBOUND_CHEST_TE_ID = "ironbound_chest_tile_entity_type";
		public static final String PIRATE_CHEST_TE_ID = "pirate_chest_tile_entity";
		public static final String IRON_STRONGBOX_TE_ID = "iron_strongbox_tile_entity";
		public static final String GOLD_STRONGBOX_TE_ID = "gold_strongbox_tile_entity";
		public static final String SAFE_TE_ID = "safe_tile_entity";
		public static final String DREAD_PIRATE_CHEST_TE_ID = "dread_pirate_chest_tile_entity";
		public static final String WHALE_BONE_PIRATE_CHEST_TE_ID = "whale_bone_pirate_chest_tile_entity";
		public static final String COMPRESSOR_CHEST_TE_ID = "compressor_chest_tile_entity";
		public static final String WITHER_CHEST_TE_ID = "wither_chest_tile_entity";
		public static final String SKULL_CHEST_TE_ID = "skull_chest_tile_entity";
		public static final String GOLD_SKULL_CHEST_TE_ID = "gold_skull_chest_tile_entity";
		public static final String CAULDRON_CHEST_TE_ID = "cauldron_chest_tile_entity";
		public static final String OYSTER_CHEST_TE_ID = "oyster_chest_tile_entity";
		public static final String CLAM_CHEST_TE_ID = "clam_chest_tile_entity";
		public static final String SPIDER_CHEST_TE_ID = "spider_chest_tile_entity";
		public static final String VIKING_CHEST_TE_ID = "viking_chest_tile_entity";
		public static final String PROXIMITY_SPAWNER_TE_ID = "proximity_spawner_tile_entity";
		public static final String GRAVESTONE_TE_ID = "gravestone_tile_entity";
		public static final String GRAVESTONE_PROXIMITY_SPAWNER_TE_ID = "gravestone_proximity_spawner_tile_entity";
	}

	/*
	 * 
	 */
	public static class General {
		public ForgeConfigSpec.BooleanValue  enableDefaultLootTablesCheck;
		public ForgeConfigSpec.BooleanValue enableDefaultTemplatesCheck;
		public ForgeConfigSpec.BooleanValue enableDefaultDecayRuleSetsCheck;
		
		public ConfigValue<List<? extends String>> dimensionsWhiteList;
		public ForgeConfigSpec.ConfigValue<Integer> surfaceStructureProbability;
		
		General(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " General properties for Treasure mod.", CATEGORY_DIV).push("03-general");

			enableDefaultLootTablesCheck = builder
					.comment(" Enable/Disable a check to ensure the default loot tables exist on the file system.", "If enabled, then you will not be able to remove any default loot tables (but they can be edited).", "Only disable if you know what you're doing.")
					.define("Enable default loot tables check:", true);
			
			enableDefaultTemplatesCheck = builder
					.comment(" Enable/Disable a check to ensure the default templates exist on the file system.", " If enabled, then you will not be able to remove any default templates.", " Only disable if you know what you're doing.")
					.define("Enable default templates check:", true);
			
			enableDefaultDecayRuleSetsCheck = builder
					.comment(" Enable/Disable a check to ensure the default decay rulesets exist on the file system.", " If enabled, then you will not be able to remove any default decay rulesets (but they can be edited).", " Only disable if you know what you're doing.")
					.define("Enable default decay rulesets check:", true);

			surfaceStructureProbability = builder
					.comment("The probability that a surface structure will generate.")
					.defineInRange("Probability of surface structure spawn:", 25, 0, 100);
			
			dimensionsWhiteList = builder
					.comment(" Allowed Dimensions for generation.", 
							" Treasure2 was designed for 'normal' overworld-type dimensions.", 
							" This setting does not use any wildcards (*). You must explicitly set the dimensions that are allowed.", 
							" ex. minecraft:overworld")
					.defineList("Dimension White List:", Arrays.asList(new String []{"minecraft:overworld"}), s -> s instanceof String);
			builder.pop();
		}
	}

	/*
	 * 
	 */
	public static class Chests {
		public ChestCollection surfaceChests;
		public ChestCollection submergedChests;

//		@RequiresMcRestart
		public ForgeConfigSpec.ConfigValue<Integer> chestRegistrySize;
		
		/**
		 * 
		 * @param builder
		 */
		Chests(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " Chest properties", CATEGORY_DIV)
			.push(CHESTS_CATEGORY);

			chestRegistrySize = builder
					.comment("The number of chests that are monitored. Most recent additions replace least recent when the registry is full.",
							"This is the set of chests used to measure distance between newly generated chests.")
					.defineInRange("Max. size of chest registry:", 75, 5, 100);
			
			Map<Rarity, ChestConfig.Data> surfaceConfigs = new HashMap<>();
			Map<Rarity, ChestConfig.Data> submergedConfigs = new HashMap<>();
			
			// setup surface properties
			surfaceConfigs.put(Rarity.COMMON, new ChestConfig.Data(true, 75, 85, 50, 20.0, new String[] {}, new String[] {"ocean"}, new String[] {}, new String[] {}));
			surfaceConfigs.put(Rarity.UNCOMMON, new ChestConfig.Data(true, 150, 75, 40, 17.5, new String[] {}, new String[] {}, new String[] {}, new String[] {}));
			
			// TODO needs all the builder stuff
			surfaceChests = new ChestCollection(builder,
					"01-Surface Chests", 
					new String[] {
							CATEGORY_DIV,
							" Chests that generate on land.", 
							UNDERLINE_DIV,
							" Note: There is a build-in check against ocean biomes for surface chests. Adding ocean biomes to the white lists will not change this functionality.",
							CATEGORY_DIV},
					surfaceConfigs);

			// setup submerged properties
			submergedConfigs.put(Rarity.COMMON, new ChestConfig.Data(false, 150, 85, 40, 0.0, new String[] {}, new String[] {}, new String[] {}, new String[] {}));
			
			// FOR submerged scarce+ chests
//					new String[] {},
//					new String[] {"ocean", "deep_ocean", "deep_frozen_ocean", 
//							"cold_ocean", "deep_cold_ocean", "lukewarm_ocean", "warm_ocean"},
//					new String[] {},
//					new String[] {"ocean", "deep_ocean"});
			
			submergedChests = new ChestCollection(builder, 
					"02-Submerged Chests", 
					new String[] {
							CATEGORY_DIV,
							" Chests that generate underwater (in ocean biomes).", 
							UNDERLINE_DIV,
							" Note: There is a build-in check to only allow ocean biomes for submerged chests. Adding other biomes to the white lists will not change this functionality.",
							CATEGORY_DIV,}, 
					submergedConfigs);

			
			builder.pop();
			
			// TODO pass in extra properties into constructor
			// setup extra properties

//			surfaceChests.scarceChestProperties.mimicProbability = 15.0;
//			submergedChests.scarceChestProperties.mimicProbability = 0.0;
		}

		public void init() {
			this.surfaceChests.init();
		}
		
		/*
		 * 
		 */
		public class ChestCollection {
			/*
			 * Map of chest configs by rarity.
			 */
			public Map<Rarity, IChestConfig> configMap = new HashMap<>();

			public ForgeConfigSpec.ConfigValue<Integer> minChunksPerChest;			
			public ForgeConfigSpec.ConfigValue<Integer> minDistancePerChest;
			public ForgeConfigSpec.ConfigValue<Integer> surfaceChestProbability;			
			public ChestConfig commonChestProperties;

			/**
			 * 
			 * @param configs
			 */
			public ChestCollection(final ForgeConfigSpec.Builder builder, String category, String[] comments, Map<Rarity, ChestConfig.Data> configs) {
				builder.comment(comments).push(category);
								
				minChunksPerChest = builder
						.comment(" The minimum number of chunks generated before another attempt to spawn a chest is made.")
						.defineInRange("Minimum chunks per chest spawn:", 50, 25, 32000);
				
				minDistancePerChest = builder
						.comment(" The minimum distance, measured in chunks (16x16), that two chests can be in proximity.",
								" Note: Only chests in the chest registry are checked against this property.",
								" Used in conjunction with the chunks per chest and spawn probability.", " Ex. ")
						.defineInRange("Minimum distance per chest spawn:", 75, 0, 32000);
		
				surfaceChestProbability = builder
						.comment(" The probability chest will appear on the surface, instead of in a pit.")
						.defineInRange("Probability of chest spawn on the surface:", 15, 0, 100);
				
				commonChestProperties = new ChestConfig(builder, configs.get(Rarity.COMMON));
			
				// update the map
				configMap.put(Rarity.COMMON, commonChestProperties);
				
				builder.pop();
			}
			
			public void init() {
				this.commonChestProperties.init();
			}
		}
	}

	/*
	 * 
	 */
	public static class Pits {
		public ForgeConfigSpec.ConfigValue<Integer> pitStructureProbability;
		
		Pits(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " Pit properties", CATEGORY_DIV)
			.push(PITS_CATEGORY);
			
			pitStructureProbability = builder
					.comment("The probability that a pit will contain a structure (treasure room(s), cavern etc.)")
					.defineInRange("Probability of pit structure spawn:", 25, 0, 100);
			builder.pop();
		}
	}
    
	public static class Markers {
		public ForgeConfigSpec.BooleanValue markersAllowed;
		public ForgeConfigSpec.BooleanValue markerStructuresAllowed;
		public ForgeConfigSpec.ConfigValue<Integer> minGravestonesPerChest;
		public ForgeConfigSpec.ConfigValue<Integer> maxGravestonesPerChest;
		public ForgeConfigSpec.ConfigValue<Integer> markerStructureProbability;
		public ForgeConfigSpec.BooleanValue gravestoneSpawnMobAllowed;
		public ForgeConfigSpec.ConfigValue<Integer> gravestoneMobProbability;
		
		public Markers(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " Gravestones and Markers properties", CATEGORY_DIV)
			.push(MARKERS_CATEGORY);
			
			markersAllowed = builder
					.comment(" Enable/Disable whether chest markers (gravestones, bones)  are generated when generating treasure chests.")
					.define("Enable markers:", true);
			
			markerStructuresAllowed = builder
					.comment(" Enable/Disable whether structures (buildings) are generated when generating  treasure chests.")
					.define("Enable structure markers:", true);
			
			minGravestonesPerChest = builder
					.comment(" The minimum number of markers (gravestones, bones) per chest.")
					.defineInRange("Minimum markers per chest:", 2, 1, 5);
			
			maxGravestonesPerChest = builder
					.comment(" The max. number of markers (gravestones, bones) per chest.")
					.defineInRange("Maximum markers per chest:", 5, 1, 10);
			
			markerStructureProbability = builder
					.comment(" The probability that a marker will be a structure.")
					.defineInRange("Probability that marker will be a structure:", 15, 1, 100);
			
			gravestoneSpawnMobAllowed = builder
					.comment(" Enable/Disable whether gravestone markers can spawn mobs (ex. Bound Soul).")
					.define("Enable gravestone markers to spawn mobs:", true);
			
			gravestoneMobProbability = builder
					.comment(" The probability that a gravestone will spawn a mob.", " Currently gravestones can spawn Bound Souls.")
					.defineInRange("Probability that grave marker will spawn a mob:", 25, 1, 100);
		}
	}
	
    /*
     *
     */
    public static class Wells implements IWellsConfig {
    	public ForgeConfigSpec.BooleanValue wellAllowed;
    	public ForgeConfigSpec.ConfigValue<Double> genProbability;
        public ForgeConfigSpec.ConfigValue<Integer> chunksPerWell;
        public BiomesConfig biomes; 
        
        Wells(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " Well properties", CATEGORY_DIV)
			.push(WELLS_CATEGORY);
			
			wellAllowed = builder
					.comment("Toggle to allow/disallow the spawn of well.")
					.define("Enabled wells:", true);
			
			chunksPerWell = builder
					.comment("The minimum number of chunks generated before another attempt to spawn a well is made.")
					.defineInRange("Chunks per well spawn:", 400, 100, 32000);
			
			genProbability = builder
					.comment("The probability that a well will generate.")
					.defineInRange("Generation probability:", 80.0, 0.0, 100.0);
			
			BiomesConfig.Data biomesData = new BiomesConfig.Data(new String[] {}, new String[] { "ocean", "deep_ocean", "deep_frozen_ocean", "cold_ocean",
					"deep_cold_ocean", "lukewarm_ocean", "warm_ocean" },
			new String[] {}, new String[] { "ocean", "deep_ocean" });
			biomes = new BiomesConfig(builder, biomesData);
			
			builder.pop();
		}

		@Override
		public void init() {
			this.biomes.init();
		}

		@Override
		public boolean isWellAllowed() {
			return wellAllowed.get();
		}

		@Override
		public int getChunksPerWell() {
			return chunksPerWell.get();
		}

		@Override
		public double getGenProbability() {
			return genProbability.get();
		}

		@Override
		public List<String> getBiomeWhiteList() {
			return (List<String>) biomes.whiteList.get();
		}

		@Override
		public List<String> getBiomeBlackList() {
			return (List<String>) biomes.blackList.get();
		}
    }

	/*
	 * 
	 */
	public static class KeysAndLocks {
		public ForgeConfigSpec.BooleanValue enableKeyBreaks;
		public ForgeConfigSpec.BooleanValue enableLockDrops;
		public ForgeConfigSpec.ConfigValue<Integer> pilferersLockPickMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> thiefsLockPickMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> woodKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> stoneKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> emberKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> leafKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> lightningKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> ironKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> goldKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> diamondKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> emeraldKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> rubyKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> sapphireKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> metallurgistsKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> skeletonKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> jewelledKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> spiderKeyMaxUses;
		public ForgeConfigSpec.ConfigValue<Integer> witherKeyMaxUses;		

		KeysAndLocks(final ForgeConfigSpec.Builder builder) {
			builder.comment(CATEGORY_DIV, " Keys and Locks properties", CATEGORY_DIV)
			.push(KEYS_AND_LOCKS_CATEGORY);

			enableKeyBreaks = builder
					.comment(" Enable/Disable whether a Key can break when attempting to unlock a Lock.")
					.define("Enable key breaks:", true);

			enableLockDrops = builder.comment("Enable/Disable whether a Lock item is dropped when unlocked by Key item.")
					.define("Enable lock drops:", true);

			pilferersLockPickMaxUses = builder
					.comment(" The maximum uses for a given pilferers lock pick.")
					.defineInRange("Pilferer's lockpick max uses:", 10, 1, 32000);

			thiefsLockPickMaxUses = builder
					.comment(" The maximum uses for a given thiefs lock pick.")
					.defineInRange("Thief's lockpick max uses:", 10, 1, 32000);

			//    		@RequiresMcRestart
			woodKeyMaxUses = builder
					.comment(" The maximum uses for a given wooden key.")
					.defineInRange("Wood key max uses:", 20, 1, 32000);

			//    		@RequiresMcRestart
			stoneKeyMaxUses = builder
					.comment(" The maximum uses for a given stone key.")
					.defineInRange("Stone key max uses:", 10, 1, 32000);

			emberKeyMaxUses = builder
					.comment(" The maximum uses for a given ember key.")
					.defineInRange("Stone key max uses:", 15, 1, 32000);

			leafKeyMaxUses = builder
					.comment(" The maximum uses for a given leaf key.")
					.defineInRange("Stone key max uses:", 15, 1, 32000); 

			lightningKeyMaxUses = builder
					.comment(" The maximum uses for a given lightning key.")
					.defineInRange("Stone key max uses:", 10, 1, 32000); 

			//    		@RequiresMcRestart
			ironKeyMaxUses = builder
					.comment(" The maximum uses for a given iron key.")
					.defineInRange("Iron key max uses:", 10, 1, 32000);

			//    		@RequiresMcRestart
			goldKeyMaxUses = builder
					.comment(" The maximum uses for a given gold key.")
					.defineInRange("Gold key max uses:", 15, 1, 32000);

			//    		@RequiresMcRestart
			diamondKeyMaxUses = builder
					.comment(" The maximum uses for a given diamond key.")
					.defineInRange("Diamond key max uses:", 20, 1, 32000);

			//    		@RequiresMcRestart
			emeraldKeyMaxUses = builder
					.comment(" The maximum uses for a given emerald key.")
					.defineInRange("Emerald key max uses:", 10, 1, 32000);

			//    		@RequiresMcRestart
			rubyKeyMaxUses = builder
					.comment(" The maximum uses for a given ruby key.")
					.defineInRange("Ruby key max uses:", 5, 1, 32000);

			//    		@RequiresMcRestart
			sapphireKeyMaxUses = builder
					.comment(" The maximum uses for a given sapphire key.")
					.defineInRange("Sapphire key max uses:", 5, 1, 32000);

			//    		@RequiresMcRestart
			metallurgistsKeyMaxUses = builder
					.comment(" The maximum uses for a given metallurgists key.")
					.defineInRange("Metallurgists key max uses:", 25, 1, 32000);

			//    		@RequiresMcRestart
			skeletonKeyMaxUses = builder
					.comment(" The maximum uses for a given skeleton key.")
					.defineInRange("Skeleton key max uses:", 5, 1, 32000);

			//    		@RequiresMcRestart
			jewelledKeyMaxUses = builder
					.comment(" The maximum uses for a given jewelled key.")
					.defineInRange("Jewelled Key max uses:", 5, 1, 32000);

			//    		@RequiresMcRestart
			spiderKeyMaxUses = builder
					.comment(" The maximum uses for a given spider key.")
					.defineInRange("Spider key max uses:", 5, 1, 32000);

			//    		@RequiresMcRestart
			witherKeyMaxUses = builder
					.comment(" The maximum uses for a given wither key.")
					.defineInRange("Wither key max uses:", 5, 1, 32000);

			builder.pop();
		}
	}
	
	/**
	 * 
	 * @author Mark Gottschling on Jan 13, 2021
	 *
	 */
	public static class Coins {
//		@RequiresMcRestart
		public ForgeConfigSpec.ConfigValue<Integer> coinMaxStackSize;		

		public Coins(final ForgeConfigSpec.Builder builder)	 {
			builder.comment(CATEGORY_DIV, " Coins and Valuables properties", CATEGORY_DIV)
			.push(COINS_CATEGORY);
			
			coinMaxStackSize = builder
					.comment(" The maximum size of a coin item stack.")
					.defineInRange("Maximum Stack Size:", 8, 1, 64);
			builder.pop();
		}
	}

	@SubscribeEvent
	public static void onLoad(final ModConfig.Loading configEvent) {
		TreasureConfig.loadConfig(TreasureConfig.COMMON_CONFIG,
				FMLPaths.CONFIGDIR.get().resolve(mod.getId() + "-common.toml"));
	}

	@SubscribeEvent
	public static void onReload(final Reloading configEvent) {
	}

	@Override
	public boolean isEnableVersionChecker() {
		return TreasureConfig.MOD.enableVersionChecker.get();
	}

	@Override
	public void setEnableVersionChecker(boolean enableVersionChecker) {
		TreasureConfig.MOD.enableVersionChecker.set(enableVersionChecker);
	}

	@Override
	public boolean isLatestVersionReminder() {
		return TreasureConfig.MOD.latestVersionReminder.get();
	}

	@Override
	public void setLatestVersionReminder(boolean latestVersionReminder) {
		TreasureConfig.MOD.latestVersionReminder.set(latestVersionReminder);
	}

	@Override
	public boolean isModEnabled() {
		return TreasureConfig.MOD.enabled.get();
	}

	@Override
	public void setModEnabled(boolean modEnabled) {
		TreasureConfig.MOD.enabled.set(modEnabled);
	}

	@Override
	public String getModsFolder() {
		return TreasureConfig.MOD.folder.get();
	}

	@Override
	public void setModsFolder(String modsFolder) {
		TreasureConfig.MOD.folder.set(modsFolder);
	}

	@Override
	public String getConfigFolder() {
		return TreasureConfig.MOD.configFolder.get();
	}

	@Override
	public void setConfigFolder(String configFolder) {
		TreasureConfig.MOD.configFolder.set(configFolder);
	}
}


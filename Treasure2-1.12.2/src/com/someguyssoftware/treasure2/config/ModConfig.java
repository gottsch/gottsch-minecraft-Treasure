/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.Rarity;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
import net.minecraftforge.common.config.Config.RangeDouble;
import net.minecraftforge.common.config.Config.RangeInt;
import net.minecraftforge.common.config.Config.RequiresWorldRestart;
import net.minecraftforge.common.config.Config.Type;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Mark Gottschling on Sep 4, 2019
 *
 */
//@Config(modid = Treasure.MODID, name = Treasure.MODID, type = Type.INSTANCE, category = "general")
public class ModConfig {
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
	
	@Name("01-logging")
	@Comment({ "Logging properties" })
	public static final Logging logging = new Logging();
	
	@Name("02-mod")
	@Comment({"General mod properties."})
	public static final Mod mod = new Mod();
	
	@Name("03-chests")
	@Comment({"Chest properties"})
	public static final Chests chests = new Chests();
	
	@Name("04-well")
	@Comment({"Well properties"})
	public static final Well well = new Well();
	
	@Name("05-wither tree")
	@Comment({"Wither tree properties"})
	public static final WitherTree witherTree = new WitherTree();
	
	/*
	 * Map of chest configs by rarity.
	 */
	@Ignore
	public static Map<Rarity, ModConfig.Chests.Chest> chestConfigs = new HashMap<>();
	
	static {
		chestConfigs.put(Rarity.COMMON, ModConfig.chests.common);
		chestConfigs.put(Rarity.UNCOMMON, ModConfig.chests.uncommon);
		chestConfigs.put(Rarity.SCARCE, ModConfig.chests.scarce);
		chestConfigs.put(Rarity.RARE, ModConfig.chests.rare);
		chestConfigs.put(Rarity.EPIC, ModConfig.chests.epic);
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
		
		@Comment({"Where default Treasure folder is located."})
		public String treasureFolder = folder + "/" + Treasure.MODID + "/";
		
		@Comment({"Enable/Disable whether a Key can break when attempting to unlock a Lock."})
		public boolean enableKeyBreaks = true;
		@Comment({"Enable/Disable whether a fog is generated (ex. around graves/tombstones and wither trees)"})
		public boolean enableFog = true;
		@Comment({"Enable/Disable whether a wither fog is generated (ex. around wither trees)"})
		public boolean enableWitherFog = true;
		@Comment({"Enable/Disable whether a poison fog is generated (ex. around wither trees)"})
		public boolean enablePoisonFog = true;
		@Comment({"Enable/Disable whether a Lock item is dropped when unlocked by Key item."})
		public boolean enableLockDrops = true;
		@Comment({"Enable/Disable a check to ensure the default loot tables exist on the file system.", "If enabled, then you will not be able to remove any default loot tables (but they can be edited).", "Only disable if you know what you're doing."})
		public boolean enableDefaultLootTablesCheck = true;
		@Comment({"Enable/Disable a check to ensure the default templates exist on the file system.", "If enabled, then you will not be able to remove any default templates.", "Only disable if you know what you're doing."})
		public boolean enableDefaultTemplatesCheck = true;
		
		@Name("02-mod-01")
		public ForeignModEnablements foreignModEnablements = new ForeignModEnablements();
		
		/*
		 * 
		 */
		public class ForeignModEnablements {
			@Comment({"Add mod's MODID to this list to enable custom loot tables for a mod."})
			public String[] enableForeignModIDs = new String[]{"mocreatures", "sgs_metals"};
			@Comment({"A list of mods that have prebuilt loot tables available.", "Note: used for informational purposes only."})
			public String[] availableForeignModLootTables = new String[]{"mocreatures", "sgs_metals"};
		}
	}
	
	/*
	 * 
	 */
	public static class Chests {
		@Comment({"The minimum distance in chunks that can be between any two chests.", 
			"Note: Only chests in the chest registry are checked against this property.",
			"Used in conjunction with the chunks per chest and generation probability.",
			"Ex. "})
		@RangeInt(min = 0, max = 32000)
		public int minDistancePerChest = 75;
		
		@Comment({"The number of chests that are monitored. Most recent additions replace least recent when the registry is full.", "This is the set of chests used to measure distance between newly generated chests."})
		@RangeInt(min = 5, max = 100)
		@RequiresWorldRestart
		public int chestRegistrySize = 25;
		
		@Name("01] Common chest properties")
		public Chest common = new Chest(true, 75, 85, 50);

		@Name("02] Uncommon chest properties")
		public Chest uncommon = new Chest(true, 150, 75, 40);
		
		@Name("03] Scarce chest properties")
		public Chest scarce = new Chest(true, 300, 50, 30);
		
		@Name("04] Rare chest properties")
		public Chest rare = new Chest(true, 500, 25, 20);

		@Name("05] Epic chest properties")
		public Chest epic = new Chest(true, 800, 15, 10);
				
		/*
		 * 
		 */
		public Chests() {
			// setup extra properties
			common.mimicProbability = 20.0;
			common.biomeWhiteList = new String[] {};
			common.biomeBlackList = new String[] {"plains", "ocean", "deep_ocean"};
		}
		
		/*
		 * 
		 */
		public class Chest {
			@Ignore
			public boolean chestAllowed = true;
			
			@Comment({"The number of chunks generated before a chest generation is attempted."})
			@RangeInt(min = 50, max = 32000)
			public int chunksPerChest = 75;
			
			@Comment({"The probability that a chest will generate."})
			@RangeDouble(min = 0.0, max = 100.0)
			public double genProbability = 50.0;
			
			@Comment({"The minimum depth (y-axis) that a chest can generate at."})
			@RangeInt(min = 5, max = 250)
			public int minYSpawn = 25;
			
			// TODO most likely going to be removed with the use of meta files / archetype : type : biome categorizations
			@Ignore
			public boolean surfaceAllowed = true;
			@Ignore
			public boolean subterraneanAllowed = true;
			
			// TODO most likely going to be removed with the use of meta files
			@Comment({"Allowable Biome Types for general Chest generation. Must match the Type identifer(s)."})
			public String[] biomeWhiteList = new String[] {};
			@Comment({"Disallowable Biome Types for general Chest generation. Must match the Type identifer(s)."})
			public String[] biomeBlackList = new String[] {};
			
			@Comment({"The probability that a chest will be a mimic."})
			@RangeDouble(min = 0.0, max = 100.0)
			public double mimicProbability = 0.0;
			
			/*
			 * 
			 */
			public Chest() {}
			
			/*
			 * 
			 */
			public Chest(boolean isAllowed, int chunksPer, double probability, int minYSpawn) {
				this.chestAllowed = isAllowed;
				this.chunksPerChest = chunksPer;
				this.genProbability = probability;
				this.minYSpawn = minYSpawn;
			}
		}
	}
	
	// TODO
	public static class Pits {
		
	}
	
	public static class Well {
		@Comment({"Toggle to allow/disallow the generation of well."})
		public boolean wellAllowed = true;
		@Comment({"The number of chunks generated before a well generation is attempted."})
		@RangeInt(min = 50, max = 32000)
		public int chunksPerWell = 500;
		@Comment({"The probability that a well will generate."})
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability = 80.0;
		
		@Comment({"Allowable Biome Types for general Well generation. Must match the Type identifer(s)."})
		public  String[] biomeWhiteList = new String[] {};
		@Comment({"Disallowable Biome Types for general Well generation. Must match the Type identifer(s)."})
		public  String[] biomeBlackList = new String[] {"ocean", "deep_ocean"} ;
	}

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
		
		@Comment({"Allowable Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
		public  String[] biomeWhiteList = new String[] {"forest", "magical", "lush", "spooky", "dead", "jungle", "coniferous", "savanna"};
		@Comment({"Disallowable Biome Types for Wither Tree generation. Must match the Type identifer(s)."})
		public  String[] biomeBlackList = new String[] {} ;
	}
	
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
}

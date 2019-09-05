/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import com.someguyssoftware.treasure2.Treasure;

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
@Config(modid = Treasure.MODID, name = "Treasure2 Config", type = Type.INSTANCE, category = "general")
public class ModConfig {
	/*
	 *  IDs
	 */
	// tab
	@Ignore public static final String TREASURE_TAB_ID = "treasure_tab";
	// chests
	@Ignore public static final String WOOD_CHEST_ID = "wood_chest";
	@Ignore public static final String CRATE_CHEST_ID = "crate_chest";
	
	@Name("01-logging")
	@Comment({ "Logging properties" })
	public static final Logging logging = new Logging();
	
	@Name("02-mod")
	@Comment({"General mod properties."})
	public static final Mod mod = new Mod();
	
	@Name("03-chests")
	@Comment({"Chest properties"})
	public static final Chests chests = new Chests();
	
	@Name("04-wells")
	@Comment({"Well properties"})
	public static final Wells wells = new Wells();
	
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
		public String latestVersion;
		@Comment({"Remind the user of the latest version (as indicated in latestVersion proeprty) update."})
		public boolean latestVersionReminder;
		
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
		// TODO separate instance for each rarity chest
		@Comment({"The minimum distance in chunks that can be between any two chests.", "Note: Only chests in the chest registry are checked against this property."})
		@RangeInt(min = 0, max = 32000)
		public int minDistancePerChest = 75;
		
		@Comment({""})
		@RangeInt(min = 5, max = 100)
		@RequiresWorldRestart
		public int chestRegistrySize = 25;
		
		@Name("03-chests-01")
		@Comment({"Common chest properties"})
		public Chest common = new Chest(true, 75, 85, 50);

		@Name("03-chests-02")
		@Comment({"Uncommon chest properties"})
		public Chest uncommon = new Chest(true, 150, 75, 40);
		
		@Name("03-chests-03")
		@Comment({"Scarce chest properties"})
		public Chest scarce = new Chest();
		
		@Name("03-chests-04")
		@Comment({"Rare chest properties"})
		public Chest rare = new Chest();
		
		@Name("03-chests-05")
		@Comment({"Epic chest properties"})
		public Chest epic = new Chest();
				
		public Chests() {
			common.mimicProbability = 20.0;
			common.biomeWhiteList = new String[] {};
			common.biomeBlackList = new String[] {"plains", "ocean", "deep_ocean"};
		}
		
		/*
		 * 
		 */
		public class Chest {
			@Ignore
			public boolean chestAllowed;
			
			@Comment({"The number of chunks generated before a chest generation is attempted."})
			@RangeInt(min = 50, max = 32000)
			public int chunksPerChest;
			
			@Comment({})
			@RangeDouble(min = 0.0, max = 100.0)
			public double genProbability;
			
			@Comment({})
			@RangeInt(min = 5, max = 250)
			public int minYSpawn;
			
			@Ignore
			public boolean surfaceAllowed;
			@Ignore
			public boolean subterraneanAllowed;
			
			@Comment({})
			@RangeDouble(min = 0.0, max = 100.0)
			public double mimicProbability = 0.0;
			
			@Comment({})
			public String[] biomeWhiteList;
			public String[] biomeBlackList;
			
			public Chest() {}
			
			public Chest(boolean isAllowed, int chunksPer, double probability, int minYSpawn) {
				this.chestAllowed = isAllowed;
				this.chunksPerChest = chunksPer;
				this.genProbability = probability;
				this.minYSpawn = minYSpawn;
			}
		}
	}
	
	public static class Pits {
		
	}
	
	public static class Wells {
		@Ignore
		public boolean wellAllowed;
		@Comment({})
		@RangeInt(min = 50, max = 32000)
		public int chunksPerWell;
		@Comment({})
		@RangeDouble(min = 0.0, max = 100.0)
		public double genProbability;
		
		@Comment({})
		public  String[] biomeWhiteList = new String[] {};
		public  String[] biomeBlackList = new String[] {"ocean", "deep_ocean"} ;
	}

	public static class WitherTree {
		
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

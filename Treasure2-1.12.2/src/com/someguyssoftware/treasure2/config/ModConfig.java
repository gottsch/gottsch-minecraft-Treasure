/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Ignore;
import net.minecraftforge.common.config.Config.Name;
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
	@Comment({ "Logging Properties" })
	public static final Logging logging = new Logging();
	
	@Name("02-mod")
	@Comment({"General mod properties."})
	public static final Mod mod = new Mod();
	
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
		@Comment({"The relative path to this mod's folder."})
		public String folder = Treasure.MODID;
		@Comment({"Enables/Disables mod."})
		public boolean enabled = true;
		@Comment({"Enables/Disables version checking."})
		public boolean enableVersionChecker = true;
		@Comment({"The latest published version number.", "This is auto-updated by the version checker.", "This may be @deprecated."})
		public String latestVersion;
		@Comment({"Remind the user of the latest version (as indicated in latestVersion proeprty) update."})
		public boolean latestVersionReminder;
	}
	
	public static class Chests {
		// TODO separate instance for each rarity chest
	}
	
	public static class Pits {
		
	}
	
	public static class Wells {
		
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

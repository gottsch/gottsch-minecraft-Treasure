/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

/**
 * @author Mark Gottschling on Nov 30, 2019
 *
 */
// TODO move to GottschCore 1.9/1.8
public class ModConfig {
	@Comment({"Enables/Disables mod."})
	@Name("01. Enable the mod:")
	public boolean enabled = true;
	
	@Comment({"The directory where mods are located.", "This is relative to the Minecraft install path."})
	@Name("02. Mods folder:")
	public String folder = "mods";
	
	@Comment({"The directory where configs are located.", "Resource files will be located here as well.", "This is relative to the Minecraft install path."})
	@Name("03. Config folder:")
	public String configFolder = "config";
	
	@Comment({"Enables/Disables version checking."})
	@Name("04. Enable the version checker:")
	public boolean enableVersionChecker = true;
	
	@Comment({"The latest published version number.", "This is auto-updated by the version checker.", "This may be @deprecated."})
	@Name("05. Latest mod version available:")
	public String latestVersion = "";
	
	@Comment({"Remind the user of the latest version (as indicated in latestVersion proeprty) update."})
	@Name("06. Enable latest mod version reminder:")
	public boolean latestVersionReminder = true;

	// this remains as general
	@Comment({"Enable/Disable a check to ensure the default loot tables exist on the file system.", "If enabled, then you will not be able to remove any default loot tables (but they can be edited).", "Only disable if you know what you're doing."})
	@Name("07. Enable default loot tables check:")
	public boolean enableDefaultLootTablesCheck = true;
	@Comment({"Enable/Disable a check to ensure the default templates exist on the file system.", "If enabled, then you will not be able to remove any default templates.", "Only disable if you know what you're doing."})
	@Name("08. Enable default templates check:")
	public boolean enableDefaultTemplatesCheck = true;
}

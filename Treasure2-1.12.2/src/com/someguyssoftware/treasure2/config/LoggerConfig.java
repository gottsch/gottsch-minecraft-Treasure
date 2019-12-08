/**
 * 
 */
package com.someguyssoftware.treasure2.config;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.common.config.Config.Comment;
import net.minecraftforge.common.config.Config.Name;

/**
 * @author Mark Gottschling on Nov 30, 2019
 *
 */
public class LoggerConfig {
	@Comment({"The logging level. Set to 'off' to disable logging.", "Values = [trace|debug|info|warn|error|off]"})
	@Name("01. Logging level:")
	public String level = "info";
	
	@Comment({"The size a log file can be before rolling over to a new file."})
	@Name("02. Logs file max. size:")
	public String size = "1000K";
	
	@Comment({"The directory where the logs should be stored.", "This is relative to the Minecraft install path."})
	@Name("03. Logs folder:")
	public String folder = "logs";

	@Comment({"The base filename of the  log file."})
	@Name("04. Base name of log file:")
	public String filename = Treasure.MODID;
}

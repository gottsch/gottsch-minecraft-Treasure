/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Random;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.well.WellGenerator;
import com.someguyssoftware.treasure2.world.gen.structure.TemplateHolder;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Aug 20, 2019
 *
 */
public class SpawnWellStructureCommand extends CommandBase {
	private static final String MOD_ID_ARG = "modid";
	private static final String NAME_ARG = "name";
	
	@Override
	public String getName() {
		return "t2-well";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-well <x> <y> <z> [-modid <mod id> -name <name>]: spawns a Treasure! wishing well at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! well ...");		
		World world = commandSender.getEntityWorld();
		Random random = new Random();
		
		try {
			int x, y, z = 0;
			x = Integer.parseInt(args[0]);
			y = Integer.parseInt(args[1]);
			z = Integer.parseInt(args[2]);
			
			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);

			// create the parser
			CommandLineParser parser = new DefaultParser();

			// create Options object
			Options options = new Options();
			options.addOption(MOD_ID_ARG, true, "");
			options.addOption(NAME_ARG, true, "");

			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);
			
			String modID = Treasure.MODID;
			if (line.hasOption(MOD_ID_ARG)) {
				modID = line.getOptionValue(MOD_ID_ARG);
			}
			
			String name = line.getOptionValue(NAME_ARG);
			if (name != null && !name.contains(".nbt")) {
				name += ".nbt";
			}

			ICoords coords = new Coords(x, y, z);			
			WellGenerator gen = new WellGenerator();
			GeneratorResult<GeneratorData> result = null;
			
			if (modID != null && name != null) {
				// build the template key
				ResourceLocation templateKey = new ResourceLocation(Treasure.MODID + ":" + Treasure.TEMPLATE_MANAGER.getBaseResourceFolder()
								+ "/" + modID + "/wells/" + name);
				
				TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplatesByResourceLocationMap().get(templateKey);
				if (holder == null) {
					Treasure.logger.debug("Unable to locate well template by key -> {}", templateKey.toString());
					return;
				}
	   			result = gen.generate(world, random, coords, holder, TreasureConfig.WELL);
			}
			else {
	   			result = gen.generate(world, random, coords, TreasureConfig.WELL);   		  
			}		
			Treasure.logger.debug("Well start coords at -> {}", result.getData().getSpawnCoords().toShortString());
		}
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! well:", e);
		}
	}
}

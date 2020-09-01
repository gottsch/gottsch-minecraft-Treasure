/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.chest.IChestGenerator;
import com.someguyssoftware.treasure2.generator.oasis.DesertOasisGenerator;
import com.someguyssoftware.treasure2.worldgen.OasisWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Apr 13, 2020
 *
 */
public class SpawnOasisCommand extends CommandBase {
	
	@Override
	public String getName() {
		return "t2-oasis";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-oasis <x> <y> <z> [-type <type>]: spawns a Treasure! oasis at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! oasis ...");
		
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
			options.addOption("type", true, "");

			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);
			
			String type = "desert";
			if (line.hasOption("type")) {
				String rarityArg = line.getOptionValue("type");
			}
			
			Treasure.logger.debug("Type: " + type);
			World world = commandSender.getEntityWorld();

			Random random = new Random();
			// TEMP only desert right now
			DesertOasisGenerator oasisGen = new DesertOasisGenerator();
			GeneratorResult<GeneratorData> result = oasisGen.generate(world, random, new Coords(x, y, z));
		}
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! oasis:", e);
		}
	}
	
    /**
     * Get a list of options for when the user presses the TAB key
     */
//	@Override
//    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
//        if (args.length > 3) {
//        	if (args[args.length - 2].equals("-" + "type")) {
//        		return getListOfStringsMatchingLastWord(args, Rarity.getNames());
//        	}
//        }		
//		return Collections.emptyList();
//    }
}

/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import com.someguyssoftware.gottschcore.positional.Coords;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.enums.PitTypes;
import com.someguyssoftware.treasure2.enums.Pits;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData2;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData2;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitStructureOnlyCommand extends CommandBase {
	private static final String PIT_TYPE_ARG = "pit";
	
	@Override
	public String getName() {
		return "t2-pitstructureonly";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-pitstructureonly <x> <y> <z> [-pit <type>]: spawns a Treasure! pit structure at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! pit structure only...");
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
			options.addOption(PIT_TYPE_ARG, true, "");

			// parse the command line arguments
			CommandLine line = parser.parse(options, parserArgs);

			Pits pit = null;
			if (line.hasOption(PIT_TYPE_ARG)) {
				String pitType = line.getOptionValue(PIT_TYPE_ARG);
				pit = Pits.valueOf(pitType.toUpperCase());
			}
			else {
				pit = Pits.values()[random.nextInt(Pits.values().length)];
			}
			Treasure.logger.debug("pit -> {}", pit);
			
			ICoords spawnCoords = new Coords(x, y, z);
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, new Coords(x, WorldInfo.getHeightValue(world, spawnCoords), z));

			Treasure.logger.debug("spawn coords -> {}", spawnCoords.toShortString());
			Treasure.logger.debug("surface coords -> {}", surfaceCoords.toShortString());
			// select a pit generator
			Map<Pits, IPitGenerator<GeneratorResult<ChestGeneratorData2>>> pitGenMap = SurfaceChestWorldGenerator.pitGens.row(PitTypes.STRUCTURE);
			Treasure.logger.debug("pitGenMap.size -> {}", pitGenMap.size());
			IPitGenerator<GeneratorResult<ChestGeneratorData2>> pitGenerator = pitGenMap.get(pit);
			Treasure.logger.debug("pitGen -> {}", pitGenerator);
			pitGenerator.generate(world, random, surfaceCoords , spawnCoords);
		}		
		catch(Exception e) {
			Treasure.logger.error("Error generating Treasure! pit structure:", e);
		}
	}
	
    /**
     * Get a list of options for when the user presses the TAB key
     */
	@Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length > 3) {
        	if (args[args.length - 2].equals("-" + PIT_TYPE_ARG)) {
        		return getListOfStringsMatchingLastWord(args, Pits.getNames());
        	}
        }		
		return Collections.emptyList();
    }
}

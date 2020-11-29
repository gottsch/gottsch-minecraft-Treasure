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
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;
import com.someguyssoftware.treasure2.generator.GeneratorResult;
import com.someguyssoftware.treasure2.generator.marker.GravestoneMarkerGenerator;
import com.someguyssoftware.treasure2.generator.pit.IPitGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling on Mar 8, 2018
 *
 */
public class SpawnPitOnlyCommand extends CommandBase {
	private static final String PIT_TYPE_ARG = "pit";

	@Override
	public String getName() {
		return "t2-pitonly";
	}

	@Override
	public String getUsage(ICommandSender var1) {
		return "/t2-pitonly <x> <y> <z> [-pit <type>]: spawns a Treasure! pit at location (x,y,z)";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
		Treasure.logger.debug("Starting to build Treasure! pit only ...");
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

			ICoords spawnCoords = new Coords(x, y, z);
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world,
					new Coords(x, WorldInfo.getHeightValue(world, spawnCoords), z));
			Map<Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenMap = SurfaceChestWorldGenerator.pitGens.row(PitTypes.STANDARD);
			IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = pitGenMap.get(pit);
			GeneratorResult<ChestGeneratorData> result = pitGenerator.generate(world, random, surfaceCoords, spawnCoords);
			
			// generate markers
			GravestoneMarkerGenerator markerGen = new GravestoneMarkerGenerator();
			markerGen.generate(world, random, surfaceCoords);
		} catch (Exception e) {
			Treasure.logger.error("Error generating Treasure! pit:", e);
			e.printStackTrace();
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

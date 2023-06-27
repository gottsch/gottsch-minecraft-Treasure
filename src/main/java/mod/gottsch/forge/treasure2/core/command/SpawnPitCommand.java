/**
 * 
 */
package mod.gottsch.forge.treasure2.core.command;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.suggestion.SuggestionProvider;

import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.IWorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldGenContext;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.enums.PitType;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import mod.gottsch.forge.treasure2.core.generator.pit.IStructurePitGenerator;
import mod.gottsch.forge.treasure2.core.registry.PitGeneratorRegistry;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

/**
 * Notes: Rarity is not needed in this command. This command is for generating pits. If a chest is required for <i>test</i> then just a literal argument for chest
 * should suffice and generate a random chest.  Do not want to duplicate the chest command here (unless it could be a sub-command?)
 * 
 * Usage: t2-debug-pit <x> <y> <z> [type [name]] 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitCommand {
	private static final String TYPE_ARG = "type";
	private static final String NAME_ARG = "name";

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPE = (source, builder) -> {
		return SharedSuggestionProvider.suggest(PitType.getNames().stream(), builder);
	};

	/**
	 * 
	 * @param dispatcher
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher
		.register(Commands.literal("t2-pit")
				.requires(source -> {
					return source.hasPermission(2);
				})
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
								.suggests(SUGGEST_TYPE)
								.then(Commands.argument(NAME_ARG, StringArgumentType.string())
										.suggests(SUGGEST_PIT)
										.executes(source -> {
											return spawn(
													source.getSource(),
													BlockPosArgument.getLoadedBlockPos(source, "pos"),
													StringArgumentType.getString(source, TYPE_ARG),
													StringArgumentType.getString(source, NAME_ARG));

										})
										)
								)				
						)
				);
	}

	/**
	 * TODO update this when PitTypes are registered
	 */
	private static final SuggestionProvider<CommandSourceStack> SUGGEST_PIT = (source, builder) -> {
		PitType pitType;
		String type = StringArgumentType.getString(source, TYPE_ARG);
		if (type == null || type.isBlank()) {
			pitType = PitType.STANDARD;
		}
		else {
			try {
				pitType = PitType.get(type);
			}
			catch(Exception e) {
				pitType = PitType.STANDARD;
			}
		}

		List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pits = PitGeneratorRegistry.get(pitType);
		// if structure get the class of the inner generator
		if (pitType == PitType.STRUCTURE) {
			return SharedSuggestionProvider.suggest(pits.stream().map(p -> ((IStructurePitGenerator)p).getGenerator().getClass().getSimpleName()).collect(Collectors.toList()), builder);
		}
		return SharedSuggestionProvider.suggest(pits.stream().map(p -> p.getClass().getSimpleName()).collect(Collectors.toList()), builder);
	};

	
	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSourceStack source, BlockPos pos, String type, String pitName) {
		Treasure.LOGGER.debug("executing spawn pit, pos -> {}, type -> {}, name -> {}", pos, type, pitName);

		try {
			ServerLevel level = source.getLevel();
			RandomSource random = level.getRandom();
			
			// get the type
			PitType pitType;
			if (type == null || type.isBlank()) {
				pitType = PitType.STANDARD;
			}
			else {
				try {
					pitType = PitType.get(type);
				}
				catch(Exception e) {
					pitType = PitType.STANDARD;
				}
			}
			
			List<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pits = PitGeneratorRegistry.get(pitType);
			
			Optional<IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenerator;
			if (pitType == PitType.STRUCTURE) { 
				pitGenerator = pits.stream().filter(p -> ((IStructurePitGenerator)p).getGenerator().getClass().getSimpleName().equalsIgnoreCase(pitName)).findFirst();

			} else {
				pitGenerator = pits.stream().filter(p -> p.getClass().getSimpleName().equalsIgnoreCase(pitName)).findFirst();
			}
			
			if (pitGenerator.isPresent()) {
				// get surface coords
				ICoords coords = new Coords(pos);
				IWorldGenContext context = new WorldGenContext(level, level.getChunkSource().getGenerator(), random);
				ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoordsWG(context,
						new Coords(coords.getX(), WorldInfo.MAX_HEIGHT, coords.getZ()));

				Optional<GeneratorResult<ChestGeneratorData>> result = pitGenerator.get().generate(context, surfaceCoords, coords);
				Treasure.LOGGER.debug("pit result -> {}", result);		
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}

}
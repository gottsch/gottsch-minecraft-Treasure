/**
 * 
 */
package mod.gottsch.forge.treasure2.core.command;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.someguyssoftware.gottschcore.spatial.Coords;
import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.gottschcore.world.WorldInfo;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.data.TreasureData;
import mod.gottsch.forge.treasure2.core.enums.PitTypes;
import mod.gottsch.forge.treasure2.core.enums.Pits;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.generator.pit.IPitGenerator;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

/**
 * Notes: Rarity is not needed in this command. This command is for generating pits. If a chest is required for <i>test</i> then just a literal argument for chest
 * should suffice and generate a random chest.  Do not want to duplicate the chest command here (unless it could be a sub-command?)
 * 
 * Usage: t2-debug-pit <x> <y> <z> [type [name]] 
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnPitCommand {
//	private static final String RARITY_ARG = "rarity";
	private static final String TYPE_ARG = "type";
	private static final String NAME_ARG = "name";

	/*
	 * This command builder pattern is RIDICULOUS
	 */
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-pit")
					.requires(source -> {
						return source.hasPermission(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getOrLoadBlockPos(source, "pos"), "", "");
							})
							.then(Commands.literal(TYPE_ARG)
									.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
									.suggests(SUGGEST_TYPE).executes(source -> {
										return spawn(source.getSource(),
												BlockPosArgument.getOrLoadBlockPos(source, "pos"),
												"",
												StringArgumentType.getString(source, TYPE_ARG));
									})
									.then(Commands.literal(NAME_ARG)
											.then(Commands.argument(NAME_ARG, StringArgumentType.string())
											.suggests(SUGGEST_PIT).executes(source -> {
												return spawn(
														source.getSource(),
														BlockPosArgument.getOrLoadBlockPos(source, "pos"),
														StringArgumentType.getString(source, NAME_ARG),
														StringArgumentType.getString(source, TYPE_ARG));
											})
									))
							))						
					)
			);
	}

	/**
	 * 
	 */
	private static final SuggestionProvider<CommandSource> SUGGEST_PIT = (source, builder) -> {
		try {
			Optional<String> typeArgValue = getArgumentFromCommandChain(source, TYPE_ARG);
			if (typeArgValue.isPresent()) {
				Treasure.LOGGER.debug("childs type arg -> {}", typeArgValue.get());
				Optional<PitTypes> pitType = getPitType(typeArgValue.get());
				if (pitType.isPresent()) {
					Set<Pits> pits = TreasureData.PIT_GENS.row(pitType.get()).keySet();
					Treasure.LOGGER.debug("pits -> {}", pits);
					return ISuggestionProvider.suggest(pits.stream().map(x -> x.name()).collect(Collectors.toList()), builder);
				}
				else {
					Treasure.LOGGER.debug("type arg is not correct/present");
				}
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.debug("exception throw -> {}", e);
		}
		return ISuggestionProvider.suggest(Pits.getNames().stream(), builder);
	};
	
//	private static final SuggestionProvider<CommandSource> SUGGEST_RARITY = (source, builder) -> {
//		return ISuggestionProvider.suggest(Rarity.getNames().stream(), builder);
//	};
	
	private static final SuggestionProvider<CommandSource> SUGGEST_TYPE = (source, builder) -> {
		return ISuggestionProvider.suggest(PitTypes.getNames().stream(), builder);
	};
	
	/**
	 * 
	 * @param source
	 * @param pos
	 * @param name
	 * @return
	 */
	private static int spawn(CommandSource source, BlockPos pos, String pitName, String type) {
		Treasure.LOGGER.debug("executing spawn pit, pos -> {}, name -> {}, type -> {}", pos, pitName, type);
		
		try {
			ServerWorld world = source.getLevel();
			Random random = new Random();
						
			IPitGenerator<GeneratorResult<ChestGeneratorData>> pitGenerator = null;							
			Map<Pits, IPitGenerator<GeneratorResult<ChestGeneratorData>>> pitGenMap = null;
			Optional<PitTypes> pitType = getPitType(type);
			if (pitType.isPresent() && pitType.get() != PitTypes.UNKNOWN) {
				Treasure.LOGGER.debug("pit type -> {}", pitType.get());
				pitGenMap =TreasureData.PIT_GENS.row(pitType.get());
			}
			else {
				pitGenMap =TreasureData.PIT_GENS.row(PitTypes.STANDARD);
			}
			
			Pits pit = null;
			if (pitName != null && !pitName.isEmpty()) {
				pit = Pits.valueOf(pitName.toUpperCase());
				Treasure.LOGGER.debug("pit name -> {}", pit.name());
				pitGenerator = pitGenMap.get(pit);
			}
			else {
				// get a random pit generator
				List<Pits> keys = pitGenMap.keySet().stream().collect(Collectors.toList());
				pitGenerator = pitGenMap.get(keys.get(random.nextInt(keys.size())));
				Treasure.LOGGER.debug("random pit generator -> {}", pitGenerator.getClass().getSimpleName());
			}

			// get surface coords
			ICoords coords = new Coords(pos);
			ICoords surfaceCoords = WorldInfo.getDryLandSurfaceCoords(world, 
					new Coords(coords.getX(), WorldInfo.getHeight(world, coords), coords.getZ()));
			
			// TODO check if coords.Y >= surface.Y. if so, error out.
			
			GeneratorResult<ChestGeneratorData> result = pitGenerator.generate(world, random, surfaceCoords, coords);
			Treasure.LOGGER.debug("pit result -> {}", result);		
		}
		catch(Exception e) {
			Treasure.LOGGER.error("an error occurred: ", e);
		}
		return 1;
	}
	
	/**
	 * Follows the Command chain searching for the argument. Swallows IllegalArgumentException exceptions and returns empty Optional if not found.
	 * @param source
	 * @param typeArg
	 * @return
	 */
	private static Optional<String> getArgumentFromCommandChain(CommandContext<CommandSource> source, String typeArg) {
		while (source != null) {
			try {
					return Optional.of(StringArgumentType.getString(source, TYPE_ARG));
			}
			catch(IllegalArgumentException e) {
				source = source.getChild();
			}
		}
		return Optional.empty();
	}

	public static Optional<PitTypes> getPitType(String type) {
		Optional<PitTypes> pitType = Optional.empty();
		pitType = Optional.ofNullable(PitTypes.getByValue(type.toLowerCase()));
		return pitType;
	}
	
	/*
	 * 
	 * 
							// rarity
							.then(Commands.literal(RARITY_ARG)
									.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
									.suggests(SUGGEST_RARITY).executes(source -> {
										return spawn(source.getSource(), 
												BlockPosArgument.getBlockPos(source, "pos"),
												StringArgumentType.getString(source, RARITY_ARG),
												"",
												"");
									})
									// rarity > name
									.then(Commands.literal(NAME_ARG)
											.then(Commands.argument(NAME_ARG, StringArgumentType.string())
											.suggests(SUGGEST_PIT).executes(source -> {
												return spawn(
														source.getSource(),
														BlockPosArgument.getBlockPos(source, "pos"),
														StringArgumentType.getString(source, RARITY_ARG),
														StringArgumentType.getString(source, NAME_ARG),
														"");
											})
											// rarity > name > type
											.then(Commands.literal(TYPE_ARG)
													.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
													.suggests(SUGGEST_TYPE).executes(source -> {
														return spawn(source.getSource(),
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))
									))
									// rarity > type
									.then(Commands.literal(TYPE_ARG)
											.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
											.suggests(SUGGEST_TYPE).executes(source -> {
												return spawn(source.getSource(),
														BlockPosArgument.getBlockPos(source, "pos"),
														StringArgumentType.getString(source, RARITY_ARG), 
														"",
														StringArgumentType.getString(source, TYPE_ARG));
											})
											// rarity > type > name
											.then(Commands.literal(NAME_ARG)
													.then(Commands.argument(NAME_ARG, StringArgumentType.string())
													.suggests(SUGGEST_PIT).executes(source -> {
														return spawn(
																source.getSource(),
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))
									))		
							))
							// name [NAME]
							.then(Commands.literal(NAME_ARG)
									.then(Commands.argument(NAME_ARG, StringArgumentType.string())
									.suggests(SUGGEST_PIT).executes(source -> {
										return spawn(
												source.getSource(),
												BlockPosArgument.getBlockPos(source, "pos"),
												Rarity.COMMON.name(),
												StringArgumentType.getString(source, NAME_ARG),
												"");
									})
									// name > rarity
									.then(Commands.literal(RARITY_ARG)
											.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
											.suggests(SUGGEST_RARITY).executes(source -> {
												return spawn(source.getSource(), 
														BlockPosArgument.getBlockPos(source, "pos"),
														StringArgumentType.getString(source, RARITY_ARG),
														StringArgumentType.getString(source, NAME_ARG),
														"");
											})
											// name > rarity > type
											.then(Commands.literal(TYPE_ARG)
													.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
													.suggests(SUGGEST_TYPE).executes(source -> {
														return spawn(source.getSource(),
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))	
									))
									// name > type
									.then(Commands.literal(TYPE_ARG)
											.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
											.suggests(SUGGEST_TYPE).executes(source -> {
												return spawn(source.getSource(),
														BlockPosArgument.getBlockPos(source, "pos"),
														Rarity.COMMON.name(), 
														StringArgumentType.getString(source, NAME_ARG),
														StringArgumentType.getString(source, TYPE_ARG));
											})
											// name > type > rarity
											.then(Commands.literal(RARITY_ARG)
													.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
													.suggests(SUGGEST_RARITY).executes(source -> {
														return spawn(source.getSource(), 
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))
									))	
							))
							// type
							.then(Commands.literal(TYPE_ARG)
									.then(Commands.argument(TYPE_ARG, StringArgumentType.string())
									.suggests(SUGGEST_TYPE).executes(source -> {
										return spawn(source.getSource(),
												BlockPosArgument.getBlockPos(source, "pos"),
												Rarity.COMMON.name(), 
												"",
												StringArgumentType.getString(source, TYPE_ARG));
									})
									// type > rarity
									.then(Commands.literal(RARITY_ARG)
											.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
											.suggests(SUGGEST_RARITY).executes(source -> {
												return spawn(source.getSource(), 
														BlockPosArgument.getBlockPos(source, "pos"),
														StringArgumentType.getString(source, RARITY_ARG),
														"",
														StringArgumentType.getString(source, TYPE_ARG));
											})
											// type > rarity > name
											.then(Commands.literal(NAME_ARG)
													.then(Commands.argument(NAME_ARG, StringArgumentType.string())
													.suggests(SUGGEST_PIT).executes(source -> {
														return spawn(
																source.getSource(),
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))
									))
									// type > name
									.then(Commands.literal(NAME_ARG)
											.then(Commands.argument(NAME_ARG, StringArgumentType.string())
											.suggests(SUGGEST_PIT).executes(source -> {
												return spawn(
														source.getSource(),
														BlockPosArgument.getBlockPos(source, "pos"),
														Rarity.COMMON.name(),
														StringArgumentType.getString(source, NAME_ARG),
														StringArgumentType.getString(source, TYPE_ARG));
											})
											// type > name > rarity
											.then(Commands.literal(RARITY_ARG)
													.then(Commands.argument(RARITY_ARG, StringArgumentType.string())
													.suggests(SUGGEST_RARITY).executes(source -> {
														return spawn(source.getSource(), 
																BlockPosArgument.getBlockPos(source, "pos"),
																StringArgumentType.getString(source, RARITY_ARG),
																StringArgumentType.getString(source, NAME_ARG),
																StringArgumentType.getString(source, TYPE_ARG));
													})
											))
									))
							))
			*/
}
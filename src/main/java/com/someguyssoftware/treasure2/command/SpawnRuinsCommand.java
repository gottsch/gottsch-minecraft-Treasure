/**
 * 
 */
package com.someguyssoftware.treasure2.command;

import com.mojang.brigadier.CommandDispatcher;
import com.someguyssoftware.treasure2.command.argument.TemplateLocationArgument;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;

import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;

/**
 * Usage: /t2-ruins <x> <y> <z> [-modid <mod id> -archetype <archetype> -name <name>] [-decay <relative filepath>] [-rarity <rarity>]
 * Spawns ruins at location (x, y, z) using the optional structure name, and option decay ruleset.
 * @author Mark Gottschling on Jan 25, 2018
 *
 */
public class SpawnRuinsCommand {
	private static final String MOD_ID_ARG = "modid";
	private static final String ARCHETYPE_ARG = "archetype";
	private static final String NAME_ARG = "name";
	private static final String DECAY_ARG = "decay";
	private static final String RARITY_ARG = "rarity";
	
	public static void register(CommandDispatcher<CommandSource> dispatcher) {
		dispatcher
			.register(Commands.literal("t2-ruins")
					.requires(source -> {
						return source.hasPermissionLevel(2);
					})
					.then(Commands.argument("pos", BlockPosArgument.blockPos())
							.executes(source -> {
								return spawn(source.getSource(), BlockPosArgument.getBlockPos(source, "pos"), null);
							})
							.then(Commands.argument("template", TemplateLocationArgument.templateLocation())
									.executes(source -> {
										return spawn(
                                            source.getSource(), 
                                            BlockPosArgument.getBlockPos(source, "pos"), 
                                            TemplateLocationArgument.getTemplateLocation(source, "template"));
                                    })
                            )
                            // .then(Commands.literal(DECAY_ARG)
                            //     .then(Commands.argument(DECAY_ARG, StringArgumentType.string())
                            //         .suggests(SUGGEST_TYPE).executes(source -> {
                            //             return spawn(source.getSource(),
                            //                     BlockPosArgument.getBlockPos(source, "pos"), TemplateLocationArgument.getTemplateLocation(source, "template"));
                            //         })
                            // )
                            .then(Commands.argument("decay", DecayArgument.decay()
                                    .execute(source -> {
                                        return spawn(
                                            source.getSource(), 
                                            BlockPosArgument.getBlockPos(source, "pos"), 
                                            Optional.empty());
                                })
                            )
					)
			);
	}
    
	public static int spawn(CommandSource source, BlockPos pos, Optional<TemplateLocation> templateLocation) {
        String modID = Treasure.MODID;
	    String archetype = StructureArchetype.SURFACE.getValue();
		String name = line.getOptionValue(NAME_ARG);
        IDecayRuleSet ruleSet = null;
        
        if (templateLocation.isPresent()) {
				modID = templateLocation.getModID();
				archetype = templateLocation.getArchetype();
                name = templateLocation.getName();
		}
        
        // get the ruleset to use from the decay manager
        if (decayRuleset.isPresent()) {
            String rulesetName = decayRuleset.getName();
            if (!ruleSetName.contains(".json")) {
                ruleSetName += ".json";
            }
            // build the key
            String key = (Treasure.MODID + ":" + "mc1_15/decay/" + ruleSetName).replace("\\", "/");
            ruleSet = TreasureDecayRegistry.get(key);
        }


		return 0;
	}
    
    // TODO where does this go? In the arugment class
    private static final SuggestionProvider<CommandSource> SUGGEST_ARCHETYPE = (source, builder) -> {
		return ISuggestionProvider.suggest(StructureArchetype.getNames().stream(), builder);
    };

//    
//	@Override
//	public void execute(MinecraftServer server, ICommandSender commandSender, String[] args) {
//		Treasure.logger.debug("Starting to build Treasure! ruins ...");
//		
//		try {
//			// extract the coords args
//			int x, y, z = 0;
//			x = Integer.parseInt(args[0]);
//			y = Integer.parseInt(args[1]);
//			z = Integer.parseInt(args[2]);
//
//			String[] parserArgs = (String[]) Arrays.copyOfRange(args, 3, args.length);
//			
//			// create the parser
//			CommandLineParser parser = new DefaultParser();			
//
//			// create Options object
//			Options options = new Options();
//			options.addOption(MOD_ID_ARG, true, "");
//			options.addOption(ARCHETYPE_ARG, true, "");
//			options.addOption(NAME_ARG, true, "");
//			options.addOption(DECAY_ARG, true, "");
//			options.addOption(RARITY_ARG, true, "");
//			
//			// parse the command line arguments
//			CommandLine line = parser.parse( options, parserArgs);
//
//			String modID = Treasure.MODID;
//			if (line.hasOption(MOD_ID_ARG)) {
//				modID = line.getOptionValue(MOD_ID_ARG);
//			}
//			
//			String archetype = StructureArchetype.SURFACE.getValue();
//			if (line.hasOption(ARCHETYPE_ARG)) {
//				archetype = line.getOptionValue(ARCHETYPE_ARG).toLowerCase();
//			}
//			
//			String name = line.getOptionValue(NAME_ARG);
//			if (name != null && !name.contains(".nbt")) {
//				name += ".nbt";
//			}
//			
//			// get the ruleset to use from the decay manager
//			IDecayRuleSet ruleSet = null;
//			if (line.hasOption(DECAY_ARG)) {
//				String ruleSetName = line.getOptionValue(DECAY_ARG);
//				if (!ruleSetName.contains(".json")) {
//					ruleSetName += ".json";
//				}
//				// build the key
//				String key = (Treasure.MODID + ":" + "decay/" + ruleSetName).replace("\\", "/");
//				ruleSet = Treasure.DECAY_MANAGER.getRuleSetMap().get(key);
//			}
//			
//			Rarity rarity = null;
//			if (line.hasOption(RARITY_ARG)) {
//				String rarityArg = line.getOptionValue(RARITY_ARG);
//				rarity = Rarity.valueOf(rarityArg.toUpperCase());			
//			}	
//			
//			World world = commandSender.getEntityWorld();
//			Random random = new Random();
//			ICoords coords = new Coords(x, y, z);
//			
//			// get the structure generator
//			SurfaceChestWorldGenerator worldGen = 
//					(SurfaceChestWorldGenerator) Treasure.WORLD_GENERATORS.get(WorldGeneratorType.SURFACE_CHEST);
//			
//			// build the template key
//			ResourceLocation templateKey = new ResourceLocation(Treasure.MODID + ":" + Treasure.TEMPLATE_MANAGER.getBaseResourceFolder()
//							+ "/" + modID + "/" + archetype	+ "/" + name);
//			
//			TemplateHolder holder = Treasure.TEMPLATE_MANAGER.getTemplatesByResourceLocationMap().get(templateKey);
//			if (holder == null) {
//				Treasure.logger.debug("Unable to locate template by key -> {}", templateKey.toString());
//			}
//				
//			// select a chest
//			if (rarity == null) {
//				rarity = Rarity.values()[random.nextInt(Rarity.values().length)];
//			}
////			IChestConfig config = TreasureConfig.CHESTS.surfaceChests.configMap.get(rarity);
//			
//			// generate
//			GeneratorResult<ChestGeneratorData> result = worldGen.generateSurfaceRuins(world, random,coords, holder, ruleSet, null);
//			Treasure.logger.debug("result from t2-ruins -> {}", result);
//			if (result.isSuccess() && result.getData().getChestContext().getCoords() != null) {
//				IChestGenerator chestGen = worldGen.getChestGenMap().get(rarity).next();
//				ICoords chestCoords = result.getData().getChestContext().getCoords();
//				Treasure.logger.debug("chestCoords -> {}", chestCoords);
//				// move the chest coords to the first dry land beneath it.
////				chestCoords = WorldInfo.getDryLandSurfaceCoords(world, chestCoords);
//				if (chestCoords == WorldInfo.EMPTY_COORDS) chestCoords = null;
//				
//				if (chestCoords != null) {
//					GeneratorResult<ChestGeneratorData> chestResult = chestGen.generate(world, random, chestCoords, rarity, result.getData().getChestContext().getState());
//				}
//			}
//		}
//		catch(Exception e) {
//			Treasure.logger.error("Error generating Treasure! ruins:", e);
//		}
//	}
//	
//    /**
//     * Get a list of options for when the user presses the TAB key
//     */
//	@Override
//    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
//        if (args.length > 3) {
//        	if (args[args.length - 2].equals("-" + RARITY_ARG)) {
//        		return getListOfStringsMatchingLastWord(args, Rarity.getNames());
//        	}
//        	else if(args[args.length - 2].equals("-" + ARCHETYPE_ARG)) {
//        		return getListOfStringsMatchingLastWord(args, StructureArchetype.getNames());
//        	}        	
//        }		
//		return Collections.emptyList();
//    }
}
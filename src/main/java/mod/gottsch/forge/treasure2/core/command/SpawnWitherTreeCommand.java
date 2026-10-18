/*
 * This file is part of Treasure2.
 * Copyright (c) 2026 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherTreeArchetype;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherTreeBlueprint;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherTreeBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.commands.PlaceCommand;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Arrays;
import java.util.Locale;

/**
 * /t2 wither_tree &lt;pos&gt; [wither|great] [seed] [scale] grows one wither tree with its trunk starting at
 * pos. The seed is echoed back, so a tree worth keeping (or a bad one) can be grown again. Scale (0.5 to 1,
 * default 1) is the size a grove gives trees toward its edge.
 * <p>
 * /t2 wither_grove &lt;pos&gt; places a whole Wither Grove, exactly as /place structure
 * treasure2:wither_tree/wither_tree does: the main tree is set in the chunk containing pos.
 *
 * @author Mark Gottschling on 10/18/2026
 */
public class SpawnWitherTreeCommand {
	private static final String T2 = "t2";
	private static final String WITHER_TREE = "wither_tree";
	private static final String WITHER_GROVE = "wither_grove";
	private static final String POS = "pos";
	private static final String TYPE = "type";
	private static final String SEED = "seed";
	private static final String SCALE = "scale";

	private static final ResourceKey<Structure> GROVE = ResourceKey.create(Registries.STRUCTURE,
			new ResourceLocation(Treasure.MODID, "wither_tree/wither_tree"));

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_TYPE = (source, builder) ->
			SharedSuggestionProvider.suggest(Arrays.stream(WitherTreeArchetype.values()).map(a -> a.name().toLowerCase(Locale.ROOT)), builder);

	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		dispatcher.register(Commands.literal(T2)
				.requires(source -> source.hasPermission(2))
				.then(Commands.literal(WITHER_TREE)
						.then(Commands.argument(POS, BlockPosArgument.blockPos())
								.executes(context -> spawnTree(context, WitherTreeArchetype.WITHER, null, 1F))
								.then(Commands.argument(TYPE, StringArgumentType.word())
										.suggests(SUGGEST_TYPE)
										.executes(context -> spawnTree(context, type(context), null, 1F))
										.then(Commands.argument(SEED, LongArgumentType.longArg())
												.executes(context -> spawnTree(context, type(context), LongArgumentType.getLong(context, SEED), 1F))
												.then(Commands.argument(SCALE, FloatArgumentType.floatArg(0.5F, 1F))
														.executes(context -> spawnTree(context, type(context), LongArgumentType.getLong(context, SEED),
																FloatArgumentType.getFloat(context, SCALE))))))))
				.then(Commands.literal(WITHER_GROVE)
						.then(Commands.argument(POS, BlockPosArgument.blockPos())
								.executes(SpawnWitherTreeCommand::spawnGrove))));
	}

	private static WitherTreeArchetype type(CommandContext<CommandSourceStack> context) {
		return WitherTreeArchetype.byName(StringArgumentType.getString(context, TYPE).toUpperCase(Locale.ROOT));
	}

	private static int spawnTree(CommandContext<CommandSourceStack> context, WitherTreeArchetype archetype, Long seed, float scale) throws CommandSyntaxException {
		ServerLevel level = context.getSource().getLevel();
		BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, POS);
		long treeSeed = seed != null ? seed : level.getRandom().nextLong();
		// the blueprint's origin is the ground block under the trunk
		BlockPos origin = pos.below();
		WitherTreeBlueprint blueprint = WitherTreeBuilder.build(treeSeed, archetype, scale);
		BoundingBox box = blueprint.bounds(origin);
		// the clearing and vines read heightmaps, which an unloaded chunk reports as the world's floor
		ChunkPos.rangeClosed(new ChunkPos(SectionPos.blockToSectionCoord(box.minX()), SectionPos.blockToSectionCoord(box.minZ())),
				new ChunkPos(SectionPos.blockToSectionCoord(box.maxX()), SectionPos.blockToSectionCoord(box.maxZ())))
				.forEach(c -> level.getChunk(c.x, c.z));
		blueprint.place(level, origin, new BoundingBox(box.minX(), level.getMinBuildHeight(), box.minZ(), box.maxX(), level.getMaxBuildHeight(), box.maxZ()));
		context.getSource().sendSuccess(() -> Component.literal(String.format("Grew a %s wither tree at %d, %d, %d (seed %d)",
				archetype.name().toLowerCase(Locale.ROOT), pos.getX(), pos.getY(), pos.getZ(), treeSeed)), true);
		return 1;
	}

	private static int spawnGrove(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		Holder.Reference<Structure> grove = context.getSource().registryAccess().registryOrThrow(Registries.STRUCTURE).getHolderOrThrow(GROVE);
		return PlaceCommand.placeStructure(context.getSource(), grove, BlockPosArgument.getLoadedBlockPos(context, POS));
	}
}

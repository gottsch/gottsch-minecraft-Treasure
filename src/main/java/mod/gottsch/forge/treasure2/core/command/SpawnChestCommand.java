/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import mod.gottsch.forge.gottschcore.random.RandomHelper;
import mod.gottsch.forge.gottschcore.spatial.Heading;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.block.AbstractTreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.ITreasureChestBlock;
import mod.gottsch.forge.treasure2.core.block.StandardChestBlock;
import mod.gottsch.forge.treasure2.core.block.entity.AbstractTreasureChestBlockEntity;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.enums.WishableExtraRarity;
import mod.gottsch.forge.treasure2.core.rarity.IRarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.RarityEntry;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.registry.ChestRegistry;
import mod.gottsch.forge.treasure2.core.registry.ChestSubprocessorDataRegistry;
import mod.gottsch.forge.treasure2.core.registry.MimicRegistry;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.IChestSubprocessor;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.TreasureChestSubprocessors;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.data.ChestSubprocessorData;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Mark Gottschling on Aug 28, 2020
 *
 */
public class SpawnChestCommand {

	private static final String NAME = "name";
	private static final String LOCKED = "locked";
	private static final String SEALED = "sealed";
	private static final String MIMIC = "mimic";

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_RARITY = (source, builder) -> {
		return SharedSuggestionProvider.suggest(TreasureApi.getRarities()
				.stream()
				.filter(r -> !(r instanceof WishableExtraRarity))
				.map(r -> r.getName()), builder);
	};

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_CHEST = (source, builder) -> {
		return SharedSuggestionProvider.suggest(ChestRegistry.getNames().stream().map(c -> c.toString()), builder);
	};

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_DIRECTION = (source, builder) -> {    	
		return SharedSuggestionProvider.suggest(Heading.getNames().stream().filter(x -> !x.equalsIgnoreCase("UP") && !x.equalsIgnoreCase("DOWN")), builder);
	};

	private static final SuggestionProvider<CommandSourceStack> SUGGEST_MIMIC = (source, builder) -> {    	
		return SharedSuggestionProvider.suggest(MimicRegistry.getMimics().stream().map(x -> x.toString()), builder);
	};

	/**
	 * registers the command with the command dispatcher.
	 * @param dispatcher The command dispatcher.
	 */
	public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
		LiteralArgumentBuilder<CommandSourceStack> baseCommand = Commands.literal("t2-chest")
				.requires(source -> source.hasPermission(2))
				.then(Commands.argument("pos", BlockPosArgument.blockPos())
						.executes(context -> spawn(context, false, false, false))
						.then(Commands.argument(NAME, ResourceLocationArgument.id())
								.suggests(SUGGEST_CHEST)
								.executes(context -> spawn(context, false, false, false))
								.then(Commands.argument("rarity", StringArgumentType.string())
										.suggests(SUGGEST_RARITY)
										.executes(context -> spawn(context, false, false, false))
										.then(Commands.argument("direction", StringArgumentType.string())
												.suggests(SUGGEST_DIRECTION)
												// Start the final, correct argument tree.
												.executes(context -> spawn(context, false, false, false))
												.then(recursiveBooleanArgumentBuilder(
														Commands.literal(LOCKED),
														List.of(SEALED, MIMIC),
														true, false, false))
												.then(recursiveBooleanArgumentBuilder(
														Commands.literal(SEALED),
														List.of(LOCKED, MIMIC),
														false, true, false))
												.then(recursiveBooleanArgumentBuilder(
														Commands.literal(MIMIC),
														List.of(LOCKED, SEALED),
														false, false, true))
										)
								)
						)
				);

		dispatcher.register(baseCommand);
	}

	/**
	 * recursively builds the command tree for all boolean combinations.
	 * @param literalBuilder the starting literal builder for this branch.
	 * @param remainingOptions a list of the remaining literal arguments to add.
	 * @param locked whether the chest should be locked in this branch.
	 * @param sealed whether the chest should be sealed in this branch.
	 * @param mimic whether the chest should be a mimic in this branch.
	 * @return a completed LiteralArgumentBuilder.
	 */
	private static LiteralArgumentBuilder<CommandSourceStack> recursiveBooleanArgumentBuilder(
			LiteralArgumentBuilder<CommandSourceStack> literalBuilder,
			List<String> remainingOptions, boolean locked, boolean sealed, boolean mimic) {

		literalBuilder.executes(context -> spawn(context, locked, sealed, mimic));

		for (String option : remainingOptions) {
			LiteralArgumentBuilder<CommandSourceStack> newBranch = Commands.literal(option);
			newBranch.executes(context -> spawn(context,
					LOCKED.equals(option) || locked,
					SEALED.equals(option) || sealed,
					MIMIC.equals(option) || mimic));

			// Recursively build the next level of options for this new branch.
			List<String> nextOptions = new ArrayList<>(remainingOptions);
			nextOptions.remove(option);
			recursiveBooleanArgumentBuilder(newBranch, nextOptions,
					LOCKED.equals(option) || locked,
					SEALED.equals(option) || sealed,
					MIMIC.equals(option) || mimic);

			literalBuilder.then(newBranch);
		}

		return literalBuilder;
	}

	private static int spawn(CommandContext<CommandSourceStack> context, boolean locked, boolean sealed, boolean mimic) {
		try {
			BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
			String chestName = "";
			String rarityName = Rarity.COMMON.name();
			String directionName = Heading.SOUTH.name();

			try { chestName = ResourceLocationArgument.getId(context, NAME).toString(); } catch (IllegalArgumentException ignored) {}
			try { rarityName = StringArgumentType.getString(context, "rarity"); } catch (IllegalArgumentException ignored) {}
			try { directionName = StringArgumentType.getString(context, "direction"); } catch (IllegalArgumentException ignored) {}

			Treasure.LOGGER.debug("executing spawn chest, pos -> {}, name -> {}, rarity -> {}, locked -> {}, sealed -> {}, mimic -> {}", pos, chestName, rarityName, locked, sealed, mimic);

			ServerLevel level = context.getSource().getLevel();
			RandomSource random = level.getRandom();

			Heading heading = Heading.valueOf(directionName.isEmpty() ? Heading.SOUTH.name() : directionName);
			FeatureType featureType = FeatureType.TERRANEAN;
			IRarityEntry rarity = TreasureRarities.getRarityByName(ModUtil.asLocation(rarityName.trim().toLowerCase()))
					.orElseGet(TreasureRarities.COMMON::get);

			// 1. get the subprocessor data with a fallback.
			ChestSubprocessorData subprocessorData = ChestSubprocessorDataRegistry.getAssociation(featureType, rarity)
					.orElseGet(() -> {
						Treasure.LOGGER.warn("unable to locate chest subprocessor data for feature type -> {} and rarity -> {}. Reverting to default.", featureType, rarity.getName());
						return TreasureChestSubprocessors.STANDARD.get().getData();
					});

			// 2. get the subprocessor based on the data, with a fallback.
			IChestSubprocessor processor = TreasureChestSubprocessors.getChestSubprocessor(subprocessorData.getType())
					.orElseGet(() -> {
						Treasure.LOGGER.warn("unable to locate chest subprocessor for processor type -> {}. Reverting to default.", subprocessorData.getType());
						return TreasureChestSubprocessors.STANDARD.get();
					});

			// 3. set the data on the (potentially default) processor.
			processor.setFeatureType(featureType);
			processor.setData(subprocessorData);

			// 4. get the chest block.
			AbstractTreasureChestBlock chest = Optional.ofNullable((AbstractTreasureChestBlock) ForgeRegistries.BLOCKS.getValue(ModUtil.asLocation(chestName)))
					.orElseGet(() -> {
						Treasure.LOGGER.warn("unable to locate a treasure chest with the given name. Selecting a random chest from the subprocessor.");
						return processor.selectChest(random, rarity);
					});

			CompoundTag tag = buildNbtTag(random, chest, processor, heading, rarity, locked, sealed, mimic);
			BlockState chestState = buildBlockState(level, pos, chest, heading);

			level.setBlock(pos, chestState, 3);

			updateBlockEntity(level, pos, tag, heading);

			return 1;
		} catch (Exception e) {
			Treasure.LOGGER.error("An error occurred: ", e);
			return 0;
		}
	}


	private static CompoundTag buildNbtTag(RandomSource random, AbstractTreasureChestBlock chest, IChestSubprocessor processor, Heading heading, IRarityEntry rarity, boolean locked, boolean sealed, boolean mimic) {
		CompoundTag tag = new CompoundTag();

		if (locked || sealed) {
			processor.addLootTable(random, tag, rarity);
		}
		processor.addSeal(tag, sealed);
		if (locked) {
			// manually add locks - min locks = 1 since required by player
			int numLocks = RandomHelper.randomInt(1, chest.getLockLayout().getMaxLocks());
			processor.addLocks(tag, random, chest.getLockLayout(), processor.selectLocks(chest.getLockLayout(), rarity), numLocks, Heading.NORTH.getRotation(heading));
		}
		if (mimic) {
			MimicRegistry.getMimic(ModUtil.getName(chest))
					.ifPresent(mimicName -> tag.putString(AbstractTreasureChestBlockEntity.MIMIC_TAG, mimicName.toString()));
		}

		processor.addGenerationContext(tag, processor.getFeatureType(), (RarityEntry) rarity);
		return tag;
	}

	private static BlockState buildBlockState(ServerLevel level, BlockPos pos, AbstractTreasureChestBlock chest, Heading heading) {
		FluidState fluidState = level.getBlockState(pos).getFluidState();
		Direction direction = heading.getDirection();
		return chest.defaultBlockState()
				.setValue(StandardChestBlock.FACING, direction)
				.setValue(ITreasureChestBlock.DISCOVERED, false)
				.setValue(AbstractTreasureChestBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER);
	}

	private static void updateBlockEntity(ServerLevel level, BlockPos pos, CompoundTag tag, Heading heading) {
		Direction direction = heading.getDirection();
		tag.putInt(AbstractTreasureChestBlockEntity.FACING_TAG, direction.get3DDataValue());

		if (level.getBlockEntity(pos) instanceof AbstractTreasureChestBlockEntity chestEntity) {
			chestEntity.load(tag);
			chestEntity.sendUpdates();
		}
	}
}

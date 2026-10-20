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
package mod.gottsch.forge.treasure2.datagen;

import java.util.concurrent.CompletableFuture;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.tags.TreasureTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

/**
 * 
 * @author Mark Gottschling on Nov 11, 2022
 *
 */
public class TreasureBlockTagsProvider extends BlockTagsProvider {
    
    public TreasureBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
			ExistingFileHelper existingFileHelper) {
    	super(output, lookupProvider, Treasure.MODID, existingFileHelper);
	}

	@Override
    protected void addTags(Provider provider) {
    	// withered grass behaves as grass: dug with a shovel, and plants grow on it
    	tag(BlockTags.MINEABLE_WITH_SHOVEL).add(TreasureBlocks.WITHERED_GRASS_BLOCK.get());
    	tag(BlockTags.DIRT).add(TreasureBlocks.WITHERED_GRASS_BLOCK.get());
    	tag(BlockTags.MINEABLE_WITH_AXE).add(TreasureBlocks.HOLLOW_WITHERWOOD_LOG.get());
    	// what the hanging roots processor hangs roots from
    	tag(TreasureTags.Blocks.HANGING_ROOTS_CEILINGS).addTags(BlockTags.DIRT, BlockTags.LOGS, BlockTags.PLANKS)
    			.add(net.minecraft.world.level.block.Blocks.MUD_BRICKS, net.minecraft.world.level.block.Blocks.PACKED_MUD,
    					net.minecraft.world.level.block.Blocks.MUD_BRICK_STAIRS, net.minecraft.world.level.block.Blocks.MUD_BRICK_SLAB,
    					TreasureBlocks.WITHERWOOD_LOG.get(), TreasureBlocks.WITHERWOOD_WOOD.get(), TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(),
    					TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(), TreasureBlocks.WITHERWOOD_PLANKS.get(), TreasureBlocks.WITHERWOOD_STAIRS.get(),
    					TreasureBlocks.WITHERWOOD_SLAB.get(), TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get(), TreasureBlocks.WITHERED_GRASS_BLOCK.get());
    	tag(BlockTags.MINEABLE_WITH_AXE).add(TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get());
    	// witherwood is wood: an axe cuts all of it. It is in neither #logs nor #planks, so nothing else puts it here
    	tag(BlockTags.MINEABLE_WITH_AXE).add(TreasureBlocks.WITHERWOOD_ROOT.get(),
    			TreasureBlocks.WITHERWOOD_BRANCH.get(),
    			TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(),
    			TreasureBlocks.WITHERWOOD_TWIG.get(),
    			TreasureBlocks.WITHERWOOD_LOG.get(),
    			TreasureBlocks.WITHERWOOD_WOOD.get(),
    			TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(),
    			TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(),
    			TreasureBlocks.WITHERWOOD_PLANKS.get(),
    			TreasureBlocks.WITHERWOOD_SLAB.get(),
    			TreasureBlocks.WITHERWOOD_STAIRS.get(),
    			TreasureBlocks.WITHERWOOD_FENCE.get(),
    			TreasureBlocks.WITHERWOOD_FENCE_GATE.get(),
    			TreasureBlocks.WITHERWOOD_DOOR.get(),
    			TreasureBlocks.WITHERWOOD_TRAPDOOR.get(),
    			TreasureBlocks.WITHERWOOD_BUTTON.get(),
    			TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get(),
    			TreasureBlocks.WITHERWOOD_SIGN.get(),
    			TreasureBlocks.WITHERWOOD_WALL_SIGN.get(),
    			TreasureBlocks.WITHERWOOD_HANGING_SIGN.get(),
    			TreasureBlocks.WITHERWOOD_WALL_HANGING_SIGN.get(),
    			TreasureBlocks.BRACKET_FUNGUS.get());

    	// blocks rarity
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.WOOD_CHEST.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.MOLDY_CRATE_CHEST.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.CARDBOARD_BOX.get());
    	tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.MILK_CRATE.get());
		tag(TreasureTags.Blocks.COMMON_CHESTS).add(TreasureBlocks.VANILLA_CHEST.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.CRATE_CHEST.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.IRONBOUND_CHEST.get());
    	tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.VIKING_CHEST.get());
		tag(TreasureTags.Blocks.UNCOMMON_CHESTS).add(TreasureBlocks.BARREL_CHEST.get());
    	tag(TreasureTags.Blocks.SCARCE_CHESTS).add(TreasureBlocks.PIRATE_CHEST.get());
    	tag(TreasureTags.Blocks.SCARCE_CHESTS).add(TreasureBlocks.IRON_STRONGBOX.get());
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.GOLD_STRONGBOX.get());
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.SAFE.get());
    	tag(TreasureTags.Blocks.RARE_CHESTS).add(TreasureBlocks.SPIDER_CHEST.get());
	   	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.DREAD_PIRATE_CHEST.get());
    	tag(TreasureTags.Blocks.EPIC_CHESTS).add(TreasureBlocks.COMPRESSOR_CHEST.get());
		tag(TreasureTags.Blocks.MYTHICAL_CHESTS).add(TreasureBlocks.CELESTIAL_CHEST.get());
		tag(TreasureTags.Blocks.MYTHICAL_CHESTS).add(TreasureBlocks.INFERNAL_CHEST.get());

    	tag(TreasureTags.Blocks.CAULDRON_CHESTS).add(TreasureBlocks.CAULDRON_CHEST.get());
    	tag(TreasureTags.Blocks.SKULL_CHESTS).add(TreasureBlocks.SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.GOLD_SKULL_CHESTS).add(TreasureBlocks.GOLD_SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.CRYSTAL_SKULL_CHESTS).add(TreasureBlocks.CRYSTAL_SKULL_CHEST.get());
    	tag(TreasureTags.Blocks.WITHER_CHESTS).add(TreasureBlocks.WITHER_CHEST.get());
		tag(TreasureTags.Blocks.BONE_CHESTS).add(TreasureBlocks.BONE_CHEST.get());

		// wishing well candidates
		tag(TreasureTags.Blocks.WISHING_WELL_CANDIDATES).add(Blocks.COBBLESTONE);
		tag(TreasureTags.Blocks.WISHING_WELL_CANDIDATES).add(Blocks.MOSSY_COBBLESTONE);
		tag(TreasureTags.Blocks.WISHING_WELL_CANDIDATES).add(Blocks.STONE_BRICKS);
		tag(TreasureTags.Blocks.WISHING_WELL_CANDIDATES).add(Blocks.MOSSY_STONE_BRICKS);

		// gravestone base
		tag(TreasureTags.Blocks.GRAVESTONE_BASE)
				.add(Blocks.GRASS_BLOCK,
						Blocks.DIRT, Blocks.COARSE_DIRT, Blocks.ROOTED_DIRT,
						Blocks.SAND, Blocks.RED_SAND,
						Blocks.PODZOL, Blocks.MYCELIUM,
						Blocks.STONE, Blocks.RED_TERRACOTTA, Blocks.YELLOW_TERRACOTTA);

		tag(BlockTags.FENCES).add(TreasureBlocks.WITHERWOOD_FENCE.get());
		tag(BlockTags.WOODEN_FENCES).add(TreasureBlocks.WITHERWOOD_FENCE.get());
		// witherwood is ordinary overworld wood, so it joins the vanilla wood families: logs burn and make
		// charcoal and campfires, planks make crafting tables and sticks, doors open for villagers, and so on
		tag(TreasureTags.Blocks.WITHERWOOD_LOGS).add(TreasureBlocks.WITHERWOOD_LOG.get(), TreasureBlocks.WITHERWOOD_WOOD.get(), TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(), TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(), TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get());
		tag(BlockTags.LOGS_THAT_BURN).addTag(TreasureTags.Blocks.WITHERWOOD_LOGS);
		tag(BlockTags.PLANKS).add(TreasureBlocks.WITHERWOOD_PLANKS.get());
		tag(BlockTags.WOODEN_SLABS).add(TreasureBlocks.WITHERWOOD_SLAB.get());
		tag(BlockTags.WOODEN_STAIRS).add(TreasureBlocks.WITHERWOOD_STAIRS.get());
		tag(BlockTags.FENCE_GATES).add(TreasureBlocks.WITHERWOOD_FENCE_GATE.get());
		tag(BlockTags.WOODEN_DOORS).add(TreasureBlocks.WITHERWOOD_DOOR.get());
		tag(BlockTags.WOODEN_TRAPDOORS).add(TreasureBlocks.WITHERWOOD_TRAPDOOR.get());
		tag(BlockTags.WOODEN_BUTTONS).add(TreasureBlocks.WITHERWOOD_BUTTON.get());
		tag(BlockTags.WOODEN_PRESSURE_PLATES).add(TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get());
		tag(BlockTags.STANDING_SIGNS).add(TreasureBlocks.WITHERWOOD_SIGN.get());
		tag(BlockTags.WALL_SIGNS).add(TreasureBlocks.WITHERWOOD_WALL_SIGN.get());
		tag(BlockTags.CEILING_HANGING_SIGNS).add(TreasureBlocks.WITHERWOOD_HANGING_SIGN.get());
		tag(BlockTags.WALL_HANGING_SIGNS).add(TreasureBlocks.WITHERWOOD_WALL_HANGING_SIGN.get());
	}

}

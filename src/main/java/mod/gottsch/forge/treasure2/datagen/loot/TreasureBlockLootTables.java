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
package mod.gottsch.forge.treasure2.datagen.loot;

import java.util.stream.IntStream;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import mod.gottsch.forge.treasure2.core.block.BracketFungusBlock;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

import java.util.Arrays;
import java.util.Set;

public class TreasureBlockLootTables extends BlockLootSubProvider {

    public TreasureBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.add(TreasureBlocks.CLOVER.get(), block ->
                createSingleItemTable(TreasureItems.CLOVER.get()));

        // legacy
        this.add(TreasureBlocks.WITHER_LOG.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_LOG.get()));
        this.add(TreasureBlocks.WITHER_BROKEN_LOG.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_BROKEN_LOG.get()));
//        this.add(TreasureBlocks.WITHER_SOUL_LOG.get(), block ->
//                createSingleItemTable(TreasureItems.WITHERWOOD_LOG.get()));
        this.add(TreasureBlocks.WITHER_PLANKS.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_PLANKS.get()));
        this.add(TreasureBlocks.WITHER_BRANCH.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_STICK.get()));
        this.add(TreasureBlocks.WITHER_ROOT.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_ROOT.get()));

        // current
        this.add(TreasureBlocks.WITHERWOOD_LOG.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_LOG.get()));
        this.add(TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(), block ->
                createSingleItemTable(TreasureItems.STRIPPED_WITHERWOOD_LOG.get()));

        this.add(TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_BROKEN_LOG.get()));

        this.add(TreasureBlocks.WITHERWOOD_WOOD.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_WOOD.get()));
        this.add(TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(), block ->
                createSingleItemTable(TreasureItems.STRIPPED_WITHERWOOD_WOOD.get()));

//        this.add(TreasureBlocks.WITHERWOOD_SOUL_LOG.get(), block ->
//                createSingleItemTable(TreasureItems.WITHERWOOD_LOG.get()));

        this.add(TreasureBlocks.WITHERWOOD_BRANCH.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_STICK.get()));
        this.add(TreasureBlocks.WITHERWOOD_ROOT.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_ROOT.get()));

        this.add(TreasureBlocks.WITHERWOOD_PLANKS.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_PLANKS.get()));
        this.add(TreasureBlocks.WITHERWOOD_SLAB.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_SLAB.get()));
        this.add(TreasureBlocks.WITHERWOOD_STAIRS.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_STAIRS.get()));

        this.add(TreasureBlocks.WITHERWOOD_FENCE.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_FENCE.get()));
        this.add(TreasureBlocks.WITHERWOOD_FENCE_GATE.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_FENCE_GATE.get()));
        this.add(TreasureBlocks.WITHERWOOD_BUTTON.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_BUTTON.get()));
        this.add(TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_PRESSURE_PLATE.get()));
        this.add(TreasureBlocks.WITHERWOOD_DOOR.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_DOOR.get()));
        this.add(TreasureBlocks.WITHERWOOD_TRAPDOOR.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_TRAPDOOR.get()));
        this.add(TreasureBlocks.WITHERWOOD_SIGN.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_SIGN.get()));
        this.add(TreasureBlocks.WITHERWOOD_WALL_SIGN.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_SIGN.get()));
        this.add(TreasureBlocks.WITHERWOOD_HANGING_SIGN.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_HANGING_SIGN.get()));
        this.add(TreasureBlocks.WITHERWOOD_WALL_HANGING_SIGN.get(), block ->
                createSingleItemTable(TreasureItems.WITHERWOOD_HANGING_SIGN.get()));

        this.add(TreasureBlocks.STRANGLE_VINES_PLANT.get(), block ->
                createSingleItemTable(TreasureItems.STRANGLE_VINES.get()));

        // one leaf per leaf, like pink petals; withered grass drops dirt unless silk touched, like grass
        this.add(TreasureBlocks.WITHERED_LEAF_LITTER.get(), this::createPetalsDrops);
        this.add(TreasureBlocks.WITHERED_GRASS_BLOCK.get(), block ->
                createSingleItemTableWithSilkTouch(block, Items.DIRT));
        // only shears take the thorns whole, as with dead bushes
        this.dropSelf(TreasureBlocks.HOLLOW_WITHERWOOD_LOG.get());
        // the vine tears off: a plain log, unless silk touched
        this.add(TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get(), block ->
                createSingleItemTableWithSilkTouch(block, TreasureItems.WITHERWOOD_LOG.get()));
        this.add(TreasureBlocks.GRAVE_THORNS.get(), BlockLootSubProvider::createShearsOnlyDrop);
        // one per bracket, like pink petals
        this.add(TreasureBlocks.BRACKET_FUNGUS.get(), block -> LootTable.lootTable().withPool(LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1.0F)).add(applyExplosionDecay(block, LootItem.lootTableItem(block)
                        .apply(IntStream.rangeClosed(1, BracketFungusBlock.MAX_COUNT).boxed().toList(), count ->
                                SetItemCountFunction.setCount(ConstantValue.exactly(count))
                                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(BracketFungusBlock.COUNT, count))))))));

    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        // only return blocks that are generated in this class
        return Arrays.asList(
                TreasureBlocks.CLOVER.get(),
                TreasureBlocks.WITHERWOOD_BROKEN_LOG.get(),
                TreasureBlocks.WITHERWOOD_BRANCH.get(),
                TreasureBlocks.WITHERWOOD_BUTTON.get(),
                TreasureBlocks.WITHERWOOD_FENCE.get(),
                TreasureBlocks.WITHERWOOD_FENCE_GATE.get(),
                TreasureBlocks.WITHERWOOD_DOOR.get(),
                TreasureBlocks.WITHERWOOD_HANGING_SIGN.get(),
                TreasureBlocks.WITHERWOOD_LOG.get(),
                TreasureBlocks.WITHERWOOD_PLANKS.get(),
                TreasureBlocks.WITHERWOOD_ROOT.get(),
                TreasureBlocks.WITHERWOOD_PRESSURE_PLATE.get(),
                TreasureBlocks.WITHERWOOD_SIGN.get(),
                TreasureBlocks.WITHERWOOD_SLAB.get(),
                TreasureBlocks.WITHERWOOD_STAIRS.get(),
//                TreasureBlocks.WITHERWOOD_SOUL_LOG.get(),
                TreasureBlocks.WITHERWOOD_TRAPDOOR.get(),
                TreasureBlocks.WITHERWOOD_WALL_SIGN.get(),
                TreasureBlocks.WITHERWOOD_WALL_HANGING_SIGN.get(),
                TreasureBlocks.WITHERWOOD_WOOD.get(),
                TreasureBlocks.STRIPPED_WITHERWOOD_LOG.get(),
                TreasureBlocks.STRIPPED_WITHERWOOD_WOOD.get(),
                TreasureBlocks.STRANGLE_VINES_PLANT.get(),
                TreasureBlocks.WITHERED_LEAF_LITTER.get(),
                TreasureBlocks.WITHERED_GRASS_BLOCK.get(),
                TreasureBlocks.GRAVE_THORNS.get(),
                TreasureBlocks.BRACKET_FUNGUS.get(),
                TreasureBlocks.HOLLOW_WITHERWOOD_LOG.get(),
                TreasureBlocks.STRANGLED_WITHERWOOD_LOG.get(),

                // legacy wither
                TreasureBlocks.WITHER_LOG.get(),
                TreasureBlocks.WITHER_BROKEN_LOG.get(),
//                TreasureBlocks.WITHER_SOUL_LOG.get(),
                TreasureBlocks.WITHER_PLANKS.get(),
                TreasureBlocks.WITHER_ROOT.get(),
                TreasureBlocks.WITHER_BRANCH.get()
        );
    }
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2024 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.world.feature.gen;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.gottschcore.world.WorldInfo;
import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.DeferredGeneratorBlock;
import mod.gottsch.forge.treasure2.core.block.entity.DeferredGeneratorBlockEntity;
import mod.gottsch.forge.treasure2.core.config.ChestPlacementConfiguration;
import mod.gottsch.forge.treasure2.core.generator.ChestGeneratorData;
import mod.gottsch.forge.treasure2.core.generator.GeneratorResult;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureGenContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;

/**
 *
 * @author Mark Gottschling July 28, 2024
 *
 */
public class DeferredFeatureGenerator implements IFeatureGenerator {

    private final ResourceLocation name;
    private final Block block;

    public DeferredFeatureGenerator(ResourceLocation name, DeferredGeneratorBlock block) {
        this.name = name;
        this.block = block;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public Optional<GeneratorResult<ChestGeneratorData>> generate(IFeatureGenContext context, ICoords spawnCoords, IRarity rarity, ChestPlacementConfiguration.ChestRarity chestRarity) {
        Treasure.LOGGER.debug("surface coords -> {}", spawnCoords.toShortString());
        if (!WorldInfo.isHeightValid(spawnCoords)) {
            Treasure.LOGGER.debug("surface coords are invalid -> {}", spawnCoords.toShortString());
            return Optional.empty();
        }

        context.level().setBlock(spawnCoords.toPos(), block.defaultBlockState(), 3);
        DeferredGeneratorBlockEntity be = (DeferredGeneratorBlockEntity) context.level().getBlockEntity(spawnCoords.toPos());
        if (be == null) {
            return Optional.empty();
        }
        be.setRarity(rarity);

        GeneratorResult<ChestGeneratorData> result = new GeneratorResult<>(ChestGeneratorData.class);
        result.getData().setCoords(spawnCoords);
        result.getData().setRarity(rarity);

        return Optional.of(result);
    }
}

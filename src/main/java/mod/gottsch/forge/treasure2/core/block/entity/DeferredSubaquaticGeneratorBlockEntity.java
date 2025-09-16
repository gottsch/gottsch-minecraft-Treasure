/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * All rights reserved.
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
package mod.gottsch.forge.treasure2.core.block.entity;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.gen.IFeatureGenerator;
import mod.gottsch.forge.treasure2.core.world.feature.gen.TreasureFeatureGenerators;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 *
 * @author Mark Gottschling on July 27, 2024
 *
 */
@Deprecated
public class DeferredSubaquaticGeneratorBlockEntity extends DeferredGeneratorBlockEntity {

    private IRarity rarity;

    public DeferredSubaquaticGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(TreasureBlockEntities.DEFERRED_SUBAQUATIC_GENERATOR_ENTITY_TYPE.get(), pos, state);
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public IFeatureType getFeatureType() {
        return FeatureType.AQUATIC;
    }

    @Override
    public IFeatureGenerator getFeatureGenerator() {
        return TreasureFeatureGenerators.SUBAQUATIC_FEATURE_GENERATOR;
    }

}

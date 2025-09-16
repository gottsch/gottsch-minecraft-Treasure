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
package mod.gottsch.forge.treasure2.core.rarity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

/**
 * A data class to hold the association between an Chest Subprocessor and a Rarity.
 *
 * @param rarityId the ResourceLocation of the Rarity to associate with.
 * @param subprocessor  the ResourceLocation of the Subprocerssor to associate with.
 * @author by Mark Gottschling on 8/27/2025
 *
 */
@Deprecated
public record RarityChestSubprocessorAssociation(ResourceLocation rarityId, ResourceLocation subprocessor) {

    // a Codec to parse this data from a JSON file.
    public static final Codec<RarityChestSubprocessorAssociation> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("rarity").forGetter(association -> association.rarityId),
            ResourceLocation.CODEC.fieldOf("subprocessor").forGetter(association -> association.subprocessor)
    ).apply(instance, RarityChestSubprocessorAssociation::new));

}

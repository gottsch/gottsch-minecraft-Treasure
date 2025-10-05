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
package mod.gottsch.forge.treasure2.core.structure;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author by Mark Gottschling on 8/16/2025
 */
public class TreasureStructures {
    public static final DeferredRegister<net.minecraft.world.level.levelgen.structure.StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_TYPE, Treasure.MODID);

    public static final RegistryObject<StructureType<ModJigsawStructure>> TERRANEAN_STRUCTURE =
            STRUCTURE_TYPES.register("terranean_structure", () -> () -> ModJigsawStructure.CODEC);

    public static final RegistryObject<StructureType<RuinsStructure>> RUINS_STRUCTURE =
            STRUCTURE_TYPES.register("ruins_structure", () -> () -> RuinsStructure.CODEC);

    public static void register(IEventBus modEventBus) {
        STRUCTURE_TYPES.register(modEventBus);
    }
}

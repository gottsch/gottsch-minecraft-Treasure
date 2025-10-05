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
package mod.gottsch.forge.treasure2.core.structure.templatesystem;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author by Mark Gottschling on 8/13/2025
 */
public class ModProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPES =
            DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, Treasure.MODID);

    public static final RegistryObject<StructureProcessorType<SpawnerProcessor>> MOB_SET_PROCESSOR =
            PROCESSOR_TYPES.register("spawner_processor", () -> () -> SpawnerProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<TreasureChestProcessor>> CHEST_PROCESSOR =
            PROCESSOR_TYPES.register("chest_processor", () -> () -> TreasureChestProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<WitherChestProcessor>> WITHER_CHEST_PROCESSOR =
            PROCESSOR_TYPES.register("wither_chest_processor", () -> () -> WitherChestProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<VanillaChestProcessor>> VANILLA_CHEST_PROCESSOR =
            PROCESSOR_TYPES.register("vanilla_chest_processor", () -> () -> VanillaChestProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<AgedProcessor>> AGED_PROCESSOR =
            PROCESSOR_TYPES.register("aged_processor", () -> () -> AgedProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<DecayProcessor>> DECAY_PROCESSOR =
            PROCESSOR_TYPES.register("decay_processor", () -> () -> DecayProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<WellFlowerProcessor>> WELL_FLOWER_PROCESSOR =
            PROCESSOR_TYPES.register("well_flower_processor", () -> () -> WellFlowerProcessor.CODEC);

    public static final RegistryObject<StructureProcessorType<GravestoneProcessor>> GRAVESTONE_PROCESSOR =
            PROCESSOR_TYPES.register("gravestone_processor", () -> () -> GravestoneProcessor.CODEC);


    public static final RegistryObject<StructureProcessorType<Dirt>> DIRT =
            PROCESSOR_TYPES.register("dirt", () -> () -> Dirt.CODEC);

    public static void register(IEventBus eventBus) {
        PROCESSOR_TYPES.register(eventBus);
    }
}

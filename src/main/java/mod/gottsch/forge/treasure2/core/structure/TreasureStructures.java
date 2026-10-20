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
import mod.gottsch.forge.treasure2.core.structure.grove.WitherBoulderPiece;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherFallenLogPiece;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherGroveAreaPiece;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherGroveStructure;
import mod.gottsch.forge.treasure2.core.structure.grove.WitherTreePiece;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
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

    public static final RegistryObject<StructureType<WitherGroveStructure>> WITHER_GROVE =
            STRUCTURE_TYPES.register("wither_grove", () -> () -> WitherGroveStructure.CODEC);

    public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES =
            DeferredRegister.create(Registries.STRUCTURE_PIECE, Treasure.MODID);

    public static final RegistryObject<StructurePieceType> WITHER_TREE_PIECE =
            STRUCTURE_PIECES.register("wither_tree", () -> (StructurePieceType.ContextlessType) WitherTreePiece::new);

    public static final RegistryObject<StructurePieceType> WITHER_GROVE_AREA_PIECE =
            STRUCTURE_PIECES.register("wither_grove_area", () -> (StructurePieceType.ContextlessType) WitherGroveAreaPiece::new);

    public static final RegistryObject<StructurePieceType> WITHER_BOULDER_PIECE =
            STRUCTURE_PIECES.register("wither_boulder", () -> (StructurePieceType.ContextlessType) WitherBoulderPiece::new);

    public static final RegistryObject<StructurePieceType> WITHER_FALLEN_LOG_PIECE =
            STRUCTURE_PIECES.register("wither_fallen_log", () -> (StructurePieceType.ContextlessType) WitherFallenLogPiece::new);

    public static void register(IEventBus modEventBus) {
        STRUCTURE_TYPES.register(modEventBus);
        STRUCTURE_PIECES.register(modEventBus);
    }
}

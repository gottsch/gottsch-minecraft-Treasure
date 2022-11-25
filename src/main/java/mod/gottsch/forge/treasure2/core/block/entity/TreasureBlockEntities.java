/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling on Nov 14, 2022
 *
 */
public class TreasureBlockEntities {
	public static final RegistryObject<BlockEntityType<WoodChestBlockEntity>> WOOD_CHEST_BLOCK_ENTITY_TYPE;

	static {
		WOOD_CHEST_BLOCK_ENTITY_TYPE = Registration.BLOCK_ENTITIES.register("wood_chest_block_entity", () -> BlockEntityType.Builder.of(WoodChestBlockEntity::new, TreasureBlocks.WOOD_CHEST.get()).build(null));
	}
	
	public static void register() {
		// cycle through all block and create items
		Registration.registerBlockEntities();
	}
}

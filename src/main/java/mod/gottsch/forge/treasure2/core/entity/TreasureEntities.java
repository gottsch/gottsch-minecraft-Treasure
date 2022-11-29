/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.entity;


import mod.gottsch.forge.treasure2.core.entity.monster.BoundSoul;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author Mark Gottschling on Jul 22, 2021
 *
 */
public class TreasureEntities {
	public static final String BOUND_SOUL = "bound_soul";
	
	public static final RegistryObject<EntityType<BoundSoul>> BOUND_SOUL_ENTITY_TYPE = Registration.ENTITIES.register(BOUND_SOUL, () -> EntityType.Builder.of(BoundSoul::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F)
			.clientTrackingRange(12)
			.setTrackingRange(80)
			.setShouldReceiveVelocityUpdates(true)
			.build(BOUND_SOUL));
	
	public static void register() {
		// cycle through all block and create items
		Registration.registerEntities();
	}
}

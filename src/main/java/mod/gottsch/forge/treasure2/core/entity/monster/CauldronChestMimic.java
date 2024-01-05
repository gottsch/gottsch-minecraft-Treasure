/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.entity.monster;

import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

/**
 * 
 * @author Mark Gottschling Jun 9, 2023
 *
 */
public class CauldronChestMimic extends Mimic {
	
	/**
	 * 
	 * @param entityType
	 * @param level
	 */
	public CauldronChestMimic(EntityType<? extends MonsterEntity> entityType, World level) {
		super(entityType, level);
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeModifierMap.MutableAttribute registerAttributes() {
		return MonsterEntity.createMonsterAttributes()				
				.add(Attributes.MAX_HEALTH, 40D)
				.add(Attributes.FOLLOW_RANGE, 45D)
				.add(Attributes.MOVEMENT_SPEED, 0.22F)
				.add(Attributes.ATTACK_DAMAGE, 5.5D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 10.0D);
	}	
	
	@Override
	protected SoundEvent getAmbientSound() {
		return TreasureSounds.AMBIENT_MIMIC3.get();
	}
}

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
package mod.gottsch.forge.treasure2.core.entity.monster;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;

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
	public CauldronChestMimic(EntityType<? extends Monster> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * 
	 * @return
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes()				
				.add(Attributes.MAX_HEALTH, 40D)
				.add(Attributes.FOLLOW_RANGE, 45D)
				.add(Attributes.MOVEMENT_SPEED, 0.22F)
				.add(Attributes.ATTACK_DAMAGE, 5.5D)
				.add(Attributes.ARMOR, 10.0D)
				.add(Attributes.ARMOR_TOUGHNESS, 10.0D);
	}	

}

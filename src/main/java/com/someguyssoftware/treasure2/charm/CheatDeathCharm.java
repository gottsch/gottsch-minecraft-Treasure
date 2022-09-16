/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
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
package com.someguyssoftware.treasure2.charm;

import java.util.Random;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * 
 * @author Mark Gottschling on Jan 27, 2022
 *
 */
public class CheatDeathCharm extends Charm {

	public static String TYPE = "cheat_death";

	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	/**
	 * 
	 * @param builder
	 */
	CheatDeathCharm(Builder builder) {
		super(builder);
	}

	/**
	 * Required so sub-classes can call super with a compatible Builder
	 * @param builder
	 */
	protected CheatDeathCharm(Charm.Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;

		if (entity.getMana() > 0 && player.isAlive()) {
			if (((LivingDamageEvent)event).getEntity() instanceof PlayerEntity) {
				// get the source and amount
				double damage = ((LivingDamageEvent)event).getAmount();
				if (damage > 0D && damage > player.getHealth()) {

					// set player's health to amount
					player.setHealth((float) entity.getAmount());

					// cost eval
					double cost = applyCost(world, random, coords, player, event, entity, 1D);

					// reduce damage to 0
					((LivingDamageEvent)event).setAmount(0F);

					result = true;
				}
			}
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.rate.cheat_death", Math.round(entity.getAmount()));
	}

	/*
	 * 
	 */
	public static class Builder extends Charm.Builder {		

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(TYPE, level)), TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new CheatDeathCharm(this);
		}
	}
}

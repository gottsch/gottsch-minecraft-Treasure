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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * The players loses x health every y seconds.
 * @author Mark Gottschling on May 4, 2020
 *
 */
public class DecayCurse extends Charm {
	public static String DECAY_TYPE = "decay";
	private static float DECAY_RATE = 2F;
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DecayCurse(Builder builder) {
		super(builder);
	}

	@Override
	public boolean isCurse() {
		return true;
	}
	
	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;
		//		Treasure.logger.debug("in decay");
//		if (world.getGameTime() % 100 == 0) {
		if (world.getGameTime() % entity.getFrequency() == 0) {
			if (player.isAlive() && entity.getMana() > 0 && player.getHealth() > 0.0) {
				//			Treasure.logger.debug("player is alive and charm is good still...");
				player.setHealth(MathHelper.clamp(player.getHealth() - (float)getAmount(), 0.0F, player.getMaxHealth()));				
//				entity.setMana(MathHelper.clamp(entity.getMana() - 1.0,  0D, entity.getMana()));
				applyCost(world, random, coords, player, event, entity, getAmount());
				//				Treasure.logger.debug("new data -> {}", data);
				result = true;
			}

		}
		return result;
	}
	
	@Override
	public TextFormatting getCharmLabelColor() {
		return TextFormatting.DARK_RED;
	}

	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.rate.decay", DECIMAL_FORMAT.format(getAmount()/2), DECIMAL_FORMAT.format(entity.getFrequency()/TICKS_PER_SECOND));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DECAY_TYPE, level)), DECAY_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecayCurse(this);
		}
	}
}

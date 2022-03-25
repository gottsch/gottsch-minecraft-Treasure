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

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * They player takes an additional x amount of damage when hit.
 * @author Mark Gottschling on May 26, 2020
 *
 */
public class DecrepitCurse extends Charm {
	public static String DECREPIT_TYPE = "decrepit";

	private static final Class<?> REGISTERED_EVENT = LivingDamageEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DecrepitCurse(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean isCurse() {
		return true;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;

		if (!player.isDead && entity.getMana() > 0 && player.getHealth() > 0.0) {
			double amount = ((LivingDamageEvent)event).getAmount();
			((LivingDamageEvent)event).setAmount((float) (amount + (amount * entity.getAmount())));
//			entity.setMana(MathHelper.clamp(entity.getMana() - 1.0,  0D, entity.getMana()));
			applyCost(world, random, coords, player, event, entity, 1.0);
			result = true;
		}
		return result;
	}
	
	@Override
	public TextFormatting getCharmLabelColor() {
		return TextFormatting.DARK_RED;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getCharmDesc(ICharmEntity entity) {
		return I18n.translateToLocalFormatted("tooltip.charm.rate.decrepit", Math.round((entity.getAmount()-1)*100));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ResourceLocationUtil.create(makeName(DECREPIT_TYPE, level)), DECREPIT_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DecrepitCurse(this);
		}
	}
}

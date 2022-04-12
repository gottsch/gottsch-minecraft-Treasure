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

import java.util.List;
import java.util.Random;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.charm.HealingCharm.Costinator;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author Mark Gottschling on Aug 15, 2021
 *
 */
public class GreaterHealingCharm extends Charm implements IHealing {
	public static String HEALING_TYPE = "greater_healing";
	private static float HEAL_RATE = 2F;
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;
	
	GreaterHealingCharm(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	/**
	 * 
	 */
	@Override
	public float getHealRate() {
		return HEAL_RATE;
	}

	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getTotalWorldTime() % entity.getFrequency() == 0) {
			if (entity.getMana() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
				float amount = Math.min(getHealRate(), player.getMaxHealth() - player.getHealth());
				player.setHealth(MathHelper.clamp(player.getHealth() + amount, 0.0F, player.getMaxHealth()));		
//				entity.setMana(MathHelper.clamp(entity.getMana() - amount,  0D, entity.getMana()));
				applyCost(world, random, coords, player, event, entity, amount);
				result = true;
			}
		}
		return result;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getCharmDesc(ICharmEntity entity) {
		return I18n.translateToLocalFormatted("tooltip.charm.rate.greater_healing", (int)(entity.getFrequency()/TICKS_PER_SECOND));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ResourceLocationUtil.create(makeName(HEALING_TYPE, level)), HEALING_TYPE, level);
			this.costEvaluator = new Costinator();
		}

		@Override
		public ICharm build() {
			return  new GreaterHealingCharm(this);
		}
	}
}

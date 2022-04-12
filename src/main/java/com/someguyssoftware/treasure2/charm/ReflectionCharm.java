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
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Fired on LivingHurtEvent, so the original amount of damage INTENDED (ie not actual Damage) to be
 * inflicated on Player is reflected back on mob.
 * reflection: value = # of uses, duration = range, percent = % of damage reflected
 * @author Mark Gottschling on Apr 30, 2020
 *
 */
public class ReflectionCharm extends Charm {
	public static String REFLECTION_TYPE = "reflection";

	private static final Class<?> REGISTERED_EVENT = LivingHurtEvent.class;

	/**
	 * 
	 * @param builder
	 */
	ReflectionCharm(Builder builder) {
		super(builder);
	}

	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (entity.getMana() > 0 && !player.isDead) {
			if (((LivingHurtEvent)event).getEntity() instanceof EntityPlayer) {
				// get player position
				double px = player.posX;
				double py = player.posY;
				double pz = player.posZ;

				// get the source and amount
				double amount = ((LivingHurtEvent)event).getAmount();
				// calculate the new amount
				double reflectedAmount = amount * entity.getAmount();
				double range = entity.getRange();
				List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				mobs.forEach(mob -> {
					boolean flag = mob.attackEntityFrom(DamageSource.GENERIC, (float) reflectedAmount);
					Treasure.LOGGER.debug("reflected damage {} onto mob -> {} was successful -> {}", reflectedAmount, mob.getName(), flag);
				});

				// get all the mob within a radius
//				entity.setMana(entity.getMana() - 1.0);
				applyCost(world, random, coords, player, event, entity, reflectedAmount);
				result = true;
			}    		
		}
		return result;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public String getCharmDesc(ICharmEntity entity) {
		return  I18n.translateToLocalFormatted("tooltip.charm.rate.reflection", Math.toIntExact((long) (entity.getAmount()*100)), entity.getRange());
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ResourceLocationUtil.create(makeName(REFLECTION_TYPE, level)), REFLECTION_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new ReflectionCharm(this);
		}
	}
}

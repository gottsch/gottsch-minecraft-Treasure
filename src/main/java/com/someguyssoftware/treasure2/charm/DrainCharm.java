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
import java.util.concurrent.atomic.AtomicInteger;

import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * 
 * @author Mark Gottschling on Dec 28, 2020
 *
 */
public class DrainCharm extends Charm {
	public static final String DRAIN_TYPE = "drain";
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	/**
	 * 
	 * @param builder
	 */
	DrainCharm(Builder builder) {
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
	public boolean update(World world, Random random, ICoords coords, EntityPlayer player, Event event, final ICharmEntity entity) {
		boolean result = false;
		if (world.getTotalWorldTime() % (TICKS_PER_SECOND * 5) == 0) {
			if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && !player.isDead) {
				// get player position
				double px = player.posX;
				double py = player.posY;
				double pz = player.posZ;

				// calculate the new amount
				int range = entity.getDuration();
				AtomicInteger drainedHealth = new AtomicInteger(0);
				List<EntityMob> mobs = world.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				if (mobs.isEmpty()) {
					return result;
				}
				mobs.forEach(mob -> {
					boolean flag = mob.attackEntityFrom(DamageSource.causePlayerDamage(player), 1.0F);
					Treasure.logger.debug("health drained from mob -> {} was successful -> {}", mob.getName(), flag);
					if (flag) {
						drainedHealth.getAndAdd(1);
					}
				});

				if (drainedHealth.get() > 0) {
					player.setHealth(MathHelper.clamp(player.getHealth() + drainedHealth.get(), 0.0F, player.getMaxHealth()));		
					entity.setValue(MathHelper.clamp(entity.getValue() - 1D,  0D, entity.getValue()));
					result = true;
				}                
			}

		}
		return result;
	}

	/**
	 * 
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void addInformation(ItemStack stack, World world, List<String> tooltip, ITooltipFlag flag, ICharmEntity entity) {
		TextFormatting color = TextFormatting.RED;
		tooltip.add("  " + color + getLabel(entity));
		tooltip.add(" " + TextFormatting.GRAY +  "" + TextFormatting.ITALIC + I18n.translateToLocalFormatted("tooltip.charm.drain_rate", entity.getDuration()));
	}
	
	public static class Builder extends Charm.Builder {

		public Builder(String name, Integer level) {
			super(ResourceLocationUtil.create(name), DRAIN_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new DrainCharm(this);
		}
	}
}

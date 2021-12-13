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

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.util.Mth;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * drains 1 health; value = uses, duration = range
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class DrainCharm extends Charm {
	public static final String DRAIN_TYPE = "drain";
	private static final Class<?> REGISTERED_EVENT = LivingUpdateEvent.class;

	DrainCharm(Builder builder) {
		super(builder);
	}

	@Override
	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}
	
	/**
	 * NOTE: it is assumed that only the allowable events are calling this action.
	 */
	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		boolean result = false;

		if (world.getGameTime() % (TICKS_PER_SECOND * 5) == 0) {
            if (entity.getValue() > 0 && player.getHealth() < player.getMaxHealth() && player.isAlive()) {
                // get player position
                double px = player.position().x;
                double py = player.position().y;
                double pz = player.position().z;
                
                // calculate the new amount
                int range = entity.getDuration();
                AtomicInteger drainedHealth = new AtomicInteger(0);
                List<Mob> mobs = world.getEntitiesOfClass(Mob.class, new AABB(px - range, py - range, pz - range, px + range, py + range, pz + range));
                if (mobs.isEmpty()) {
                    return result;
                }
                mobs.forEach(mob -> {
                	boolean flag = mob.hurt(DamageSource.playerAttack(player), (float) 1.0F);
                    Treasure.LOGGER.debug("health drained from mob -> {} was successful -> {}", mob.getName(), flag);
                    if (flag) {
                        drainedHealth.getAndAdd(1);
                    }
                });
                
                if (drainedHealth.get() > 0) {
                    player.setHealth(Mth.clamp(player.getHealth() + drainedHealth.get(), 0.0F, player.getMaxHealth()));		
                    entity.setValue(Mth.clamp(entity.getValue() - 1D,  0D, entity.getValue()));
                    result = true;
                }
			}
		}
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.RED;       
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.drain", this.getMaxDuration()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));
	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(DRAIN_TYPE, level)), DRAIN_TYPE, level);
		}
		
		@Override
		public ICharm build() {
			return  new DrainCharm(this);
		}
	}
}

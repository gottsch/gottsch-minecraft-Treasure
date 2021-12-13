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

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.Event;

/**
 * Fired on LivingHurtEvent, so the original amount of damage INTENDED (ie not actual Damage) to be
 * inflicated on Player is reflected back on mob.
 * reflection: value = # of uses, duration = range, percent = % of damage reflected
 * @author Mark Gottschling on Aug 23, 2021
 *
 */
public class ReflectionCharm extends Charm {
	public static String REFLECTION_TYPE = "reflection";

	private static final Class<?> REGISTERED_EVENT = LivingHurtEvent.class;

	/**
	 * 
	 * @param builder
	 */
	public ReflectionCharm(Builder builder) {
		super(builder);
	}

	protected ReflectionCharm(Charm.Builder builder) {
		super(builder);
	}

	public Class<?> getRegisteredEvent() {
		return REGISTERED_EVENT;
	}

	@Override
	public boolean update(Level world, Random random, ICoords coords, Player player, Event event, final ICharmEntity entity) {
		Treasure.LOGGER.debug("calling reflectiion");
		boolean result = false;
		if (entity.getValue() > 0 && player.isAlive()) {
			if (((LivingHurtEvent)event).getEntity() instanceof Player) {
				// get player position
				double px = player.position().x;
				double py = player.position().y;
				double pz = player.position().z;

				// get the source and amount
				double amount = ((LivingHurtEvent)event).getAmount();
				// calculate the new amount
				double reflectedAmount = amount * entity.getPercent();
				int range = entity.getDuration();
				// get all the mob within a radius
				List<Mob> mobs = world.getEntitiesOfClass(Mob.class, new AABB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				mobs.forEach(mob -> {
					boolean flag = mob.hurt(DamageSource.playerAttack(player), (float) reflectedAmount);
					Treasure.LOGGER.debug("reflected damage {} onto mob -> {} was successful -> {}", reflectedAmount, mob.getName(), flag);
				});
				entity.setValue(entity.getValue() - 1.0);
				result = true;
			}
		}    		
		return result;
	}

	/**
	 * 
	 */
	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, ICharmEntity entity) {
		ChatFormatting color = ChatFormatting.BLUE;
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent(getLabel(entity)).withStyle(color)));
		tooltip.add(new TranslatableComponent("tooltip.indent2", new TranslatableComponent("tooltip.charm.rate.reflection", Math.round(this.getMaxPercent()*100), this.getMaxDuration()).withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC)));

	}

	public static class Builder extends Charm.Builder {

		public Builder(Integer level) {
			super(ModUtils.asLocation(makeName(REFLECTION_TYPE, level)), REFLECTION_TYPE, level);
		}

		@Override
		public ICharm build() {
			return  new ReflectionCharm(this);
		}
	}
}

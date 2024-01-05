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
package mod.gottsch.forge.treasure2.core.charm;

import java.util.List;
import java.util.Random;

import com.google.common.util.concurrent.AtomicDouble;
import com.someguyssoftware.gottschcore.spatial.ICoords;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.eventbus.api.Event;

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
	public boolean update(World world, Random random, ICoords coords, PlayerEntity player, Event event, final ICharmEntity entity) {
		boolean result = false;

		if (world.getGameTime() % entity.getFrequency() == 0) {
			if (entity.getMana() > 0 && player.getHealth() < player.getMaxHealth() && player.isAlive()) {
				// get player position
				double px = player.getX();
				double py = player.getY();
				double pz = player.getZ();

				// calculate the new amount
				double range = entity.getRange();
				AtomicDouble drainedHealth = new AtomicDouble(0);
				List<MonsterEntity> mobs = world.getEntitiesOfClass(MonsterEntity.class, new AxisAlignedBB(px - range, py - range, pz - range, px + range, py + range, pz + range));
				if (mobs.isEmpty()) {
					return result;
				}
				mobs.forEach(mob -> {
//					boolean flag = mob.attackEntityFrom(DamageSource.GENERIC, (float)getAmount());
					boolean flag = mob.hurt(DamageSource.GENERIC, (float)getAmount());
					Treasure.LOGGER.debug("health drained from mob -> {} was successful -> {}", mob.getName(), flag);
					if (flag) {
						drainedHealth.addAndGet(getAmount());
					}
				});

				if (drainedHealth.get() > 0.0) {
					player.setHealth(MathHelper.clamp(player.getHealth() + (float)drainedHealth.get(), 0.0F, player.getMaxHealth()));		
					//					entity.setMana(MathHelper.clamp(entity.getMana() - 1D,  0D, entity.getMana()));
					applyCost(world, random, coords, player, event, entity, 1.0);
					result = true;
				}                
			}

		}
		return result;
	}
	
	@Override
	public ITextComponent getCharmDesc(ICharmEntity entity) {
		return new TranslationTextComponent("tooltip.charm.rate.drain", entity.getRange());
	}
	
	/*
	 * 
	 */
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

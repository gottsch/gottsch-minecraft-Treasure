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
package com.someguyssoftware.treasure2.charm.cost;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

import com.someguyssoftware.gottschcore.item.ModItem;
import com.someguyssoftware.gottschcore.positional.ICoords;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.item.Adornment;
import com.someguyssoftware.treasure2.item.CharmItem;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This class implements the Decorator Pattern.
 * @author Mark Gottschling on Jan 21, 2022
 *
 */
public class EquipmentCostEvaluator extends CostEvaluator {

	private ICostEvaluator evaluator;

	/**
	 * 
	 */
	public EquipmentCostEvaluator() {
		evaluator = new CostEvaluator();
	}

	/**
	 * 
	 * @param evaluator
	 */
	public EquipmentCostEvaluator(ICostEvaluator evaluator) {
		Treasure.LOGGER.debug("receiving child evaluator of -> {}", evaluator.getClass().getSimpleName());
		if (isSame(evaluator)) {
			this.evaluator = new CostEvaluator();
		} else {
			this.evaluator = evaluator;
		}
	}

	public boolean isSame(ICostEvaluator evaluator) {
		return (evaluator instanceof EquipmentCostEvaluator);
	}

	@Override
	public double apply(World world, Random random, ICoords coords, EntityPlayer player, Event event,
			ICharmEntity entity, double amount) {
		Treasure.LOGGER.debug("executing...");
		//		double newAmount = amount * 2;
		//		boolean isDamaged = true;

		// search for equipment
		List<ItemStack> equipmentList = new ArrayList<>();
		player.getEquipmentAndArmor().iterator().forEachRemaining(itemStack -> {
			if (itemStack.isItemStackDamageable() && !(itemStack.getItem() instanceof Adornment) && !(itemStack.getItem() instanceof CharmItem)) {
				equipmentList.add(itemStack);
			}
		});
		if (!equipmentList.isEmpty()) {
			double newAmount = amount * 2;
			// randomly select a piece of equipment
			ItemStack selectedStack = equipmentList.get(random.nextInt(equipmentList.size()));
			Treasure.LOGGER.debug("selected equip -> {}", selectedStack.getDisplayName());
			Treasure.LOGGER.debug("going to apply damage -> {} to equip current damage -> {}", newAmount, selectedStack.getItemDamage());
			// damage the item
			// only return true if the item is damaged/destroyed... need to test is newDamage > oldDamage
			int oldDamage = selectedStack.getItemDamage();
			selectedStack.attemptDamageItem(Math.toIntExact((long) newAmount), random, null);
			if (selectedStack.getItemDamage() > oldDamage) {
				Treasure.LOGGER.debug("equip damaged after -> {}", selectedStack.getItemDamage());
				return 0;
			}
		}

		// if not damaged, process against default evaluator
		return entity.getCharm().getCostEvaluator().apply(world, random, coords, player, event, entity, amount);
		//		if (!isDamaged) {
		//			Treasure.LOGGER.debug("no damage done, use mana using cost eval ->{}", evaluator.getClass().getSimpleName());
		//			// execute the orignal evaluator
		//			newAmount = entity.getCharm().getCostEvaluator().apply(world, random, coords, player, event, entity, amount);
		//		}		
		//		return newAmount;
	}

	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		try {
			super.save(nbt); // save my className to nbt

			NBTTagCompound tag = new NBTTagCompound();
			// protect from recursive save
			if (!isSame(evaluator)) {
				evaluator.save(tag);
				nbt.setTag("evaluator", tag);
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error saving EquipmentCostEvaluator -> ", e);
		}
		return nbt;
	}

	@Override
	public void load(NBTTagCompound nbt) {
		try {
			super.load(nbt);
			//		Treasure.logger.debug("loading equipment cost eval...");
			// (do what charm does to load evaluator)
			if (nbt.hasKey("evaluator") && nbt.getCompoundTag("evaluator").hasKey("costClass")) {
				try {
					NBTTagCompound tag = nbt.getCompoundTag("evaluator");
					String costEvalClass = nbt.getString("costClass");
					if (!this.getClass().getCanonicalName().equals(costEvalClass)) {
						//					Treasure.logger.debug("child cost class -> {}", costEvalClass);
						Object o = Class.forName(costEvalClass).newInstance();
						((ICostEvaluator)o).load(tag);
						this.evaluator = (ICostEvaluator)o;
					} else {
						this.evaluator = new CostEvaluator();
					}
				}
				catch(Exception e) {
					Treasure.LOGGER.warn("unable to create cost evaluator from class string:");
					Treasure.LOGGER.error(e);
					this.evaluator = new CostEvaluator();
				}
			}
			else {
				this.evaluator = new CostEvaluator();
			}
		}
		catch(Exception e) {
			Treasure.LOGGER.error("error loading EquipmentCostEvaluator -> ", e);
		}
	}

	public ICostEvaluator getEvaluator() {
		return evaluator;
	}
}

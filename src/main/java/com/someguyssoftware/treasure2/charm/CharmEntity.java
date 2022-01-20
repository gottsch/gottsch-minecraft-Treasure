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

import com.someguyssoftware.treasure2.charm.cost.ICostEvaluator;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Mark Gottschling on Oct 25, 2021
 *
 */
public class CharmEntity implements ICharmEntity {	
	private ICharm charm;
	private double mana; // changes - needs client update
	private int duration; // static (but is a property that could chane/require update in future
	private double frequency;
	private double range;
	private double cooldown;
	private double amount;

	// NOTE this is the cost eval that can be modified by a runestone
	// calculates the actual cost given an input
	private ICostEvaluator costEvaluator;
	
	// TESTing for Runestones
	private double maxMana;
	
	/**
	 * 
	 */
	public CharmEntity() { }

	public CharmEntity(ICharm charm) {
		setCharm(charm);
		setAmount(charm.getAmount());
		setCooldown(charm.getCooldown());
		setDuration(charm.getDuration());
		setFrequency(charm.getFrequency());
		setMana(charm.getMana());
		setRange(charm.getRange());
		setCostEvaluator(charm.getCostEvaluator());
		
		setMaxMana(charm.getMana());
	}
	
	/**
	 * Copy constructor
	 * @param entity
	 */
	public CharmEntity(ICharmEntity entity) {
		setCharm(entity.getCharm());
		setAmount(entity.getAmount());
		setCooldown(entity.getCooldown());
		setDuration(entity.getDuration());
		setFrequency(entity.getFrequency());
		setMana(entity.getMana());
		setRange(entity.getRange());
		// TODO create a new costevaluator instance
		setCostEvaluator(entity.getCostEvaluator());
		
		setMaxMana(entity.getMaxMana());
	}
	
	// TODO add a builder
	
	/**
	 * Client-side. Only update those properties that need to be reflected on the client-side.
	 */
	@Override
	public void update(ICharmEntity entity) {
		this.setMana(entity.getMana());
	}
	
	// TODO probably add a update(Consumer<>) just in case someone wants to update something
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		NBTTagCompound charmNbt = new NBTTagCompound();
		// save the charm
		nbt.setTag(CHARM, getCharm().save(charmNbt));
		// save the entity data
		nbt.setDouble(MANA, getMana());
		nbt.setInteger(DURATION, getDuration());
		nbt.setDouble(FREQUENCY, getFrequency());
		nbt.setDouble(AMOUNT, getAmount());
		nbt.setDouble(COOLDOWN, getCooldown());
		nbt.setDouble(RANGE, getRange());
		if (getCostEvaluator() != null) {
			nbt.setString(COST_EVALUATOR, getCostEvaluator().getClass().getCanonicalName());
		}
		nbt.setDouble("maxMana", getMaxMana());
		return nbt;
	}
	
	@Override
	public ICharm getCharm() {
		return charm;
	}

	@Override
	public void setCharm(ICharm charm) {
		this.charm = charm;
	}

	@Override
	public double getMana() {
		return mana;
	}

	@Override
	public void setMana(double value) {
		this.mana = value;
	}

	@Override
	public int getDuration() {
		return duration;
	}

	@Override
	public void setDuration(int duration) {
		this.duration = duration;
	}

	@Override
	public double getFrequency() {
		return frequency;
	}

	@Override
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	@Override
	public String toString() {
		return "CharmEntity [charm=" + charm + ", mana=" + mana + ", duration=" + duration + ", frequency=" + frequency
				+ ", range=" + range + ", cooldown=" + cooldown + ", amount=" + amount + ", costEvaluator="
				+ costEvaluator + ", maxMana=" + maxMana + "]";
	}

	@Override
	public double getRange() {
		return range;
	}

	@Override
	public void setRange(double range) {
		this.range = range;
	}

	@Override
	public double getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(double cooldown) {
		this.cooldown = cooldown;
	}

	@Override
	public double getAmount() {
		return amount;
	}

	@Override
	public void setAmount(double amount) {
		this.amount = amount;
	}

	@Override
	public ICostEvaluator getCostEvaluator() {
		return costEvaluator;
	}

	@Override
	public void setCostEvaluator(ICostEvaluator costEvaluator) {
		this.costEvaluator = costEvaluator;
	}

	@Override
	public double getMaxMana() {
		return maxMana;
	}

	@Override
	public void setMaxMana(double maxMana) {
		this.maxMana = maxMana;
	}
}

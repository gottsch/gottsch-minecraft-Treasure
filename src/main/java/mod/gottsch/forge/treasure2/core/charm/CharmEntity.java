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

import mod.gottsch.forge.treasure2.core.charm.cost.ICostEvaluator;
import net.minecraft.nbt.CompoundNBT;


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
	private boolean exclusive;
	
	// NOTE this is the cost eval that can be modified by a runestone
	// calculates the actual cost given an input
	private ICostEvaluator costEvaluator;
	
	// TESTing for Runestones
	private double maxMana;
	
	private int recharges;
	private int maxRecharges;
	
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
		setExclusive(charm.isExclusive());
		setRecharges(charm.getRecharges());
		setMaxRecharges(charm.getRecharges());
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
		setCostEvaluator(entity.getCostEvaluator());
		
		setMaxMana(entity.getMaxMana());
		setExclusive(entity.isExclusive());
		setRecharges(entity.getRecharges());
		setMaxRecharges(entity.getRecharges());
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
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT charmNbt = new CompoundNBT();
		// save the charm
		nbt.put(CHARM, getCharm().save(charmNbt));
		// save the entity data
		nbt.putDouble(MANA, getMana());
		nbt.putInt(DURATION, getDuration());
		nbt.putDouble(FREQUENCY, getFrequency());
		nbt.putDouble(AMOUNT, getAmount());
		nbt.putDouble(COOLDOWN, getCooldown());
		nbt.putDouble(RANGE, getRange());
		if (getCostEvaluator() != null) {
			CompoundNBT ceNbt = new CompoundNBT();
			getCostEvaluator().save(ceNbt);
			nbt.put(COST_EVALUATOR, ceNbt);
		}
		nbt.putDouble("maxMana", getMaxMana());
		
		nbt.putBoolean(EXCLUSIVE, isExclusive());
		nbt.putInt(RECHARGES, getRecharges());
		nbt.putInt(MAX_RECHARGES, getMaxRecharges());
		
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

	@Override
	public boolean isExclusive() {
		return exclusive;
	}

	@Override
	public void setExclusive(boolean exclusive) {
		this.exclusive = exclusive;
	}

	@Override
	public int getRecharges() {
		return recharges;
	}

	@Override
	public void setRecharges(int recharges) {
		this.recharges = recharges;
	}

	@Override
	public int getMaxRecharges() {
		return maxRecharges;
	}

	@Override
	public void setMaxRecharges(int maxRecharges) {
		this.maxRecharges = maxRecharges;
	}
}

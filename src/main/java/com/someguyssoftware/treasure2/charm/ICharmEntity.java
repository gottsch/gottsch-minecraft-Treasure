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

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.cost.CostEvaluator;
import com.someguyssoftware.treasure2.charm.cost.ICostEvaluator;

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Mark Gottschling on Oct 25, 2021
 *
 */
public interface ICharmEntity {
	public static final String CHARM = "charm";
	@Deprecated
	public static final String VALUE = "value";
	public static final String MANA = "mana";
	public static final String MAX_MANA = "maxMana";
	public static final String DURATION = "duration";
	public static final String FREQUENCY = "frequency";
	public static final String AMOUNT = "amount";
	public static final String COOLDOWN = "cooldown";
	public static final String RANGE = "range";
	public static final String COST_EVALUATOR = "costEvaluator";
	
	public static final String EXCLUSIVE = "exclusive";
	public static final String RECHARGES = "recharges";
	public static final String MAX_RECHARGES = "maxRecharges";
	
	ICharm getCharm();

	void setCharm(ICharm charm);

	double getMana();
	void setMana(double value);

	int getDuration();
	void setDuration(int duration);

	double getFrequency();	
	void setFrequency(double frequency);
	
	double getRange();
	void setRange(double range);

	double getCooldown();
	void setCooldown(double cooldown);
	
	double getAmount();
	void setAmount(double amount);

	ICostEvaluator getCostEvaluator();
	void setCostEvaluator(ICostEvaluator costEvaluator);
	
	// TESTING
	double getMaxMana();
	void setMaxMana(double maxMana);
	
	void update(ICharmEntity entity);

	/**
	 * 
	 * @param nbt
	 * @return
	 */
	NBTTagCompound save(NBTTagCompound nbt);

	default public boolean load(NBTTagCompound nbt) {
		if (nbt.hasKey(MANA)) {
			setMana(nbt.getDouble(MANA));
		}
		// legacy
		else if (nbt.hasKey(VALUE)) {
			setMana(nbt.getDouble(VALUE));
		}
		
		if (nbt.hasKey(DURATION)) {
			setDuration(nbt.getInteger(DURATION));
		}
		if (nbt.hasKey(FREQUENCY)) {
			setFrequency(nbt.getDouble(FREQUENCY));
		}
		if (nbt.hasKey(AMOUNT)) {
			setAmount(nbt.getDouble(AMOUNT));
		}
		if (nbt.hasKey(COOLDOWN)) {
			setCooldown(nbt.getDouble(COOLDOWN));
		}
		if (nbt.hasKey(RANGE)) {
			setRange(nbt.getDouble(RANGE));
		}
		if (nbt.hasKey(COST_EVALUATOR) && nbt.getCompoundTag(COST_EVALUATOR).hasKey("costClass")) {
			try {
				NBTTagCompound tag = nbt.getCompoundTag(COST_EVALUATOR);
//				if (tag.hasKey("class")) {
					String costEvalClass = tag.getString("costClass");
//					Treasure.logger.warn("using parent cost eval class -> {}", costEvalClass);
					Object o = Class.forName(costEvalClass).newInstance();
					((ICostEvaluator)o).load(tag);
					setCostEvaluator((ICostEvaluator)o);
//				}
				
//				String costEvalClass = nbt.getString(COST_EVALUATOR);
//				Object o = Class.forName(costEvalClass).newInstance();
//				((ICostEvaluator)o).load(nbt);
//				setCostEvaluator((ICostEvaluator)o);
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("unable to create cost evaluator from class string -> {}", nbt.getCompoundTag(COST_EVALUATOR).getString("costClass"));
				Treasure.LOGGER.error(e);
				setCostEvaluator(new CostEvaluator());
			}
		}
		else {
			setCostEvaluator(new CostEvaluator());
		}
		
		if (nbt.hasKey("maxMana")) {
			setMaxMana(nbt.getDouble("maxMana"));
		}
		
		if (nbt.hasKey(EXCLUSIVE)) {
			setExclusive(nbt.getBoolean(EXCLUSIVE));
		}
		
		if (nbt.hasKey(RECHARGES)) {
			setRecharges(nbt.getInteger(RECHARGES));
		}
		if (nbt.hasKey(MAX_RECHARGES)) {
			setMaxRecharges(nbt.getInteger(MAX_RECHARGES));
		}
		return true;
	}

	boolean isExclusive();

	void setExclusive(boolean exclusive);

	int getRecharges();

	void setRecharges(int recharges);

	int getMaxRecharges();

	void setMaxRecharges(int maxRecharges);
}

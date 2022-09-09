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

import net.minecraft.nbt.CompoundNBT;

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
	CompoundNBT save(CompoundNBT nbt);

	default public boolean load(CompoundNBT nbt) {
		if (nbt.contains(MANA)) {
			setMana(nbt.getDouble(MANA));
		}
		// legacy
		else if (nbt.contains(VALUE)) {
			setMana(nbt.getDouble(VALUE));
		}
		
		if (nbt.contains(DURATION)) {
			setDuration(nbt.getInt(DURATION));
		}
		if (nbt.contains(FREQUENCY)) {
			setFrequency(nbt.getDouble(FREQUENCY));
		}
		if (nbt.contains(AMOUNT)) {
			setAmount(nbt.getDouble(AMOUNT));
		}
		if (nbt.contains(COOLDOWN)) {
			setCooldown(nbt.getDouble(COOLDOWN));
		}
		if (nbt.contains(RANGE)) {
			setRange(nbt.getDouble(RANGE));
		}
		if (nbt.contains(COST_EVALUATOR) && nbt.getCompound(COST_EVALUATOR).contains("costClass")) {
			try {
				CompoundNBT tag = nbt.getCompound(COST_EVALUATOR);

					String costEvalClass = tag.getString("costClass");
//					Treasure.logger.warn("using parent cost eval class -> {}", costEvalClass);
					Object o = Class.forName(costEvalClass).newInstance();
					((ICostEvaluator)o).load(tag);
					setCostEvaluator((ICostEvaluator)o);
			}
			catch(Exception e) {
				Treasure.LOGGER.warn("unable to create cost evaluator from class string -> {}", nbt.getCompound(COST_EVALUATOR).getString("costClass"));
				Treasure.LOGGER.error(e);
				setCostEvaluator(new CostEvaluator());
			}
		}
		else {
			setCostEvaluator(new CostEvaluator());
		}
		
		if (nbt.contains("maxMana")) {
			setMaxMana(nbt.getDouble("maxMana"));
		}
		
		if (nbt.contains(EXCLUSIVE)) {
			setExclusive(nbt.getBoolean(EXCLUSIVE));
		}
		
		if (nbt.contains(RECHARGES)) {
			setRecharges(nbt.getInt(RECHARGES));
		}
		if (nbt.contains(MAX_RECHARGES)) {
			setMaxRecharges(nbt.getInt(MAX_RECHARGES));
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

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
	public static final String DURATION = "duration";
	public static final String FREQUENCY = "frequency";
	public static final String AMOUNT = "amount";
	public static final String COOLDOWN = "cooldown";
	public static final String RANGE = "range";
	
	ICharm getCharm();

	void setCharm(ICharm charm);

	double getMana();
	void setMana(double value);

	int getDuration();
	void setDuration(int duration);

	double getFrequency();	
	void setFrequency(double frequency);
	
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
		return true;
	}

	double getRange();
	void setRange(double range);

	double getCooldown();
	void setCooldown(double cooldown);
	
	double getAmount();
	void setAmount(double amount);
}

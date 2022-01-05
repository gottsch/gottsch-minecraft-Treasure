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
	public static String CHARM = "charm";
	public static String VALUE = "value";
	
	ICharm getCharm();

	void setCharm(ICharm charm);

	double getValue();

	void setValue(double value);

	int getDuration();

	void setDuration(int duration);

	double getPercent();

	void setPercent(double percent);

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
		if (nbt.hasKey(VALUE)) {
			setValue(nbt.getDouble(VALUE));
		}
		if (nbt.hasKey("duration")) {
			setDuration(nbt.getInteger("duration"));
		}
		if (nbt.hasKey("percent")) {
			setPercent(nbt.getDouble("percent"));
		}
		if (nbt.hasKey("frequency")) {
			setFrequency(nbt.getDouble("frequency"));
		}		
		return true;
	}
}

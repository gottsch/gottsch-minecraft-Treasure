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
public class CharmEntity implements ICharmEntity {
	private ICharm charm;
	private double value;
	private int duration;
	private double percent;
	private double frequency;
	
	/**
	 * 
	 */
	public CharmEntity() { }

	/**
	 * 
	 * @param charm
	 * @param value
	 * @param duration
	 * @param percent
	 */
	public CharmEntity(ICharm charm, double value, int duration, double percent, double frequency) {
		setCharm(charm);
		setValue(value);
		setDuration(duration);
		setPercent(percent);
		setFrequency(frequency);
	}
	
	@Override
	public void update(ICharmEntity entity) {
		this.setValue(entity.getValue());
		this.setDuration(entity.getDuration());
		this.setPercent(entity.getPercent());
		this.setFrequency(entity.getFrequency());
	}
	
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
		nbt.setDouble(VALUE, getValue());
		nbt.setInteger("duration", getDuration());
		nbt.setDouble("percent", getPercent());
		nbt.setDouble("frequency", getFrequency());
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
	public double getValue() {
		return value;
	}

	@Override
	public void setValue(double value) {
		this.value = value;
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
	public double getPercent() {
		return percent;
	}

	@Override
	public void setPercent(double percent) {
		this.percent = percent;
	}

	@Override
	public double getFrequency() {
		return frequency;
	}

	@Override
	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}
}

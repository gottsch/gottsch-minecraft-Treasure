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

public class CharmEntity implements ICharmEntity {
	private ICharm charm;
	private double value;
	private int duration;
	private double percent;

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
	public CharmEntity(ICharm charm, double value, int duration, double percent) {
		setCharm(charm);
		setValue(value);
		setDuration(duration);
		setPercent(percent);
	}
	
	@Override
	public ICharm getCharm() {
		return charm;
	}

	@Override
	public void setCharm(ICharm charm) {
		this.charm = charm;
	}

	@Deprecated
	@Override
	public ICharmData getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	@Override
	public void setData(ICharmData data) {
		// TODO Auto-generated method stub
		
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	@Override
	public String toString() {
		return "CharmEntity [charm=" + charm + ", value=" + value + ", duration=" + duration + ", percent=" + percent
				+ "]";
	}

}

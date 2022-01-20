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

import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author Mark Gottschling on Jan 13, 2022
 *
 */
public class LifeStrikeCharmEntity extends CharmEntity {
	private double lifeCost;

	public LifeStrikeCharmEntity() {
		super();		
	}
	
	public LifeStrikeCharmEntity(LifeStrikeCharm charm) {
		super(charm);
		setLifeCost(charm.getLifeCost());
	}
	
	public LifeStrikeCharmEntity(LifeStrikeCharmEntity entity) {
		super(entity);
		setLifeCost(entity.getLifeCost());
	}
	
	@Override
	public boolean load(NBTTagCompound nbt) {
		super.load(nbt);		
		if (nbt.hasKey("lifeCost")) {
			setLifeCost(nbt.getDouble("lifeCost"));
		}	
		return true;
	}
	
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		nbt = super.save(nbt);
		try {
			nbt.setDouble("lifeCost", getLifeCost());
		}
		catch(Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	public double getLifeCost() {
		return lifeCost;
	}

	public void setLifeCost(double lifeCost) {
		this.lifeCost = lifeCost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lifeCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LifeStrikeCharmEntity other = (LifeStrikeCharmEntity) obj;
		if (Double.doubleToLongBits(lifeCost) != Double.doubleToLongBits(other.lifeCost))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LifeStrikeCharmEntity [lifeCost=" + lifeCost + "]";
	}
	
}

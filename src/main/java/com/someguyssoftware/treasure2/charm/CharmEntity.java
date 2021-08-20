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

import net.minecraft.nbt.CompoundNBT;

/**
 * 
 * @author Mark Gottschling on Aug 19, 2021
 *
 */
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
	public void update(ICharmEntity entity) {
		this.setValue(entity.getValue());
		this.setDuration(entity.getDuration());
		this.setPercent(entity.getPercent());
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
//	public static Optional<ICharmEntity> load(CompoundNBT nbt) {
//		Optional<ICharm> charm = Charm.load((CompoundNBT) nbt.get(COPPER_CHARM));
//		if (!charm.isPresent()) {
//			return Optional.empty();
//		}
//		
//		ICharmEntity entity = charm.get().createEntity();
//		if (nbt.contains(VALUE)) {
//			entity.setValue(nbt.getDouble(VALUE));
//		}
//		if (nbt.contains("duration")) {
//			entity.setDuration(nbt.getInt("duration"));
//		}
//		if (nbt.contains("percent")) {
//			entity.setPercent(nbt.getDouble("percent"));
//		}
//		return Optional.of(entity);
//	}
	
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
		nbt.putDouble(VALUE, getValue());
		nbt.putInt("duration", getDuration());
		nbt.putDouble("percent", getPercent());
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
	public String toString() {
		return "CharmEntity [charm=" + charm + ", value=" + value + ", duration=" + duration + ", percent=" + percent
				+ "]";
	}
}

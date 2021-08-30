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

import java.util.Optional;

import net.minecraft.nbt.CompoundNBT;

/**
 * 
 * @author Mark Gottschling on Jan 16, 2021
 *
 */
public interface ICharmEntity {
	static String CHARM = "charm";
	static String VALUE = "value";
		
	ICharm getCharm();
	void setCharm(ICharm charm);

	double getValue();
	void setValue(double value);

	int getDuration();
	void setDuration(int duration);

	double getPercent();
	void setPercent(double percent);
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	CompoundNBT save(CompoundNBT nbt);
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
//	public static Optional<ICharmEntity> load(CompoundNBT nbt) {
//		Optional<ICharm> charm = Charm.load((CompoundNBT) nbt.get(CHARM));
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
	
	default public boolean load(CompoundNBT nbt) {
		if (nbt.contains(VALUE)) {
			setValue(nbt.getDouble(VALUE));
		}
		if (nbt.contains("duration")) {
			setDuration(nbt.getInt("duration"));
		}
		if (nbt.contains("percent")) {
			setPercent(nbt.getDouble("percent"));
		}
		return true;
	}
	
	void update(ICharmEntity entity);
}

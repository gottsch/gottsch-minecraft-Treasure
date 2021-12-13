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

import net.minecraft.nbt.CompoundTag;

/**
 * @author Mark Gottschling on Apr 27, 2020
 *
 */
@Deprecated
public interface ICharmData {
	public double getValue();
	public void setValue(double value);
	
	public int getDuration();
	public void setDuration(int duration);
	
	public double getPercent();
	public void setPercent(double percent);
	
	public CompoundTag writeToNBT(CompoundTag nbt);
	public void readFromNBT(CompoundTag tag);
}
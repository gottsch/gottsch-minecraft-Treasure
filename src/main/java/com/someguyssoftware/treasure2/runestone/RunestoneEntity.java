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
package com.someguyssoftware.treasure2.runestone;

import org.apache.commons.lang3.ObjectUtils;

import net.minecraft.nbt.NBTTagCompound;

/**
 * TODO this is probably an unneeded class, as nothing is changing.
 * Only the CharmEntity is changed.... unless there is some state information
 * that the rune needs to save to perform an Undo()
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public class RunestoneEntity implements IRunestoneEntity {
	private IRunestone runestone;

	public RunestoneEntity() {}
	
	/**
	 * Copy constructor
	 * @param value
	 */
	public RunestoneEntity(IRunestoneEntity entity) {
		this.setRunestone(entity.getRunestone());
		// TODO complete
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	@Override
	public NBTTagCompound save(NBTTagCompound nbt) {
		NBTTagCompound runestoneNbt = new NBTTagCompound();
		// save the charm
		nbt.setTag("runestone", getRunestone().save(runestoneNbt));
		// save the entity data

		return nbt;
	}
	
	@Override
	public IRunestone getRunestone() {
		return runestone;
	}
	
	@Override
	public void setRunestone(IRunestone runestone) {
		this.runestone = runestone;
	}

	@Override
	public String toString() {
		return "RunestoneEntity [runestone=" + runestone + "]";
	}
	
}

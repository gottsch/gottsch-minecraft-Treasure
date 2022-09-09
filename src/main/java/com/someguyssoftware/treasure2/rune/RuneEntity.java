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
package com.someguyssoftware.treasure2.rune;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;

/**
 * TODO this is probably an unneeded class, as nothing is changing.
 * Only the CharmEntity is changed.... unless there is some state information
 * that the rune needs to save to perform an Undo()
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public class RuneEntity implements IRuneEntity {
	private IRune runestone;
	private boolean applied;
	
	private List<String> appliedTo;
	
	/**
	 * 
	 */
	public RuneEntity() {}
	
	/**
	 * Copy constructor
	 * @param value
	 */
	public RuneEntity(IRuneEntity entity) {
		this.setRunestone(entity.getRunestone());
		this.setApplied(entity.isApplied());
		entity.getAppliedTo().forEach(type -> {
			this.getAppliedTo().add(type);
		});
		// TODO complete
	}
	
	public boolean isAppliedTo(String type) {
		return getAppliedTo().contains(type);
	}
	
	/**
	 * 
	 * @param nbt
	 * @return
	 */
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		CompoundNBT runestoneNbt = new CompoundNBT();
		// save the charm
		nbt.put("runestone", getRunestone().save(runestoneNbt));
		// save the entity data
		nbt.putBoolean(APPLIED, isApplied());
		ListNBT list = new ListNBT();
		getAppliedTo().forEach(type -> {
			list.add(StringNBT.valueOf(type));
		});
		nbt.put("appliedTo", list);
		
		return nbt;
	}
	
	@Override
	public IRune getRunestone() {
		return runestone;
	}
	
	@Override
	public void setRunestone(IRune runestone) {
		this.runestone = runestone;
	}

	@Override
	public String toString() {
		return "RunestoneEntity [runestone=" + runestone + ", applied=" + applied + ", appliedTo=" + appliedTo + "]";
	}

	@Override
	public boolean isApplied() {
		return applied;
	}

	@Override
	public void setApplied(boolean applied) {
		this.applied = applied;
	}

	@Override
	public List<String> getAppliedTo() {
		if (appliedTo == null) {
			appliedTo = new ArrayList<>();
		}
		return appliedTo;
	}

	@Override
	public void setAppliedTo(List<String> appliedTo) {
		this.appliedTo = appliedTo;
	}
	
}

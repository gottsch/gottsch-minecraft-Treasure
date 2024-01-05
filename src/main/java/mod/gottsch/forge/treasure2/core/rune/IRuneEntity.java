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
package mod.gottsch.forge.treasure2.core.rune;

import java.util.List;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

/**
 * 
 * @author Mark Gottschling on Jan 14, 2022
 *
 */
public interface IRuneEntity {

	public static final String RUNESTONE = "runestone";
	public static final String APPLIED = "applied";

	IRune getRunestone();

	void setRunestone(IRune runestone);

	/**
	 * 
	 * @param nbt
	 * @return
	 */
	CompoundNBT save(CompoundNBT nbt);
	
	default public boolean load(CompoundNBT nbt) {
		if (nbt.contains(APPLIED)) {
			setApplied(nbt.getBoolean(APPLIED));
		}
		if (nbt.contains("appliedTo")) {
			getAppliedTo().clear();
			ListNBT list = nbt.getList("appliedTo", 8);
			list.forEach(s -> {
				getAppliedTo().add(s.getAsString());
			});
		}
		return true;
	}

	boolean isApplied();

	void setApplied(boolean applied);

	List<String> getAppliedTo();

	void setAppliedTo(List<String> appliedTo);
	
	boolean isAppliedTo(String type);

}

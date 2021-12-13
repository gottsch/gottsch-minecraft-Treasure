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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

/**
 * 
 * @author Mark Gottschling on Aug 24, 2021
 *
 */
public class IlluminationCharmEntity extends CharmEntity {
	private List<ICoords> coordsList;

	/**
	 * 
	 * @param charm
	 * @param value
	 * @param duration
	 * @param percent
	 */
	public IlluminationCharmEntity(ICharm charm, double value, int duration, double percent) {
		super(charm, value, duration, percent);
		setCoordsList(Collections.synchronizedList(new LinkedList<>()));
	}	
	
	/**
	 * 
	 */
	@Override
	public boolean load(CompoundTag nbt) {
		super.load(nbt);		
		ListTag list = nbt.getList("illuminationCoords", 10);
//		Treasure.logger.debug("illumination tag list size -> {}", list.tagCount());
		for (int i = 0; i < list.size(); i++) {
			CompoundTag tag = (CompoundTag) list.get(i);
			ICoords coords = ICoords.readFromNBT(tag);
			if (coords != null) {
				getCoordsList().add(coords);
			}
		}
		return true;
	}
	
	@Override
	public CompoundTag save(CompoundTag nbt) {
		nbt = super.save(nbt);
		try {
			ListTag list = new ListTag();
			synchronized (coordsList) {
				for (ICoords coords : coordsList) {
					// create a new nbt
					CompoundTag coordsTag = new CompoundTag();
					coords.writeToNBT(coordsTag);
					list.add(coordsTag);
				}
			}
			nbt.remove("illuminationCoords");
			nbt.put("illuminationCoords", list);
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<ICoords> getCoordsList() {
		if (coordsList == null) {
			coordsList = new LinkedList<>();
		}
		return coordsList;
	}

	/**
	 * 
	 * @param blockList
	 */
	public void setCoordsList(List<ICoords> blockList) {
		this.coordsList = blockList;
	}

	@Override
	public String toString() {
		return "IlluminationCharmEntity [" + /*coordsList=" + coordsList + ",*/ " toString()=" + super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((coordsList == null) ? 0 : coordsList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		IlluminationCharmEntity other = (IlluminationCharmEntity) obj;
		if (coordsList == null) {
			if (other.coordsList != null)
				return false;
		} else if (!coordsList.equals(other.coordsList))
			return false;
		return true;
	}
}

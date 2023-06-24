/*
 * This file is part of  Treasure2.
 * Copyright (c) 2019 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.structure;

import java.util.ArrayList;
import java.util.List;

import mod.gottsch.forge.gottschcore.enums.IRarity;


/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
@Deprecated
public class StructureMeta {
	private String name;
	private List<String> region;
	private String type;
	private int verticalOffset;
	private String nullBlockName;
	private List<String> biomeWhitelist;
	private List<String> biomeBlacklist;
	private List<IRarity> rarities;

	public StructureMeta() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRegion() {
		if (region == null) {
			region = new ArrayList<>();
		}
		return region;
	}

	public void setRegion(List<String> region) {
		this.region = region;
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public String getNullBlockName() {
		return nullBlockName;
	}

	public void setNullBlockName(String nullBlockName) {
		this.nullBlockName = nullBlockName;
	}

	public List<String> getBiomeWhitelist() {
		if (biomeWhitelist == null) {
			biomeWhitelist = new ArrayList<>();
		}
		return biomeWhitelist;
	}

	public void setBiomeWhitelist(List<String> biomeWhitelist) {
		this.biomeWhitelist = biomeWhitelist;
	}

	public List<String> getBiomeBlacklist() {
		if (biomeBlacklist == null) {
			biomeBlacklist = new ArrayList<>();
		}
		return biomeBlacklist;
	}

	public void setBiomeBlacklist(List<String> biomeBlacklist) {
		this.biomeBlacklist = biomeBlacklist;
	}

	public List<IRarity> getRarities() {
		if (rarities == null) {
			rarities = new ArrayList<>();
		}
		return rarities;
	}

	public void setRarities(List<IRarity> rarities) {
		this.rarities = rarities;
	}

	@Override
	public String toString() {
		return "StructureMeta [name=" + name + ", region=" + region + ", verticalOffset=" + verticalOffset
				+ ", nullBlockName=" + nullBlockName + ", biomeWhiteList=" + biomeWhitelist + ", biomeBlackList="
				+ biomeBlacklist + ", rarities=" + rarities + "]";
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	
}

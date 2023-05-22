/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.config;

import java.util.ArrayList;
import java.util.List;

import mod.gottsch.forge.gottschcore.spatial.ICoords;

/**
 * 
 * @author Mark Gottschling on May 20, 2023
 *
 */
public class StructureConfiguration {
	List<StructMeta> structMetas;
	
	/*
	 * 
	 */
	public static class StructMeta {
		private String name;
		private List<String> biomeWhitelist;
		private List<String> biomeBlacklist;
		private ICoords offset;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<String> getBiomeWhitelist() {
			if (biomeWhitelist == null) {
				biomeWhitelist = new ArrayList<>(1);
			}
			return biomeWhitelist;
		}
		public void setBiomeWhitelist(List<String> biomeWhitelist) {
			this.biomeWhitelist = biomeWhitelist;
		}
		public List<String> getBiomeBlacklist() {
			if (biomeBlacklist == null) {
				biomeBlacklist = new ArrayList<>(1);
			}
			return biomeBlacklist;
		}
		public void setBiomeBlacklist(List<String> biomeBlacklist) {
			this.biomeBlacklist = biomeBlacklist;
		}
		public ICoords getOffset() {
			return offset;
		}
		public void setOffset(ICoords offset) {
			this.offset = offset;
		}
	}

	public List<StructMeta> getStructMetas() {
		return structMetas;
	}

	public void setStructMetas(List<StructMeta> structMetas) {
		this.structMetas = structMetas;
	}
}

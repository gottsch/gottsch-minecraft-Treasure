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

/**
 * 
 * @author Mark Gottschling on May 20, 2023
 *
 */
public class StructureConfiguration {
	List<StructMeta> structMetas;
	
//	private StructType terranean;
//	private StructType subterranean;
//	private StructType subaqueous;
	
//	private List<StructMeta> well;
//	private List<StructMeta> ruin;
//	private List<StructMeta> room;
//	private List<StructMeta> marker;
	
	/*
	 * 
	 */
//	public static class StructType {
//		private List<StructMeta> well;
//		private List<StructMeta> ruin;		
//		private List<StructMeta> room;
//		private List<StructMeta> marker;
//		
//		public List<StructMeta> getWell() {
//			return well;
//		}
//		public void setWell(List<StructMeta> well) {
//			this.well = well;
//		}
//		public List<StructMeta> getRuin() {
//			return ruin;
//		}
//		public void setRuin(List<StructMeta> ruin) {
//			this.ruin = ruin;
//		}
//		public List<StructMeta> getRoom() {
//			return room;
//		}
//		public void setRoom(List<StructMeta> room) {
//			this.room = room;
//		}
//		public List<StructMeta> getMarker() {
//			return marker;
//		}
//		public void setMarker(List<StructMeta> marker) {
//			this.marker = marker;
//		}
//	}
//	
	/*
	 * 
	 */
	public static class StructMeta {
		private String name;
		private List<String> biomeWhitelist;
		private List<String> biomeBlacklist;
		
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
	}
//
//	public StructType getTerranean() {
//		return terranean;
//	}
//
//	public void setTerranean(StructType terranean) {
//		this.terranean = terranean;
//	}
//
//	public StructType getSubterranean() {
//		return subterranean;
//	}
//
//	public void setSubterranean(StructType subterranean) {
//		this.subterranean = subterranean;
//	}
//
//	public StructType getSubaqueous() {
//		return subaqueous;
//	}
//
//	public void setSubaqueous(StructType subaqueous) {
//		this.subaqueous = subaqueous;
//	}

	public List<StructMeta> getStructMetas() {
		return structMetas;
	}

	public void setStructMetas(List<StructMeta> structMetas) {
		this.structMetas = structMetas;
	}

//	public List<StructMeta> getWell() {
//		return well;
//	}
//
//	public void setWell(List<StructMeta> well) {
//		this.well = well;
//	}
//
//	public List<StructMeta> getRuin() {
//		return ruin;
//	}
//
//	public void setRuin(List<StructMeta> ruin) {
//		this.ruin = ruin;
//	}
//
//	public List<StructMeta> getRoom() {
//		return room;
//	}
//
//	public void setRoom(List<StructMeta> room) {
//		this.room = room;
//	}
//
//	public List<StructMeta> getMarker() {
//		return marker;
//	}
//
//	public void setMarker(List<StructMeta> marker) {
//		this.marker = marker;
//	}



}

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

import mod.gottsch.forge.gottschcore.spatial.Coords;
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
		private Position offset;
		
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
		public Position getOffset() {
			if (offset == null) {
				offset = new Position();
			}
			return offset;
		}
		public void setOffset(Position offset) {
			this.offset = offset;
		}
		
		@Override
		public String toString() {
			return "StructMeta [name=" + name + ", biomeWhitelist=" + biomeWhitelist + ", biomeBlacklist="
					+ biomeBlacklist + ", offset=" + offset + "]";
		}
	}

	public static class Position {
		private Integer x;
		private Integer y;
		private Integer z;
		
		public ICoords asCoords() {
			if (x == null && y == null && z == null) {
				return Coords.EMPTY;
			}
			if (x == null) {
				x = 0;
			}
			if (y == null) {
				y = 0;
			}
			if (z == null) {
				z = 0;
			}
			return new Coords(x, y, z);
		}
		
		public Integer getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public Integer getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public Integer getZ() {
			return z;
		}
		public void setZ(int z) {
			this.z = z;
		}

		@Override
		public String toString() {
			return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
		}
	}
	
	public List<StructMeta> getStructMetas() {
		return structMetas;
	}

	public void setStructMetas(List<StructMeta> structMetas) {
		this.structMetas = structMetas;
	}
}

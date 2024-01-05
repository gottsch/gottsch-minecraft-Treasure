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
package mod.gottsch.forge.treasure2.core.registry;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Dec 16, 2021
 *
 */
public class Manifest {
	   // lists
    private List<String> resources;
	
	// folders
	private List<String>resourceFolderLocations;
	
	/**
	 * 
	 */
	public Manifest() {}

	public List<String> getResources() {
		return resources;
	}

	public void setResources(List<String> resources) {
		this.resources = resources;
	}

	public List<String> getResourceFolderLocations() {
		return resourceFolderLocations;
	}

	public void setResourceFolderLocations(List<String> resourceFolderLocations) {
		this.resourceFolderLocations = resourceFolderLocations;
	}

	@Override
	public String toString() {
		return "MetaResources [resources=" + resources + ", resourceFolderLocations=" + resourceFolderLocations + "]";
	}

}
/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry.manifest;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Nov 27, 2022
 *
 */
@Deprecated
public class ResourceManifest {
    // lists
    private List<String> resources;
	
	// folders
	private List<String> resourceFolderLocations;
	
	/**
	 * 
	 */
	public ResourceManifest() {}

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
		return "ResourceManifest [resources=" + resources + ", resourceFolderLocations=" + resourceFolderLocations + "]";
	}

}

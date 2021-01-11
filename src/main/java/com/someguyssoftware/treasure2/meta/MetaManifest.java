/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class MetaManifest {
    // lists
    private List<String> resources;
	
	// folders
	private List<String>resourceFolderLocations;
	
	/**
	 * 
	 */
	public MetaManifest() {}

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

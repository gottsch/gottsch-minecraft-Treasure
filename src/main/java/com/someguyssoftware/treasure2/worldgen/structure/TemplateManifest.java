/**
 * 
 */
package com.someguyssoftware.treasure2.worldgen.structure;

import java.util.List;

/**
 * 
 * @author Mark Gottschling on Jan 10, 2021
 *
 */
public class TemplateManifest {
    // lists
    private List<String> resources;
	
	// folders
	private List<String>resourceFolderLocations;
	
	/**
	 * 
	 */
	public TemplateManifest() {}

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
		return "TemplateResources [resources=" + resources + ", resourceFolderLocations=" + resourceFolderLocations + "]";
	}

}

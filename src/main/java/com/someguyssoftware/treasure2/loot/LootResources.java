/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.List;

/**
 * @author Mark Gottschling on Aug 30, 2020
 *
 */
public class LootResources {
    // lists
    // TODO rename
    private List<String> supportingResources;
    private List<String> injectResources;
	private List<String> chestResources;
	private List<String> specialResources;
	
	// folders
	private List<String> specialLootTableFolderLocations;
    private List<String> chestLootTableFolderLocations;
    // TODO fix
    private List<String> injectLootTableFolderLocations;
    // TODO rename
    private List<String> supportingLootTableFolderLocatoins;
	
	/**
	 * 
	 */
	public LootResources() {}

	public List<String> getSupportingResources() {
		return supportingResources;
	}

	public void setSupportingResources(List<String> supportingResources) {
		this.supportingResources = supportingResources;
	}

    public List<String> getInjectResources() {
        return injectResources;
    }

    public void setInjectResources(List<String> injectResources) {
        this.injectResources = injectLootTableFolderLocations;
    }

	public List<String> getChestResources() {
		return chestResources;
	}

	public void setChestResources(List<String> chestResources) {
		this.chestResources = chestResources;
	}

	public List<String> getSpecialResources() {
		return specialResources;
	}

	public void setSpecialResources(List<String> specialResources) {
		this.specialResources = specialResources;
	}

	public List<String> getSpecialLootTableFolderLocations() {
		return specialLootTableFolderLocations;
	}

	public void setSpecialLootTableFolderLocations(List<String> specialChestLootTableFolderLocations) {
		this.specialLootTableFolderLocations = specialChestLootTableFolderLocations;
	}

	public List<String> getChestLootTableFolderLocations() {
		return chestLootTableFolderLocations;
	}

	public void setChestLootTableFolderLocations(List<String> chestLootTableFolderLocations) {
		this.chestLootTableFolderLocations = chestLootTableFolderLocations;
	}

	public List<String> getSupportingLootTableFolderLocatoins() {
		return supportingLootTableFolderLocatoins;
	}

	public void setSupportingLootTableFolderLocatoins(List<String> supportingLootTableFolderLocatoins) {
		this.supportingLootTableFolderLocatoins = supportingLootTableFolderLocatoins;
    }
    
    public List<String> getInjectLootTableFolderLocations() {
        return injectLootTableFolderLocations;
    }

    public void setInjectLootTableFolderLocations(List<String> locations) {
        this.injectLootTableFolderLocations = locations;
    }

	@Override
	public String toString() {
		return "DecayResources [supportingResources=" + supportingResources + ", chestResources=" + chestResources
				+ ", specialResources=" + specialResources + ", specialLootTableFolderLocations="
				+ specialLootTableFolderLocations + ", chestLootTableFolderLocations="
				+ chestLootTableFolderLocations + ", supportingLootTableFolderLocatoins="
				+ supportingLootTableFolderLocatoins + "]";
	}
}

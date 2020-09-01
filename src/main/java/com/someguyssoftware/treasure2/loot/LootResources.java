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
	private List<String> supportingResources;
	private List<String> chestResources;
	private List<String> specialResources;
	
	// folders
	private List<String> specialChestLootTableFolderLocations;
	private List<String> chestLootTableFolderLocations;
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

	public List<String> getSpecialChestLootTableFolderLocations() {
		return specialChestLootTableFolderLocations;
	}

	public void setSpecialChestLootTableFolderLocations(List<String> specialChestLootTableFolderLocations) {
		this.specialChestLootTableFolderLocations = specialChestLootTableFolderLocations;
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

	@Override
	public String toString() {
		return "LootResources [supportingResources=" + supportingResources + ", chestResources=" + chestResources
				+ ", specialResources=" + specialResources + ", specialChestLootTableFolderLocations="
				+ specialChestLootTableFolderLocations + ", chestLootTableFolderLocations="
				+ chestLootTableFolderLocations + ", supportingLootTableFolderLocatoins="
				+ supportingLootTableFolderLocatoins + "]";
	}
}

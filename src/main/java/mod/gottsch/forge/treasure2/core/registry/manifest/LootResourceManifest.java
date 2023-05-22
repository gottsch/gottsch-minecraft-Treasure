/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.registry.manifest;

import java.util.List;

/**
 * @author Mark Gottschling on Aug 30, 2020
 *
 */
@Deprecated
public class LootResourceManifest {
    // lists
    private List<String> supportingResources;
    private List<String> injectResources;
	private List<String> chestResources;
	private List<String> specialResources;
	
	// folders
	private List<String> specialLootTableFolderLocations;
    private List<String> chestLootTableFolderLocations;
    private List<String> injectLootTableFolderLocations;
    private List<String> supportingLootTableFolderLocations;
	
	/**
	 * 
	 */
	public LootResourceManifest() {}

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

	public List<String> getSupportingLootTableFolderLocations() {
		return supportingLootTableFolderLocations;
	}

	public void setSupportingLootTableFolderLocations(List<String> supportingLootTableFolderLocatoins) {
		this.supportingLootTableFolderLocations = supportingLootTableFolderLocatoins;
    }
    
    public List<String> getInjectLootTableFolderLocations() {
        return injectLootTableFolderLocations;
    }

    public void setInjectLootTableFolderLocations(List<String> locations) {
        this.injectLootTableFolderLocations = locations;
    }

	@Override
	public String toString() {
		return "LootResources [supportingResources=" + supportingResources + ", chestResources=" + chestResources
				+ ", specialResources=" + specialResources + ", specialLootTableFolderLocations="
				+ specialLootTableFolderLocations + ", chestLootTableFolderLocations="
				+ chestLootTableFolderLocations + ", supportingLootTableFolderLocatoins="
				+ supportingLootTableFolderLocations + "]";
	}
}

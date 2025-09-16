/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.config;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.treasure2.Treasure;

import java.util.List;
import java.util.Optional;

/**
 * 
 * @author Mark Gottschling on Nov 8, 2022
 *
 */
public class ChestPlacementConfiguration {
	private List<PlacementSetting> placementSettings;

	/**
	 * 
	 * @param key
	 * @return
	 */
	public PlacementSetting getPlacementSetting(String key) {
		// cycle through all the placementSettings (it's a small list)
		for (PlacementSetting placementSetting : placementSettings) {
			if (placementSetting.key.equalsIgnoreCase(key)) {
				return placementSetting;
			}
		}
		return null;
	}
		
	/*
	 * 
	 */
	public static class PlacementSetting {
		private String key;
		private Integer registrySize;
		private Double probability;
		private List<ChestRarity> rarities;
		
		public Optional<ChestRarity> getRarity(IRarity rarity) {
			try {
				return rarities.stream().filter(r -> r.getRarity().equalsIgnoreCase(rarity.getName())).findFirst();
			} catch(Exception e) {
				Treasure.LOGGER.error("A registered Rarity was not configured properly in the treasure2-chests-x.toml file.", e);
				throw e;
			}
		}
		
		public String getKey() {
			return key;
		}
		public void setKey(String key) {
			this.key = key;
		}

		public Integer getRegistrySize() {
			return registrySize;
		}
		public void setRegistrySize(Integer registrySize) {
			this.registrySize = registrySize;
		}

		public Double getProbability() {
			return probability;
		}
		public void setProbability(Double probability) {
			this.probability = probability;
		}

		public List<ChestRarity> getRarities() {
			return rarities;
		}
		public void setRarities(List<ChestRarity> rarities) {
			this.rarities = rarities;
		}
	}

	/*
	 * 
	 */
	public static class ChestRarity {
		private String rarity;
		private Integer weight;
		private List<String> biomeWhitelist;
		private List<String> biomeTypeWhitelist;
		private List<String> biomeFilter;
		private List<String> biomeTypeFilter;
		
		public String getRarity() {
			return rarity;
		}
		public void setRarity(String rarity) {
			this.rarity = rarity;
		}
		public Integer getWeight() {
			return weight;
		}
		public void setWeight(Integer weight) {
			this.weight = weight;
		}

		public List<String> getBiomeFilter() {
			return biomeFilter;
		}
		public void setBiomeFilter(List<String> biomeFilter) {
			this.biomeFilter = biomeFilter;
		}
		public List<String> getBiomeTypeFilter() {
			return biomeTypeFilter;
		}
		public void setBiomeTypeFilter(List<String> biomeTypeFilter) {
			this.biomeTypeFilter = biomeTypeFilter;
		}
		
	}

	public List<PlacementSetting> getPlacementSettings() {
		return placementSettings;
	}

	public void setPlacementSettings(List<PlacementSetting> placementSettings) {
		this.placementSettings = placementSettings;
	}
}

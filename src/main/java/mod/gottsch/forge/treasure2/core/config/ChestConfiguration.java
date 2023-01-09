/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.config;

import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.gottschcore.enums.IRarity;

/**
 * 
 * @author Mark Gottschling on Nov 8, 2022
 *
 */
public class ChestConfiguration {
	private List<String > dimensions;
	private Double treasureMapProbability;
	private List<Generator> generators;
	private List<Chest> chests;

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Generator getGenerator(String key) {
		// cycle through all the generators (it's a small list)
		for (Generator generator : generators) {
			if (generator.key.equalsIgnoreCase(key)) {
				return generator;
			}
		}
		return null;
	}
	
	/*
	 * 
	 */
	public static class Generator {
		private String key;
		private Integer registrySize;
		private Double probability;
		private Integer minBlockDistance;
		private Integer waitChunks;
		private Double surfaceProbability;
		private Double structureProbability;
		private List<ChestRarity> rarities;
		
		public Optional<ChestRarity> getRarity(IRarity rarity) {
			return rarities.stream().filter(r -> r.getRarity().equalsIgnoreCase(rarity.getName())).findFirst();
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
		public Integer getMinBlockDistance() {
			return minBlockDistance;
		}
		public void setMinBlockDistance(Integer minBlockDistance) {
			this.minBlockDistance = minBlockDistance;
		}
		public Integer getWaitChunks() {
			return waitChunks;
		}
		public void setWaitChunks(Integer waitChunks) {
			this.waitChunks = waitChunks;
		}
		public Double getSurfaceProbability() {
			return surfaceProbability;
		}
		public void setSurfaceProbability(Double surfaceProbability) {
			this.surfaceProbability = surfaceProbability;
		}
		public Double getStructureProbability() {
			return structureProbability;
		}
		public void setStructureProbability(Double structureProbability) {
			this.structureProbability = structureProbability;
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
		private Integer minDepth;
		private Integer maxDepth;
		private List<String> biomeWhitelist;
		private List<String> biomeTypeWhitelist;
		private List<String> biomeBlacklist;
		private List<String> biomeTypeBlacklist;
		
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
		public Integer getMinDepth() {
			return minDepth;
		}
		public void setMinDepth(Integer minDepth) {
			this.minDepth = minDepth;
		}
		public Integer getMaxDepth() {
			return maxDepth;
		}
		public void setMaxDepth(Integer maxDepth) {
			this.maxDepth = maxDepth;
		}
		public List<String> getBiomeWhitelist() {
			return biomeWhitelist;
		}
		public void setBiomeWhitelist(List<String> biomeWhitelist) {
			this.biomeWhitelist = biomeWhitelist;
		}
		public List<String> getBiomeTypeWhitelist() {
			return biomeTypeWhitelist;
		}
		public void setBiomeTypeWhitelist(List<String> biomeTypeWhitelist) {
			this.biomeTypeWhitelist = biomeTypeWhitelist;
		}
		public List<String> getBiomeBlacklist() {
			return biomeBlacklist;
		}
		public void setBiomeBlacklist(List<String> biomeBlacklist) {
			this.biomeBlacklist = biomeBlacklist;
		}
		public List<String> getBiomeTypeBlacklist() {
			return biomeTypeBlacklist;
		}
		public void setBiomeTypeBlacklist(List<String> biomeTypeBlacklist) {
			this.biomeTypeBlacklist = biomeTypeBlacklist;
		}
		
	}
	
	/*
	 * 
	 */
	public static class Chest {
		private String name;
		private Boolean	enabled;
		private String rarity;
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public Boolean getEnabled() {
			return enabled;
		}
		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}
		public String getRarity() {
			return rarity;
		}
		public void setRarity(String rarity) {
			this.rarity = rarity;
		}
		
	}

	public List<String> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<String> dimensions) {
		this.dimensions = dimensions;
	}

	public Double getTreasureMapProbability() {
		return treasureMapProbability;
	}

	public void setTreasureMapProbability(Double treasureMapProbability) {
		this.treasureMapProbability = treasureMapProbability;
	}

	public List<Generator> getGenerators() {
		return generators;
	}

	public void setGenerators(List<Generator> generators) {
		this.generators = generators;
	}

	public List<Chest> getChests() {
		return chests;
	}

	public void setChests(List<Chest> chests) {
		this.chests = chests;
	}
}

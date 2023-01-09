/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.core.enums.IRegionPlacement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author Mark Gottschling on Feb 8, 2020
 *
 */
public class ChestGeneratorData extends GeneratorData {
	private boolean markers;
	private boolean structure;
	private boolean pit;
	private IRarity rarity;
	private ResourceLocation registryName;
	private BlockState state;
	private IRegionPlacement placement;
	
	/*
	 * the actual coords of the chest.
	 * could be different from GeneratorData.spawnCoords.
	 */
	private ICoords coords;
	
	/**
	 * 
	 */
	public ChestGeneratorData() {
	}
	
	/**
	 * 
	 * @param chestCoords
	 * @param chestState
	 */
	public ChestGeneratorData(ICoords chestCoords, BlockState chestState) {
		super();
		this.setSpawnCoords(chestCoords);
		this.setState(chestState);
	}

	public IRarity getRarity() {
		return rarity;
	}

	public void setRarity(IRarity rarity) {
		this.rarity = rarity;
	}

	public boolean isMarkers() {
		return markers;
	}

	public void setMarkers(boolean markers) {
		this.markers = markers;
	}

	public boolean isStructure() {
		return structure;
	}

	public void setStructure(boolean structure) {
		this.structure = structure;
	}

	public boolean isPit() {
		return pit;
	}

	public void setPit(boolean pit) {
		this.pit = pit;
	}

	@Override
	public String toString() {
		return "ChestGeneratorData [markers=" + markers + ", structure=" + structure + ", pit=" + pit + ", rarity="
				+ rarity + ", registryName=" + registryName + ", state=" + state + ", placement=" + placement
				+ ", coords=" + coords + "]";
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}

	public BlockState getState() {
		return state;
	}

	public void setState(BlockState state) {
		this.state = state;
	}

	public ICoords getCoords() {
		return coords;
	}

	public void setCoords(ICoords coords) {
		this.coords = coords;
	}

	public IRegionPlacement getPlacement() {
		return placement;
	}

	public void setPlacement(IRegionPlacement placement) {
		this.placement = placement;
	}
}

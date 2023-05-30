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
package mod.gottsch.forge.treasure2.core.registry.support;

import java.util.Optional;

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.enums.IRegionPlacement;
import mod.gottsch.forge.treasure2.core.enums.RegionPlacement;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Dec 2, 2022
 *
 */
public class ChestGenContext extends GeneratedContext {
	private static final String LEGACY_NAME = "registryName";
	
	public enum GenType {
		CHEST,
		NONE;
	}
	
	private ResourceLocation name;
	private IRegionPlacement placement;
	private GenType genType;
	private boolean discovered;
	private ICoords chartedFrom;
	
	public ChestGenContext() {
		super();
		this.genType = GenType.CHEST;
	}
	
	public ChestGenContext(IRarity rarity, ICoords coords) {
		this(rarity, coords, GenType.CHEST);
	}
	
	public ChestGenContext(IRarity rarity, ICoords coords, GenType genType) {
		super(rarity, coords);
		this.genType = genType;
	}

	/**
	 * 
	 */
	public CompoundTag save() {		
		CompoundTag tag = super.save();
		if (getName() != null) {
			tag.putString("name", getName().toString());
		}
		tag.putBoolean("discovered",isDiscovered());

		if (getPlacement() != null) {
			tag.putString("placement", getPlacement().getValue());
		}
		if (isCharted()) {
			CompoundTag chartedFromTag = getChartedFrom().save(new CompoundTag());
			tag.put("chartedFrom", chartedFromTag);
		}
		if (getGenType() != null) {
			tag.putString("genType", getGenType().name());
		}
		else {
			tag.putString("genType", GenType.CHEST.name());
		}
		return tag;
	}
	
	/**
	 * 
	 */
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("name")) {
			this.name = ModUtil.asLocation(tag.getString("name"));
		}
		else if (tag.contains(LEGACY_NAME)) {
			this.name = ModUtil.asLocation(tag.getString(LEGACY_NAME));
		}
		if (tag.contains("discovered")) {
			this.discovered = tag.getBoolean("discovered");
		}
		if (tag.contains("placement")) {
			Optional<IRegionPlacement> placement = TreasureApi.getRegionPlacement(tag.getString("placement"));
			if (placement.isPresent()) {
				this.placement = placement.get();
			}
			else {
				this.placement = RegionPlacement.SURFACE; // TODO change to NONE
			}
		}
		if (tag.contains("chartedFrom")) {
			this.chartedFrom = Coords.EMPTY.load(tag.getCompound("chartedFrom"));
		}
		if (tag.contains("genType")) {
			this.genType = GenType.valueOf(tag.getString("genType").toUpperCase());
		}
	}
	
	public ResourceLocation getName() {
		return name;
	}

	public void setName(ResourceLocation name) {
		this.name = name;
	}

	public IRegionPlacement getPlacement() {
		return placement;
	}

	public void setPlacement(IRegionPlacement placement) {
		this.placement = placement;
	}

	public GenType getGenType() {
		return genType;
	}

	public void setGenType(GenType genType) {
		this.genType = genType;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public ICoords getChartedFrom() {
		return chartedFrom;
	}

	public void setChartedFrom(ICoords chartedFrom) {
		this.chartedFrom = chartedFrom;
	}
	
	public boolean isCharted() {
		return chartedFrom != null && chartedFrom != Coords.EMPTY;
	}

	@Override
	public String toString() {
		return "ChestGenContext [name=" + name + ", placement=" + placement + ", genType=" + genType + ", discovered="
				+ discovered + ", chartedFrom=" + chartedFrom + ", getCoords()=" + getCoords() + ", getRarity()="
				+ getRarity() + "]";
	}
}

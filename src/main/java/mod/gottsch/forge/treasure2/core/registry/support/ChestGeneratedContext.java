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

import mod.gottsch.forge.gottschcore.enums.IRarity;
import mod.gottsch.forge.gottschcore.spatial.Coords;
import mod.gottsch.forge.gottschcore.spatial.ICoords;
import mod.gottsch.forge.treasure2.api.TreasureApi;
import mod.gottsch.forge.treasure2.core.enums.Rarity;
import mod.gottsch.forge.treasure2.core.util.ModUtil;
import mod.gottsch.forge.treasure2.core.world.feature.FeatureType;
import mod.gottsch.forge.treasure2.core.world.feature.IFeatureType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

/**
 * 
 * @author Mark Gottschling on Dec 2, 2022
 *
 */
public class ChestGeneratedContext extends GeneratedContext {
	private static final String LEGACY_NAME = "registryName";
	
	private static final String NAME = "name";
	private static final String DISCOVERED = "discovered";
	private static final String FEATURE_TYPE = "featureType";
	private static final String SURFACE_COORDS = "surfaceCoords";
	private static final String GENERATED_TYPE = "generatedType";
	private static final String CHARTED_FROM = "chartedFrom";
	
	// generation type indicates the whether a chest was generated or nothing
	public interface IGeneratedType {
		public String name();
	}
	public enum GeneratedType implements IGeneratedType {
		CHEST,
		NONE;
	}
	
	// resource name of the chest
	private ResourceLocation name;
	// the feature type that generated this chest
	private IFeatureType featureType;
	// type of generation
	private IGeneratedType generatedType;
	// has the chest been discovered
	private boolean discovered;
	// coords of chest that contains a treasure map to this chest
	private ICoords chartedFrom;
	// coords of the surface spawn
	private ICoords surfaceCoords;
	
	/**
	 * 
	 */
	public ChestGeneratedContext() {
		super();
		this.generatedType = GeneratedType.CHEST;
		this.featureType = FeatureType.UNKNOWN;
		this.setRarity(Rarity.NONE);
	}
	
	public ChestGeneratedContext(IRarity rarity, ICoords coords) {
		this(rarity, coords, GeneratedType.CHEST);
	}
	
	public ChestGeneratedContext(IRarity rarity, ICoords coords, IGeneratedType generatedType) {
		super(rarity, coords);
		this.generatedType = generatedType;
		this.featureType = FeatureType.UNKNOWN;
		this.surfaceCoords = coords;
	}

	/**
	 * 
	 */
	public CompoundTag save() {		
		CompoundTag tag = super.save();
		if (getName() != null) {
			tag.putString(NAME, getName().toString());
		}
		tag.putBoolean(DISCOVERED, isDiscovered());
		
		if (isCharted()) {
			CompoundTag chartedFromTag = getChartedFrom().save(new CompoundTag());
			tag.put(CHARTED_FROM, chartedFromTag);
		}
		if (getGeneratedType() != null) {
			tag.putString(GENERATED_TYPE, getGeneratedType().name());
		}
		else {
			tag.putString(GENERATED_TYPE, GeneratedType.CHEST.name());
		}
		
		if (getSurfaceCoords() != null) {
			tag.put(SURFACE_COORDS, getSurfaceCoords().save(new CompoundTag()));
		}
		
		tag.putString(FEATURE_TYPE, getFeatureType().getName());
		
		return tag;
	}
	
	/**
	 * 
	 */
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains(NAME)) {
			this.name = ModUtil.asLocation(tag.getString(NAME));
		}
		else if (tag.contains(LEGACY_NAME)) {
			this.name = ModUtil.asLocation(tag.getString(LEGACY_NAME));
		}
		if (tag.contains(DISCOVERED)) {
			this.discovered = tag.getBoolean(DISCOVERED);
		}

		if (tag.contains(CHARTED_FROM)) {
			this.chartedFrom = Coords.EMPTY.load(tag.getCompound(CHARTED_FROM));
		}
		if (tag.contains(GENERATED_TYPE)) {
			// TODO may have to have a registry at some point
			this.generatedType = GeneratedType.valueOf(tag.getString(GENERATED_TYPE).toUpperCase());
		}
		
		if (tag.contains(SURFACE_COORDS)) {
			this.surfaceCoords = Coords.EMPTY.load(tag.getCompound(SURFACE_COORDS));
		}
		if (tag.contains(FEATURE_TYPE)) {
			this.featureType = TreasureApi.getFeatureType(tag.getString(FEATURE_TYPE).toUpperCase()).orElse(FeatureType.UNKNOWN);		
		}
	}
	
	public ResourceLocation getName() {
		return name;
	}

	public void setName(ResourceLocation name) {
		this.name = name;
	}
	
	public ChestGeneratedContext withName(ResourceLocation name) {
		setName(name);
		return this;
	}

	public IGeneratedType getGeneratedType() {
		return generatedType;
	}

	public void setGeneratedType(IGeneratedType generatedType) {
		this.generatedType = generatedType;
	}
	
	public ChestGeneratedContext withGeneratedType(IGeneratedType type) {
		setGeneratedType(type);
		return this;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}

	public ChestGeneratedContext withDiscovered(boolean discovered) {
		setDiscovered(discovered);
		return this;		
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
		return "ChestGeneratedContext [name=" + name + ", featureType=" + featureType + ", generatedType="
				+ generatedType + ", discovered=" + discovered + ", chartedFrom=" + chartedFrom + ", surfaceCoords="
				+ surfaceCoords + "]";
	}

	public IFeatureType getFeatureType() {
		return featureType;
	}

	public void setFeatureType(IFeatureType featureType) {
		this.featureType = featureType;
	}

	public ChestGeneratedContext withFeatureType(IFeatureType featureType) {
		setFeatureType(featureType);
		return this;
	}
	
	public ICoords getSurfaceCoords() {
		return surfaceCoords;
	}

	public void setSurfaceCoords(ICoords surfaceCoords) {
		this.surfaceCoords = surfaceCoords;
	}
	
	public ChestGeneratedContext withSurfaceCoords(ICoords surfaceCoords) {
		setSurfaceCoords(surfaceCoords);
		return this;
	}
}

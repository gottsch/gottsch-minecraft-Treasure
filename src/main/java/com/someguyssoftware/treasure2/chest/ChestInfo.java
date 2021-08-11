/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import java.util.Optional;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.enums.ChestGeneratorType;
import com.someguyssoftware.treasure2.enums.Rarity;
import com.someguyssoftware.treasure2.generator.ChestGeneratorData;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestInfo {
	private ICoords coords;
	private Rarity rarity;
	private ChestEnvironment environment;
	private ResourceLocation registryName;
	private boolean markers;
	private boolean structure;
	private boolean pit;
	private boolean discovered;

	
//	private Optional<ICoords> mappedToCoords = Optional.empty();
	private Optional<ICoords> mappedFromCoords = Optional.empty();
	
	/**
	 * 
	 */
	public ChestInfo() {	}

	/**
	 * 
	 * @param rarity
	 * @param coords
	 */
	public ChestInfo(Rarity rarity, ICoords coords) {
		setRarity(rarity);
		setCoords(coords);
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	public static ChestInfo from(ChestGeneratorData data) {
		ChestInfo info = new ChestInfo(data.getRarity(), data.getChestContext().getCoords());
		info.setEnvironment(data.getEnvironment());
		info.setRegistryName(data.getRegistryName());
		info.setMarkers(data.isMarkers());
		info.setPit(data.isPit());
		info.setStructure(data.isStructure());
		return info;
	}
	
	/**
	 * does this treasure chest contain a map item to another chest.
	 * @return
	 */
//	public boolean containsTreasureMapTo() {
//		if (mappedToCoords.isPresent()) {
//			return true;
//		}
//		return false;
//	}
//	
//	public Optional<ICoords> getTreasureMapToCoords() {
//		return this.mappedToCoords;
//	}
//	
//	public void setTreasureMapTo(ICoords coords) {
//		this.mappedToCoords = Optional.ofNullable(coords);
//	}
	
	/**
	 * is there a map item referencing this chest.
	 * @return
	 */
	public boolean isTreasureMapOf() {
		if (mappedFromCoords.isPresent()) {
			return true;
		}
		return false;
	}
	
	public Optional<ICoords> getTreasureMapFromCoords() {
		return this.mappedFromCoords;
	}
	
	public void setTreasureMapFrom(ICoords coords) {
		this.mappedFromCoords = Optional.ofNullable(coords);
	}
	
	/**
	 * @return the coords
	 */
	public ICoords getCoords() {
		return coords;
	}

	/**
	 * @param coords the coords to set
	 */
	public void setCoords(ICoords coords) {
		this.coords = coords;
	}

	/**
	 * @return the rarity
	 */
	public Rarity getRarity() {
		return rarity;
	}

	/**
	 * @param rarity the rarity to set
	 */
	public void setRarity(Rarity rarity) {
		this.rarity = rarity;
	}

	public boolean hasMarkers() {
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

	public ChestEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(ChestEnvironment environment) {
		this.environment = environment;
	}

	public ResourceLocation getRegistryName() {
		return registryName;
	}

	public void setRegistryName(ResourceLocation registryName) {
		this.registryName = registryName;
	}

//	public Optional<ICoords> getMappedToCoords() {
//		return mappedToCoords;
//	}
//
//	public void setMappedToCoords(Optional<ICoords> mappedToCoords) {
//		this.mappedToCoords = mappedToCoords;
//	}

	public boolean isMarkers() {
		return markers;
	}

	public boolean isDiscovered() {
		return discovered;
	}

	public void setDiscovered(boolean discovered) {
		this.discovered = discovered;
	}
	
	@Override
	public String toString() {
		return "ChestInfo [coords=" + coords + ", rarity=" + rarity + ", environment=" + environment + ", registryName="
				+ registryName + ", markers=" + markers + ", structure=" + structure + ", pit=" + pit + ", discovered="
				+ discovered + ", mappedFromCoords=" + mappedFromCoords + "]";
	}
}

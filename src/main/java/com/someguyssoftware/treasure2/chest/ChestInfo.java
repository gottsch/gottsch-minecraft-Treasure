/**
 * 
 */
package com.someguyssoftware.treasure2.chest;

import com.someguyssoftware.gottschcore.spatial.ICoords;
import com.someguyssoftware.treasure2.enums.Rarity;

/**
 * @author Mark Gottschling on Jan 22, 2018
 *
 */
public class ChestInfo {
	private ICoords coords;
	private Rarity rarity;
	
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
}

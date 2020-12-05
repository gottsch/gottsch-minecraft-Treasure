/**
 * 
 */
package com.someguyssoftware.treasure2.generator.oasis;

import com.someguyssoftware.gottschcore.positional.ICoords;

/**
 * @author Mark Gottschling on Apr 8, 2020
 *
 */
public class OasisInfo {
	private ICoords coords;
	private Integer dimensionID;
	private Integer biomeID;
	
	/**
	 * 
	 */
	public OasisInfo() {}

	/**
	 * 
	 * @param coords
	 * @param dimensionID
	 * @param biomeID
	 */
	public OasisInfo(ICoords coords, Integer dimensionID, Integer biomeID) {
		super();
		this.coords = coords;
		this.dimensionID = dimensionID;
		this.biomeID = biomeID;
	}

	public ICoords getCoords() {
		return coords;
	}

	public void setCoords(ICoords coords) {
		this.coords = coords;
	}

	public Integer getDimensionID() {
		return dimensionID;
	}

	public void setDimensionID(Integer dimensionID) {
		this.dimensionID = dimensionID;
	}

	public Integer getBiomeID() {
		return biomeID;
	}

	public void setBiomeID(Integer biomeID) {
		this.biomeID = biomeID;
	}
}

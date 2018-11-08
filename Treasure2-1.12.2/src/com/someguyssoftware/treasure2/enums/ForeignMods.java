/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

/**
 * @author Mark Gottschling on Oct 27, 2018
 *
 */
@Deprecated
public enum ForeignMods {
	MO_CREATURES("mocreatures");
	
	String modID;
	
	ForeignMods(String modID) {
		this.modID = modID;
	}

	public String getModID() {
		return modID;
	}

	public void setModID(String modID) {
		this.modID = modID;
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import com.someguyssoftware.gottschcore.meta.Meta;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public class StructureMeta extends Meta {
	private int verticalOffset;
	private boolean includeGraves;
	private boolean includeFog;
	private String decayRuleSetName;
	
	public StructureMeta() {}

	public boolean isIncludeGraves() {
		return includeGraves;
	}

	public void setIncludeGraves(boolean includeGraves) {
		this.includeGraves = includeGraves;
	}

	public boolean isIncludeFog() {
		return includeFog;
	}

	public void setIncludeFog(boolean includeFog) {
		this.includeFog = includeFog;
	}

	@Override
	public String toString() {
		return "StructureMeta [verticalOffset=" + verticalOffset + ", includeGraves=" + includeGraves + ", includeFog="
				+ includeFog + ", decayRuleSetName=" + decayRuleSetName + ", getName()=" + getName()
				+ ", getDescription()=" + getDescription() + ", getAuthor()=" + getAuthor() + ", getParent()="
				+ getParent() + ", getArchetypes()=" + getArchetypes() + ", getType()=" + getType() + ", getThemes()="
				+ getThemes() + ", getBiomeWhiteList()=" + getBiomeWhiteList() + ", getBiomeBlackList()="
				+ getBiomeBlackList() + ", getRarities()=" + getRarities() + ", getOrder()=" + getOrder()
				+ ", getOffset()=" + getOffset() + "]";
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public String getDecayRuleSetName() {
		return decayRuleSetName;
	}

	public void setDecayRuleSetName(String decayRuleSetName) {
		this.decayRuleSetName = decayRuleSetName;
	}
		
}

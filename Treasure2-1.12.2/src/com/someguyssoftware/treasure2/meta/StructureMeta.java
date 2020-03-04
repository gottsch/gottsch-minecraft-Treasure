/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.util.List;

import com.someguyssoftware.gottschcore.meta.Meta;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public class StructureMeta extends Meta {
	private int verticalOffset;
	private boolean includeGraves;
	private boolean includeFog;
	private List<String> decayRuleSetName;
	private String nullBlockName;

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
				+ includeFog + ", decayRuleSetName=" + decayRuleSetName + ", nullBlockName=" + nullBlockName
				+ ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getAuthor()=" + getAuthor()
				+ ", getParent()=" + getParent() + ", getArchetypes()=" + getArchetypes() + ", getType()=" + getType()
				+ ", getThemes()=" + getThemes() + ", getBiomeWhiteList()=" + getBiomeWhiteList()
				+ ", getBiomeBlackList()=" + getBiomeBlackList() + ", getRarities()=" + getRarities() + ", getOrder()="
				+ getOrder() + ", getOffset()=" + getOffset() + "]";
	}

	public int getVerticalOffset() {
		return verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}

	public List<String> getDecayRuleSetName() {
		return decayRuleSetName;
	}

	public void setDecayRuleSetName(List<String> decayRuleSetName) {
		this.decayRuleSetName = decayRuleSetName;
	}

	public String getNullBlockName() {
		return nullBlockName;
	}

	public void setNullBlockName(String nullBlockName) {
		this.nullBlockName = nullBlockName;
	}
}

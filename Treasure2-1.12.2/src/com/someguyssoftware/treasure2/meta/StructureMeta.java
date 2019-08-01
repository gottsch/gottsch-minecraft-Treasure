/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.util.List;

import com.someguyssoftware.gottschcore.biome.BiomeTypeHolder;
import com.someguyssoftware.gottschcore.enums.IRarity;
import com.someguyssoftware.gottschcore.meta.IMeta;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaTheme;
import com.someguyssoftware.gottschcore.meta.IMetaType;
import com.someguyssoftware.gottschcore.meta.Meta;
import com.someguyssoftware.gottschcore.positional.ICoords;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public class StructureMeta extends Meta {
	
	private ICoords offset;
	private boolean includeGraves;
	private boolean includeFog;
	
	public StructureMeta() {}

	public ICoords getOffset() {
		return offset;
	}

	public void setOffset(ICoords offset) {
		this.offset = offset;
	}

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
		return "StructureMeta [offset=" + offset + ", includeGraves=" + includeGraves + ", includeFog=" + includeFog
				+ ", getName()=" + getName() + ", getDescription()=" + getDescription() + ", getAuthor()=" + getAuthor()
				+ ", getParent()=" + getParent() + ", getArchetypes()=" + getArchetypes() + ", getType()=" + getType()
				+ ", getThemes()=" + getThemes() + ", getBiomeWhiteList()=" + getBiomeWhiteList()
				+ ", getBiomeBlackList()=" + getBiomeBlackList() + ", getRarities()=" + getRarities() + ", getOrder()="
				+ getOrder() + ", toString()=" + super.toString() + ", getClass()=" + getClass() + ", hashCode()="
				+ hashCode() + "]";
	}
		
}

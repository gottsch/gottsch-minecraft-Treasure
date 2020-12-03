/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.List;

/**
 * @author Mark Gottschling on Dec 1, 2020
 *
 */
public class LootTableShell {
	private String version;
	private List<String> categories;
	private List<LootPoolShell> pools;

	public LootTableShell() {}
	
	public List<LootPoolShell> getPools() {
		return pools;
	}

	public void setPools(List<LootPoolShell> pools) {
		this.pools = pools;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "LootTableShell [version=" + version + ", categories=" + categories + "]";
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.loot;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.ResourceLocation;

/**
 * @author Mark Gottschling on Dec 1, 2020
 *
 */
public class LootTableShell {
	// currently, this class is not serialized by other serializers so transient is safe to use for Gson
	private transient ResourceLocation resourceLocation;
	private String version;
	private String category;
	private List<String> categories;
	private List<LootPoolShell> pools;

	public LootTableShell() {}
	
	public List<LootPoolShell> getPools() {
		if (pools == null) {
			pools = new ArrayList<>();
		}
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
		if (categories == null) {
			categories = new ArrayList<String>();
			if (category != null && !category.isEmpty()) {
				categories.add(category);
			}
		}
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	@Override
	public String toString() {
		return "LootTableShell [version=" + version + ", categories=" + getCategories() + "]";
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ResourceLocation getResourceLocation() {
		return resourceLocation;
	}

	public void setResourceLocation(ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}
}

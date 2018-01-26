/**
 * 
 */
package com.someguyssoftware.lootbuilder.model;

import java.util.List;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Mark Gottschling on Jan 18, 2018
 *
 */
@DatabaseTable(tableName = "containers")
public class LootContainer {
	public final static String ID_FIELD_NAME = "id";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	@DatabaseField(columnName = "name", canBeNull = false, unique = true, width = 45)
	private String name;
	@DatabaseField(canBeNull = false, unique = false, defaultValue = "0", width = 3)
	private Byte rarity;
	@DatabaseField(canBeNull = true, unique = false)
	private String category;
	
	@DatabaseField(columnName = "min_groups", canBeNull = false, unique = false, defaultValue = "0", width = 3)
	private Byte minGroups;
	@DatabaseField(columnName = "max_groups", canBeNull = false, unique = false, defaultValue = "0", width = 3)
	private Byte maxGroups;
	@DatabaseField(columnName = "min_items", canBeNull = false, unique = false, defaultValue = "0", width = 3)
	private Byte minItems;
	@DatabaseField(columnName = "max_items", canBeNull = false, unique = false, defaultValue = "0", width = 3)
	private Byte maxItems;
	
	/**
	 * 
	 */
	public LootContainer() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getRarity() {
		return rarity;
	}

	public void setRarity(Byte rarity) {
		this.rarity = rarity;
	}

	public Byte getMinGroups() {
		return minGroups;
	}

	public void setMinGroups(Byte minGroups) {
		this.minGroups = minGroups;
	}

	public Byte getMaxGroups() {
		return maxGroups;
	}

	public void setMaxGroups(Byte maxGroups) {
		this.maxGroups = maxGroups;
	}

	public Byte getMinItems() {
		return minItems;
	}

	public void setMinItems(Byte minItems) {
		this.minItems = minItems;
	}

	public Byte getMaxItems() {
		return maxItems;
	}

	public void setMaxItems(Byte maxItems) {
		this.maxItems = maxItems;
	}

	/**
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LootContainer [id=" + id + ", name=" + name + ", rarity=" + rarity + ", category=" + category
				+ ", minGroups=" + minGroups + ", maxGroups=" + maxGroups + ", minItems=" + minItems + ", maxItems="
				+ maxItems + "]";
	}

}

/**
 * 
 */
package com.someguyssoftware.lootbuilder.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @author Mark Gottschling on Jan 18, 2018
 *
 */
@DatabaseTable(tableName = "groups_has_items")
public class LootGroupHasItem {
	public final static String ID_FIELD_NAME = "id";
	public final static String ITEM_ID_FIELD_NAME = "item_id";
	public final static String GROUP_ID_FIELD_NAME = "group_id";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	@DatabaseField(foreign = true, columnName = GROUP_ID_FIELD_NAME)
	private LootGroup group;
	@DatabaseField(foreign = true, foreignAutoRefresh=true, columnName = ITEM_ID_FIELD_NAME)
	private LootItem item;
	
	@DatabaseField(columnName = "item_weight", canBeNull = false, unique = false, width = 4, defaultValue = "0")
	private Short weight;
	
	/*
	 * the quantity of the particular item
	 */
	@DatabaseField(canBeNull = false, defaultValue = "0")
	private Short min;
	@DatabaseField(canBeNull = false, defaultValue = "0")
	private Short max;
	
	/*
	 * enchantments
	 */
	@DatabaseField(columnName = "min_enchants", canBeNull = false, defaultValue = "0")
	private Short minEnchants;
	@DatabaseField(columnName = "max_enchants", canBeNull = false, defaultValue = "0")
	private Short maxEnchants;
	
	// TODO add XREF table for specific enchantments
	
	/*
	 * order
	 */
	@DatabaseField(columnName="ordering", canBeNull = false, defaultValue = "0")
	private Short order;
	
	/**
	 * 
	 */
	public LootGroupHasItem() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LootItem getItem() {
		return item;
	}

	public void setItem(LootItem item) {
		this.item = item;
	}

	public LootGroup getGroup() {
		return group;
	}

	public void setGroup(LootGroup group) {
		this.group = group;
	}

	public Short getWeight() {
		return weight;
	}

	public void setWeight(Short weight) {
		this.weight = weight;
	}

	public Short getMin() {
		return min;
	}

	public void setMin(Short min) {
		this.min = min;
	}

	public Short getMax() {
		return max;
	}

	public void setMax(Short max) {
		this.max = max;
	}

	public Short getOrder() {
		return order;
	}

	public void setOrder(Short order) {
		this.order = order;
	}

	public Short getMinEnchants() {
		return minEnchants;
	}

	public void setMinEnchants(Short minEnchants) {
		this.minEnchants = minEnchants;
	}

	public Short getMaxEnchants() {
		return maxEnchants;
	}

	public void setMaxEnchants(Short maxEnchants) {
		this.maxEnchants = maxEnchants;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LootGroupHasItem [id=" + id + ", group=" + group + ", item=" + item + ", weight=" + weight + ", min="
				+ min + ", max=" + max + ", minEnchants=" + minEnchants + ", maxEnchants=" + maxEnchants + ", order="
				+ order + "]";
	}
}

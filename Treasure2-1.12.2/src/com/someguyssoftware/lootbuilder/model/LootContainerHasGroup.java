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
@DatabaseTable(tableName = "containers_has_groups")
public class LootContainerHasGroup {
	public final static String ID_FIELD_NAME = "id";
	public final static String CONTAINER_ID_FIELD_NAME = "container_id";
	public final static String GROUP_ID_FIELD_NAME = "group_id";
	public final static String SPECIAL_FIELD_NAME = "special";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;	
	@DatabaseField(foreign = true, columnName = CONTAINER_ID_FIELD_NAME)
	private LootContainer container;
	@DatabaseField(foreign = true, foreignAutoRefresh=true, columnName = GROUP_ID_FIELD_NAME)
	private LootGroup group;
	@DatabaseField(columnName = "group_weight", canBeNull = false, unique = false, width = 4, defaultValue = "0")
	private Short weight;
	
	/*
	 * min/max  number of items from group
	 */
	@DatabaseField(columnName = "min_items", canBeNull = false, defaultValue = "0")
	private Byte min;
	@DatabaseField(columnName = "max_items", canBeNull = false, defaultValue = "0")
	private Byte max;

	/*
	 * order
	 */
	@DatabaseField(columnName = "ordering", canBeNull = false, defaultValue = "0")
	private Short order;
	
	/*
	 * special - indicates that this group requires special processing. ex. treat group as non-weighted required group
	 * for treasure items like keys.
	 * default value: false
	 */
	@DatabaseField(columnName = "special", canBeNull = false, defaultValue = "0")
	private Boolean special;
	
	/**
	 * Empty constructor as required by ORMLite.
	 */
	public LootContainerHasGroup() {}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LootContainer getContainer() {
		return container;
	}

	public void setContainer(LootContainer container) {
		this.container = container;
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

	/**
	 * @return the min
	 */
	public Byte getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(Byte min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public Byte getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(Byte max) {
		this.max = max;
	}

	/**
	 * @return the order
	 */
	public Short getOrder() {
		return order;
	}

	/**
	 * @param order the order to set
	 */
	public void setOrder(Short order) {
		this.order = order;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LootContainerHasGroup [id=" + id + ", container=" + container + ", group=" + group + ", weight="
				+ weight + ", min=" + min + ", max=" + max + ", order=" + order + "]";
	}

	/**
	 * @return the special
	 */
	public Boolean getSpecial() {
		return special;
	}

	/**
	 * @param special the special to set
	 */
	public void setSpecial(Boolean special) {
		this.special = special;
	}
}

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
@DatabaseTable(tableName = "groups")
public class LootGroup {
	public final static String ID_FIELD_NAME = "id";
	
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	
	@DatabaseField(columnName = "name", canBeNull = false, unique = true, width = 45)
	private String name;
	
	// TODO number of items etc
	
	/**
	 * 
	 */
	public LootGroup() {}

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

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LootGroup [id=" + id + ", name=" + name + "]";
	}
}

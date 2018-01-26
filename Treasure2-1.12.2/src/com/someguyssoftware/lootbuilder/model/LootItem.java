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
@DatabaseTable(tableName = "items")
public class LootItem {
	public final static String ID_FIELD_NAME = "id";
	public static final String MOD_ID_FIELD_NAME = "mod_id";
	
	/*
	 * Database ID field
	 */
	@DatabaseField(generatedId = true, columnName = ID_FIELD_NAME)
	private Integer id;
	
	/*
	 * Database Name field.  Unique.  This is to allow items such as dye0, dye1, dye2, dyeWhite to be included,
	 * whereas the mc_name would all be "dye".
	 */
	@DatabaseField(columnName = "name", canBeNull = false, unique = true, width = 45)
	private String name;
	
	/*
	 * Minecraft name identifier - the key that minecraft uses to identify blocks/items in-game.
	 */
	@DatabaseField(columnName = "mc_name", canBeNull = false, unique = false, width = 45)
	private String mcName;
	
	@DatabaseField
	private Byte damage;
	
	//@DatabaseField
	private String type;
	
	@DatabaseField(foreign = true, columnName = MOD_ID_FIELD_NAME)
	private LootMod mod;
	
	/**
	 * 
	 */
	public LootItem() {}
	
	/**
	 * 
	 * @param name
	 */
	public LootItem(String name) {
		setName(name);
		setDamage((byte) 0);		
	}

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

	public Byte getDamage() {
		return damage;
	}

	public void setDamage(Byte damage) {
		this.damage = damage;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public LootMod getMod() {
		return mod;
	}

	public void setMod(LootMod mod) {
		this.mod = mod;
	}

	public static String getIdFieldName() {
		return ID_FIELD_NAME;
	}

	public static String getModIdFieldName() {
		return MOD_ID_FIELD_NAME;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "LootItem [id=" + id + ", name=" + name + ", mcName=" + mcName + ", damage=" + damage + ", type=" + type
				+ ", mod=" + mod + "]";
	}

	/**
	 * @return the mcName
	 */
	public String getMcName() {
		return mcName;
	}

	/**
	 * @param mcName the mcName to set
	 */
	public void setMcName(String mcName) {
		this.mcName = mcName;
	}
}

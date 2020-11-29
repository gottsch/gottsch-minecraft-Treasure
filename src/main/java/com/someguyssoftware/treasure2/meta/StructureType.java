/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.gottschcore.enums.IEnum;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;
import com.someguyssoftware.gottschcore.meta.IMetaType;

/**
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public enum StructureType implements IMetaType {
	RUIN(0, "ruin"),
	MARKER(1, "marker"),
	WELL(2, "well"),
	ROOM(3, "room");

	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (IMetaType type : EnumSet.allOf(StructureType.class)) {
			codes.put(type.getCode(), type);
			values.put(type.getValue(), type);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	StructureType(Integer code, String value) {
		this.code = code;
		this.value = value;
	}
	
	@Override
	public String getName() {
		return name();
	}
	
	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public void setCode(Integer code) {
		this.code = code;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param code
	 * @return
	 */
	public static StructureType getByCode(Integer code) {
		return (StructureType) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static StructureType getByValue(String value) {
		return (StructureType) values.get(value);
	}

	@Override
	public Map<Integer, IEnum> getCodes() {
		return codes;
	}
	@Override
	public Map<String, IEnum> getValues() {
		return values;
	}
}

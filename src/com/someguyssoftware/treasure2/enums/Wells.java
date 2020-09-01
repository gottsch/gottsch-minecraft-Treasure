/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mark Gottschling on Feb 16, 2018
 *
 */
public enum Wells implements IEnum {
	WISHING_WELL(0, "Wishing Well"),
	@Deprecated CANOPY_WISHING_WELL(1, "Canopy Wishing Well"),
	@Deprecated WOOD_DRAW_WISHING_WELL(2, "Wishing Well");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (Wells ps : EnumSet.allOf(Wells.class)) {
			codes.put(ps.getCode(), ps);
			values.put(ps.getValue(), ps);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	Wells(Integer code, String value) {
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
	public static Wells getByCode(Integer code) {
		return (Wells) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Wells getByValue(String value) {
		return (Wells) values.get(value);
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

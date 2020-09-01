/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Mark Gottschling on Aug 18, 2019
 *
 */
public enum Pearls implements IEnum {
	WHITE(0, "White PearlItem"),
	BLACK(1, "Black PearlItem");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (Pearls p : EnumSet.allOf(Pearls.class)) {
			codes.put(p.getCode(), p);
			values.put(p.getValue(), p);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	Pearls(Integer code, String value) {
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
	public static Pearls getByCode(Integer code) {
		return (Pearls) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Pearls getByValue(String value) {
		return (Pearls) values.get(value);
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

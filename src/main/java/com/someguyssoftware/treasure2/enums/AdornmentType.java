/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * 
 * @author Mark Gottschling on Sep 13, 2014
 *
 */
public enum AdornmentType implements IEnum {
	RING(0, "ring"),
    NECKLACE(1, "necklace"),
    BRACELET(2, "bracelet"),
    POCKET(3, "pocket"),
    EARRING(4, "earring");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (AdornmentType x : EnumSet.allOf(AdornmentType.class)) {
			codes.put(x.getCode(), x);
			values.put(x.getValue(), x);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	AdornmentType(Integer code, String value) {
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
	public static AdornmentType getByCode(Integer code) {
		return (AdornmentType) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static AdornmentType getByValue(String value) {
		return (AdornmentType) values.get(value);
	}

	@Override
	public Map<Integer, IEnum> getCodes() {
		return codes;
	}
	@Override
	public Map<String, IEnum> getValues() {
		return values;
	}
	
	/**
	 * 
	 * @return
	 */
	public static List<String> getNames() {
		List<String> names = EnumSet.allOf(AdornmentType.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
	
	@Override
	public String toString() {
		return super.toString().toLowerCase();
	}
}

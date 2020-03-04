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
 * @author Mark Gottschling on Feb 19, 2018
 *
 */
public enum Pits implements IEnum {
	SIMPLE_PIT(0, "Simple Pit"),
	AIR_PIT(1, "Air Pit"),
	TNT_TRAP_PIT(2, "TNT Trapped Pit"),
	LAVA_TRAP_PIT(3, "Lava Trapped Pit"),
	MOB_TRAP_PIT(4, "Mob Trapped Pit"),
	LAVA_SIDE_TRAP_PIT(5, "Lava Side Trapped Pit"),
	BIG_BOTTOM_MOB_TRAP_PIT(6, "Big Bottom Mob Trapped Pit"),
	COLLAPSING_TRAP_PIT(7, "Collapsing Trapped Pit");
	
//	
//	STRUCTURE_PIT(7, "Structure Pit");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (Pits x : EnumSet.allOf(Pits.class)) {
			codes.put(x.getCode(), x);
			values.put(x.getValue(), x);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	Pits(Integer code, String value) {
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
	public static Pits getByCode(Integer code) {
		return (Pits) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Pits getByValue(String value) {
		return (Pits) values.get(value);
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
		List<String> names = EnumSet.allOf(Pits.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

/**
 * 
 */
package com.someguyssoftware.treasure2.meta;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.enums.IEnum;
import com.someguyssoftware.gottschcore.meta.IMetaArchetype;

/**
 * TODO this is the same as StructureArchetype. May need a common TreasureArchetype
 * @author Mark Gottschling on Aug 30, 2020
 *
 */
public enum ChestArchetype implements IMetaArchetype {
	SURFACE(0, "surface"),
	SUBTERRANEAN(1, "subterranean"),
	SUBMERGED(2, "submerged"),
    FLOAT(3, "float");

	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (IMetaArchetype ps : EnumSet.allOf(ChestArchetype.class)) {
			codes.put(ps.getCode(), ps);
			values.put(ps.getValue(), ps);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	ChestArchetype(Integer code, String value) {
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
	public static ChestArchetype getByCode(Integer code) {
		return (ChestArchetype) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static ChestArchetype getByValue(String value) {
		return (ChestArchetype) values.get(value);
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
		List<String> names = EnumSet.allOf(ChestArchetype.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

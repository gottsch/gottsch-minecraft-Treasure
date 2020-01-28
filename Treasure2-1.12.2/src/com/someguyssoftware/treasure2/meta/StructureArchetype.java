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
 * @author Mark Gottschling on Jul 29, 2019
 *
 */
public enum StructureArchetype implements IMetaArchetype {
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
		for (IMetaArchetype ps : EnumSet.allOf(StructureArchetype.class)) {
			codes.put(ps.getCode(), ps);
			values.put(ps.getValue(), ps);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	StructureArchetype(Integer code, String value) {
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
	public static StructureArchetype getByCode(Integer code) {
		return (StructureArchetype) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static StructureArchetype getByValue(String value) {
		return (StructureArchetype) values.get(value);
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
		List<String> names = EnumSet.allOf(StructureArchetype.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

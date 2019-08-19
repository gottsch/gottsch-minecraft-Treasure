/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.someguyssoftware.gottschcore.enums.IEnum;
import com.someguyssoftware.gottschcore.enums.IRarity;
import com.someguyssoftware.gottschcore.meta.IMetaTheme;
import com.someguyssoftware.treasure2.meta.StructureTheme;

/**
 * @author Mark Gottschling onJan 11, 2018
 *
 */
public enum Rarity implements IRarity {
	COMMON(0, "common"),
	UNCOMMON(1, "uncommon"),
	SCARCE(2, "scarce"),
	RARE(3, "rare"),
	EPIC(4, "epic");//,
//	UNIQUE;
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (IMetaTheme type : EnumSet.allOf(StructureTheme.class)) {
			codes.put(type.getCode(), type);
			values.put(type.getValue(), type);
		}
	}

	
	/**
	 * 
	 * @param value
	 */
	Rarity(int code, String value) {
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
	public static StructureTheme getByCode(Integer code) {
		return (StructureTheme) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static StructureTheme getByValue(String value) {
		return (StructureTheme) values.get(value);
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

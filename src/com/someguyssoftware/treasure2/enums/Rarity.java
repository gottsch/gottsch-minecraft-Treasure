/**
 * 
 */
package com.someguyssoftware.treasure2.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.someguyssoftware.gottschcore.enums.IEnum;
import com.someguyssoftware.gottschcore.enums.IRarity;

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
		for (Rarity type : EnumSet.allOf(Rarity.class)) {
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
	public static Rarity getByCode(Integer code) {
		return (Rarity) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static Rarity getByValue(String value) {
		return (Rarity) values.get(value);
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
		List<String> names = EnumSet.allOf(Rarity.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

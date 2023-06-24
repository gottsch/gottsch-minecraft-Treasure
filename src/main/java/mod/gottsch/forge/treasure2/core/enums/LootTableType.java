package mod.gottsch.forge.treasure2.core.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IEnum;

public enum LootTableType implements ILootTableType {
	CHESTS(0, "chests"),
	WISHABLES(1, "wishables"),
	INJECTS(2, "injects");

	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (LootTableType type : EnumSet.allOf(LootTableType.class)) {
			codes.put(type.getCode(), type);
			values.put(type.getValue(), type);
		}
	}
	
	LootTableType(int code, String value) {
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
	public static ILootTableType getByCode(Integer code) {
		return (ILootTableType) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static ILootTableType getByValue(String value) {
		return (ILootTableType) values.get(value);
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
		List<String> names = EnumSet.allOf(LootTableType.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

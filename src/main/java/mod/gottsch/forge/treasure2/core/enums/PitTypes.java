/**
 * 
 */
package mod.gottsch.forge.treasure2.core.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 
 * @author Mark Gottschling on Jul 15, 2019
 *
 */
public enum PitTypes implements IEnum {
	STANDARD(0, "standard"),
	STRUCTURE(1, "structure"),
	UNKNOWN(-1, "unknown");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (PitTypes x : EnumSet.allOf(PitTypes.class)) {
			codes.put(x.getCode(), x);
			values.put(x.getValue(), x);
		}
	}
	
	// TODO override valueOf();
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	PitTypes(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static PitTypes get(String name) {
		try {
			return valueOf(name);
		}
		catch(Exception e) {
			return PitTypes.UNKNOWN;
		}
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
	public static PitTypes getByCode(Integer code) {
		return (PitTypes) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static PitTypes getByValue(String value) {
		return (PitTypes) values.get(value);
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
		List<String> names = EnumSet.allOf(PitTypes.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

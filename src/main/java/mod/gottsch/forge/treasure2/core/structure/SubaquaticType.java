/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Treasure2.  If not, see <http://www.gnu.org/licenses/lgpl>.
 */
package mod.gottsch.forge.treasure2.core.structure;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IEnum;

/**
 * 
 * @author Mark Gottschling on May 15, 2023
 *
 */
public enum SubaquaticType implements ISubaquaticType {
	NONE(-1, "none"),
	SHALLOW(0, "shallow");

	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (ISubaquaticType type : EnumSet.allOf(SubaquaticType.class)) {
			codes.put(type.getCode(), type);
			values.put(type.getValue(), type);
		}
	}
	
	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	SubaquaticType(Integer code, String value) {
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
	public static SubaquaticType getByCode(Integer code) {
		return (SubaquaticType) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static SubaquaticType getByValue(String value) {
		return (SubaquaticType) values.get(value);
	}

	@Override
	public Map<Integer, IEnum> getCodes() {
		return codes;
	}
	@Override
	public Map<String, IEnum> getValues() {
		return values;
	}
	
	public static List<String> getNames() {
		List<String> names = EnumSet.allOf(SubaquaticType.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

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
package mod.gottsch.forge.treasure2.core.world.feature;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import mod.gottsch.forge.gottschcore.enums.IEnum;

/**
 * TODO this really only applies to Chest Features - rename and remove well.
 * @author Mark Gottschling May 12, 2023
 *
 */
public enum FeatureType implements IFeatureType {
	UNKNOWN(-1, "unknown"),
	TERRANEAN(0, "terrestrial"),
	AQUATIC(1, "aquatic"), 
	WELL(2, "well");
	
	private static final Map<Integer, IEnum> codes = new HashMap<Integer, IEnum>();
	private static final Map<String, IEnum> values = new HashMap<String, IEnum>();
	private Integer code;
	private String value;
	
	// setup reverse lookup
	static {
		for (FeatureType x : EnumSet.allOf(FeatureType.class)) {
			codes.put(x.getCode(), x);
			values.put(x.getValue(), x);
		}
	}

	/**
	 * Full constructor
	 * @param code
	 * @param value
	 */
	FeatureType(Integer code, String value) {
		this.code = code;
		this.value = value;
	}

	public static FeatureType get(String name) {
		try {
			return valueOf(name);
		}
		catch(Exception e) {
			return FeatureType.UNKNOWN;
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
	public static IFeatureType getByCode(Integer code) {
		return (IFeatureType) codes.get(code);
	}
	/**
	 * 
	 * @param value
	 * @return
	 */
	public static IFeatureType getByValue(String value) {
		return (IFeatureType) values.get(value);
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
		List<String> names = EnumSet.allOf(FeatureType.class).stream().map(x -> x.name()).collect(Collectors.toList());
		return names;
	}
}

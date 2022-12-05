/*
 * This file is part of  Treasure2.
 * Copyright (c) 2020 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.generator.chest;

import java.util.Map;

import mod.gottsch.forge.gottschcore.enums.IEnum;

/**
 * @author Mark Gottschling on Sep 4, 2020
 *
 */
public enum ChestGeneratorType implements IChestGeneratorType {
	COMMON,
	UNCOMMON,
	SCARCE,
	RARE,
	EPIC,
	LEGENDARY,
	MYTHICAL,
	WITHER,
	SKULL,
	GOLD_SKULL,
	CRYSTAL_SKULL,
	CAULDRON;

	@Override
	public String getName() {
		return name();
	}
	
	@Override
	public Integer getCode() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<Integer, IEnum> getCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, IEnum> getValues() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setCode(Integer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setValue(String arg0) {
		// TODO Auto-generated method stub
		
	}
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2021, Mark Gottschling (gottsch)
 * 
 * All rights reserved.
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
package com.someguyssoftware.treasure2.capability;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author Mark Gottschling on Sep 6, 2020
 *
 */
public class DurabilityCapability implements IDurabilityCapability  {
	private int durability;
	
	@CapabilityInject(IDurabilityCapability.class)
	public static Capability<IDurabilityCapability> DURABILITY_CAPABILITY = null;
	
	public static void register() {
		Treasure.LOGGER.debug("register the fucking capability already!");
        CapabilityManager.INSTANCE.register(IDurabilityCapability.class, new DurabilityCapabilityStorage(), DurabilityCapability::new);
    }
	
	/**
	 * 
	 */
	public DurabilityCapability() {
		
	}
	
	@Override
	public int getDurability() {
		return durability;
	}
	
	@Override
	public void setDurability(int durabilityIn) {
		if (durabilityIn > IDurabilityCapability.MAX_DURABILITY) {
            this.durability = IDurabilityCapability.MAX_DURABILITY;
        }
        else {
            this.durability = durabilityIn;
        }
	}
}
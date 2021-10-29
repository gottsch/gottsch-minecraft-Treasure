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

import java.util.ArrayList;
import java.util.List;

import com.someguyssoftware.treasure2.charm.ICharmEntity;

/**
 * The CharmInventoryCapability provides any item with a Charm Inventory, which is a datatype of List<CharmEntity> much like the current CharmCapability.
 * This class is a transitionary class from original 1.12.2 charm capabilities and back-porting of 1.16.5 charm capabilities.
 * @author Mark Gottschling on Oct 25, 2021
 *
 */
public class CharmInventoryCapability implements ICharmInventoryCapability {
	private List<ICharmEntity> charmEntities = new ArrayList<>(3);
	private int slots;

	@Override
	public List<ICharmEntity> getCharmEntities() {
		return charmEntities;
	}

	@Override
	public void setCharmEntities(List<ICharmEntity> charmEntities) {
		this.charmEntities = charmEntities;
	}

	@Override
	public int getSlots() {
		return slots;
	}

	@Override
	public void setSlots(int slots) {
		this.slots = slots;
	}

}

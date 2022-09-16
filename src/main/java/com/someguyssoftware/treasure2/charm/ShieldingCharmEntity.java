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
package com.someguyssoftware.treasure2.charm;

import com.someguyssoftware.treasure2.Treasure;

import net.minecraft.nbt.CompoundNBT;

/**
 * 
 * @author Mark Gottschling on Jan 13, 2022
 *
 */
public class ShieldingCharmEntity extends CharmEntity implements ICooldownCharmEntity {
	// TODO will have to send message to client for this value (will be displayed in gui)
	private double cooldownEnd;

	public ShieldingCharmEntity(ShieldingCharm charm) {
		super(charm);
	}
	
	public ShieldingCharmEntity(ShieldingCharmEntity entity) {
		super(entity);
		setCooldownEnd(entity.getCooldownEnd());
	}
	
	@Override
	public void update(ICharmEntity entity) {
		super.update(entity);
		setCooldownEnd(((ICooldownCharmEntity)entity).getCooldownEnd());
	}
	
	/**
	 * 
	 */
	@Override
	public boolean load(CompoundNBT nbt) {
		super.load(nbt);		
		if (nbt.contains("cooldownEnd")) {
			setCooldownEnd(nbt.getDouble("cooldownEnd"));
		}		
		return true;
	}
	
	/**
	 * 
	 * @param tag
	 * @return
	 */
	@Override
	public CompoundNBT save(CompoundNBT nbt) {
		nbt = super.save(nbt);
		try {
			// save the entity data
			nbt.putDouble("cooldownEnd", getCooldownEnd());
		}
		catch(Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}
	
	@Override
	public double getCooldownEnd() {
		return cooldownEnd;
	}

	@Override
	public void setCooldownEnd(double cooldownEnd) {
		this.cooldownEnd = cooldownEnd;
	}	
}

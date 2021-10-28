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

import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Oct 26, 2021
 *
 */
public class CharmInventoryCapabilityStorage implements Capability.IStorage<ICharmInventoryCapability> {
	private static final String SLOTS_TAG = "slots";
	private static final String CHARMS_TAG ="charms";

	@Override
	public NBTBase writeNBT(Capability<ICharmInventoryCapability> capability, ICharmInventoryCapability instance,
			EnumFacing side) {

		NBTTagCompound nbt = new NBTTagCompound();
		try {
			/*
			 * save charms
			 */
			// create a new list nbt for each inventory type
			NBTTagList listNbt = new NBTTagList();
			for (ICharmEntity entity : instance.getCharmEntities()) {
				NBTTagCompound entityNbt = new NBTTagCompound();
				listNbt.appendTag(entity.save(entityNbt));						
			}
			nbt.setTag(CHARMS_TAG, listNbt);

			/*
			 * save charm cap properties
			 */
			nbt.setInteger(SLOTS_TAG, instance.getSlots());

		} catch (Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICharmInventoryCapability> capability, ICharmInventoryCapability instance, EnumFacing side,
			NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;

			// load the charm entities
			if (tag.hasKey(CHARMS_TAG)) {
				NBTTagList charmsTag = tag.getTagList(CHARMS_TAG, 10);
				charmsTag.forEach(e -> {
					/*
					 * load the charm first.
					 * need to the load the charm prior to CharmEntity.load() because the CharmEntity instance needs to be
					 *  created first using the Charm, then the entity can be loaded.
					 */
					Optional<ICharm> charm = Charm.load((NBTTagCompound) ((NBTTagCompound)e).getCompoundTag(ICharmEntity.CHARM));
					if (!charm.isPresent()) {
						return;
					}
					// create an entity
					ICharmEntity entity = charm.get().createEntity();
					// load entity
					entity.load((NBTTagCompound)e);
					// add the entity to the list
					instance.getCharmEntities().add(entity);
				});
				
				// load cap properties
				if (tag.hasKey(SLOTS_TAG)) {
					int slots = tag.getInteger(SLOTS_TAG);
					instance.setSlots(slots);
				}
			}
		} else {
			Treasure.logger.warn("Not a tag compound!");
		}
	}
}

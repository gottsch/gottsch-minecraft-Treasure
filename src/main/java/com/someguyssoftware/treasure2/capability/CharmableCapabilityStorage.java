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

import java.util.List;
import java.util.Optional;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.MagicsInventoryCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.util.ResourceLocationUtil;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Aug 14, 2021
 *
 */
public class CharmableCapabilityStorage implements Capability.IStorage<ICharmableCapability> {
	private static final String SOURCE = "source";
	private static final String EXECUTING = "executing";
	private static final String BINDABLE = "bindable";
	private static final String INNATE = "innate";	
	private static final String IMBUABLE = "imbuable";
	private static final String IMBUING = "imbuing";	
	private static final String SOCKETABLE = "socketable";	
	private static final String BASE_MATERIAL = "baseMaterial";
	private static final String SOURCE_ITEM = "sourceItem";
	private static final String MAX_CHARM_LEVEL = "maxCharmLevel";
	private static final String NAMED_BY_MATERIAL = "namedByMaterial";
	private static final String NAMED_BY_CHARM = "namedByCharm";

	@Override
	public NBTBase writeNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		try {
			/*
			 * save charms
			 */
			// create a new list nbt for each inventory type
			for (InventoryType type : InventoryType.values()) {
				List<ICharmEntity> entityList = (List<ICharmEntity>) instance.getCharmEntities().get(type);
				if (entityList != null && !entityList.isEmpty()) {
					NBTTagList listNbt = new NBTTagList();
					for (ICharmEntity entity : entityList) {
						NBTTagCompound entityNbt = new NBTTagCompound();
						listNbt.appendTag(entity.save(entityNbt));						
					}
					nbt.setTag(type.name(), listNbt);
				}
			}

			/*
			 * save charm cap properties
			 */
			nbt.setBoolean(SOURCE, instance.isSource());
			nbt.setBoolean(EXECUTING, instance.isExecuting());;
			nbt.setBoolean(BINDABLE, instance.isBindable());			
			nbt.setBoolean(INNATE, instance.isInnate());			
			nbt.setBoolean(IMBUABLE, instance.isImbuable());
			nbt.setBoolean(IMBUING, instance.isImbuing());
			
			nbt.setBoolean(SOCKETABLE, instance.isSocketable());
			nbt.setString(BASE_MATERIAL, instance.getBaseMaterial().toString());
			nbt.setString(SOURCE_ITEM, instance.getSourceItem().toString());
			nbt.setBoolean(NAMED_BY_MATERIAL, instance.isNamedByMaterial());	
			nbt.setBoolean(NAMED_BY_CHARM, instance.isNamedByCharm());

		} catch (Exception e) {
			Treasure.logger.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, EnumFacing side, NBTBase nbt) {

		if (nbt instanceof NBTTagCompound) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			
			for (InventoryType type : InventoryType.values()) {
				// clear the list
				instance.getCharmEntities().get(type).clear();
				
				// load the charm entities
				if (tag.hasKey(type.name())) {
					NBTTagList listNbt = tag.getTagList(type.name(), 10);
					listNbt.forEach(e -> {
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
						instance.getCharmEntities().get(type).add(entity);
					});
					
					// load cap properties
					if (tag.hasKey(SOURCE)) {
						instance.setSource(tag.getBoolean(SOURCE));
					}
					if (tag.hasKey(EXECUTING)) {
						instance.setExecuting(tag.getBoolean(EXECUTING));
					}
					
					if (tag.hasKey(BINDABLE)) {
						instance.setBindable(tag.getBoolean(BINDABLE));
					}
					
					if (tag.hasKey(INNATE)) {
						instance.setInnate(tag.getBoolean(INNATE));
					}				
					
					if (tag.hasKey(IMBUABLE)) {
						instance.setImbuable(tag.getBoolean(IMBUABLE));
					}				

					if (tag.hasKey(IMBUING)) {
						instance.setImbuing(tag.getBoolean(IMBUING));
					}	
					
					if (tag.hasKey(SOCKETABLE)) {
						instance.setSocketable(tag.getBoolean(SOCKETABLE));
					}				

					if (tag.hasKey(BASE_MATERIAL)) {
						instance.setBaseMaterial(ResourceLocationUtil.create(tag.getString(BASE_MATERIAL)));
					}
					
					if (tag.hasKey(SOURCE_ITEM)) {
						instance.setSourceItem(ResourceLocationUtil.create(tag.getString(SOURCE_ITEM)));
					}
					
					if (tag.hasKey(NAMED_BY_MATERIAL)) {
						instance.setNamedByMaterial(tag.getBoolean(NAMED_BY_MATERIAL));
					}
					if (tag.hasKey(NAMED_BY_CHARM)) {
						instance.setNamedByCharm(tag.getBoolean(NAMED_BY_CHARM));
					}
				}
			}
		} else {
			Treasure.logger.warn("Not a tag compound!");
		}
	}
}

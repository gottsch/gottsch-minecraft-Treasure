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
import com.someguyssoftware.treasure2.capability.CharmableCapability.InventoryType;
import com.someguyssoftware.treasure2.charm.BaseMaterial2;
import com.someguyssoftware.treasure2.charm.Charm;
import com.someguyssoftware.treasure2.charm.CharmableMaterial;
import com.someguyssoftware.treasure2.charm.ICharm;
import com.someguyssoftware.treasure2.charm.ICharmEntity;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

/**
 * 
 * @author Mark Gottschling on Aug 14, 2021
 *
 */
public class CharmableCapabilityStorage implements Capability.IStorage<ICharmableCapability> {
	
	private static final String BINDABLE = "bindable";
	private static final String INNATE = "innate";
	private static final String MAX_INNATE_SIZE = "maxInnateSize";
	private static final String IMBUABLE = "imbuable";
	private static final String IMBUING = "imbuing";
	private static final String MAX_IMBUE_SIZE = "maxImbueSize";
	private static final String SOCKETABLE = "socketable";
	private static final String SOCKETING = "socketing";
	private static final String MAX_SOCKET_SIZE = "maxSocketSize";
	private static final String BASE_MATERIAL = "baseMaterial";
	private static final String SOURCE_ITEM = "sourceItem";
	private static final String MAX_CHARM_LEVEL = "maxCharmLevel";
	private static final String CHARM = "charm";
	
	@Override
	public INBT writeNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		try {
			/*
			 * save charm cap inventories
			 */
			// create a new list nbt for each inventory type
			for (int index = 0; index < instance.getCharmEntities().length; index++) {
				List<ICharmEntity> entityList = instance.getCharmEntities()[index];
				if (entityList != null && !entityList.isEmpty()) {
					ListNBT listNbt = new ListNBT();
					for (ICharmEntity entity : entityList) {
						CompoundNBT entityNbt = new CompoundNBT();
						listNbt.add(entity.save(entityNbt));						
					}
					nbt.put(InventoryType.getByValue(index).name(), listNbt);
				}
			}
			
			/*
			 * save charm cap properties
			 */
			nbt.putBoolean(BINDABLE, instance.isBindable());
			
			nbt.putBoolean(INNATE, instance.isInnate());
			nbt.putInt(MAX_INNATE_SIZE, instance.getMaxInnateSize());
			
			nbt.putBoolean(IMBUABLE, instance.isImbuable());
			nbt.putBoolean(IMBUING, instance.isImbuing());			
			nbt.putInt(MAX_IMBUE_SIZE, instance.getMaxImbueSize());
			
			nbt.putBoolean(SOCKETABLE, instance.isSocketable());
			nbt.putBoolean(SOCKETING, instance.isSocketing());
			nbt.putInt(MAX_SOCKET_SIZE, instance.getMaxSocketsSize());
			nbt.putString(BASE_MATERIAL, instance.getBaseMaterial().toString());
			nbt.putString(SOURCE_ITEM, instance.getSourceItem().toString());
			
		} catch (Exception e) {
			Treasure.LOGGER.error("Unable to write state to NBT:", e);
		}
		return nbt;
	}

	@Override
	public void readNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, Direction side,
			INBT nbt) {
		if (nbt instanceof CompoundNBT) {
			CompoundNBT tag = (CompoundNBT) nbt;
			for (InventoryType type : InventoryType.values()) {
				// clear the list
				instance.getCharmEntities()[type.getValue()].clear();				
				/*
				 *  load the list
				 */
				if (tag.contains(type.name())) {
					ListNBT listNbt = tag.getList(type.name(), 10);
					listNbt.forEach(e -> {
						// load the charm
						Optional<ICharm> charm = Charm.load((CompoundNBT) ((CompoundNBT)e).get(CHARM));
						if (!charm.isPresent()) {
							return;
						}
						// create an entity
						ICharmEntity entity = charm.get().createEntity();
						
						// load entity
						entity.load((CompoundNBT)e);
//						Optional<ICharmEntity> entity = ICharmEntity.load((CompoundNBT)e);
//						if (!entity.isPresent()) {
//							return;
//						}
						
						// add the entity to the list
//						instance.getCharmEntities()[type.getValue()].add(entity.get());
						instance.getCharmEntities()[type.getValue()].add(entity);
					});
				}
				
				// load cap properties
				if (tag.contains(BINDABLE)) {
					instance.setBindable(tag.getBoolean(BINDABLE));
				}
				
				if (tag.contains(INNATE)) {
					instance.setInnate(tag.getBoolean(INNATE));
				}				
				if (tag.contains(MAX_INNATE_SIZE)) {
					instance.setMaxInnateSize(tag.getInt(MAX_INNATE_SIZE));
				}
				
				if (tag.contains(IMBUABLE)) {
					instance.setImbuable(tag.getBoolean(IMBUABLE));
				}				
				if (tag.contains(MAX_IMBUE_SIZE)) {
					instance.setMaxImbueSize(tag.getInt(MAX_IMBUE_SIZE));
				}
				if (tag.contains(IMBUING)) {
					instance.setImbuing(tag.getBoolean(IMBUING));
				}	
				
				if (tag.contains(SOCKETABLE)) {
					instance.setSocketable(tag.getBoolean(SOCKETABLE));
				}				
				if (tag.contains(MAX_SOCKET_SIZE)) {
					instance.setMaxSocketsSize(tag.getInt(MAX_SOCKET_SIZE));
				}
				if (tag.contains(SOCKETING)) {
					instance.setSocketing(tag.getBoolean(SOCKETING));
				}	
				if (tag.contains(BASE_MATERIAL)) {
//					Optional<BaseMaterial2> material = TreasureCharms.getBaseMaterial(ModUtils.asLocation(tag.getString(BASE_MATERIAL)));
//					Optional<CharmableMaterial> material = TreasureCharms.getBaseMaterial(ModUtils.asLocation(tag.getString(BASE_MATERIAL)));
					instance.setBaseMaterial(ModUtils.asLocation(tag.getString(BASE_MATERIAL))); //BaseMaterial.valueOf(tag.getString(BASE_MATERIAL).toUpperCase()));
				}
				
				if (tag.contains(SOURCE_ITEM)) {
					instance.setSourceItem(ModUtils.asLocation(tag.getString(SOURCE_ITEM)));
				}
			}
		}
	}
}

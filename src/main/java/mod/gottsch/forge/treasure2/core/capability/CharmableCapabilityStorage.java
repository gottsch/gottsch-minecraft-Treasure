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
package mod.gottsch.forge.treasure2.core.capability;

import java.util.List;
import java.util.Optional;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.capability.modifier.ILevelModifier;
import mod.gottsch.forge.treasure2.core.charm.Charm;
import mod.gottsch.forge.treasure2.core.charm.ICharm;
import mod.gottsch.forge.treasure2.core.charm.ICharmEntity;
import mod.gottsch.forge.treasure2.core.util.ModUtils;
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
	private static final String LEVEL_MODIFIER = "levelModifier";
	
	private static final String MAX_SOCKET_SIZE = "maxSocketSize";
	
	@Override
	public INBT writeNBT(Capability<ICharmableCapability> capability, ICharmableCapability instance, Direction side) {
		CompoundNBT nbt = new CompoundNBT();
		try {
			/*
			 * save charm cap inventories
			 */
			// create a new list nbt for each inventory type
			for (InventoryType type : InventoryType.values()) {
				List<ICharmEntity> entityList = (List<ICharmEntity>) instance.getCharmEntities().get(type);
				if (entityList != null && !entityList.isEmpty()) {
					ListNBT listNbt = new ListNBT();
					for (ICharmEntity entity : entityList) {
						CompoundNBT entityNbt = new CompoundNBT();
						listNbt.add(entity.save(entityNbt));						
					}
					nbt.put(type.name(), listNbt);
				}
			}
			
			/*
			 * save charm cap properties
			 */
			nbt.putBoolean(SOURCE, instance.isSource());
			nbt.putBoolean(EXECUTING, instance.isExecuting());;
			nbt.putBoolean(BINDABLE, instance.isBindable());			
			nbt.putBoolean(INNATE, instance.isInnate());			
			nbt.putBoolean(IMBUABLE, instance.isImbuable());
			nbt.putBoolean(IMBUING, instance.isImbuing());

			nbt.putBoolean(SOCKETABLE, instance.isSocketable());
			nbt.putString(BASE_MATERIAL, instance.getBaseMaterial().toString());
			nbt.putString(SOURCE_ITEM, instance.getSourceItem().toString());
			nbt.putBoolean(NAMED_BY_MATERIAL, instance.isNamedByMaterial());	
			nbt.putBoolean(NAMED_BY_CHARM, instance.isNamedByCharm());
			nbt.putString(LEVEL_MODIFIER, instance.getLevelModifier().getClass().getName());
			nbt.putInt(MAX_SOCKET_SIZE, instance.getMaxSocketSize());
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
				instance.getCharmEntities().get(type).clear();
				
				// load the charm entities
				if (tag.contains(type.name())) {
					ListNBT listNbt = tag.getList(type.name(), 10);
					listNbt.forEach(e -> {
						/*
						 * load the charm first.
						 * need to the load the charm prior to CharmEntity.load() because the CharmEntity instance needs to be
						 *  created first using the Charm, then the entity can be loaded.
						 */
						Optional<ICharm> charm = Charm.load((CompoundNBT) ((CompoundNBT)e).getCompound(ICharmEntity.CHARM));
						if (!charm.isPresent()) {
							return;
						}
						// create an entity
						ICharmEntity entity = charm.get().createEntity();
						// load entity
						entity.load((CompoundNBT)e);
						// add the entity to the list
						instance.getCharmEntities().get(type).add(entity);
					});
				}
				
				// load cap properties
				if (tag.contains(SOURCE)) {
					instance.setSource(tag.getBoolean(SOURCE));
				}
				if (tag.contains(EXECUTING)) {
					instance.setExecuting(tag.getBoolean(EXECUTING));
				}
				
				if (tag.contains(BINDABLE)) {
					instance.setBindable(tag.getBoolean(BINDABLE));
				}
				
				if (tag.contains(INNATE)) {
					instance.setInnate(tag.getBoolean(INNATE));
				}				
				
				if (tag.contains(IMBUABLE)) {
					instance.setImbuable(tag.getBoolean(IMBUABLE));
				}				

				if (tag.contains(IMBUING)) {
					instance.setImbuing(tag.getBoolean(IMBUING));
				}	
				
				if (tag.contains(SOCKETABLE)) {
					instance.setSocketable(tag.getBoolean(SOCKETABLE));
				}				
				
				if (tag.contains(SOURCE_ITEM)) {
					instance.setSourceItem(ModUtils.asLocation(tag.getString(SOURCE_ITEM)));
				}
				
				if (tag.contains(NAMED_BY_MATERIAL)) {
					instance.setNamedByMaterial(tag.getBoolean(NAMED_BY_MATERIAL));
				}
				if (tag.contains(NAMED_BY_CHARM)) {
					instance.setNamedByCharm(tag.getBoolean(NAMED_BY_CHARM));
				}
				
				if (tag.contains(LEVEL_MODIFIER)) {
					try {
						ILevelModifier levelModifier = (ILevelModifier) Class.forName(tag.getString(LEVEL_MODIFIER)).newInstance();
						instance.setLevelModifier(levelModifier);
					}
					catch(Exception e) {
						Treasure.LOGGER.warn("unable to create level modifier -> {}", tag.getString(LEVEL_MODIFIER));
					}
				}
				if (tag.contains(MAX_SOCKET_SIZE)) {
					instance.setMaxSocketSize(tag.getInt(MAX_SOCKET_SIZE));
				}
			}
		}
	}
}

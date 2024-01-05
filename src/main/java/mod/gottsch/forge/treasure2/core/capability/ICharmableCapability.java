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

import com.google.common.collect.Multimap;

import mod.gottsch.forge.treasure2.core.capability.modifier.ILevelModifier;
import mod.gottsch.forge.treasure2.core.charm.ICharm;
import mod.gottsch.forge.treasure2.core.charm.ICharmEntity;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

/**
 * CharmableCapability is a capability that has an ICharmEntity inventory
 * 
 * @author Mark Gottschling on Apr 27, 2020
 * @version 2.0
 * @since Aug 31, 2022
 *
 */
public interface ICharmableCapability {
	
	Multimap<InventoryType, ICharmEntity> getCharmEntities();
	
	boolean isCharmed();

	public void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag);
	void appendHoverText(ItemStack stack, World world, List<ITextComponent> tooltip, ITooltipFlag flag, InventoryType inventoryType, boolean titleFlag);
	
	/**
	 * 
	 * @param charm
	 * @return
	 */
	boolean contains(ICharm charm);

	/**
	 * Convenience method.
	 * @param type
	 * @param entity
	 */
	void add(InventoryType type, ICharmEntity entity);

	/**
	 * 
	 * @param type
	 * @param index
	 */
	void remove(InventoryType type, int index);
	
	/**
	 * Convenience method
	 * @param type
	 * @return
	 */
	int getMaxSize(InventoryType type);

	int getCurrentSize(InventoryType type);

	public boolean isSource();
	public void setSource(boolean source);
	
	public boolean isBindable();
	public void setBindable(boolean bindable);
	
	public boolean isInnate();
	public void setInnate(boolean innate);
	
	public boolean isImbuable();
	public void setImbuable(boolean imbue);
	
	public boolean isImbuing();
	public void setImbuing(boolean imbuing);
	
	public boolean isSocketable();
	public void setSocketable(boolean socketable);

	boolean isExecuting();
	public void setExecuting(boolean executing);
	
	ResourceLocation getBaseMaterial();
	void setBaseMaterial(ResourceLocation baseMaterial);
	
	ResourceLocation getSourceItem();
	void setSourceItem(ResourceLocation sourceItem);
	
	boolean isNamedByMaterial();
	void setNamedByMaterial(boolean namedByMaterial);
	
	boolean isNamedByCharm();
	void setNamedByCharm(boolean namedByCharm);

	/**
	 * 
	 */
	int getMaxCharmLevel();
	
	// wrapper methods
	void setMaxSocketSize(int size);
	int getMaxSocketSize();

	int getMaxImbueSize();

	int getMaxInnateSize();

	int getSocketSize();
	void addMaxSocketSize(int increment);

	int getImbueSize();

	int getInnateSize();

	ICharmEntity getHighestLevel();

	void setHighestLevel(ICharmEntity highestLevel);

	ILevelModifier getLevelModifier();

	void setLevelModifier(ILevelModifier levelModifier);

	void clearCharms();

	ITextComponent getCapacityHoverText(ItemStack stack, World world, InventoryType type);

	void transferTo(ItemStack dest, InventoryType sourceType, InventoryType destType);

	void copyTo(ItemStack source);

	boolean hasCharmType(ItemStack source, ItemStack dest, InventoryType sourceType, InventoryType destType);

}
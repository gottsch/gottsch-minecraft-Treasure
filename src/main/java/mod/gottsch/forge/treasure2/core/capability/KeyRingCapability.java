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
package mod.gottsch.forge.treasure2.core.capability;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;

/**
 * @author Mark Gottschling on May 12, 2020
 *
 */
public class KeyRingCapability implements ICapabilitySerializable<CompoundTag> {
	public static final ResourceLocation ID = new ResourceLocation(Treasure.MODID, "key_ring");
	public static final int INVENTORY_SIZE = 14;
	
	private static final String INVENTORY_TAG = "inventory";
	private static final String DATA_TAG = "data";
	
	// reference of handler/data for easy access
	private final KeyRingHandler dataHandler = new KeyRingHandler();
	private final ItemStackHandler itemHandler = new ItemStackHandler(INVENTORY_SIZE);

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == TreasureCapabilities.KEY_RING) {
			return  LazyOptional.of(() -> dataHandler).cast();
		}
		if (cap ==ForgeCapabilities.ITEM_HANDLER) {
			return LazyOptional.of(() -> itemHandler).cast();
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		CompoundTag dataTag = dataHandler.save();
		CompoundTag itemTag = itemHandler.serializeNBT();
		tag.put(DATA_TAG, dataTag);
		tag.put(INVENTORY_TAG, itemTag);
		return tag;
	}
	
	@Override
	public void deserializeNBT(CompoundTag tag) {
		dataHandler.load((CompoundTag) tag.get(DATA_TAG));
		itemHandler.deserializeNBT((CompoundTag) tag.get(INVENTORY_TAG));
	}
	
	/**
	 * 
	 * @param event
	 */
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IKeyRingHandler.class);
	}
}

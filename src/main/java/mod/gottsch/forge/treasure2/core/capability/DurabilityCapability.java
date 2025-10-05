/*
 * This file is part of Treasure2.
 * Copyright (c) 2025 Mark Gottschling (gottsch)
 *
 * Treasure2 is free software: you can redistribute it and/or modify
 * it under the terms of the Open Software Licence 3.0.
 *
 * Treasure2 is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * Open Software Licence 3.0 for more details.
 *
 * You should have received a copy of the Open Software Licence
 * along with Treasure2. If not, see <https://www.tldrlegal.com/license/open-software-licence-3-0>.
 */
package mod.gottsch.forge.treasure2.core.capability;

import mod.gottsch.forge.treasure2.Treasure;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;

/**
 * 
 * @author Mark Gottschling on Nov 9, 2022
 *
 */
public class DurabilityCapability implements ICapabilitySerializable<CompoundTag> {
	public static final ResourceLocation ID = new ResourceLocation(Treasure.MODID, "durability");
	
	// reference of handler/data for easy access
	private final IDurabilityHandler handler;
	// holder of the handler/data
//	private final LazyOptional<IDurabilityHandler> optional = LazyOptional.of(() -> handler);

	public DurabilityCapability() {
		handler  = new DurabilityHandler();
	}

	public DurabilityCapability(IDurabilityHandler handler) {
		this.handler = handler;
	}

	/**
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if (cap == TreasureCapabilities.DURABILITY) {
			return (LazyOptional<T>) LazyOptional.of(() -> handler);
		}
		return LazyOptional.empty();
	}
	
	@Override
	public CompoundTag serializeNBT() {
		return handler.save();
	}
		
	@Override
	public void deserializeNBT(CompoundTag tag) {
		handler.load(tag);
	}

	/**
	 * 
	 * @param event
	 */
	public static void register(RegisterCapabilitiesEvent event) {
		event.register(IDurabilityHandler.class);
	}
}

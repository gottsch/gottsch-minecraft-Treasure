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
package com.someguyssoftware.treasure2.init;

import java.util.Optional;

import com.google.common.util.concurrent.AtomicDouble;
import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.capability.CharmableCapability;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.CharmableMaterial;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.data.TreasureData;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootFunctions;
import com.someguyssoftware.treasure2.loot.TreasureLootTableRegistry;
import com.someguyssoftware.treasure2.registry.TreasureDecayRegistry;
import com.someguyssoftware.treasure2.registry.TreasureMetaRegistry;
import com.someguyssoftware.treasure2.registry.TreasureTemplateRegistry;
import com.someguyssoftware.treasure2.util.ModUtils;

import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * TODO probably get rid of this class and place back into main Treasure class
 * @author Mark Gottschling on Jan 5, 2021
 *
 */
public class TreasureSetup implements IModSetup {
	/**
	 * 
	 * @param event
	 */
	public static void common(final FMLCommonSetupEvent event) {
		Treasure.LOGGER.debug("registering in TreasureSetup");
		// add mod specific logging
		IModSetup.addRollingFileAppender(Treasure.instance.getName(), null);

		// regsiter functions
		TreasureLootFunctions.register();
		
		// register capabilities
		TreasureCapabilities.register();

		// initialize all the data lists, maps, etc
		TreasureData.initialize();

		// start the treasure registries
		TreasureLootTableRegistry.create(Treasure.instance);
		TreasureMetaRegistry.create(Treasure.instance);
		TreasureTemplateRegistry.create(Treasure.instance);
		TreasureDecayRegistry.create(Treasure.instance);
	}

	/**
	 * 
	 * @param event
	 */
	public static void clientSetup(final FMLClientSetupEvent event) {
		Treasure.LOGGER.debug("setting up item properties dynamically...");
		
		// create the gem property getter
		IItemPropertyGetter gemGetter = (stack, world, living) -> {
			AtomicDouble d = new AtomicDouble(0);
			stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
				Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
				if (source.isPresent()) {
					d.set(source.get().getId());
				}
			});
			return d.floatValue();
		};
		
		event.enqueueWork(() -> {
			ItemModelsProperties.register(TreasureItems.COPPER_CHARM, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			ItemModelsProperties.register(TreasureItems.SILVER_CHARM, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			ItemModelsProperties.register(TreasureItems.GOLD_CHARM, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			ItemModelsProperties.register(TreasureItems.COPPER_RING, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			ItemModelsProperties.register(TreasureItems.SILVER_RING, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			ItemModelsProperties.register(TreasureItems.GOLD_RING, 
					new ResourceLocation(Treasure.MODID, "gem"), (stack, world, living) -> {
						AtomicDouble d = new AtomicDouble(0);
						stack.getCapability(TreasureCapabilities.CHARMABLE).ifPresent(cap -> {
							Optional<CharmableMaterial> source = TreasureCharms.getSourceItem(cap.getSourceItem());
							if (source.isPresent()) {
								d.set(source.get().getId());
							}
						});
						return d.floatValue();
					});
			
			ItemModelsProperties.register(TreasureItems.COPPER_NECKLACE, new ResourceLocation(Treasure.MODID, "gem"), gemGetter);
			ItemModelsProperties.register(TreasureItems.SILVER_NECKLACE, new ResourceLocation(Treasure.MODID, "gem"), gemGetter);
			ItemModelsProperties.register(TreasureItems.GOLD_NECKLACE, new ResourceLocation(Treasure.MODID, "gem"), gemGetter);
		});
	}
}

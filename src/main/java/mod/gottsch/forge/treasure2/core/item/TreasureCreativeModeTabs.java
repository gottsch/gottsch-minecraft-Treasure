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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling Feb 17, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureCreativeModeTabs {
	
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Treasure.MODID);
	
	public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register("treasure_tab", 
			() -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroup.treasure2"))
			.icon(TreasureItems.LOGO.get()::getDefaultInstance)
			.displayItems((displayParams, output) -> {
				// add all items
				Registration.ITEMS.getEntries().forEach(item -> {
					if (!TreasureItems.TAB_IGNORE.contains(item)) {
						output.accept(item.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
					}
				});
			})
			.build()
			);
}

/*
 * This file is part of  Treasure2.
 * Copyright (c) 2023 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.item;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.core.setup.Registration;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab.TabVisibility;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 
 * @author Mark Gottschling Feb 17, 2023
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TreasureCreativeModeTabs {
//	public static CreativeModeTab MOD_TAB;
	
	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Treasure.MODID);
	
	public static final RegistryObject<CreativeModeTab> MOD_TAB = TABS.register("treasure_tab", 
			() -> CreativeModeTab.builder()
			.title(Component.translatable("itemGoup.treasure2"))
			.icon(TreasureItems.LOGO.get()::getDefaultInstance)
			.displayItems((displayParams, output) -> {
				// add all items
				Registration.ITEMS.getEntries().forEach(item -> {
					if (!item.equals(TreasureItems.LOGO)) {
						output.accept(item.get(), TabVisibility.PARENT_AND_SEARCH_TABS);
					}
				});
			})
			.build()
			);
			
//	@SubscribeEvent
//	public static void registerTab(CreativeModeTabEvent.Register event) {
//		MOD_TAB = event.registerCreativeModeTab(new ResourceLocation(Treasure.MODID, "treasure_tab"),
//				builder -> builder.icon(() -> new ItemStack(TreasureItems.LOGO.get())).title(Component.translatable("itemGroup.treasure2")));
//	}
}

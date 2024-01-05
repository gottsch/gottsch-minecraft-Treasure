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
package mod.gottsch.forge.treasure2.core.gui;

import mod.gottsch.forge.treasure2.core.Treasure;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.CardboardBoxTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.CauldronChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.CompressorChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.CrateChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.DreadPirateChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.GoldStrongboxTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.IronStrongboxTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.IronboundChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.MilkCreateTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.MoldyCrateChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.PirateChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.SafeTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.SkullChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.SpiderChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.VikingChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.WitherChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.gui.render.tileentity.WoodChestTileEntityRenderer;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.tileentity.TreasureTileEntities;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * @author Mark Gottschling on Aug 18, 2020
 *
 */
public class TreasureGuis {
	@Mod.EventBusSubscriber(modid = Treasure.MODID, bus = EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
	public static class RegistrationHandler {
		// register the factory that is used on the client to generate Screen corresponding to our Container
		@SubscribeEvent
		public static void onClientSetupEvent(FMLClientSetupEvent event) {
			ScreenManager.register(TreasureContainers.STANDARD_CHEST_CONTAINER_TYPE, StandardChestContainerScreen::new);
			ScreenManager.register(TreasureContainers.STRONGBOX_CHEST_CONTAINER_TYPE, StrongboxChestContainerScreen::new);
			ScreenManager.register(TreasureContainers.WITHER_CHEST_CONTAINER_TYPE, WitherChestContainerScreen::new);
			ScreenManager.register(TreasureContainers.SKULL_CHEST_CONTAINER_TYPE, SkullChestContainerScreen::new);
			ScreenManager.register(TreasureContainers.COMPRESSOR_CHEST_CONTAINER_TYPE, CompressorChestContainerScreen::new);

			ScreenManager.register(TreasureContainers.KEY_RING_CONTAINER_TYPE, KeyRingContainerScreen::new);
			ScreenManager.register(TreasureContainers.POUCH_CONTAINER_TYPE, PouchContainerScreen::new);
			ScreenManager.register(TreasureContainers.CHARMING_TABLE_CONTAINER_TYPE, CharmingTableContainerScreen::new);
			ScreenManager.register(TreasureContainers.JEWELER_BENCH_CONTAINER_TYPE, JewelerBenchContainerScreen::new);
			
			// tell the renderer that the base is rendered using CUTOUT_MIPPED (to match the Block Hopper)
			RenderTypeLookup.setRenderLayer(TreasureBlocks.WOOD_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CRATE_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.MOLDY_CRATE_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.IRONBOUND_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.PIRATE_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.IRON_STRONGBOX, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.GOLD_STRONGBOX, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.DREAD_PIRATE_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.WITHER_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SAFE, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.COMPRESSOR_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SKULL_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.GOLD_SKULL_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CRYSTAL_SKULL_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CAULDRON_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SPIDER_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.VIKING_CHEST, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CARDBOARD_BOX, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.MILK_CRATE, RenderType.cutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.RUBY_ORE, RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SAPPHIRE_ORE, RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SPANISH_MOSS, RenderType.cutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SKELETON, RenderType.cutoutMipped());
            
			// register the custom renderer for our tile entity
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.WOOD_CHEST_TILE_ENTITY_TYPE, WoodChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.CRATE_CHEST_TILE_ENTITY_TYPE, CrateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.MOLDY_CRATE_CHEST_TILE_ENTITY_TYPE, MoldyCrateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.IRONBOUND_CHEST_TILE_ENTITY_TYPE, IronboundChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.PIRATE_CHEST_TILE_ENTITY_TYPE, PirateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.IRON_STRONGBOX_TILE_ENTITY_TYPE, IronStrongboxTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.GOLD_STRONGBOX_TILE_ENTITY_TYPE, GoldStrongboxTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.DREAD_PIRATE_CHEST_TILE_ENTITY_TYPE, DreadPirateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.WITHER_CHEST_TILE_ENTITY_TYPE, WitherChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.SAFE_TILE_ENTITY_TYPE, SafeTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.COMPRESSOR_CHEST_TILE_ENTITY_TYPE, CompressorChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.SKULL_CHEST_TILE_ENTITY_TYPE, SkullChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.GOLD_SKULL_CHEST_TILE_ENTITY_TYPE, SkullChestTileEntityRenderer::createGoldSkull);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.CRYSTAL_SKULL_CHEST_TILE_ENTITY_TYPE, SkullChestTileEntityRenderer::createCrystalSkull);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.CAULDRON_CHEST_TILE_ENTITY_TYPE, CauldronChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.SPIDER_CHEST_TILE_ENTITY_TYPE, SpiderChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.VIKING_CHEST_TILE_ENTITY_TYPE, VikingChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.CARDBOARD_BOX_TILE_ENTITY_TYPE, CardboardBoxTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.MILK_CRATE_TILE_ENTITYT_TYPE, MilkCreateTileEntityRenderer::new);

		}
	}
}

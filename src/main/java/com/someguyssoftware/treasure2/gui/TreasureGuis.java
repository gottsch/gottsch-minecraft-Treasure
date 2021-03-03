/**
 * 
 */
package com.someguyssoftware.treasure2.gui;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.gui.render.tileentity.CardboardBoxTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.CauldronChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.CompressorChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.CrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.DreadPirateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.GoldStrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.IronStrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.IronboundChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.MilkCreateTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.MoldyCrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.PirateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SafeTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SkullChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SpiderChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.VikingChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.WitherChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.WoodChestTileEntityRenderer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;

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
			ScreenManager.registerFactory(TreasureContainers.STANDARD_CHEST_CONTAINER_TYPE, StandardChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.STRONGBOX_CHEST_CONTAINER_TYPE, StrongboxChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.WITHER_CHEST_CONTAINER_TYPE, WitherChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.SKULL_CHEST_CONTAINER_TYPE, SkullChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.COMPRESSOR_CHEST_CONTAINER_TYPE, CompressorChestContainerScreen::new);

			// tell the renderer that the base is rendered using CUTOUT_MIPPED (to match the Block Hopper)
			RenderTypeLookup.setRenderLayer(TreasureBlocks.WOOD_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CRATE_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.MOLDY_CRATE_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.IRONBOUND_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.PIRATE_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.IRON_STRONGBOX, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.GOLD_STRONGBOX, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.DREAD_PIRATE_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.WITHER_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SAFE, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.COMPRESSOR_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SKULL_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.GOLD_SKULL_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CRYSTAL_SKULL_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CAULDRON_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SPIDER_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.VIKING_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CARDBOARD_BOX, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.MILK_CRATE, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.RUBY_ORE, RenderType.getCutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SAPPHIRE_ORE, RenderType.getCutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SPANISH_MOSS, RenderType.getCutoutMipped());
            RenderTypeLookup.setRenderLayer(TreasureBlocks.SKELETON, RenderType.getCutoutMipped());
            
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

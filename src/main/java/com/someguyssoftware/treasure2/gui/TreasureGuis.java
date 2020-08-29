/**
 * 
 */
package com.someguyssoftware.treasure2.gui;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.gui.render.tileentity.CauldronChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.CompressorChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.CrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.DreadPirateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.GoldStrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.IronStrongboxTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.IronboundChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.MoldyCrateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.PirateChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SafeTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SkullChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.SpiderChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.AbstractChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.VikingChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.WitherChestTileEntityRenderer;
import com.someguyssoftware.treasure2.gui.render.tileentity.WoodChestTileEntityRenderer;
import com.someguyssoftware.treasure2.inventory.TreasureContainers;
import com.someguyssoftware.treasure2.tileentity.TreasureTileEntities;
import com.someguyssoftware.treasure2.util.AnimationTickCounter;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
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
			ScreenManager.registerFactory(TreasureContainers.standardChestContainerType, StandardChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.strongboxChestContainerType, StrongboxChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.witherChestContainerType, WitherChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.skullChestContainerType, SkullChestContainerScreen::new);
			ScreenManager.registerFactory(TreasureContainers.compressorChestContainerType, CompressorChestContainerScreen::new);

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
			RenderTypeLookup.setRenderLayer(TreasureBlocks.CAULDRON_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.SPIDER_CHEST, RenderType.getCutoutMipped());
			RenderTypeLookup.setRenderLayer(TreasureBlocks.VIKING_CHEST, RenderType.getCutoutMipped());


			// register the custom renderer for our tile entity
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.woodChestTileEntityType, WoodChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.crateChestTileEntityType, CrateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.moldyCrateChestTileEntityType, MoldyCrateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.ironboundChestTileEntityType, IronboundChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.pirateChestTileEntityType, PirateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.ironStrongboxTileEntityType, IronStrongboxTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.goldStrongboxTileEntityType, GoldStrongboxTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.dreadPirateChestTileEntityType, DreadPirateChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.witherChestTileEntityType, WitherChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.safeTileEntityType, SafeTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.compressorChestTileEntityType, CompressorChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.skullChestTileEntityType, SkullChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.goldSkullChestTileEntityType, SkullChestTileEntityRenderer::createGoldSkull);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.cauldronChestTileEntityType, CauldronChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.spiderChestTileEntityType, SpiderChestTileEntityRenderer::new);
			ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.vikingChestTileEntityType, VikingChestTileEntityRenderer::new);

		}
	}
}

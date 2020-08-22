/**
 * 
 */
package com.someguyssoftware.treasure2.gui;

import com.someguyssoftware.treasure2.Treasure;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.gui.render.tileentity.TreasureChestTileEntityRenderer;
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
		    
		    // Tell the renderer that the base is rendered using CUTOUT_MIPPED (to match the Block Hopper)
		    RenderTypeLookup.setRenderLayer(TreasureBlocks.WOOD_CHEST, RenderType.getCutoutMipped());
		    // Register the custom renderer for our tile entity
		    ClientRegistry.bindTileEntityRenderer(TreasureTileEntities.woodChestTileEntityType, WoodChestTileEntityRenderer::new);

		    // NOTE don't need this
		    MinecraftForge.EVENT_BUS.register(AnimationTickCounter.class);  // counts ticks, used for animation
		  }
	}
}

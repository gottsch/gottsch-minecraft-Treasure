/*
 * This file is part of  Treasure2.
 * Copyright (c) 2022 Mark Gottschling (gottsch)
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
package mod.gottsch.forge.treasure2.core.setup;

import mod.gottsch.forge.treasure2.Treasure;
import mod.gottsch.forge.treasure2.client.model.blockentity.StandardChestModel;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.WoodChestRenderer;
import mod.gottsch.forge.treasure2.client.screen.KeyRingScreen;
import mod.gottsch.forge.treasure2.client.screen.PouchScreen;
import mod.gottsch.forge.treasure2.client.screen.StandardChestScreen;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.particle.SpanishMossParticle;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * 
 * @author Mark Gottschling on Nov 16, 2022
 *
 */
@Mod.EventBusSubscriber(modid = Treasure.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

	/**
	 * 
	 * @param event
	 */
    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
        	// attach our container(s) to the screen(s)
            MenuScreens.register(TreasureContainers.STANDARD_CHEST_CONTAINER.get(), StandardChestScreen::new);           
            MenuScreens.register(TreasureContainers.KEY_RING_CONTAINER.get(), KeyRingScreen::new);           
            MenuScreens.register(TreasureContainers.POUCH_CONTAINER.get(), PouchScreen::new);           

            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.WOOD_CHEST.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.SPANISH_MOSS.get(), RenderType.cutout());
        });
    }
    /**
     * Add model textures to the atlas.
     * @param event
     */
    @SubscribeEvent
    public static void onTextureStitch(TextureStitchEvent.Pre event) {
        if (!event.getAtlas().location().equals(TextureAtlas.LOCATION_BLOCKS)) {
            return;
        }
        event.addSprite(WoodChestRenderer.WOOD_CHEST_RENDERER_ATLAS_TEXTURE);
    }
    
	/**
	 * register renderers
	 * @param event
	 */
	@SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
		event.registerBlockEntityRenderer(TreasureBlockEntities.WOOD_CHEST_BLOCK_ENTITY_TYPE.get(), WoodChestRenderer::new);
	}
	
	/**
	 * register model layer definitions
	 * @param event
	 */
	@SubscribeEvent()
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(StandardChestModel.LAYER_LOCATION, StandardChestModel::createBodyLayer);
	}
	
	
	@SubscribeEvent
	public static void registerParticleProviders(ParticleFactoryRegisterEvent event) {
		ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
		
		particleEngine.register(TreasureParticles.SPANISH_MOSS_PARTICLE.get(), 
				SpanishMossParticle.Provider::new);
	}
}

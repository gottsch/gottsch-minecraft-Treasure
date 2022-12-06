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
import mod.gottsch.forge.treasure2.client.model.blockentity.DreadPirateChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.StandardChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.VikingChestModel;
import mod.gottsch.forge.treasure2.client.model.entity.BoundSoulModel;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.DreadPirateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.PirateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.VikingChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.WoodChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.BoundSoulRenderer;
import mod.gottsch.forge.treasure2.client.screen.KeyRingScreen;
import mod.gottsch.forge.treasure2.client.screen.PouchScreen;
import mod.gottsch.forge.treasure2.client.screen.StandardChestScreen;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.particle.BillowingMistParticle;
import mod.gottsch.forge.treasure2.core.particle.MistParticle;
import mod.gottsch.forge.treasure2.core.particle.PoisonMistParticle;
import mod.gottsch.forge.treasure2.core.particle.SpanishMossParticle;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.particle.WitherMistParticle;
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

//            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.WOOD_CHEST.get(), RenderType.cutoutMipped());
            
            TreasureBlocks.CHESTS.forEach(chest -> {
            	ItemBlockRenderTypes.setRenderLayer(chest.get(), RenderType.cutoutMipped());
            });
//            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.VIKING_CHEST.get(), RenderType.cutoutMipped());
            
            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.SPANISH_MOSS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(TreasureBlocks.SKELETON.get(), RenderType.cutout());
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
        event.addSprite(PirateChestRenderer.PIRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(DreadPirateChestRenderer.DREAD_PIRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(VikingChestRenderer.VIKING_CHEST_RENDERER_ATLAS_TEXTURE);
    }
    
	/**
	 * register renderers
	 * @param event
	 */
	@SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
	    // TODO try (Dispatcher d)-> { return new StandardChestRenderer(d, texture); }
		event.registerBlockEntityRenderer(TreasureBlockEntities.WOOD_CHEST_BLOCK_ENTITY_TYPE.get(), WoodChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.PIRATE_CHEST_BLOCK_ENTITY_TYPE.get(), PirateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.DREAD_PIRATE_CHEST_BLOCK_ENTITY_TYPE.get(), DreadPirateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.VIKING_CHEST_BLOCK_ENTITY_TYPE.get(), VikingChestRenderer::new);
		
		
		
		event.registerEntityRenderer(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), BoundSoulRenderer::new);
	}
	
	/**
	 * register model layer definitions
	 * @param event
	 */
	@SubscribeEvent()
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(StandardChestModel.LAYER_LOCATION, StandardChestModel::createBodyLayer);
		event.registerLayerDefinition(DreadPirateChestModel.LAYER_LOCATION, DreadPirateChestModel::createBodyLayer);
		event.registerLayerDefinition(VikingChestModel.LAYER_LOCATION, VikingChestModel::createBodyLayer);
		event.registerLayerDefinition(BoundSoulModel.LAYER_LOCATION, BoundSoulModel::createBodyLayer);
	}
	
	
	@SubscribeEvent
	public static void registerParticleProviders(ParticleFactoryRegisterEvent event) {
		ParticleEngine particleEngine = Minecraft.getInstance().particleEngine;
		
		particleEngine.register(TreasureParticles.SPANISH_MOSS_PARTICLE.get(), 
				SpanishMossParticle.Provider::new);
		
		particleEngine.register(TreasureParticles.MIST_PARTICLE.get(), 
				MistParticle.Provider::new);
		
		particleEngine.register(TreasureParticles.BILLOWING_MIST_PARTICLE.get(), 
				BillowingMistParticle.Provider::new);
		
		particleEngine.register(TreasureParticles.POISON_MIST_PARTICLE.get(), 
				PoisonMistParticle.Provider::new);
		
		particleEngine.register(TreasureParticles.WITHER_MIST_PARTICLE.get(), 
				WitherMistParticle.Provider::new);
	}
}

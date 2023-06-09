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
import mod.gottsch.forge.treasure2.client.model.blockentity.*;
import mod.gottsch.forge.treasure2.client.model.entity.BoundSoulModel;
import mod.gottsch.forge.treasure2.client.model.entity.PirateChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.VikingChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.WoodChestMimicModel;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.*;
import mod.gottsch.forge.treasure2.client.renderer.entity.BoundSoulRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.PirateChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.VikingChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.WoodChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.screen.*;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.particle.*;
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
        	// TODO might need a screen for every chest if there is a different colored gui to use.
        	// attach our container(s) to the screen(s)
            MenuScreens.register(TreasureContainers.STANDARD_CHEST_CONTAINER.get(), StandardChestScreen::new);           
            MenuScreens.register(TreasureContainers.STRONGBOX_CONTAINER.get(), StrongboxScreen::new);
            MenuScreens.register(TreasureContainers.SKULL_CHEST_CONTAINER.get(), SkullChestScreen::new);
            MenuScreens.register(TreasureContainers.COMPRESSOR_CHEST_CONTAINER.get(), CompressorChestScreen::new);
            MenuScreens.register(TreasureContainers.VIKING_CHEST_CONTAINER.get(), VikingChestScreen::new);
            MenuScreens.register(TreasureContainers.WITHER_CHEST_CONTAINER.get(), WitherChestScreen::new); 
            MenuScreens.register(TreasureContainers.KEY_RING_CONTAINER.get(), KeyRingScreen::new);           
            MenuScreens.register(TreasureContainers.POUCH_CONTAINER.get(), PouchScreen::new);           
            
            TreasureBlocks.CHESTS.forEach(chest -> {
            	ItemBlockRenderTypes.setRenderLayer(chest.get(), RenderType.cutoutMipped());
            });

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
        event.addSprite(CrateChestRenderer.CRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(MoldyCrateChestRenderer.CRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(IronboundChestRenderer.IRONBOUND_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(PirateChestRenderer.PIRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(SafeRenderer.SAFE_RENDERER_ATLAS_TEXTURE);
        event.addSprite(StrongboxRenderer.IRON_STRONGBOX_RENDERER_ATLAS_TEXTURE);
        event.addSprite(StrongboxRenderer.GOLD_STRONGBOX_RENDERER_ATLAS_TEXTURE);
        event.addSprite(DreadPirateChestRenderer.DREAD_PIRATE_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(CompressorChestRenderer.COMPRESSOR_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(SkullChestRenderer.SKULL_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(SkullChestRenderer.GOLD_SKULL_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(SkullChestRenderer.CRYSTAL_SKULL_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(CauldronChestRenderer.CAULDRON_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(SpiderChestRenderer.SPIDER_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(VikingChestRenderer.VIKING_CHEST_RENDERER_ATLAS_TEXTURE);
        event.addSprite(CardboardBoxRenderer.CARDBOARD_BOX_RENDERER_ATLAS_TEXTURE);
        event.addSprite(MilkCrateRenderer.MILK_CRATE_RENDERER_ATLAS_TEXTURE);
        event.addSprite(WitherChestRenderer.WITHER_CHEST_RENDERER_ATLAS_TEXTURE);
    }
    
	/**
	 * register renderers
	 * @param event
	 */
	@SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
	    // TODO try (Dispatcher d)-> { return new StandardChestRenderer(d, texture); }
		event.registerBlockEntityRenderer(TreasureBlockEntities.WOOD_CHEST_BLOCK_ENTITY_TYPE.get(), WoodChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.CRATE_CHEST_BLOCK_ENTITY_TYPE.get(), CrateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.MOLDY_CRATE_CHEST_BLOCK_ENTITY_TYPE.get(), MoldyCrateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.IRONBOUND_CHEST_BLOCK_ENTITY_TYPE.get(), IronboundChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.PIRATE_CHEST_BLOCK_ENTITY_TYPE.get(), PirateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.SAFE_BLOCK_ENTITY_TYPE.get(), SafeRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.IRON_STRONGBOX_BLOCK_ENTITY_TYPE.get(), StrongboxRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.GOLD_STRONGBOX_BLOCK_ENTITY_TYPE.get(), StrongboxRenderer::createGoldSkull);
		event.registerBlockEntityRenderer(TreasureBlockEntities.DREAD_PIRATE_CHEST_BLOCK_ENTITY_TYPE.get(), DreadPirateChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.COMPRESSOR_CHEST_BLOCK_ENTITY_TYPE.get(), CompressorChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.SKULL_CHEST_BLOCK_ENTITY_TYPE.get(), SkullChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.GOLD_SKULL_CHEST_BLOCK_ENTITY_TYPE.get(), SkullChestRenderer::createGoldSkull);
		event.registerBlockEntityRenderer(TreasureBlockEntities.CRYSTAL_SKULL_CHEST_BLOCK_ENTITY_TYPE.get(), SkullChestRenderer::createCrystalSkull);
		event.registerBlockEntityRenderer(TreasureBlockEntities.CAULDRON_CHEST_BLOCK_ENTITY_TYPE.get(), CauldronChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.SPIDER_CHEST_BLOCK_ENTITY_TYPE.get(), SpiderChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.VIKING_CHEST_BLOCK_ENTITY_TYPE.get(), VikingChestRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.CARDBOARD_BOX_BLOCK_ENTITY_TYPE.get(), CardboardBoxRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.MILK_CRATE_BLOCK_ENTITY_TYPE.get(), MilkCrateRenderer::new);
		event.registerBlockEntityRenderer(TreasureBlockEntities.WITHER_CHEST_BLOCK_ENTITY_TYPE.get(), WitherChestRenderer::new);
		
		event.registerEntityRenderer(TreasureEntities.BOUND_SOUL_ENTITY_TYPE.get(), BoundSoulRenderer::new);
		event.registerEntityRenderer(TreasureEntities.WOOD_CHEST_MIMIC_ENTITY_TYPE.get(), WoodChestMimicRenderer::new);
		event.registerEntityRenderer(TreasureEntities.PIRATE_CHEST_MIMIC_ENTITY_TYPE.get(), PirateChestMimicRenderer::new);
		event.registerEntityRenderer(TreasureEntities.VIKING_CHEST_MIMIC_ENTITY_TYPE.get(), VikingChestMimicRenderer::new);
	}
	
	/**
	 * register model layer definitions
	 * @param event
	 */
	@SubscribeEvent()
	public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(StandardChestModel.LAYER_LOCATION, StandardChestModel::createBodyLayer);
		event.registerLayerDefinition(CrateChestModel.LAYER_LOCATION, CrateChestModel::createBodyLayer);
		event.registerLayerDefinition(BandedChestModel.LAYER_LOCATION, BandedChestModel::createBodyLayer);
		event.registerLayerDefinition(SafeModel.LAYER_LOCATION, SafeModel::createBodyLayer);
		event.registerLayerDefinition(StrongboxModel.LAYER_LOCATION, StrongboxModel::createBodyLayer);
		event.registerLayerDefinition(DreadPirateChestModel.LAYER_LOCATION, DreadPirateChestModel::createBodyLayer);
		event.registerLayerDefinition(CompressorChestModel.LAYER_LOCATION, CompressorChestModel::createBodyLayer);
		event.registerLayerDefinition(SkullChestModel.LAYER_LOCATION, SkullChestModel::createBodyLayer);
		event.registerLayerDefinition(CauldronChestModel.LAYER_LOCATION, CauldronChestModel::createBodyLayer);
		event.registerLayerDefinition(SpiderChestModel.LAYER_LOCATION, SpiderChestModel::createBodyLayer);
		event.registerLayerDefinition(VikingChestModel.LAYER_LOCATION, VikingChestModel::createBodyLayer);
		event.registerLayerDefinition(CardboardBoxModel.LAYER_LOCATION, CardboardBoxModel::createBodyLayer);
		event.registerLayerDefinition(MilkCrateModel.LAYER_LOCATION, MilkCrateModel::createBodyLayer);
		event.registerLayerDefinition(WitherChestModel.LAYER_LOCATION, WitherChestModel::createBodyLayer);
		
		event.registerLayerDefinition(BoundSoulModel.LAYER_LOCATION, BoundSoulModel::createBodyLayer);
		event.registerLayerDefinition(WoodChestMimicModel.LAYER_LOCATION, WoodChestMimicModel::createBodyLayer);
		event.registerLayerDefinition(PirateChestMimicModel.LAYER_LOCATION, PirateChestMimicModel::createBodyLayer);
		event.registerLayerDefinition(VikingChestMimicModel.LAYER_LOCATION, VikingChestMimicModel::createBodyLayer);
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
		
		particleEngine.register(TreasureParticles.COPPER_COIN_PARTICLE.get(), CoinParticle.Provider::new);
		particleEngine.register(TreasureParticles.SILVER_COIN_PARTICLE.get(), CoinParticle.Provider::new);
		particleEngine.register(TreasureParticles.GOLD_COIN_PARTICLE.get(), CoinParticle.Provider::new);
	}
}

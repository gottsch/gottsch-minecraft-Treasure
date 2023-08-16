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
import mod.gottsch.forge.treasure2.client.model.blockentity.BandedChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.CardboardBoxModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.CauldronChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.CompressorChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.CrateChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.DreadPirateChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.MilkCrateModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.SafeModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.SkullChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.SpiderChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.StandardChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.StrongboxModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.VikingChestModel;
import mod.gottsch.forge.treasure2.client.model.blockentity.WitherChestModel;
import mod.gottsch.forge.treasure2.client.model.entity.BoundSoulModel;
import mod.gottsch.forge.treasure2.client.model.entity.CauldronChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.CrateChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.MoldyCrateChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.PirateChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.VikingChestMimicModel;
import mod.gottsch.forge.treasure2.client.model.entity.WoodChestMimicModel;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.CardboardBoxRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.CauldronChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.CompressorChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.CrateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.DreadPirateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.IronboundChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.MilkCrateRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.MoldyCrateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.PirateChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.SafeRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.SkullChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.SpiderChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.StrongboxRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.VikingChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.WitherChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.blockentity.WoodChestRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.BoundSoulRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.CauldronChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.CrateChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.MoldyCrateChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.PirateChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.VikingChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.renderer.entity.WoodChestMimicRenderer;
import mod.gottsch.forge.treasure2.client.screen.CompressorChestScreen;
import mod.gottsch.forge.treasure2.client.screen.KeyRingScreen;
import mod.gottsch.forge.treasure2.client.screen.PouchScreen;
import mod.gottsch.forge.treasure2.client.screen.SkullChestScreen;
import mod.gottsch.forge.treasure2.client.screen.StandardChestScreen;
import mod.gottsch.forge.treasure2.client.screen.StrongboxScreen;
import mod.gottsch.forge.treasure2.client.screen.VikingChestScreen;
import mod.gottsch.forge.treasure2.client.screen.WitherChestScreen;
import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.particle.BillowingMistParticle;
import mod.gottsch.forge.treasure2.core.particle.CoinParticle;
import mod.gottsch.forge.treasure2.core.particle.MistParticle;
import mod.gottsch.forge.treasure2.core.particle.PoisonMistParticle;
import mod.gottsch.forge.treasure2.core.particle.SpanishMossParticle;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.particle.WitherMistParticle;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
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
	 * Register the {@link IBlockColor} handlers.
	 *
	 * @param event The event
	 */
	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
		event.register(
				(state, reader, pos, color) -> {
					return (reader != null && pos != null) ? BiomeColors.getAverageGrassColor(reader, pos)  : GrassColor.get(0.5D, 1.0D);
				},
				TreasureBlocks.FALLING_GRASS.get());
	}
	
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
		event.registerEntityRenderer(TreasureEntities.CAULDRON_CHEST_MIMIC_ENTITY_TYPE.get(), CauldronChestMimicRenderer::new);
		event.registerEntityRenderer(TreasureEntities.CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), CrateChestMimicRenderer::new);
		event.registerEntityRenderer(TreasureEntities.MOLDY_CRATE_CHEST_MIMIC_ENTITY_TYPE.get(), MoldyCrateChestMimicRenderer::new);

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
		event.registerLayerDefinition(CauldronChestMimicModel.LAYER_LOCATION, CauldronChestMimicModel::createBodyLayer);
		event.registerLayerDefinition(CrateChestMimicModel.LAYER_LOCATION, CrateChestMimicModel::createBodyLayer);
		event.registerLayerDefinition(MoldyCrateChestMimicModel.LAYER_LOCATION, MoldyCrateChestMimicModel::createBodyLayer);

	}
	
	
//	@SubscribeEvent
//	public static void registerParticleProviders(RegisterParticleProvidersEvent event) {
//		event.registerSpecial(TreasureParticles.SPANISH_MOSS_PARTICLE.get(), SpanishMossParticle.Provider::new);		
//		event.register(TreasureParticles.MIST_PARTICLE.get(), MistParticle.Provider::new);		
//		event.register(TreasureParticles.BILLOWING_MIST_PARTICLE.get(), BillowingMistParticle.Provider::new);		
//		event.register(TreasureParticles.POISON_MIST_PARTICLE.get(), 	PoisonMistParticle.Provider::new);		
//		event.register(TreasureParticles.WITHER_MIST_PARTICLE.get(), 	WitherMistParticle.Provider::new);		
//		event.register(TreasureParticles.COPPER_COIN_PARTICLE.get(), CoinParticle.Provider::new);
//		event.register(TreasureParticles.SILVER_COIN_PARTICLE.get(), CoinParticle.Provider::new);
//		event.register(TreasureParticles.GOLD_COIN_PARTICLE.get(), CoinParticle.Provider::new);
//	}
}

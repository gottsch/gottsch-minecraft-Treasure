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
package mod.gottsch.forge.treasure2;

import mod.gottsch.forge.treasure2.core.block.TreasureBlocks;
import mod.gottsch.forge.treasure2.core.block.entity.TreasureBlockEntities;
import mod.gottsch.forge.treasure2.core.config.Config;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.inventory.TreasureContainers;
import mod.gottsch.forge.treasure2.core.item.TreasureCreativeModeTabs;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.loot.TreasureLootTableTypes;
import mod.gottsch.forge.treasure2.core.loot.modifier.TreasureLootModifiers;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.rarity.TreasureRarities;
import mod.gottsch.forge.treasure2.core.setup.ClientSetup;
import mod.gottsch.forge.treasure2.core.setup.CommonSetup;
import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import mod.gottsch.forge.treasure2.core.structure.TreasureStructures;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.ModProcessors;
import mod.gottsch.forge.treasure2.core.structure.templatesystem.chest.TreasureChestSubprocessors;
import mod.gottsch.forge.treasure2.core.world.feature.TreasureConfiguredFeatures;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Mark Gottschling on Aug 11, 2020
 *
 */
@Mod(value = Treasure.MODID)
public class Treasure {
	// logger
	public static Logger LOGGER = LogManager.getLogger(Treasure.MODID);

	// constants
	public static final String MODID = "treasure2";

	private static final String MOBS_CONFIG_VERSION = "1.20.1-v3";
	
	public static Treasure instance;

	/**
	 * 
	 */
	public Treasure() {
		Treasure.instance = this;
		Config.register();

		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		TreasureRarities.register(modEventBus);
		TreasureBlocks.register(modEventBus);
		TreasureItems.register(modEventBus);
		TreasureBlockEntities.register(modEventBus);
		TreasureContainers.register(modEventBus);
		TreasureParticles.register(modEventBus);
		TreasureEntities.register(modEventBus);
		TreasureConfiguredFeatures.register(modEventBus);
		TreasureSounds.register(modEventBus);
		TreasureLootModifiers.register(modEventBus);
		TreasureCreativeModeTabs.TABS.register(modEventBus);
		TreasureStructures.register(modEventBus);
		ModProcessors.register(modEventBus);
		TreasureChestSubprocessors.register(modEventBus);
		TreasureLootTableTypes.register(modEventBus);

		// register 'ModSetup::init' to be called at mod setup time (server and client)
		modEventBus.addListener(CommonSetup::init);

        // register 'ClientSetup::init' to be called at mod setup time (client only)
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init)); 	

	}

	@SubscribeEvent
	public static void onModelRegister(ModelEvent.RegisterAdditional event) {
		// This event is fired to tell us to register our custom model
		event.register(new ResourceLocation(MODID, "block/pyramid_block"));
	}

	@SubscribeEvent
	public static void onModelBake(ModelEvent.BakingCompleted event) {
		// This event is fired after all models are baked. We can replace the standard model
		// for our block with our custom baked model.
		ModelResourceLocation blockLocation = new ModelResourceLocation(new ResourceLocation(MODID, "pyramid_block"), "");
//		event.getModels().put(blockLocation, new PyramidBakedModel());

		// Also replace the item model with our baked model
		ModelResourceLocation itemLocation = new ModelResourceLocation(new ResourceLocation(MODID, "pyramid_block"), "inventory");
//		event.getModels().put(itemLocation, new PyramidBakedModel());
	}

	@SubscribeEvent
	public static void onModelRegistry(ModelEvent.RegisterGeometryLoaders event) {
		// Register our custom model loader
//		event.register(new ResourceLocation(MODID, "pyramid_loader"), new PyramidModelLoader());
	}
}

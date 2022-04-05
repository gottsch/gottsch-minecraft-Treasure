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
package com.someguyssoftware.treasure2;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.Appender;

import com.someguyssoftware.gottschcore.GottschCore;
import com.someguyssoftware.gottschcore.annotation.Credits;
import com.someguyssoftware.gottschcore.command.ShowVersionCommand;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.config.ILoggerConfig;
import com.someguyssoftware.gottschcore.mod.AbstractMod;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.gottschcore.version.BuildVersion;
import com.someguyssoftware.treasure2.api.TreasureApi;
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.capability.IKeyRingCapability;
import com.someguyssoftware.treasure2.capability.KeyRingCapability;
import com.someguyssoftware.treasure2.capability.KeyRingStorage;
import com.someguyssoftware.treasure2.capability.TreasureCapabilities;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.command.SpawnCharmCommand;
import com.someguyssoftware.treasure2.command.SpawnChestCommand;
import com.someguyssoftware.treasure2.command.SpawnPitCommand;
import com.someguyssoftware.treasure2.command.SpawnPitOnlyCommand;
import com.someguyssoftware.treasure2.command.SpawnPitStructureOnlyCommand;
import com.someguyssoftware.treasure2.command.SpawnRuinsCommand;
import com.someguyssoftware.treasure2.command.SpawnWellStructureCommand;
import com.someguyssoftware.treasure2.command.SpawnWitherTreeCommand;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.WorldGeneratorType;
import com.someguyssoftware.treasure2.eventhandler.AnvilEventHandler;
import com.someguyssoftware.treasure2.eventhandler.CharmEventHandler;
import com.someguyssoftware.treasure2.eventhandler.HotbarEquipmentCharmHandler;
import com.someguyssoftware.treasure2.eventhandler.IEquipmentCharmHandler;
import com.someguyssoftware.treasure2.eventhandler.LogoutEventHandler;
import com.someguyssoftware.treasure2.eventhandler.MimicEventHandler;
import com.someguyssoftware.treasure2.eventhandler.PlayerEventHandler;
import com.someguyssoftware.treasure2.eventhandler.WorldEventHandler;
import com.someguyssoftware.treasure2.integration.baubles.BaublesIntegration;
import com.someguyssoftware.treasure2.item.PaintingItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.function.CharmRandomly;
import com.someguyssoftware.treasure2.loot.function.RandomAdornment;
import com.someguyssoftware.treasure2.loot.function.RandomCharm;
import com.someguyssoftware.treasure2.loot.function.RandomRunestone;
import com.someguyssoftware.treasure2.material.TreasureCharmableMaterials;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;
import com.someguyssoftware.treasure2.network.CharmMessageHandlerOnClient;
import com.someguyssoftware.treasure2.network.CharmMessageToClient;
import com.someguyssoftware.treasure2.network.PoisonMistMessageHandlerOnServer;
import com.someguyssoftware.treasure2.network.PoisonMistMessageToServer;
import com.someguyssoftware.treasure2.network.WitherMistMessageHandlerOnServer;
import com.someguyssoftware.treasure2.network.WitherMistMessageToServer;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureDecayManager;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;
import com.someguyssoftware.treasure2.worldgen.GemOreWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.ITreasureWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SubmergedChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WellWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WitherTreeWorldGenerator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author Mark Gottschling onDec 22, 2017
 *
 */
@Mod(modid = Treasure.MODID, name = Treasure.NAME, version = Treasure.VERSION, dependencies = "required-after:gottschcore@[1.14.0,)", acceptedMinecraftVersions = "[1.12.2]", updateJSON = Treasure.UPDATE_JSON_URL)
@Credits(values = { "Treasure was first developed by Mark Gottschling on Aug 27, 2014.",
		"Treasure2 was first developed by Mark Gottschling on Jan 2018.",
		"Credits to Mason Gottschling for ideas and debugging.",
		"Credits to CuddleBeak for some Keys and Locks textures.",
		"Credits to mn_ti for Chinese and to DarkKnightComes for Polish translation.",
		"Credits to Mythical Sausage for tutorials on house/tower designs.",
		"Credits to OdinsRagnarok for Spanish translation and DarvinSlav for Russian translation.",
		"Credits to sfs131010 for updated Chinese translation.",
		"Credits to Xemnes for updated textures for locks, keys, adornments, gems, ores."})

public class Treasure extends AbstractMod {

	// constants
	public static final String MODID = "treasure2";
	protected static final String NAME = "Treasure2";
	protected static final String VERSION = "2.0.0";

	public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/master/update.json";

	private static final String VERSION_URL = "";
	private static final BuildVersion MINECRAFT_VERSION = new BuildVersion(1, 12, 2);

	// latest version
	private static BuildVersion latestVersion;

	// logger
	public static final Logger LOGGER = LogManager.getLogger(Treasure.NAME);

	@Instance(value = Treasure.MODID)
	public static Treasure instance;

	/*
	 * Treasure Creative Tab Must be initialized <b>before</b> any registry events
	 * so that it is available to assign to blocks and items.
	 */
	public static CreativeTabs TREASURE_TAB = new CreativeTabs(CreativeTabs.getNextID(),
			Treasure.MODID + ":" + TreasureConfig.TREASURE_TAB_ID) {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(TreasureItems.TREASURE_TAB, 1);
		}
	};
	public static CreativeTabs ADORNMENTS_TAB = new CreativeTabs(CreativeTabs.getNextID(),
			Treasure.MODID + ":adornments_tab") {
		@SideOnly(Side.CLIENT)
		public ItemStack getTabIconItem() {
			return new ItemStack(TreasureItems.ADORNMENTS_TAB, 1);
		}
	};

	// forge world generators
	public final static Map<WorldGeneratorType, ITreasureWorldGenerator> WORLD_GENERATORS = new HashMap<>();

	// TEMP home
	public static SimpleNetworkWrapper simpleNetworkWrapper; // used to transmit your network messages

	/**
	 * 
	 */
	public Treasure() {
	}

	/**
	 * 
	 * @param event
	 */
	@Override
	@EventHandler
	public void preInt(FMLPreInitializationEvent event) {
		super.preInt(event);

		// initialize/reload the config
		TreasureConfig.init();

		// register additional events
		MinecraftForge.EVENT_BUS.register(new LogoutEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new MimicEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new AnvilEventHandler(getInstance()));

		// configure logging
		// create a rolling file appender
		Appender appender = createRollingFileAppender(Treasure.instance, Treasure.NAME + "Appender",
				(ILoggerConfig) getConfig());
		// add appender to mod logger
		addAppenderToLogger(appender, Treasure.NAME, (ILoggerConfig) getConfig());
		// add appender to the GottschCore logger
		addAppenderToLogger(appender, GottschCore.instance.getName(), (ILoggerConfig) getConfig());

		// register the GUI handler
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		int PARTICLE_MESSAGE_ID = 14;
		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("treasure2_channel");
		simpleNetworkWrapper.registerMessage(PoisonMistMessageHandlerOnServer.class, PoisonMistMessageToServer.class,
				PARTICLE_MESSAGE_ID, Side.SERVER);
		simpleNetworkWrapper.registerMessage(WitherMistMessageHandlerOnServer.class, WitherMistMessageToServer.class,
				15, Side.SERVER);
		simpleNetworkWrapper.registerMessage(CharmMessageHandlerOnClient.class, CharmMessageToClient.class,
				25, Side.CLIENT);

		// add capabilities
		TreasureCapabilities.register();
		CapabilityManager.INSTANCE.register(IKeyRingCapability.class, new KeyRingStorage(), KeyRingCapability::new);
		
		// register custom loot functions
		net.minecraft.world.storage.loot.functions.LootFunctionManager.registerFunction(new RandomAdornment.Serializer());
		net.minecraft.world.storage.loot.functions.LootFunctionManager.registerFunction(new RandomCharm.Serializer());
		net.minecraft.world.storage.loot.functions.LootFunctionManager.registerFunction(new RandomRunestone.Serializer());
		net.minecraft.world.storage.loot.functions.LootFunctionManager.registerFunction(new CharmRandomly.Serializer());
		
		// create the treasure registries
		TreasureApi.registerLootTables(MODID);
		TreasureApi.registerMeta(MODID);
		TreasureApi.registerTemplates(MODID);
		TreasureApi.registerDecays(MODID);
				
		// integrations
		BaublesIntegration.init();
		
		IEquipmentCharmHandler equipmentCharmHandler = null;
		if (BaublesIntegration.isEnabled()) {
			LOGGER.debug("baubles IS loaded");
			try {
				equipmentCharmHandler = 
						(IEquipmentCharmHandler) Class.forName("com.someguyssoftware.treasure2.eventhandler.BaublesEquipmentCharmHandler").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				LOGGER.warn("Unable to load Baubles compatiblity class.", e);
			}
		}
		if (equipmentCharmHandler == null) {
			LOGGER.debug("equipmentHandler is null");
			equipmentCharmHandler = new HotbarEquipmentCharmHandler();
		}
		MinecraftForge.EVENT_BUS.register(new CharmEventHandler(equipmentCharmHandler));
		
		TreasureCharms.init();
	}

	/**
	 * 
	 * @param event
	 */
	@EventHandler
	public void serverStarted(FMLServerStartingEvent event) {
		if (!getConfig().isModEnabled())
			return;

		// add a show version command
		event.registerServerCommand(new ShowVersionCommand(this));

		/*
		 * FOR DEBUGGING ONLY register additional commands
		 */
		event.registerServerCommand(new SpawnChestCommand());
		event.registerServerCommand(new SpawnPitCommand());
		event.registerServerCommand(new SpawnPitOnlyCommand());
		event.registerServerCommand(new SpawnPitStructureOnlyCommand());
		event.registerServerCommand(new SpawnWellStructureCommand());
		event.registerServerCommand(new SpawnWitherTreeCommand());
		event.registerServerCommand(new SpawnRuinsCommand());
		event.registerServerCommand(new SpawnCharmCommand());		
	}

	/**
	 * 
	 */
	@Override
	@EventHandler
	public void init(FMLInitializationEvent event) {
		// don't process is mod is disabled
		if (!getConfig().isModEnabled())
			return;

		super.init(event);

		// register world generators
		WORLD_GENERATORS.put(WorldGeneratorType.SURFACE_CHEST, new SurfaceChestWorldGenerator());
		WORLD_GENERATORS.put(WorldGeneratorType.SUBMERGED_CHEST, new SubmergedChestWorldGenerator());
		WORLD_GENERATORS.put(WorldGeneratorType.WELL, new WellWorldGenerator());
		WORLD_GENERATORS.put(WorldGeneratorType.WITHER_TREE, new WitherTreeWorldGenerator());
		WORLD_GENERATORS.put(WorldGeneratorType.GEM, new GemOreWorldGenerator());

		int genWeight = 0;
		for (Entry<WorldGeneratorType, ITreasureWorldGenerator> gen : WORLD_GENERATORS.entrySet()) {
			GameRegistry.registerWorldGenerator(gen.getValue(), genWeight++);
		}
	}

	/**
	 * 
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		if (!getConfig().isModEnabled())
			return;

		// perform any post init
		super.postInit(event);

		// associate painting items to painting blocks and vice versa
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_BRICKS).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_BRICKS);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_COBBLESTONE)
		.setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_COBBLESTONE);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_DIRT).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_DIRT);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_LAVA).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_LAVA);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_SAND).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_SAND);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_WATER).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_WATER);
		((PaintingItem) TreasureItems.PAINTING_BLOCKS_WOOD).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_WOOD);

		TreasureBlocks.PAINTING_BLOCKS_BRICKS.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_BRICKS);
		TreasureBlocks.PAINTING_BLOCKS_COBBLESTONE.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_COBBLESTONE);
		TreasureBlocks.PAINTING_BLOCKS_DIRT.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_DIRT);
		TreasureBlocks.PAINTING_BLOCKS_LAVA.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_LAVA);
		TreasureBlocks.PAINTING_BLOCKS_SAND.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_SAND);
		TreasureBlocks.PAINTING_BLOCKS_WATER.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_WATER);
		TreasureBlocks.PAINTING_BLOCKS_WOOD.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_WOOD);

        // associate ore blocks with items
        TreasureBlocks.AMETHYST_ORE.setItem(TreasureItems.AMETHYST);
        TreasureBlocks.ONYX_ORE.setItem(TreasureItems.ONYX);
		TreasureBlocks.SAPPHIRE_ORE.setItem(TreasureItems.SAPPHIRE);
		TreasureBlocks.RUBY_ORE.setItem(TreasureItems.RUBY);
		
		// register charmable materials
		TreasureCharmableMaterials.setup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getConfig()
	 */
	@Override
	public IConfig getConfig() {
		//		return Configs.modConfig;
		return TreasureConfig.instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getMinecraftVersion()
	 */
	@Override
	public BuildVersion getMinecraftVersion() {
		return Treasure.MINECRAFT_VERSION;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getVerisionURL()
	 */
	@Override
	public String getVerisionURL() {
		return Treasure.VERSION_URL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getName()
	 */
	@Override
	public String getName() {
		return Treasure.NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.IMod#getId()
	 */
	@Override
	public String getId() {
		return Treasure.MODID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.AbstractMod#getInstance()
	 */
	@Override
	public IMod getInstance() {
		return Treasure.instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.someguyssoftware.gottschcore.mod.AbstractMod#getVersion()
	 */
	@Override
	public String getVersion() {
		return Treasure.VERSION;
	}

	@Override
	public BuildVersion getModLatestVersion() {
		return latestVersion;
	}

	@Override
	public void setModLatestVersion(BuildVersion version) {
		Treasure.latestVersion = version;
	}

	@Override
	public String getUpdateURL() {
		return Treasure.UPDATE_JSON_URL;
	}

}

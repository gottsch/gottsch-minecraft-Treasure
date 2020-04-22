/**
 * 
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
import com.someguyssoftware.treasure2.block.TreasureBlocks;
import com.someguyssoftware.treasure2.client.gui.GuiHandler;
import com.someguyssoftware.treasure2.command.SpawnChestCommand;
import com.someguyssoftware.treasure2.command.SpawnOasisCommand;
import com.someguyssoftware.treasure2.command.SpawnPitCommand;
import com.someguyssoftware.treasure2.command.SpawnPitOnlyCommand;
import com.someguyssoftware.treasure2.command.SpawnPitStructureOnlyCommand;
import com.someguyssoftware.treasure2.command.SpawnRuinsCommand;
import com.someguyssoftware.treasure2.command.SpawnWellStructureCommand;
import com.someguyssoftware.treasure2.command.SpawnWitherTreeCommand;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.enums.WorldGenerators;
import com.someguyssoftware.treasure2.eventhandler.LogoutEventHandler;
import com.someguyssoftware.treasure2.eventhandler.MimicEventHandler;
import com.someguyssoftware.treasure2.eventhandler.PlayerEventHandler;
import com.someguyssoftware.treasure2.eventhandler.WorldEventHandler;
import com.someguyssoftware.treasure2.item.PaintingItem;
import com.someguyssoftware.treasure2.item.TreasureItems;
import com.someguyssoftware.treasure2.loot.TreasureLootTableMaster;
import com.someguyssoftware.treasure2.meta.TreasureMetaManager;
import com.someguyssoftware.treasure2.network.PoisonMistMessageHandlerOnServer;
import com.someguyssoftware.treasure2.network.PoisonMistMessageToServer;
import com.someguyssoftware.treasure2.network.WitherMistMessageHandlerOnServer;
import com.someguyssoftware.treasure2.network.WitherMistMessageToServer;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureDecayManager;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureTemplateManager;
import com.someguyssoftware.treasure2.worldgen.GemOreWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.ITreasureWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.OasisWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SubmergedChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.SurfaceChestWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WellWorldGenerator;
import com.someguyssoftware.treasure2.worldgen.WitherTreeWorldGenerator;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
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
import net.minecraftforge.oredict.OreDictionary;

/**
 * 
 * @author Mark Gottschling onDec 22, 2017
 *
 */
@Mod(modid = Treasure.MODID, name = Treasure.NAME, version = Treasure.VERSION, dependencies = "required-after:gottschcore@[1.12.1,)", acceptedMinecraftVersions = "[1.12.2]", updateJSON = Treasure.UPDATE_JSON_URL)
@Credits(values = { "Treasure was first developed by Mark Gottschling on Aug 27, 2014.",
		"Treasure2 was first developed by Mark Gottschling on Jan 2018.",
		"Credits to Mason Gottschling for ideas and debugging.",
		"Credits to CuddleBeak for some Keys and Locks textures.",
		"Credits to mn_ti for Chinese and to DarkKnightComes for Polish translation.",
		"Credits to Mythical Sausage for tutorials on house/tower designs.",
		"Credits to OdinsRagnarok for Spanish translation and DarvinSlav for Russian translation." })
public class Treasure extends AbstractMod {

	// constants
	public static final String MODID = "treasure2";
	protected static final String NAME = "Treasure2";
	protected static final String VERSION = "1.11.1";

	public static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/master/Treasure2-1.12.2/update.json";

	private static final String VERSION_URL = "";
	private static final BuildVersion MINECRAFT_VERSION = new BuildVersion(1, 12, 2);

	// latest version
	private static BuildVersion latestVersion;

	// logger
	public static Logger logger = LogManager.getLogger(Treasure.NAME);

	@Instance(value = Treasure.MODID)
	public static Treasure instance;

	// TODO remove this. should have static final properties.
	// loot tables management
	public static TreasureLootTableMaster LOOT_TABLES;

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

	// forge world generators
	public final static Map<WorldGenerators, ITreasureWorldGenerator> WORLD_GENERATORS = new HashMap<>();

	// template manager
	public static TreasureTemplateManager TEMPLATE_MANAGER;

	// meta manager // NOTE can't be final as Treasure.instance is required.
	public static TreasureMetaManager META_MANAGER;

	public static TreasureDecayManager DECAY_MANAGER;

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
		((TreasureConfig) getConfig()).init();

		// register additional events
		MinecraftForge.EVENT_BUS.register(new LogoutEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new MimicEventHandler(getInstance()));

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
		event.registerServerCommand(new SpawnOasisCommand());
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
		WORLD_GENERATORS.put(WorldGenerators.SURFACE_CHEST, new SurfaceChestWorldGenerator());
		WORLD_GENERATORS.put(WorldGenerators.SUBMERGED_CHEST, new SubmergedChestWorldGenerator());
		WORLD_GENERATORS.put(WorldGenerators.WELL, new WellWorldGenerator());
		WORLD_GENERATORS.put(WorldGenerators.WITHER_TREE, new WitherTreeWorldGenerator());
		WORLD_GENERATORS.put(WorldGenerators.GEM, new GemOreWorldGenerator());
		WORLD_GENERATORS.put(WorldGenerators.OASIS, new OasisWorldGenerator());

		int genWeight = 0;
		for (Entry<WorldGenerators, ITreasureWorldGenerator> gen : WORLD_GENERATORS.entrySet()) {
			GameRegistry.registerWorldGenerator(gen.getValue(), genWeight++);
		}

		// add the loot table managers
		LOOT_TABLES = new TreasureLootTableMaster(Treasure.instance, "", "loot_tables");

		TEMPLATE_MANAGER = new TreasureTemplateManager(Treasure.instance, "/structures",
				FMLCommonHandler.instance().getDataFixer());

		META_MANAGER = new TreasureMetaManager(Treasure.instance, "meta");

		DECAY_MANAGER = new TreasureDecayManager(Treasure.instance, "decay");
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
		TreasureBlocks.SAPPHIRE_ORE.setItem(TreasureItems.SAPPHIRE);
		TreasureBlocks.RUBY_ORE.setItem(TreasureItems.RUBY);
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

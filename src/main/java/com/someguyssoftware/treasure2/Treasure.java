package com.someguyssoftware.treasure2;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mojang.serialization.Codec;
import com.someguyssoftware.gottschcore.annotation.Credits;
import com.someguyssoftware.gottschcore.annotation.ModInfo;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.eventhandler.ClientEventHandler;
import com.someguyssoftware.treasure2.eventhandler.PlayerEventHandler;
import com.someguyssoftware.treasure2.eventhandler.WorldEventHandler;
import com.someguyssoftware.treasure2.init.TreasureSetup;
import com.someguyssoftware.treasure2.particle.TreasureParticles;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureConfiguredStructures;
import com.someguyssoftware.treasure2.world.gen.structure.TreasureStructures;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

/**
 * 
 * @author Mark Gottschling on Aug 11, 2020
 *
 */
@Mod(value = Treasure.MODID)
@ModInfo(
		modid = Treasure.MODID, 
		name = Treasure.NAME, 
		version = Treasure.VERSION, 
		minecraftVersion = "1.16.5", 
		forgeVersion = "36.1.0", 
		updateJsonUrl = Treasure.UPDATE_JSON_URL)
@Credits(values = { "Treasure was first developed by Mark Gottschling on Aug 27, 2014.",
		"Treasure2 was first developed by Mark Gottschling on Jan 2018.",
		"Credits to Mason Gottschling for ideas and debugging.",
		"Credits to CuddleBeak for some Keys and Locks textures.",
		"Credits to mn_ti for Chinese and to DarkKnightComes for Polish translation.",
		"Credits to Mythical Sausage for tutorials on house/tower designs.",
		"Credits to OdinsRagnarok for Spanish translation and DarvinSlav for Russian translation." })
public class Treasure implements IMod {
	// logger
	public static Logger LOGGER = LogManager.getLogger(Treasure.NAME);

	// constants
	public static final String MODID = "treasure2";
	protected static final String NAME = "Treasure2";
	protected static final String VERSION = "1.0.0";
	protected static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/1.16.5-master/update.json";

	public static Treasure instance;
	private static TreasureConfig config;
	public static IEventBus MOD_EVENT_BUS;
	
	public Treasure() {
		Treasure.instance = this;
		Treasure.config = new TreasureConfig(this);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TreasureConfig.COMMON_CONFIG);

		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		
		TreasureStructures.DEFERRED_REGISTRY_STRUCTURE.register(eventBus);
		
		eventBus.addListener(this::config);
		eventBus.addListener(this::setup);
		eventBus.addListener(TreasureSetup::common);
		eventBus.addListener(this::clientSetup);

        // for events that happen after initialization. This is probably going to be use a lot.
        IEventBus forgeBus = MinecraftForge.EVENT_BUS;
//        forgeBus.addListener(EventPriority.NORMAL, this::addDimensionalSpacing);
		
        // the comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
        forgeBus.addListener(EventPriority.HIGH, this::biomeModification);
        
		TreasureConfig.loadConfig(TreasureConfig.COMMON_CONFIG,
				FMLPaths.CONFIGDIR.get().resolve("treasure2-common.toml"));

		// test accessing the logging properties
		TreasureConfig.LOGGING.filename.get();

		// needs to be registered here instead of @Mod.EventBusSubscriber because we need to pass in a constructor argument
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(getInstance()));
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
//		MinecraftForge.EVENT_BUS.register(new TreasureParticles());
		MOD_EVENT_BUS = FMLJavaModLoadingContext.get().getModEventBus();
//		MOD_EVENT_BUS.register(TreasureParticles.class);
//		DistExecutor.runWhenOn(Dist.CLIENT, () -> Treasure::clientOnly);
	}
	
	public static void clientOnly() {
//		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		MOD_EVENT_BUS.register(ClientEventHandler.class);
	}
	
	/**
	 * ie. preint
	 * 
	 * @param event
	 */
	@SuppressWarnings("deprecation")
	private void setup(final FMLCommonSetupEvent event) {
		
		/**
	     * Here, setupStructures will be ran after registration of all structures are finished.
	     * This is important to be done here so that the Deferred Registry has already run and
	     * registered/created our structure for us.
	     *
	     * Once after that structure instance is made, we then can now do the rest of the setup
	     * that requires a structure instance such as setting the structure spacing, creating the
	     * configured structure instance, and more.
	     */
        event.enqueueWork(() -> {
//            TreasureStructures.setupStructures();
//            TreasureConfiguredStructures.registerConfiguredStructures();
        });
        
//		TreasureData.initializeData();
//		Treasure.lootTableMaster = new TreasureLootTableMaster2(Treasure.instance);
//		
//		DeferredWorkQueue.runLater(TreasureFeatures::addFeatureToBiomes);
		

//		InputStream is = Dungeons2.instance.getClass().getResourceAsStream(BUILT_IN_STYLE_SHEET_PATH);
//		URL url = this.getClass().getResource("/meta");
//		URL url = Treasure.class.getClassLoader().getResource("/meta");
//		Treasure.LOGGER.info("setup resource URL -> {}", url.toString());
//		
//		List<IResource> r;
//		try {
//			r = Minecraft.getInstance().getResourceManager().getAllResources(new ResourceLocation("treasure2" ,"meta/treasure2/structures"));
//			Treasure.LOGGER.info("testing resource manager -> {}", r.get(0).getLocation());
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void clientSetup(final FMLClientSetupEvent event) {
		Treasure.LOGGER.info("in client setup event");
	}
	 private void postSetup(final FMLLoadCompleteEvent event) { 
		 Treasure.LOGGER.info("in post setup event");
	 }
	 
	 private void config(final ModConfigEvent evt) {
		 Treasure.LOGGER.info("in config event");
	 }
	 
	private void onServerStarted(final FMLDedicatedServerSetupEvent event) {
		Treasure.LOGGER.info("in onServerStarted");
	}

    /**
     * This is the event you will use to add anything to any biome.
     * This includes spawns, changing the biome's looks, messing with its surfacebuilders,
     * adding carvers, spawning new features... etc
     *
     * Here, we will use this to add our structure to all biomes.
     */
    public void biomeModification(final BiomeLoadingEvent event) {
        /*
         * Add our structure to all biomes including other modded biomes.
         * You can skip or add only to certain biomes based on stuff like biome category,
         * temperature, scale, precipitation, mod id, etc. All kinds of options!
         *
         * You can even use the BiomeDictionary as well! To use BiomeDictionary, do
         * RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName()) to get the biome's
         * registrykey. Then that can be fed into the dictionary to get the biome's types.
         */
//        event.getGeneration().getStructures().add(() -> TreasureConfiguredStructures.CONFIGURED_SURFACE_CHEST);
    	// see TreasureFeatures.BiomeRegistrationHandler.onBiomeLoading()
    }
    
    /**
     * Will go into the world's chunkgenerator and manually add our structure spacing.
     * If the spacing is not added, the structure doesn't spawn.
     *
     * Use this for dimension blacklists for your structure.
     * (Don't forget to attempt to remove your structure too from the map if you are blacklisting that dimension!)
     * (It might have your structure in it already.)
     *
     * Basically use this to make absolutely sure the chunkgenerator can or cannot spawn your structure.
     */
    private static Method GETCODEC_METHOD;
    public void addDimensionalSpacing(final WorldEvent.Load event) {
        if(event.getWorld() instanceof ServerWorld){
            ServerWorld serverWorld = (ServerWorld)event.getWorld();

            /*
             * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
             * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
             * This here is done with reflection as this tutorial is not about setting up and using Mixins.
             * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
             */
            try {
                if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkSource().generator));
                if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            }
            catch(Exception e){
                Treasure.LOGGER.error("Was unable to check if " + serverWorld.dimension().location() + " is using Terraforged's ChunkGenerator.");
            }

            /*
             * Prevent spawning our structure in Vanilla's superflat world as
             * people seem to want their superflat worlds free of modded structures.
             * Also that vanilla superflat is really tricky and buggy to work with in my experience.
             */
            if(serverWorld.getChunkSource().getGenerator() instanceof FlatChunkGenerator &&
                serverWorld.dimension().equals(World.OVERWORLD)){
                return;
            }

            /*
             * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
             * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
             *
             * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
             * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
             * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
             */
            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkSource().generator.getSettings().structureConfig());
            tempMap.putIfAbsent(TreasureStructures.SURFACE_CHEST.get(), DimensionStructuresSettings.DEFAULTS.get(TreasureStructures.SURFACE_CHEST.get()));
            tempMap.putIfAbsent(TreasureStructures.UNCOMMON_SURFACE_CHEST.get(), DimensionStructuresSettings.DEFAULTS.get(TreasureStructures.UNCOMMON_SURFACE_CHEST.get()));
            serverWorld.getChunkSource().generator.getSettings().structureConfig = tempMap;
        }
   }
	
//	// NOTE can't make final here as it is set during world load
//	// loot tables management
//	public static TreasureLootTableMaster LOOT_TABLES;
//
//	/*
//	 * Treasure Creative Tab Must be initialized <b>before</b> any registry events
//	 * so that it is available to assign to blocks and items.
//	 */
//	public static CreativeTabs TREASURE_TAB = new CreativeTabs(CreativeTabs.getNextID(),
//			Treasure.MODID + ":" + TreasureConfig.TREASURE_TAB_ID) {
//		@SideOnly(Side.CLIENT)
//		public ItemStack getTabIconItem() {
//			return new ItemStack(TreasureItems.TREASURE_TAB, 1);
//		}
//	};
//
//	// forge world generators
//	public final static Map<WorldGenerators, ITreasureWorldGenerator> WORLD_GENERATORS = new HashMap<>();
//
//	// template manager
//	public static TreasureTemplateManager TEMPLATE_MANAGER;
//
//	// meta manager // NOTE can't be final as Treasure.instance is required.
//	public static TreasureMetaManager META_MANAGER;
//
//	public static TreasureDecayManager DECAY_MANAGER;
//
//	// TEMP home
//	public static SimpleNetworkWrapper simpleNetworkWrapper; // used to transmit your network messages
//
//	private static TreasureConfig config;
//	/**
//	 * 
//	 */
//	public Treasure() {
//		Treasure.instance = this;
//	}
//
//	/**
//	 * 
//	 * @param event
//	 */
//	@Override
//	@EventHandler
//	public void preInt(FMLPreInitializationEvent event) {
//		super.preInt(event);
//
//		// initialize/reload the config
//		((TreasureConfig) getConfig()).init();
//
//		// register additional events
//		MinecraftForge.EVENT_BUS.register(new LogoutEventHandler(getInstance()));
//		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler(getInstance()));
//		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(getInstance()));
//		MinecraftForge.EVENT_BUS.register(new MimicEventHandler(getInstance()));
//
//		// configure logging
//		// create a rolling file appender
//		Appender appender = createRollingFileAppender(Treasure.instance, Treasure.NAME + "Appender",
//				(ILoggerConfig) getConfig());
//		// add appender to mod logger
//		addAppenderToLogger(appender, Treasure.NAME, (ILoggerConfig) getConfig());
//		// add appender to the GottschCore logger
//		addAppenderToLogger(appender, GottschCore.instance.getName(), (ILoggerConfig) getConfig());
//
//		// register the GUI handler
//		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
//
//		int PARTICLE_MESSAGE_ID = 14;
//		simpleNetworkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel("treasure2_channel");
//		simpleNetworkWrapper.registerMessage(PoisonMistMessageHandlerOnServer.class, PoisonMistMessageToServer.class,
//				PARTICLE_MESSAGE_ID, Side.SERVER);
//		simpleNetworkWrapper.registerMessage(WitherMistMessageHandlerOnServer.class, WitherMistMessageToServer.class,
//				15, Side.SERVER);
//		simpleNetworkWrapper.registerMessage(CharmMessageHandlerOnClient.class, CharmMessageToClient.class,
//				25, Side.CLIENT);
//		
//		// add capabilities
//		CapabilityManager.INSTANCE.register(ICharmCapability.class, new CharmStorage(), CharmCapability::new);
//		CapabilityManager.INSTANCE.register(IKeyRingCapability.class, new KeyRingStorage(), KeyRingCapability::new);
//		
//		// register custom loot functions
//		LootFunctionManager.registerFunction(new CharmRandomly.Serializer());
//		LootFunctionManager.registerFunction(new SetCharms.Serializer());
//	}
//
//	/**
//	 * 
//	 * @param event
//	 */
//	@EventHandler
//	public void serverStarted(FMLServerStartingEvent event) {
//		if (!getConfig().isModEnabled())
//			return;
//
//		// add a show version command
//		event.registerServerCommand(new ShowVersionCommand(this));
//
//		/*
//		 * FOR DEBUGGING ONLY register additional commands
//		 */
//		event.registerServerCommand(new SpawnChestCommand());
//		event.registerServerCommand(new SpawnDebugPitCommand());
//		event.registerServerCommand(new SpawnPitOnlyCommand());
//		event.registerServerCommand(new SpawnPitStructureOnlyCommand());
//		event.registerServerCommand(new SpawnWellStructureCommand());
//		event.registerServerCommand(new SpawnWitherTreeCommand());
//		event.registerServerCommand(new SpawnRuinsCommand());
//		event.registerServerCommand(new SpawnOasisCommand());
//	}
//
//	/**
//	 * 
//	 */
//	@Override
//	@EventHandler
//	public void init(FMLInitializationEvent event) {
//		// don't process is mod is disabled
//		if (!getConfig().isModEnabled())
//			return;
//
//		super.init(event);
//
//		// register world generators
//		WORLD_GENERATORS.put(WorldGenerators.SURFACE_CHEST, new SurfaceChestWorldGenerator());
//		WORLD_GENERATORS.put(WorldGenerators.SUBMERGED_CHEST, new SubmergedChestWorldGenerator());
//		WORLD_GENERATORS.put(WorldGenerators.WELL, new WellWorldGenerator());
//		WORLD_GENERATORS.put(WorldGenerators.WITHER_TREE, new WitherTreeWorldGenerator());
//		WORLD_GENERATORS.put(WorldGenerators.GEM, new GemOreWorldGenerator());
//		WORLD_GENERATORS.put(WorldGenerators.OASIS, new OasisWorldGenerator());
//
//		int genWeight = 0;
//		for (Entry<WorldGenerators, ITreasureWorldGenerator> gen : WORLD_GENERATORS.entrySet()) {
//			GameRegistry.registerWorldGenerator(gen.getValue(), genWeight++);
//		}
//
//		// add the loot table managers
//		LOOT_TABLES = new TreasureLootTableMaster(Treasure.instance, "", "loot_tables");		
//		
//		TEMPLATE_MANAGER = new TreasureTemplateManager(Treasure.instance, "/structures",
//				FMLCommonHandler.instance().getDataFixer());
//
//		META_MANAGER = new TreasureMetaManager(Treasure.instance, "meta");
//
//		DECAY_MANAGER = new TreasureDecayManager(Treasure.instance, "decay");
//	}
//
//	/**
//	 * 
//	 */
//	@EventHandler
//	public void postInit(FMLPostInitializationEvent event) {
//		if (!getConfig().isModEnabled())
//			return;
//
//		// perform any post init
//		super.postInit(event);
//
//		// associate painting items to painting blocks and vice versa
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_BRICKS).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_BRICKS);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_COBBLESTONE)
//				.setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_COBBLESTONE);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_DIRT).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_DIRT);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_LAVA).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_LAVA);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_SAND).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_SAND);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_WATER).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_WATER);
//		((PaintingItem) TreasureItems.PAINTING_BLOCKS_WOOD).setPaintingBlock(TreasureBlocks.PAINTING_BLOCKS_WOOD);
//
//		TreasureBlocks.PAINTING_BLOCKS_BRICKS.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_BRICKS);
//		TreasureBlocks.PAINTING_BLOCKS_COBBLESTONE.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_COBBLESTONE);
//		TreasureBlocks.PAINTING_BLOCKS_DIRT.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_DIRT);
//		TreasureBlocks.PAINTING_BLOCKS_LAVA.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_LAVA);
//		TreasureBlocks.PAINTING_BLOCKS_SAND.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_SAND);
//		TreasureBlocks.PAINTING_BLOCKS_WATER.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_WATER);
//		TreasureBlocks.PAINTING_BLOCKS_WOOD.setItem((PaintingItem) TreasureItems.PAINTING_BLOCKS_WOOD);
//
//		// associate ore blocks with items
//		TreasureBlocks.SAPPHIRE_ORE.setItem(TreasureItems.SAPPHIRE);
//		TreasureBlocks.RUBY_ORE.setItem(TreasureItems.RUBY);
//	}


	@Override
	public IMod getInstance() {
		return Treasure.instance;
	}

	@Override
	public String getId() {
		return Treasure.MODID;
	}

	@Override
	public IConfig getConfig() {
		return Treasure.config;
	}

//	public static TreasureLootTableMaster2 getLootTableMaster() {
//		return lootTableMaster;
//	}
}

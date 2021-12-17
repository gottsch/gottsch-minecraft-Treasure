package com.someguyssoftware.treasure2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.annotation.Credits;
import com.someguyssoftware.gottschcore.annotation.ModInfo;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.mod.IMod;
import com.someguyssoftware.treasure2.charm.TreasureCharms;
import com.someguyssoftware.treasure2.config.TreasureConfig;
import com.someguyssoftware.treasure2.entity.TreasureEntities;
import com.someguyssoftware.treasure2.eventhandler.CharmEventHandler;
import com.someguyssoftware.treasure2.eventhandler.HotbarEquipmentCharmHandler;
import com.someguyssoftware.treasure2.eventhandler.IEquipmentCharmHandler;

import com.someguyssoftware.treasure2.eventhandler.WorldEventHandler;
import com.someguyssoftware.treasure2.init.TreasureSetup;
import com.someguyssoftware.treasure2.network.TreasureNetworking;
import com.someguyssoftware.treasure2.particle.TreasureParticles;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

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
		forgeVersion = "36.2.0", 
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
	protected static final String VERSION = "1.7.3";


	protected static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/1.16.5-master/update.json";

	public static Treasure instance;
	private static TreasureConfig config;
	public static IEventBus MOD_EVENT_BUS;
	
	public Treasure() {
		Treasure.instance = this;
		Treasure.config = new TreasureConfig(this);
		
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TreasureConfig.COMMON_CONFIG);

		TreasureConfig.loadConfig(TreasureConfig.COMMON_CONFIG,
				FMLPaths.CONFIGDIR.get().resolve("treasure2-common.toml"));
				
		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		// deferred register
		TreasureParticles.PARTICLE_TYPES.register(eventBus);
		// regular register
		eventBus.addListener(this::config);
		eventBus.addListener(TreasureNetworking::common);
		eventBus.addListener(TreasureSetup::common);
		eventBus.addListener(TreasureCharms::setup);
		eventBus.addListener(TreasureSetup::clientSetup);
		eventBus.addListener(this::clientSetup);
		eventBus.addListener(this::interModComms);

		// needs to be registered here instead of @Mod.EventBusSubscriber because an instance of the handler
		// is required and constructor arguments may need to be passed in
		MinecraftForge.EVENT_BUS.register(new WorldEventHandler(getInstance()));
		
		IEquipmentCharmHandler equipmentCharmHandler = null;
		if (ModList.get().isLoaded("curios")) {
			LOGGER.debug("curios IS loaded");
			try {
				equipmentCharmHandler = 
						(IEquipmentCharmHandler) Class.forName("com.someguyssoftware.treasure2.eventhandler.CuriosEquipmentCharmHandler").newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				LOGGER.warn("Unable to load Curios compatiblity class.");
			}
		}
		if (equipmentCharmHandler == null) {
			LOGGER.debug("equipmentHandler is null");
			equipmentCharmHandler = new HotbarEquipmentCharmHandler();
		}
		MinecraftForge.EVENT_BUS.register(new CharmEventHandler(equipmentCharmHandler));

	}
	
	/**
	 * 
	 * @param event
	 */
    private void interModComms(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CHARM.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.RING.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BRACELET.getMessageBuilder().build());
        InterModComms.sendTo("curios", SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.BELT.getMessageBuilder().build());
    }
    
	public static void clientOnly() {

	}
	
	private void clientSetup(final FMLClientSetupEvent event) {
		Treasure.LOGGER.info("in client setup event");
		TreasureEntities.registerEntityRenderers();
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

}

package mod.gottsch.forge.treasure2.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.someguyssoftware.gottschcore.annotation.Credits;
import com.someguyssoftware.gottschcore.annotation.ModInfo;
import com.someguyssoftware.gottschcore.config.IConfig;
import com.someguyssoftware.gottschcore.mod.IMod;

import mod.gottsch.forge.treasure2.core.charm.TreasureCharms;
import mod.gottsch.forge.treasure2.core.config.TreasureConfig;
import mod.gottsch.forge.treasure2.core.entity.TreasureEntities;
import mod.gottsch.forge.treasure2.core.eventhandler.WorldEventHandler;
import mod.gottsch.forge.treasure2.core.init.TreasureSetup;
import mod.gottsch.forge.treasure2.core.item.TreasureItems;
import mod.gottsch.forge.treasure2.core.network.TreasureNetworking;
import mod.gottsch.forge.treasure2.core.particle.TreasureParticles;
import mod.gottsch.forge.treasure2.core.rune.TreasureRunes;
import mod.gottsch.forge.treasure2.core.sound.TreasureSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
		forgeVersion = "36.2.34", 
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
	protected static final String VERSION = "2.5.0";


	protected static final String UPDATE_JSON_URL = "https://raw.githubusercontent.com/gottsch/gottsch-minecraft-Treasure/1.16.5-master/update.json";

	public static Treasure instance;
	
	private static TreasureConfig config;

	/**
	 * 
	 */
	public Treasure() {
		Treasure.instance = this;
		Treasure.config = new TreasureConfig(this);
		// force load of static blocks
		TreasureCharms.init();
		TreasureRunes.init();
		// register the deferred registries
		TreasureItems.register();
		TreasureSounds.register();
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, TreasureConfig.COMMON_CONFIG);
				
		// Register the setup method for modloading
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
		// deferred register
		TreasureParticles.register(eventBus);
		// regular register
		eventBus.addListener(TreasureNetworking::common);
		eventBus.addListener(TreasureSetup::common);
		eventBus.addListener(this::clientSetup);
		eventBus.addListener(this::interModComms);

		MinecraftForge.EVENT_BUS.register(new WorldEventHandler());
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
